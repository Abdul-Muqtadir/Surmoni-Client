package com.surmoni.surmoniclient;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class LoadingFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Thread welcomeThread = new Thread() {

            @Override
            public void run() {
                try {
                    super.run();
                    sleep(3500);  //Delay of 10 seconds
                } catch (Exception e) {

                } finally {
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frame_layout,new LoginSignupFragment());
                    fragmentTransaction.commit();
                }
            }
        };
        welcomeThread.start();
        return inflater.inflate(R.layout.fragment_loading, container, false);
    }

}
