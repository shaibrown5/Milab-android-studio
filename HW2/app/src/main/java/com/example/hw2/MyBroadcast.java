package com.example.hw2;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class MyBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // get quotes
        String[] quote = Quotes.getQuote();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "C1")
                .setSmallIcon(R.drawable.joke_icon)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentTitle(quote[0])
                .setContentText(quote[1]);
        int id = MainActivity.getUniqueId();
        notificationManager.notify(id, builder.build());
    }
}
