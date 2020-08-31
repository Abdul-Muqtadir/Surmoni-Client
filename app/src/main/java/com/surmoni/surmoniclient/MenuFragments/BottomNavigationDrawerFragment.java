package com.surmoni.surmoniclient.MenuFragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.surmoni.surmoniclient.Feature_Fragments.BatteryFragment;
import com.surmoni.surmoniclient.Feature_Fragments.ClientIdFragment;
import com.surmoni.surmoniclient.Feature_Fragments.HumiditySensorFragment;
import com.surmoni.surmoniclient.Feature_Fragments.MotionDetectionFragment;
import com.surmoni.surmoniclient.Feature_Fragments.TemperatureSensorFragment;
import com.surmoni.surmoniclient.MainActivity;
import com.surmoni.surmoniclient.NotificationService;
import com.surmoni.surmoniclient.R;
import com.surmoni.surmoniclient.Stream.PlayerFragment;
import com.surmoni.surmoniclient.Stream.StreamEnableFragment;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.Context.MODE_PRIVATE;

public class BottomNavigationDrawerFragment extends BottomSheetDialogFragment {
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRefrence;
    private int fabCounter;
    SharedPreferences pref;
    MenuItem prevItem;
   @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_bottom_menu, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        NavigationView navigationView = (NavigationView) getView().findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();
                menuItem.setChecked(true);
                if(prevItem!=null) {
                    prevItem.setChecked(false);
                }
                prevItem = menuItem;


                switch (id) {
                    case R.id.nav1:
                        {
                        TemperatureSensorFragment temperatureSensorFragment = new TemperatureSensorFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("user_id", MenuMainActivity.getData());
                        temperatureSensorFragment.setArguments(bundle);

                        FragmentTransaction fragmentTransaction = ((AppCompatActivity)getContext()).getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame_layout_menu, temperatureSensorFragment);
                        fragmentTransaction.commit();
                        return true;
                        }
                    case R.id.nav2:
                        {
                        HumiditySensorFragment humiditySensorFragment = new HumiditySensorFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("user_id",MenuMainActivity.getData());
                        humiditySensorFragment.setArguments(bundle);

                        FragmentTransaction fragmentTransaction = ((AppCompatActivity)getContext()).getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame_layout_menu,humiditySensorFragment);
                        fragmentTransaction.commit();
                        return true;
                        }
                    case R.id.nav3:
                    {
                        mDatabase= FirebaseDatabase.getInstance();
                        mRefrence=mDatabase.getReference(MenuMainActivity.getData());
                        mRefrence.child("Stream").child("stream_enable_id").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String data=dataSnapshot.getValue(String.class);
                                if(data.equals("nulll"))
                                {
                                    StreamEnableFragment streamEnableFragment = new StreamEnableFragment();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("user_id",MenuMainActivity.getData());
                                    streamEnableFragment.setArguments(bundle);

                                    FragmentTransaction fragmentTransaction = ((AppCompatActivity)getContext()).getSupportFragmentManager().beginTransaction();
                                    fragmentTransaction.replace(R.id.frame_layout_menu,streamEnableFragment);
                                    fragmentTransaction.commit();
                                }
                                else
                                {
                                    PlayerFragment playerFragment = new PlayerFragment();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("user_id",MenuMainActivity.getData());
                                    playerFragment.setArguments(bundle);

                                    FragmentTransaction fragmentTransaction = ((AppCompatActivity)getContext()).getSupportFragmentManager().beginTransaction();
                                    fragmentTransaction.replace(R.id.frame_layout_menu,playerFragment);
                                    fragmentTransaction.commit();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        return true;
                    }
                    case R.id.nav4:
                    {
                        MotionDetectionFragment motionDetectionFragment = new MotionDetectionFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("user_id",MenuMainActivity.getData());
                        motionDetectionFragment.setArguments(bundle);

                        FragmentTransaction fragmentTransaction = ((AppCompatActivity)getContext()).getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame_layout_menu,motionDetectionFragment);
                        fragmentTransaction.commit();
                        return true;
                    }

                    case R.id.nav5:
                    {
                        BatteryFragment batteryFragment = new BatteryFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("user_id",MenuMainActivity.getData());
                        batteryFragment.setArguments(bundle);

                        FragmentTransaction fragmentTransaction = ((AppCompatActivity)getContext()).getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame_layout_menu,batteryFragment);
                        fragmentTransaction.commit();
                        return true;
                    }
                    case R.id.nav6:
                    {
                        ClientIdFragment clientIdFragment = new ClientIdFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("user_id",MenuMainActivity.getData());
                        clientIdFragment.setArguments(bundle);

                        FragmentTransaction fragmentTransaction = ((AppCompatActivity)getContext()).getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame_layout_menu,clientIdFragment);
                        fragmentTransaction.commit();
                        return true;
                    }
                    case R.id.nav7:
                    {
                        pref = getContext().getSharedPreferences("com.surmoni.client.user_details",MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.clear();
                        editor.commit();

                        pref = getContext().getSharedPreferences("com.surmoni.client.user_settings",MODE_PRIVATE);
                        SharedPreferences.Editor editor_settings = pref.edit();
                        editor_settings.clear();
                        editor_settings.commit();

                        Intent serviceIntent=new Intent(getContext(), NotificationService.class);
                        getContext().stopService(serviceIntent);

                        startActivity(new Intent(getContext(), MainActivity.class));
                        return true;
                    }
                    default:
                        return false;
                }
            }
        });
    }
}