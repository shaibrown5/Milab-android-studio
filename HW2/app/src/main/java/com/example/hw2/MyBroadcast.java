package com.example.hw2;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class MyBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // get quotes
        String quote = Quotes.getQuote();
        if (intent.getAction().equalsIgnoreCase("com.example.hw2.action.SEND")) {
            createNotificationChannel(context);
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "C1")
                    .setSmallIcon(R.drawable.joke_icon)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setContentTitle("quote of the moment:")
                    .setContentText(quote)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(quote)
                            .setBigContentTitle("quote of the moment:"));

            int id = MainActivity.getUniqueId();
            if (notificationManager != null){
                notificationManager.notify(id, builder.build());
            }
        }
    }

    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            if (notificationManager != null){
                NotificationChannel channel = new NotificationChannel("C1", "C1", NotificationManager.IMPORTANCE_HIGH);
                notificationManager.createNotificationChannel(channel);
            }
        }
    }
}
