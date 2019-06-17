package com.example.android.materialme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView imageView = findViewById(R.id.sports_image_detail);
        TextView titleView = findViewById(R.id.title_detail);

        int imageRes = getIntent().getIntExtra("image_resource", 0);
        String title = getIntent().getStringExtra("title");

        titleView.setText(title);
        Glide.with(this).load(imageRes).into(imageView);
    }
}
