package com.example.gotapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Layout;

public class StarkHouse extends AppCompatActivity {

    String houseMemberNames[];
    int images[] ={R.drawable.eddard, R.drawable.catelyn, R.drawable.robb, R.drawable.sansa, R.drawable.bran, R.drawable.arya,
            R.drawable.rickon, R.drawable.jon};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starkhouse);


        houseMemberNames = getResources().getStringArray(R.array.stark_house);
        RecyclerView rView = (RecyclerView)findViewById(R.id.slist);
        MyAdapter myAdapter = new MyAdapter(this, houseMemberNames, images, "#7E8188");
        rView.setAdapter(myAdapter);
        rView.setLayoutManager(new LinearLayoutManager(this));
    }
}