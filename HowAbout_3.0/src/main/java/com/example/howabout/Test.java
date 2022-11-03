package com.example.howabout;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.airbnb.lottie.LottieAnimationView;

public class Test extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        LottieAnimationView animationView=findViewById(R.id.animationView);
        animationView.setAnimation("39589-space-ride.json");
        animationView.loop(true);
        animationView.playAnimation();
    }
}