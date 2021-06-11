package com.example.riderapp.Connections;

import com.example.riderapp.Classes.User;

import retrofit2.http.POST;

public interface API_Connection {

    @POST("/api/rider/login")
    User api_login(String email, String password);

    @POST("/api/rider/account")
    User api_signUp(String first_name, String last_name, String email, String password, String telefone, String transportation);

}
