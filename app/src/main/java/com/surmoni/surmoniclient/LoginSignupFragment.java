package com.surmoni.surmoniclient;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.surmoni.surmoniclient.MenuFragments.MenuMainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.content.Context.MODE_PRIVATE;

public class LoginSignupFragment extends Fragment {
    private Button login;
    private Button signup;
    private TextInputLayout email;
    private TextInputLayout password;
    private TextView client_id;
    private TextView change_pass;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog pd;
    SharedPreferences pref;
    private CheckBox remember_cb;
    private AdView mAdView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth=FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_login_signup, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        login=view.findViewById(R.id.bt_login);
        signup=view.findViewById(R.id.bt_signup);
        change_pass=view.findViewById(R.id.forgot_pass);
        pd = new ProgressDialog(getContext());
        pd.setTitle("Please wait...");
        pd.setCancelable(true);
        already_loggedIn();
        pref = getContext().getSharedPreferences("com.surmoni.client.user_details",MODE_PRIVATE);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.show();
                login();
            }
        });

        change_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout,new ForgotPasswordFragment());
                fragmentTransaction.commit();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.show();
                signup();
            }
        });
       ActivateAdds();
    }

    protected void already_loggedIn()
    {
        final String Email;
        final String Pass;
        email = getView().findViewById(R.id.et_email);
        password = getView().findViewById(R.id.et_password);
        remember_cb = getView().findViewById(R.id.cb_remember_me);
        pref = getContext().getSharedPreferences("com.surmoni.client.user_details",MODE_PRIVATE);
        if(pref.contains("Email") && pref.contains("Password"))
        {
            pd.show();
            Email= pref.getString("Email",null);
            Pass=pref.getString("Password",null);
            if (!TextUtils.isEmpty(Email) && !TextUtils.isEmpty(Pass)) {
                firebaseAuth.signInWithEmailAndPassword(Email, Pass).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            String currentUserID = firebaseAuth.getCurrentUser().getUid().toString();
                            Toast.makeText(getContext(), "Your Client ID is \n" + currentUserID,
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getContext(), MenuMainActivity.class);
                            intent.putExtra("user_id", currentUserID);
                            pd.dismiss();
                            startActivity(intent);

                        } else {
                            pd.dismiss();
                            Toast.makeText(getContext(), "Login failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                pd.dismiss();
                Toast.makeText(getContext(), "Fields are empty.",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected void login() {
        final String Email;
        final String Pass;
        email = getView().findViewById(R.id.et_email);
        password = getView().findViewById(R.id.et_password);
        remember_cb = getView().findViewById(R.id.cb_remember_me);
        pref = getContext().getSharedPreferences("com.surmoni.client.user_details",MODE_PRIVATE);
                    if(remember_cb.isChecked())
                    {
                    Email = email.getEditText().getText().toString().trim();
                    Pass = password.getEditText().getText().toString().trim();
                        if (!TextUtils.isEmpty(Email) && !TextUtils.isEmpty(Pass)) {
                            firebaseAuth.signInWithEmailAndPassword(Email, Pass).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = firebaseAuth.getCurrentUser();
                                        String currentUserID = firebaseAuth.getCurrentUser().getUid().toString();
                                        Toast.makeText(getContext(), "Your Client ID is \n" + currentUserID,
                                                Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getContext(), MenuMainActivity.class);
                                        intent.putExtra("user_id", currentUserID);
                                        SharedPreferences.Editor editor = pref.edit();
                                        editor.putString("Email",Email);
                                        editor.putString("Password",Pass);
                                        editor.putString("user_id",currentUserID);
                                        editor.apply();
                                        pd.dismiss();
                                        startActivity(intent);

                                    } else {
                                        pd.dismiss();
                                        Toast.makeText(getContext(), "Login failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            pd.dismiss();
                            Toast.makeText(getContext(), "Fields are empty.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                        {
                            Email = email.getEditText().getText().toString().trim();
                            Pass = password.getEditText().getText().toString().trim();
                            if (!TextUtils.isEmpty(Email) && !TextUtils.isEmpty(Pass)) {
                                firebaseAuth.signInWithEmailAndPassword(Email, Pass).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            FirebaseUser user = firebaseAuth.getCurrentUser();
                                            String currentUserID = firebaseAuth.getCurrentUser().getUid().toString();
                                            Toast.makeText(getContext(), "Your Client ID is \n" + currentUserID,
                                                    Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(getContext(), MenuMainActivity.class);
                                            intent.putExtra("user_id", currentUserID);
                                            SharedPreferences.Editor editor = pref.edit();
                                            editor.putString("user_id",currentUserID);
                                            editor.apply();
                                            pd.dismiss();
                                            startActivity(intent);

                                        } else {
                                            pd.dismiss();
                                            Toast.makeText(getContext(), "Login failed.",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                            else
                                {
                                    pd.dismiss();
                                    Toast.makeText(getContext(), "Fields are empty.",
                                            Toast.LENGTH_SHORT).show();
                                }
                        }

    }

    protected void signup()
    {
        final String Email;
        final String Pass;
        email=getView().findViewById(R.id.et_email);
        password=getView().findViewById(R.id.et_password);
        remember_cb=getView().findViewById(R.id.cb_remember_me);
        pref = getContext().getSharedPreferences("com.surmoni.client.user_details",MODE_PRIVATE);
        if(remember_cb.isChecked()){
            Email=email.getEditText().getText().toString().trim();
            Pass=password.getEditText().getText().toString().trim();
            if (!TextUtils.isEmpty(Email) && !TextUtils.isEmpty(Pass))
            {
                firebaseAuth.createUserWithEmailAndPassword(Email,Pass).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            String currentUserID=firebaseAuth.getCurrentUser().getUid();
                            Toast.makeText(getContext(), "Your Client ID is \n"+currentUserID,
                                    Toast.LENGTH_SHORT).show();
                            SensorCRUD sensorCRUD=new SensorCRUD();
                            sensorCRUD.CreateUserDatabase(currentUserID);
                            Intent intent=new Intent(getContext(),MenuMainActivity.class);
                            intent.putExtra("user_id",currentUserID);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("Email",Email);
                            editor.putString("Password",Pass);
                            editor.putString("user_id",currentUserID);
                            editor.apply();
                            pd.dismiss();
                            startActivity(intent);
                        } else {
                            pd.dismiss();
                            Toast.makeText(getContext(), "Signup failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            else
            {
                pd.dismiss();
                Toast.makeText(getContext(), "Fields are empty.",
                        Toast.LENGTH_SHORT).show();
            }
        }
        else
            {
                Email=email.getEditText().getText().toString().trim();
                Pass=password.getEditText().getText().toString().trim();
                if (!TextUtils.isEmpty(Email) && !TextUtils.isEmpty(Pass))
                {
                    firebaseAuth.createUserWithEmailAndPassword(Email,Pass).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                String currentUserID=firebaseAuth.getCurrentUser().getUid();
                                Toast.makeText(getContext(), "Your Client ID is \n"+currentUserID,
                                        Toast.LENGTH_SHORT).show();
                                SensorCRUD sensorCRUD=new SensorCRUD();
                                sensorCRUD.CreateUserDatabase(currentUserID);
                                Intent intent=new Intent(getContext(),MenuMainActivity.class);
                                intent.putExtra("user_id",currentUserID);
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString("user_id",currentUserID);
                                editor.apply();
                                pd.dismiss();
                                startActivity(intent);
                            } else {
                                pd.dismiss();
                                Toast.makeText(getContext(), "Signup failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else
                {
                    pd.dismiss();
                    Toast.makeText(getContext(), "Fields are empty.",
                            Toast.LENGTH_SHORT).show();
                }
            }
    }

    public void ActivateAdds()
    {
        MobileAds.initialize(getContext(), "ca-app-pub-4686298843634340~7338286667");
        AdView adView = new AdView(getContext());
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-4686298843634340/5269137463");
        mAdView = getView().findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }

}


