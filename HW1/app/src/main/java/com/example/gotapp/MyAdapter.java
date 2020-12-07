package com.example.gotapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import org.w3c.dom.Text;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private String[] names;
    private int images[];
    private String textColor;
    Context context;

    public MyAdapter(Context ct, String name[], int img[], String color){
        context = ct;
        names = name;
        images = img;
        textColor = color;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.mTextView.setText(names[position]);
        holder.mTextView.setTextColor(Color.parseColor(textColor));
        holder.mImageView.setImageResource(images[position]);
    }

    @Override
    public int getItemCount() {
        return names.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView mTextView;
        private ImageView mImageView;

        public MyViewHolder(@NonNull View view){
            super(view);
            mTextView = view.findViewById(R.id.memberName);
            mImageView = view.findViewById(R.id.picture);
        }
    }
}
