package com.hactiv8.project1todolist;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
//launch screen which appears for a specific amount of time, generally shows for the first time when the app is launched.

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        time();
    }

    private void time() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Intent I = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(I);
            }
        },1500);
    }
}