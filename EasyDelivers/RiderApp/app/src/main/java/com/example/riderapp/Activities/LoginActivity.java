package com.example.riderapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.riderapp.Connections.API_Connection;

import com.example.riderapp.R;
import com.example.riderapp.Services.RiderService;

public class LoginActivity extends AppCompatActivity {
    API_Connection api_connection;
    RiderService riderService;

    EditText emailText;
    EditText passwordText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        riderService = new RiderService(api_connection);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        emailText =findViewById(R.id.editTextTextEmail1);
        passwordText =findViewById(R.id.editTextTextPassword1);
    }


        public void onClick(View v) {

        }
}