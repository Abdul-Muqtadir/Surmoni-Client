package com.surmoni.surmoniclient;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

public class Notification {

    public void addNotification(Context context,String title,String text) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(android.R.drawable.ic_dialog_info)
                        .setContentTitle(title)
                        .setContentText(text);

//        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
//        bigTextStyle.setBigContentTitle("Music player implemented by foreground service.");
//        bigTextStyle.bigText("Android foreground service is a android service which can run in foreground always, it can be controlled by user via notification.");
//        // Set big text style
//        builder.setStyle(bigTextStyle);

        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);


        Intent notificationIntent2 = new Intent();
        PendingIntent contentIntent2 = PendingIntent.getActivity(context, 0, notificationIntent2,
                PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(contentIntent2);

        Intent playIntent = new Intent(context, MainActivity.class);
        playIntent.setAction("Open");
        PendingIntent pendingPlayIntent = PendingIntent.getService(context, 0, notificationIntent, 0);
        NotificationCompat.Action playAction = new NotificationCompat.Action(android.R.drawable.ic_dialog_info, "Open", contentIntent);
        builder.addAction(playAction);

        Intent pauseIntent = new Intent(context, MainActivity.class);
        pauseIntent.setAction("Dismiss");
        PendingIntent pendingpauseIntent = PendingIntent.getService(context, 0, notificationIntent2, 0);
        NotificationCompat.Action pauseAction = new NotificationCompat.Action(android.R.drawable.ic_delete, "Dismiss", contentIntent2);
        builder.addAction(pauseAction);



        // Add as notification
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1, builder.build());

    }

}
