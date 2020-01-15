package com.kumawat.frozer.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.kumawat.frozer.R;

public class SplashScreen extends AppCompatActivity {
    private boolean flag = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_splash);
    }

    @Override
    protected void onResume() {
        super.onResume();
        flag=true;
        final boolean loggedIn = FirebaseAuth.getInstance().getCurrentUser()!=null;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (flag) {
                    if (loggedIn)
                        startActivity(new Intent(SplashScreen.this, Dashboard.class));
                    else
                        startActivity(new Intent(SplashScreen.this, LoginPanel.class));
                    finish();
                }
            }
        }, 1000);
    }

    @Override
    protected void onPause() {
        flag=false;
        super.onPause();
    }
}
