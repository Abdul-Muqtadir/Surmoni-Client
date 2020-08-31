package com.surmoni.surmoniclient;

import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SensorCRUD {
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRefrence;

    public void CreateUserDatabase(String user_id) {
        mDatabase= FirebaseDatabase.getInstance();
        mRefrence=mDatabase.getReference(user_id);
        mRefrence.child("Sensor").child("Temperature").child("sensor_id").setValue("1");
        mRefrence.child("Sensor").child("Temperature").child("sensor_name").setValue("temperature");
        mRefrence.child("Sensor").child("Temperature").child("sensor_value").setValue("0");
        mRefrence.child("Sensor").child("Temperature").child("sensor_status").setValue("changed");
        mRefrence.child("Sensor").child("Humidity").child("sensor_id").setValue("1");
        mRefrence.child("Sensor").child("Humidity").child("sensor_name").setValue("humidity");
        mRefrence.child("Sensor").child("Humidity").child("sensor_value").setValue("0");
        mRefrence.child("Sensor").child("Humidity").child("sensor_status").setValue("changed");

        mRefrence.child("Motion").child("motion_detected").setValue("no");
        mRefrence.child("Motion").child("motion_detected_at").setValue("There is a motion");
        mRefrence.child("Motion").child("motion_request").setValue("stop");
        mRefrence.child("Motion").child("motion_camera").setValue("CAMERA_FACING_BACK");
        mRefrence.child("Motion").child("motion_status").setValue("not_changed");

        mRefrence.child("Stream").child("stream_enable_id").setValue("nulll");
        mRefrence.child("Stream").child("stream_id").setValue("nulll");
        mRefrence.child("Stream").child("stream_request").setValue("no");
        mRefrence.child("Stream").child("stream_camera").setValue("CAMERA_FACING_BACK");
        mRefrence.child("Stream").child("stream_status").setValue("stop");

        mRefrence.child("Charge").child("battery_status").setValue(0);
    }

    public void GetSensorData(String user_id,final String sensor_name,final TextView tv,final ProgressBar pb)
    {
        mDatabase= FirebaseDatabase.getInstance();
        mRefrence=mDatabase.getReference(user_id);
        mRefrence.child("Sensor").child(sensor_name).child("sensor_value").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String data=dataSnapshot.getValue(String.class);
                if(sensor_name.equals("Humidity"))
                {
                    tv.setText(sensor_name+" = " + data + " %");
                    pb.setProgress(Math.round(Float.valueOf(data)));
                }
                else
                {
                    tv.setText(sensor_name+" = " + data + " °C");
                    pb.setProgress(Math.round(Float.valueOf(data)));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
