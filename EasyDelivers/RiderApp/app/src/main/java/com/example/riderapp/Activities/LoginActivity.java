package com.example.riderapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.riderapp.Classes.User;
import com.example.riderapp.Connections.API_Connection;

import com.example.riderapp.Connections.API_Service;
import com.example.riderapp.MainActivity;
import com.example.riderapp.R;
import com.example.riderapp.Services.RiderService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    API_Connection api_connection;
    EditText emailText;
    EditText passwordText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        api_connection = API_Service.getClient();
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        emailText =findViewById(R.id.editTextTextEmail1);
        passwordText =findViewById(R.id.editTextTextPassword1);
    }
        public void onClick(View v) {
            String email = emailText.getText().toString();
            String password= passwordText.getText().toString();
            Log.w("LoginProcess", "EMAIL:"+ email);
            Call<User> call1 = api_connection.api_login(email,password);
            call1.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    User logged = response.body();
                    if (logged==null){
                        emailText.getText().clear();
                        passwordText.getText().clear();
                        Toast.makeText(getApplicationContext(),"Invalid Login",Toast.LENGTH_SHORT).show();
                        Log.w("LoginProcess", "Invalid Login");
                    }
                    else{
                        SharedPreferences sharedPreferences = getSharedPreferences("UserData",MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("FirstName", logged.getFirstname());
                        editor.putString("LastName", logged.getLastname());
                        editor.putString("Email", logged.getEmail());
                        editor.putString("Telephone", logged.getTelephone());
                        editor.putString("Transportation", logged.getTransportation());
                        editor.putInt("Delivery_radius", logged.getDelivery_radius());
                        editor.apply();
                        Log.w("LoginProcess", "Sucess:"+ logged.toString());
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }
                }
                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    call.cancel();
                    emailText.getText().clear();
                    passwordText.getText().clear();

                }
            });

        }
}