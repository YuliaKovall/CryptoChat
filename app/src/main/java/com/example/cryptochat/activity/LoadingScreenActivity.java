package com.example.cryptochat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cryptochat.R;

public class LoadingScreenActivity extends AppCompatActivity {
    Animation rotateLoadingWheelAnimation;
    ImageView imageView;
    private static final long DELAY_TIME = 2500;
    boolean firstLaunch = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);
        imageView=(ImageView)findViewById(R.id.rotatingWheel);
        rotateLoadingWheelAnimation();

        if (firstLaunch) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Start the main activity after the delay time
                    Intent intent = new Intent(LoadingScreenActivity.this, WelcomeScreenActivity.class);
                    startActivity(intent);
                    // Finish the loading activity to remove it from the back stack
                    finish();
                }
            }, DELAY_TIME);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Start the main activity after the delay time
                    Intent intent = new Intent(LoadingScreenActivity.this, MainActivity.class);
                    startActivity(intent);
                    // Finish the loading activity to remove it from the back stack
                    finish();
                }
            }, DELAY_TIME);
        }


    }
    private void rotateLoadingWheelAnimation() {
        rotateLoadingWheelAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate);
        imageView.startAnimation(rotateLoadingWheelAnimation);
    }
}