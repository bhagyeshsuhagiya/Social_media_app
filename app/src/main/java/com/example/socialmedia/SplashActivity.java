package com.example.socialmedia;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


public class SplashActivity extends AppCompatActivity {
    Animation topanimation,downanimation,logoanimation,leftanimation,rightanimation;
ImageView logo,top,down,left,right;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        topanimation = AnimationUtils.loadAnimation(this , R.anim.top_animation);
        downanimation = AnimationUtils.loadAnimation(this , R.anim.down_animation);
         leftanimation= AnimationUtils.loadAnimation(this , R.anim.left_animation);
        rightanimation = AnimationUtils.loadAnimation(this , R.anim.right_animation);


        logoanimation = AnimationUtils.loadAnimation(this , R.anim.logo_animation);
         logo = findViewById(R.id.logo);
        top = findViewById(R.id.top);
        down = findViewById(R.id.down);
        left = findViewById(R.id.left);
        right = findViewById(R.id.right);


        logo.setAnimation(logoanimation);
        top.setAnimation(topanimation);
        down.setAnimation(downanimation);
        left.setAnimation(leftanimation);
        right.setAnimation(rightanimation);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(i);
            }
        }, 3000);
    }


}