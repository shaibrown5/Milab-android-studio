package com.example.hw2;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;


/*
        - Create an app with a simple service that returns quotes every X
          minutes (hint: use AlarmManager, a Service and a BroadcastReceiver)
        - The app should popup a quote every 5 minutes using a notification
        - Use whatever quotes you like
        - Tag it as EXERCISE_03

 */


public class MainActivity extends AppCompatActivity{
    static int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Switch startSwitch = (Switch)findViewById(R.id.mainSwitch);
        startSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    QuoteIntentService.startActionSend(startSwitch.getContext());
                } else {
                    QuoteIntentService.startActionCancel(startSwitch.getContext());
                }
            }
        });
    }

    public static int getUniqueId(){
        return ++id;
    }
}