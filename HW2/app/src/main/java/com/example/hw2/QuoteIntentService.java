package com.example.hw2;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.os.SystemClock;

public class QuoteIntentService extends IntentService {

    private static final String SEND_QUOTE = "com.example.hw2.action.SEND";
    private static final String CANCEL_QUOTE = "com.example.hw2.action.CANCEL";
    private static final int fiveMin = 300000;

    public QuoteIntentService() {
        super("QuoteIntentService");
    }

    /**
     * This method activates the messages
     * @param context
     */
    public static void startActionSend(Context context) {
        Intent intent = new Intent(context, QuoteIntentService.class);
        intent.setAction(SEND_QUOTE);
        context.startService(intent);
    }

    /**
     * This method cancels the messages
     * @param context
     */
    public static void startActionCancel(Context context) {
        Intent intent = new Intent(context, QuoteIntentService.class);
        intent.setAction(CANCEL_QUOTE);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (SEND_QUOTE.equals(action)) {
                handleActionSend();
            } else if (CANCEL_QUOTE.equals(action)) {
                handleActionCancel();
            }
            else {
                throw new RuntimeException("Unknown action provided");
            }
        }
    }

    /**
     * Handle action Send in the provided background thread
     */
    private void handleActionSend() {
        Intent intent = new Intent(this, MyBroadcast.class);
        intent.setAction(SEND_QUOTE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime(),1000*10,
                    pendingIntent);
        }
    }

    /**
     * Handle action Cancel in the provided background thread
     */
    private void handleActionCancel() {
        Intent intent = new Intent(this, MyBroadcast.class);
        intent.setAction(CANCEL_QUOTE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }
}