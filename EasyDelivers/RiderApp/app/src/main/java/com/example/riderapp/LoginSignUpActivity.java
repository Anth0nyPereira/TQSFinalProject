package com.example.riderapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;

import com.example.riderapp.Fragments.LoginFragment;
import com.example.riderapp.Fragments.SignUpFragment;

public class LoginSignUpActivity extends AppCompatActivity implements View.OnClickListener{

    LoginFragment loginFragment = new LoginFragment();
    SignUpFragment signUpFragment = new SignUpFragment();

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
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (v.getId()) {
            case R.id.buttonSignUp:
                transaction.replace(R.id.container, signUpFragment);
                transaction.commit();;
                break;
            case R.id.buttonLogin:
                transaction.replace(R.id.container,loginFragment );
                transaction.commit();;
                break;
        }
    }



}