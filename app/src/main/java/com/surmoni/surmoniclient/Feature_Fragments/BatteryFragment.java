package com.surmoni.surmoniclient.Feature_Fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

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

public class BatteryFragment extends Fragment {
    private String user_id;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRefrence;
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
            user_id = bundle.getString("user_id");
        }
        return inflater.inflate(R.layout.fragment_battery, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        BatteryData();
        ActivateAdds();
    }

    public void BatteryData() {

        TextView tv = getView().findViewById(R.id.tv_battery_status);
        ProgressBar progressBar = getView().findViewById(R.id.pb_battery);
        GetBatteryData(user_id,tv,progressBar);
    }

    public void GetBatteryData(String user_id,final TextView tv,final ProgressBar pb)
    {
        mDatabase= FirebaseDatabase.getInstance();
        mRefrence=mDatabase.getReference(user_id);
        mRefrence.child("Charge").child("battery_status").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Long data=dataSnapshot.getValue(Long.class);

                    tv.setText(data + " %");
                    pb.setProgress(Math.round(Long.valueOf(data)));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void ActivateAdds()
    {
        MobileAds.initialize(getContext(), "ca-app-pub-4686298843634340~7338286667");
        AdView adView = new AdView(getContext());
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-4686298843634340/1883592070");
        mAdView = getView().findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }
}