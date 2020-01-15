package com.kumawat.frozer.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.kumawat.frozer.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final boolean loggedIn = FirebaseAuth.getInstance().getCurrentUser()!=null;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (loggedIn)
                    startActivity(new Intent(MainActivity.this, Dashboard.class));
                else
                    startActivity(new Intent(MainActivity.this, LoginPanel.class));
                finish();
            }
        }, 1000);
    }
}
