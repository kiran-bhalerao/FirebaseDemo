package com.example.kiran.firebasedemo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {

    Button gotoButton;
    boolean isLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferences sharedPreferences = getSharedPreferences("LoginData", MODE_PRIVATE);
        isLogin = sharedPreferences.getBoolean("isLogin", false);

        gotoButton = findViewById(R.id.gotoButton);
        gotoButton.setOnClickListener((view) -> {
            if (isLogin) {
                Intent intent = new Intent(SplashActivity.this, SecActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
