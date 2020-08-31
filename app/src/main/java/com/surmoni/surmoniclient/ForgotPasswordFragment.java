package com.surmoni.surmoniclient;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordFragment extends Fragment {

private Button forgotButton;
    private TextInputLayout Email;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_forgot_password, container, false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        Email = getView().findViewById(R.id.et_forgot_email);
        forgotButton=view.findViewById(R.id.bt_forgot);
        forgotButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String email=Email.getEditText().getText().toString().trim();
            FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getContext(), "Recovery Email Sent, Please check your inbox.", Toast.LENGTH_LONG).show();
                                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                                fragmentTransaction.replace(R.id.frame_layout,new LoginSignupFragment());
                                fragmentTransaction.commit();
                            }
                            else{
                                Toast.makeText(getContext(), "Such Email does not exists.", Toast.LENGTH_SHORT).show();
                                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                                fragmentTransaction.replace(R.id.frame_layout,new LoginSignupFragment());
                                fragmentTransaction.commit();
                            }
                        }
                    });
        }
    });
    }
}
