package com.surmoni.surmoniclient.Stream;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.surmoni.surmoniclient.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class StreamEnableFragment extends Fragment {

    private Button submit;
    private TextInputLayout stream_id;
    private String user_id;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRefrence;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            user_id=bundle.getString("user_id");
        }
        return inflater.inflate(R.layout.fragment_stream_enable, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        submit=view.findViewById(R.id.bt_submit_key);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetStreamEnableId();
                PlayerFragment playerFragment = new PlayerFragment();
                Bundle bundle = new Bundle();
                bundle.putString("user_id",user_id);
                playerFragment.setArguments(bundle);

                FragmentTransaction fragmentTransaction = ((AppCompatActivity)getContext()).getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout_menu,playerFragment);
                fragmentTransaction.commit();
            }
        });
    }

    public void SetStreamEnableId() {
        mDatabase = FirebaseDatabase.getInstance();
        mRefrence = mDatabase.getReference(user_id);
        stream_id = getView().findViewById(R.id.et_private_key);
        String private_key = stream_id.getEditText().getText().toString();
        if (!TextUtils.isEmpty(private_key)){
            mRefrence.child("Stream").child("stream_enable_id").setValue(private_key);
        }else
        {
            Toast.makeText(getContext(),"Field Empty!!!",Toast.LENGTH_SHORT).show();
        }
    }

}
