package com.example.gotapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class LannisterHouse extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lannister_house);

        RecyclerView rView = (RecyclerView)findViewById(R.id.lList);
        rView.setLayoutManager(new LinearLayoutManager(this));
    }
}