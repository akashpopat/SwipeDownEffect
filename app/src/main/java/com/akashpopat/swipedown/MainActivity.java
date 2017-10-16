package com.akashpopat.swipedown;

import android.app.ActivityOptions;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ImageView imageView = (ImageView) findViewById(R.id.imageView);

        Picasso.with(this)
                .load("https://s3-ap-southeast-1.amazonaws.com/seenitcdn/39699599417a6e30c5.jpg")
                .into(imageView);


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ActivityOptions options = ActivityOptions
                        .makeSceneTransitionAnimation(MainActivity.this, imageView, "image");

                startActivity(new Intent(MainActivity.this, FullScreenImageActivity.class), options.toBundle());
            }
        });

    }
}
