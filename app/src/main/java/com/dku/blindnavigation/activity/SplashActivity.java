package com.dku.blindnavigation.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.dku.blindnavigation.R;
import com.dku.blindnavigation.activity.bluetooth.BluetoothConnectActivity;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView capstone_icon = findViewById(R.id.logo);
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.horizon_enter);
        capstone_icon.startAnimation(anim);

        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                applicationStart();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void applicationStart() {
        Intent intent = new Intent(this,
                getSharedPreferences("setting", MODE_PRIVATE).getString("MAC_ADDR", null) != null ?
                        MainMenuActivity.class :
                        MainMenuActivity.class);
        new Handler().postDelayed(() -> {
            startActivity(intent);
            finish();
        }, 1500);
    }

}