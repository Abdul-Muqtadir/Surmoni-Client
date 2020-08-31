package com.surmoni.surmoniclient.MenuFragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import com.surmoni.surmoniclient.Feature_Fragments.BatteryFragment;
import com.surmoni.surmoniclient.Feature_Fragments.ClientIdFragment;
import com.surmoni.surmoniclient.Feature_Fragments.HumiditySensorFragment;
import com.surmoni.surmoniclient.Feature_Fragments.MotionDetectionFragment;
import com.surmoni.surmoniclient.Feature_Fragments.TemperatureSensorFragment;
import com.surmoni.surmoniclient.HelpFragment;
import com.surmoni.surmoniclient.MainActivity;
import com.surmoni.surmoniclient.NotificationService;
import com.surmoni.surmoniclient.R;
import com.surmoni.surmoniclient.Settings;
import com.surmoni.surmoniclient.Stream.PlayerFragment;
import com.surmoni.surmoniclient.Stream.StreamEnableFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MenuMainActivity extends AppCompatActivity {
    public static String user_id;
    private FloatingActionButton fab;
    private int fabCounter;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRefrence;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottom_menu_main);

        Intent intent=getIntent();
        user_id=intent.getStringExtra("user_id");

        Toolbar toolbar=findViewById(R.id.bottom_app_bar);
        setSupportActionBar(toolbar);
        fab=findViewById(R.id.fab);

        ListMenuContentFragment listMenuContentFragment = new ListMenuContentFragment();
        Bundle bundle = new Bundle();
        bundle.putString("user_id",user_id);
        listMenuContentFragment.setArguments(bundle);


        FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_menu, listMenuContentFragment);
        fragmentTransaction.commit();


        Intent serviceIntent=new Intent(getBaseContext(),NotificationService.class);
        startService(serviceIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bottomappbar_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.app_bar_help:
                {
                HelpFragment helpFragment = new HelpFragment();
                Bundle bundle = new Bundle();
                bundle.putString("user_id",user_id);
                helpFragment.setArguments(bundle);

                FragmentTransaction fragmentTransaction = MenuMainActivity.this.getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout_menu,helpFragment);
                fragmentTransaction.commit();
                return true;
                }
            case R.id.app_bar_settings:
                Settings settings = new Settings();
                Bundle bundle = new Bundle();
                bundle.putString("user_id",user_id);
                settings.setArguments(bundle);

                FragmentTransaction fragmentTransaction = MenuMainActivity.this.getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout_menu,settings);
                fragmentTransaction.commit();
                return true;
            case android.R.id.home:
                BottomNavigationDrawerFragment bottomNavDrawerFragment = new BottomNavigationDrawerFragment();
                bottomNavDrawerFragment.show(getSupportFragmentManager(), bottomNavDrawerFragment.getTag());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static String getData()
    {
        return user_id;
    }

    public void ChangeFragmentByFab(View view) {
        fabCounter++;
        if(fabCounter==1)
        {
            TemperatureSensorFragment temperatureSensorFragment = new TemperatureSensorFragment();
            Bundle bundle = new Bundle();
            bundle.putString("user_id", MenuMainActivity.getData());
            temperatureSensorFragment.setArguments(bundle);

            FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout_menu, temperatureSensorFragment);
            fragmentTransaction.commit();
        }
        else if(fabCounter==2)
        {
            HumiditySensorFragment humiditySensorFragment = new HumiditySensorFragment();
            Bundle bundle = new Bundle();
            bundle.putString("user_id",MenuMainActivity.getData());
            humiditySensorFragment.setArguments(bundle);

            FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout_menu,humiditySensorFragment);
            fragmentTransaction.commit();
        }
        else if(fabCounter==3)
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

                        FragmentTransaction fragmentTransaction = MenuMainActivity.this.getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame_layout_menu,streamEnableFragment);
                        fragmentTransaction.commit();
                    }
                    else
                    {
                        PlayerFragment playerFragment = new PlayerFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("user_id",MenuMainActivity.getData());
                        playerFragment.setArguments(bundle);

                        FragmentTransaction fragmentTransaction = MenuMainActivity.this.getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame_layout_menu,playerFragment);
                        fragmentTransaction.commit();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else if(fabCounter==4)
        {
            MotionDetectionFragment motionDetectionFragment = new MotionDetectionFragment();
            Bundle bundle = new Bundle();
            bundle.putString("user_id",MenuMainActivity.getData());
            motionDetectionFragment.setArguments(bundle);

            FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout_menu,motionDetectionFragment);
            fragmentTransaction.commit();
        }
        else if(fabCounter==5)
        {
            BatteryFragment batteryFragment = new BatteryFragment();
            Bundle bundle = new Bundle();
            bundle.putString("user_id",MenuMainActivity.getData());
            batteryFragment.setArguments(bundle);

            FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout_menu,batteryFragment);
            fragmentTransaction.commit();
        }
        else if(fabCounter==6)
        {
            ClientIdFragment clientIdFragment = new ClientIdFragment();
            Bundle bundle = new Bundle();
            bundle.putString("user_id",MenuMainActivity.getData());
            clientIdFragment.setArguments(bundle);

            FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout_menu,clientIdFragment);
            fragmentTransaction.commit();
            fabCounter=0;
        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("EXIT", true);
        startActivity(intent);
    }
}
