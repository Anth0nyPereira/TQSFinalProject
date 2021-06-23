package com.example.riderapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.riderapp.Activities.LoginActivity;
import com.example.riderapp.Activities.SignUpActivity;
import com.example.riderapp.Connections.API_Service;

public class LoginSignUpActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "LoginSignInActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        ApplicationInfo ai = null;
        try {
            ai = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            String url = bundle.getString("PlatformAccess");
            API_Service.setBaseUrl(url);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG,e.getMessage());
        }

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