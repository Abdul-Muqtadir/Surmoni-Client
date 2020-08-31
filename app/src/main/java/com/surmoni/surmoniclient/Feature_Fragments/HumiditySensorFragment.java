package com.surmoni.surmoniclient.Feature_Fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.surmoni.surmoniclient.R;
import com.surmoni.surmoniclient.SensorCRUD;


public class HumiditySensorFragment extends Fragment {
    private String user_id;
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
        return inflater.inflate(R.layout.fragment_humidity_sensor, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        HumiditySensorData();
        ActivateAdds();
    }

    public void HumiditySensorData()
    {
        SensorCRUD sensorCRUD=new SensorCRUD();
        TextView tv =getView(). findViewById(R.id.tv_humidity);
        ProgressBar progressBar = getView().findViewById(R.id.pb_humidity);
        sensorCRUD.GetSensorData(user_id,"Humidity",tv,progressBar);
    }
    public void ActivateAdds()
    {
        MobileAds.initialize(getContext(), "ca-app-pub-4686298843634340~7338286667");
        AdView adView = new AdView(getContext());
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-4686298843634340/6571999901");
        mAdView = getView().findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }

}
