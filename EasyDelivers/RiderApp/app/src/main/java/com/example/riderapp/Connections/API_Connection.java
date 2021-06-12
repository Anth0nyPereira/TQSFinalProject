package com.example.riderapp.Connections;

import com.example.riderapp.Classes.User;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface API_Connection {

    @POST("/api/rider/login")
    Call<User> api_login(@Body JsonObject body);

    @POST("/api/rider/account")
    Call<User> api_signUp(@Body User user);

}
