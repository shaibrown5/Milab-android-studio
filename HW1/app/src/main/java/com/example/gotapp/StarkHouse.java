package com.example.gotapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class StarkHouse extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starkhouse);

        RecyclerView rView = (RecyclerView)findViewById(R.id.slist);
        rView.setLayoutManager(new LinearLayoutManager(this));
    }
}