package com.example.stocksapp;


import android.app.NotificationChannel;
import android.app.NotificationManager;
    import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private RequestQueue m_queue;
    private static final String m_REQUEST_URL = "http://10.0.2.2:8080/";
    private static final String m_USERNAME = "username";
    private static final String m_Channel = "channel";

    @Override
    public void onCreate() {
        super.onCreate();
        m_queue = Volley.newRequestQueue(this);
    }


    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    String value = remoteMessage.getNotification().getBody();
                    String symbol = remoteMessage.getNotification().getTitle();

                    Intent intent = new Intent(MyFirebaseMessagingService.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    //PendingIntent pendingIntent = new PendingIntent(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        int importance = NotificationManager.IMPORTANCE_HIGH;

                        NotificationChannel channel = new NotificationChannel(m_Channel, "updatedChanel", importance);
                        NotificationManager notificationManager = getSystemService(NotificationManager.class);
                        notificationManager.createNotificationChannel(channel);
                    }

                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), m_Channel)
                            .setSmallIcon(R.drawable.notification)
                            .setContentTitle("Stock Update")
                            .setContentText(value + ": " + symbol)
                            .setAutoCancel(true)
                            .setPriority(NotificationManager.IMPORTANCE_HIGH);
                            //.setContentIntent(pendingIntent);

                    NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                    Log.d(TAG, "sending notification");
                    notificationManager.notify(1, builder.build());
                }
            });

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

    /**
    * gets new token and send to registration
     */
    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);
        super.onNewToken(token);
        Log.d(TAG, "on new token called");
        sendRegistrationToServer(token);
    }

    /**
     * This method send the token to registration
     * @param token - the token to send
     */
    private void sendRegistrationToServer(String token) {
        JSONObject requestObject = new JSONObject();
        try {
            requestObject.put("token", token);
        }
        catch (JSONException e) {}

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST,  m_REQUEST_URL + m_USERNAME +  "/token",
                requestObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i(TAG, "Token saved successfully");
                Toast.makeText(MyFirebaseMessagingService.this, "server received token", Toast.LENGTH_SHORT).show();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Failed to save token - " + error);
                    }
                });
        m_queue.add(req);
    }
}
