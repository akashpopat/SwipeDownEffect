package com.akashpopat.swipedown;

import android.content.ClipData;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;

public class FullScreenImageActivity extends AppCompatActivity {

    private static final String TAG = "heyFull";
    private float startX;
    private float startY;
    private RelativeLayout rootView;
    private int windowWidth;
    private int windowHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);


        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        rootView = (RelativeLayout) findViewById(R.id.rootView);

        Picasso.with(this)
                .load("https://s3-ap-southeast-1.amazonaws.com/seenitcdn/39699599417a6e30c5.jpg")
                .into(imageView);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        windowWidth = size.x;
        windowHeight = size.y;

        imageView.setOnTouchListener(new MyTouchListener());
        imageView.getRootView().setOnDragListener(new MyDragView());
    }

    private final class MyTouchListener implements View.OnTouchListener {
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
                        view);

                view.startDrag(data, shadowBuilder, view, 0);
                startX = motionEvent.getX();
                startY = motionEvent.getY();
                Log.d(TAG, "onTouch: startDrag");
                view.setVisibility(View.INVISIBLE);

                return true;
            } else {
                return false;
            }
        }
    }


    private class MyDragView implements View.OnDragListener {
        @Override
        public boolean onDrag(View v, DragEvent event) {
            float X = event.getX();
            float Y = event.getY();

            switch (event.getAction()) {

                case DragEvent.ACTION_DRAG_LOCATION:
                    if((Math.abs(startX - X) < windowWidth/10) && (Math.abs(startY - Y) < windowHeight/10)) {
                        Log.d(TAG, "onDrag: close");
                        rootView.setBackgroundColor(Color.BLACK);
                    }
                    else {
                        float xDis = (Math.abs(startX - X) / windowWidth);
                        float yDis =  (Math.abs(startY - Y) / windowHeight);

                        float decreaseAlphaBy = xDis > yDis ? xDis * 250 : yDis * 250;

//                        Log.d(TAG, "onDrag: descrease by " + decreaseAlphaBy);

                        int alpha = (int) (230 - decreaseAlphaBy);

                        rootView.setBackgroundColor(Color.argb(alpha,0,0,0));
                    }
                    break;

                case DragEvent.ACTION_DROP:

                    View view = (View) event.getLocalState();
                    if((Math.abs(startX - X) < windowWidth/10) && (Math.abs(startY - Y) < windowHeight/10)){

                        // close to origin
                        view.setVisibility(View.VISIBLE);
                    }
                    else {
                        view.setX(X - (view.getWidth() / 2));
                        view.setY(Y - (view.getHeight() / 2));
                        view.setVisibility(View.VISIBLE);
                        supportFinishAfterTransition();
                    }

                default:
                    break;
            }
            return true;
        }
    }
}
