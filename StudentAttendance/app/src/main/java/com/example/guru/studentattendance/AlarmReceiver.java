package com.example.guru.studentattendance;

/**
 * Created by Guru on 4/14/2018.
 */

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.widget.Toast;

import static com.example.guru.studentattendance.AppStatus.context;

public class AlarmReceiver extends BroadcastReceiver {

//    @Override
//    public void onReceive(Context context, Intent arg1) {
////        // For our recurring task, we'll just display a message
//        NotificationManager notificationManager =(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//
//        Intent repeating_intent =  new Intent(context,MainActivity.class);
//
//        repeating_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//        PendingIntent pendingIntent = PendingIntent.getActivity(context,100,repeating_intent,PendingIntent.FLAG_UPDATE_CURRENT);
//        android.support.v4.app.NotificationCompat.Builder builder = new android.support.v4.app.NotificationCompat.Builder(context)
//                .setContentIntent(pendingIntent)
//                .setSmallIcon(android.R.drawable.arrow_up_float)
//                .setContentTitle("Student Attendance")
//                .setContentText("It's time to mark your attendance")
//                .setAutoCancel(true);
//        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        builder.setSound(alarmSound);
//        notificationManager.notify(100,builder.build());
//
//
//    }


    String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";
    @Override
    public void onReceive(Context arg0, Intent arg1) {
// For our recurring task, we'll just display a message
// Toast.makeText(arg0, "I'm running", Toast.LENGTH_SHORT).show();
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(arg0,NOTIFICATION_CHANNEL_ID)
// NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getContext(), "M_CH_ID");
                .setSmallIcon(android.R.drawable.arrow_up_float)
                .setContentTitle("Student Attendance")
                .setContentText("pls mark your attendance")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        Intent resultIntent = new Intent(arg0, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(arg0);
        stackBuilder.addParentStack(MainActivity.class);

// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) arg0.getSystemService(Context.NOTIFICATION_SERVICE);


        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder.setSound(alarmSound);
// notificationID allows you to update the notification later on.
        mNotificationManager.notify(0, mBuilder.build());

    }
}