package com.example.riderapp.Connections;

import com.example.riderapp.Classes.Encomenda;
import com.example.riderapp.Classes.User;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface API_Connection {

    @POST("/api/rider/login")
    Call<User> api_login(@Body JsonObject body);

    @POST("/api/rider/account")
    Call<User> api_signUp(@Body User user);

    @GET("/api/rider/deliveries")
    Call<List<Encomenda>> api_get_deliveries();

}
