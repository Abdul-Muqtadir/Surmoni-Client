package com.surmoni.surmoniclient;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.preference.ListPreference;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.surmoni.surmoniclient.NotificationService;

import androidx.core.app.ActivityCompat;
import androidx.preference.CheckBoxPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.preference.SwitchPreference;

import static android.content.Context.MODE_PRIVATE;

public class Settings extends PreferenceFragmentCompat {

    private FirebaseDatabase mDatabase;
    private DatabaseReference mRefrence;
    private String user_id;
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        // Indicate here the XML resource you created above that holds the preferences
        setPreferencesFromResource(R.xml.prefrences, rootKey);

        Bundle bundle = getArguments();
        if (bundle != null) {
            user_id=bundle.getString("user_id");
        }

        mDatabase= FirebaseDatabase.getInstance();
        mRefrence=mDatabase.getReference(user_id);

        final SharedPreferences user_prefs = getContext().getSharedPreferences("com.surmoni.client.user_settings",MODE_PRIVATE);

        final PreferenceCategory preferenceCategory=findPreference("settings");

        final ListPreference minTemp=(ListPreference)findPreference("minTemp");
        final ListPreference maxTemp=(ListPreference)findPreference("maxTemp");
        final ListPreference minHumidity=(ListPreference)findPreference("minHumidity");
        final ListPreference maxHumidity=(ListPreference)findPreference("maxHumidity");
        final ListPreference motionCamera=(ListPreference)findPreference("motion");
        final ListPreference streamCamera=(ListPreference)findPreference("stream");
        final ListPreference battery=(ListPreference)findPreference("battery");

        final SwitchPreference enableMotionNotifications=findPreference("enableMotionNotification");


        enableMotionNotifications.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue)
            {
                boolean checked = Boolean.valueOf(newValue.toString());
                if(checked)
                {
                    SharedPreferences.Editor editor = user_prefs.edit();
                    editor.putString("motion","yes");
                    editor.apply();
                }
                else
                {
                    SharedPreferences.Editor editor = user_prefs.edit();
                    editor.putString("motion","no");
                    editor.apply();
                }
                //set your shared preference value equal to checked

                return true;
            }
        });
        //temperature min value setting
        minTemp.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String temp = String.valueOf(newValue.toString());
                SharedPreferences.Editor editor = user_prefs.edit();
                editor.putString("min_temp",temp);
                editor.apply();

                return true;
            }
        });

        //temperature max value setting
        maxTemp.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String temp = String.valueOf(newValue.toString());
                SharedPreferences.Editor editor = user_prefs.edit();
                editor.putString("max_temp",temp);
                editor.apply();

                return true;
            }
        });

        //humidity min value setting
        minHumidity.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String humidity = String.valueOf(newValue.toString());
                SharedPreferences.Editor editor = user_prefs.edit();
                editor.putString("min_humidity",humidity);
                editor.apply();

                return true;
            }
        });

        //humidity min value setting
        maxHumidity.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String humidity = String.valueOf(newValue.toString());
                SharedPreferences.Editor editor = user_prefs.edit();
                editor.putString("max_humidity",humidity);
                editor.apply();

                return true;
            }
        });

        //battery value setting
        battery.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String battery_status = String.valueOf(newValue.toString());
                SharedPreferences.Editor editor = user_prefs.edit();
                editor.putString("battery_status",battery_status);
                editor.apply();

                return true;
            }
        });

        //motion detection value setting
        motionCamera.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String motion = String.valueOf(newValue.toString());
                mRefrence.child("Motion").child("motion_camera").setValue(motion);
                return true;
            }
        });

        //motion detection value setting
        streamCamera.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String stream = String.valueOf(newValue.toString());
                mRefrence.child("Stream").child("stream_camera").setValue(stream);
                return true;
            }
        });


    }


}
