package com.example.stocksapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdReceiver;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.installations.FirebaseInstallations;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private static String stockName;
    EditText stockNameInput;
    Button submitButton;
    private RequestQueue m_queue;
    private static String token = "";
    private static String prevSentStockName = ""; // the name of the stock already sent
    static double stockValue = 0; // the value of the stock
    private static final String TAG = "MainActivity";
    private static final String REQUEST_URL = "http://192.168.1.23/:8080/"; // tama

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        m_queue = Volley.newRequestQueue(this);
        stockNameInput = (EditText) findViewById(R.id.userInput);
        submitButton = (Button) findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stockName = stockNameInput.getText().toString();
                // Hide keyboard:
                try {
                    InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    mgr.hideSoftInputFromWindow(stockNameInput.getWindowToken(), 0);
                } catch (NullPointerException e) {
                }

                // Same request sent twice in a row, no need to re-fetch stock:
                if (!stockName.equalsIgnoreCase(prevSentStockName)) {
                    stockValue = 0;
                    //fetchStocks(stock);
                }
            }
        });
    }

    private void sendStocks() {
        // get token
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {

                if (!task.isSuccessful()) {
                    Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                    return;
                }

                // Get new FCM registration token
                token = task.getResult();

                // Log and toast
                Log.d(TAG, "token received");
                Toast.makeText(MainActivity.this, "token received", Toast.LENGTH_SHORT).show();
            }
        });

        // create body with info to send to server
        JSONObject requestObject = new JSONObject();
        try {
            requestObject.put("stock", stockName);
            requestObject.put("token", token);
        } catch (JSONException e) {
        }

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, REQUEST_URL + "/input", requestObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i(TAG, "Message sent successfully");
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError e) {
                        // Send error intent to Broadcast Receiver:
                        Intent errorIntent = new Intent();
                        errorIntent.setAction("com.example.stocksapp.MESSAGE_ERROR");
                        sendBroadcast(errorIntent);
                    }
                });

        m_queue.add(req);
    }


}

//in my server use firebase api to send to app