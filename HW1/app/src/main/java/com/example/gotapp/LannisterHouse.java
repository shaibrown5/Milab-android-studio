package com.example.gotapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;

public class LannisterHouse extends AppCompatActivity {

    String houseMemberNames[];
    int images[] ={R.drawable.tywin, R.drawable.cersei, R.drawable.jaime, R.drawable.tyrion, R.drawable.lancel, R.drawable.joffrey,
            R.drawable.myrcella, R.drawable.tommen};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lannister_house);


        houseMemberNames = getResources().getStringArray(R.array.lannister_house);
        RecyclerView rView = (RecyclerView)findViewById(R.id.lList);

        MyAdapter myAdapter = new MyAdapter(this, houseMemberNames, images);
        rView.setAdapter(myAdapter);
        rView.setBackgroundColor(Color.parseColor("#FFCC1212"));
        rView.setLayoutManager(new LinearLayoutManager(this));
    }
}