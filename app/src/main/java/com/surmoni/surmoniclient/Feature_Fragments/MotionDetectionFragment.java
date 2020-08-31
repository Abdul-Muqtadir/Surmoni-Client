package com.surmoni.surmoniclient.Feature_Fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.surmoni.surmoniclient.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MotionDetectionFragment extends Fragment {
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRefrence;
    private String user_id;

    private Button startMotionDetect;
    private Button stopMotionDetect;

    private AdView mAdView;

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
        return inflater.inflate(R.layout.fragment_motion_detection, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {

        ActivateAdds();
        startMotionDetect=view.findViewById(R.id.bt_startMotionDetect);
        stopMotionDetect=view.findViewById(R.id.bt_stopMotionDetect);
        startMotionDetect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClickDetectMotion();
            }
        });

        stopMotionDetect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClickStopDetectMotion();
            }
        });
    }




    public void OnClickDetectMotion() {
        mDatabase= FirebaseDatabase.getInstance();
        mRefrence=mDatabase.getReference(user_id);
        mRefrence.child("Motion").child("motion_request").setValue("start");
        mRefrence.child("Motion").child("motion_detected").addValueEventListener(new ValueEventListener() {
            TextView tv=getView().findViewById(R.id.tv_motion);
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String motion_status=dataSnapshot.getValue().toString();
                if(motion_status.equals("yes"))
                {
                    mRefrence.child("Motion").child("motion_detected_at").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String motion_at=dataSnapshot.getValue().toString();
                            tv.setText("Detected at "+motion_at);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
                else
                {
                    tv.setText("no motion detected yet!!!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void OnClickStopDetectMotion() {
        mDatabase= FirebaseDatabase.getInstance();
        mRefrence=mDatabase.getReference(user_id);
        mRefrence.child("Motion").child("motion_request").setValue("stop");
    }

    public void ActivateAdds()
    {
        MobileAds.initialize(getContext(), "ca-app-pub-4686298843634340~7338286667");
        AdView adView = new AdView(getContext());
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-4686298843634340/1009494108");
        mAdView = getView().findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }
}
