package com.example.hw2;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;


/*
        - Create an app with a simple service that returns quotes every X
          minutes (hint: use AlarmManager, a Service and a BroadcastReceiver)
        - The app should popup a quote every 5 minutes using a notification
        - Use whatever quotes you like
        - Tag it as EXERCISE_03

 */


public class MainActivity extends AppCompatActivity {
    static long fiveMin = 300000;
    static int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNotificationChannel();

        Intent intent = new Intent(MainActivity.this, MyBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast( this, 0 ,intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmM = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmM.set(AlarmManager.RTC_WAKEUP,
                1000*30,
                pendingIntent);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            NotificationChannel channel = new NotificationChannel("C1", "C1", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public static int getUniqueId(){
        return ++id;
    }
}