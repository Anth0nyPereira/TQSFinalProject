package com.example.riderapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.riderapp.Activities.LoginActivity;
import com.example.riderapp.Activities.SignUpActivity;

public class LoginSignUpActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "LoginSignInActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_sign_up);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonSignUp:
                Log.w(TAG, "Sign Up");
                startActivity(new Intent(this, SignUpActivity.class));
                finish();
                break;
            case R.id.buttonLogin:
                Log.w(TAG, "Login");
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
        }
    }



}