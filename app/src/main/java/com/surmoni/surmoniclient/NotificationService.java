package com.surmoni.surmoniclient;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NotificationService extends Service {
    //creating a mediaplayer object
    SharedPreferences pref;
    SharedPreferences user_settings;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRefrence;

    private Notification notification;

    public static final String ACTION_START_FOREGROUND_SERVICE = "ACTION_START_FOREGROUND_SERVICE";

    public static final String ACTION_STOP_FOREGROUND_SERVICE = "ACTION_STOP_FOREGROUND_SERVICE";


    public void onCreate()
    {
        user_settings = getSharedPreferences("com.surmoni.client.user_settings",MODE_PRIVATE);
        super.onCreate();
        notification=new Notification();
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {

        new Thread(new Runnable() {
            @Override
            public void run() {
//                if(intent != null)
//                {
//                    String action = intent.getAction();
//
//                    switch (action) {
//                        case ACTION_START_FOREGROUND_SERVICE:
                            pref = getSharedPreferences("com.surmoni.client.user_details",MODE_PRIVATE);
                            String user_id=pref.getString("user_id",null);
                            startForegroundService(user_id);
//                           break;
//                        case ACTION_STOP_FOREGROUND_SERVICE:
//                            stopForegroundService();
//                            break;
//                    }
//                }
           }
        }).start();

        return super.onStartCommand(intent, flags, startId);
    }



    private void stopForegroundService()
    {
        // Stop foreground service and remove the notification.
        stopForeground(true);

        // Stop the foreground service.
        stopSelf();
    }

    private int startForegroundService(String user_id)
    {
        notifyAboutSensorData(user_id,"Humidity");
        notifyAboutSensorData(user_id,"Temperature");
        notifyAboutMotionDetection(user_id);
        notifyAboutBattery(user_id);
        return START_STICKY;
    }
    public void notifyAboutSensorData(String user_id,final String sensor_name)
    {
        mDatabase= FirebaseDatabase.getInstance();
        mRefrence=mDatabase.getReference(user_id);
        mRefrence.child("Sensor").child(sensor_name).child("sensor_value").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String data=dataSnapshot.getValue(String.class);
                float humidity=Math.round(Float.valueOf(data));
                if(sensor_name.equals("Humidity"))
                {

                    if(user_settings.contains("min_humidity") && user_settings.contains("max_humidity"))
                    {
                        Float min= Float.valueOf(user_settings.getString("min_humidity",null));
                        Float max= Float.valueOf(user_settings.getString("max_humidity",null));

                        if(humidity<min)
                        {

                            notification.addNotification(getBaseContext(),"Low Humidity "+humidity,"Humidity has been critically low.");
                        }
                        else if(humidity>max)
                        {
                            notification.addNotification(getBaseContext(),"High Humidity "+humidity,"Humidity has been critically high.");
                        }
                    }
                    else
                        {
                            if(humidity<15.0)
                            {

                                notification.addNotification(getBaseContext(),"Low Humidity "+humidity,"Humidity has been critically low.");
                            }
                            else if(humidity>50.0)
                            {
                                notification.addNotification(getBaseContext(),"High Humidity "+humidity,"Humidity has been critically high.");
                            }
                        }


                }
                else
                {
                    float temp=Math.round(Float.valueOf(data));
                    if(user_settings.contains("min_temp") && user_settings.contains("max_temp"))
                    {
                        Float min= Float.valueOf(user_settings.getString("min_temp",null));
                        Float max= Float.valueOf(user_settings.getString("max_temp",null));

                        if(temp<min)
                        {

                            notification.addNotification(getBaseContext(),"Low Temperature "+temp,"Temperature has been critically low.");
                        }
                        else if(temp>max)
                        {
                            notification.addNotification(getBaseContext(),"High Temperature "+temp,"Temperature has been critically high.");
                        }
                    }
                    else
                        {
                            if(temp<15.0)
                            {

                                notification.addNotification(getBaseContext(),"Low Temperature "+temp,"Temperature has been critically low.");
                            }
                            else if(temp>50.0)
                            {
                                notification.addNotification(getBaseContext(),"High Temperature "+temp,"Temperature has been critically high.");
                            }
                        }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void notifyAboutMotionDetection(String user_id)
    {
        mDatabase= FirebaseDatabase.getInstance();
        mRefrence=mDatabase.getReference(user_id);
        mRefrence.child("Motion").child("motion_detected_at").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String data=dataSnapshot.getValue(String.class);
                if(user_settings.contains("motion"))
                {

                    String status= user_settings.getString("motion",null);
                    if(status.equals("yes"))
                    {
                        notification.addNotification(getBaseContext(),"Motion Detected At",data);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void notifyAboutBattery(String user_id)
    {
        mDatabase= FirebaseDatabase.getInstance();
        mRefrence=mDatabase.getReference(user_id);
        mRefrence.child("Charge").child("battery_status").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Long data=dataSnapshot.getValue(Long.class);

                if(user_settings.contains("battery_status"))
                {

                    Long status= Long.valueOf(user_settings.getString("battery_status",null));
                    if(data<status)
                    {
                        notification.addNotification(getBaseContext(),"Battery Low",data +"% Remaining");
                    }
                }
                else
                {
                if(data<15)
                    {
                     notification.addNotification(getBaseContext(),"Battery Low",data +"% Remaining");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
