package com.example.riderapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.riderapp.Classes.User;
import com.example.riderapp.Connections.API_Connection;
import com.example.riderapp.Connections.API_Service;
import com.example.riderapp.MainActivity;
import com.example.riderapp.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
    API_Connection api_connection;
    EditText emailText;
    EditText passwordText;
    EditText telephoneText;
    EditText firstNameText;
    EditText lastNameText;
    EditText transportationText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        api_connection = new API_Service().getClient();
        emailText=findViewById(R.id.editTextTextEmailAddress);
        passwordText=findViewById(R.id.editTextTextPassword1);
        telephoneText=findViewById(R.id.editTextPhone);
        firstNameText=findViewById(R.id.editTextTextFirstName);
        lastNameText=findViewById(R.id.editTextTextLastName);
        transportationText=findViewById(R.id.editTextTextTransport);
    }
    public void onClick(View v) {
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();
        String telephone = telephoneText.getText().toString();
        String firstname = firstNameText.getText().toString();
        String lastname = lastNameText.getText().toString();
        String transportation = transportationText.getText().toString();
        Call<User> call1 = api_connection.api_signUp(new User(firstname,lastname,email,password,telephone,transportation));
        call1.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User logged = response.body();
                if(logged!=null){
                    SharedPreferences sharedPreferences = getSharedPreferences("UserData",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("Email", logged.getEmail());
                    editor.putString("Telephone", logged.getTelephone());
                    editor.putString("Transportation", logged.getTransportation());
                    editor.putLong("salary", Double.valueOf(logged.getSalary()).longValue());
                    editor.putString("FirstName", logged.getFirstname());
                    editor.putString("LastName", logged.getLastname());
                    editor.putInt("score", (int)Math.round(logged.getSalary()));
                    editor.putInt("Delivery_radius", logged.getDelivery_radius());
                    editor.putInt("id",logged.getId());
                    editor.apply();
                    Log.w("SignProcessProcess", "Sucess:"+ logged.toString());
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                call.cancel();
                emailText.getText().clear();
                passwordText.getText().clear();
                telephoneText.getText().clear();
                firstNameText.getText().clear();
                lastNameText.getText().clear();
                transportationText.getText().clear();
                Toast.makeText(getApplicationContext(),"Invalid Sign Up",Toast.LENGTH_SHORT).show();
                Log.w("LoginProcess", "Invalid Login");
            }
        });


    }
}