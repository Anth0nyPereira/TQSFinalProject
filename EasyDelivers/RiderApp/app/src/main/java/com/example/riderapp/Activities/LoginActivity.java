package com.example.riderapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.riderapp.Classes.User;
import com.example.riderapp.LoginSignUpActivity;
import com.example.riderapp.MainActivity;
import com.example.riderapp.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }

        public void onClick(View v) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
}