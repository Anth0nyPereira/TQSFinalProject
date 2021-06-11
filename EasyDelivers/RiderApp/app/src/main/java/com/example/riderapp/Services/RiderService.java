package com.example.riderapp.Services;

import com.example.riderapp.Classes.User;
import com.example.riderapp.Connections.API_Connection;


public class RiderService {

    private static API_Connection api_connection;

    private User user;

    public RiderService(API_Connection api_connection) {
        this.api_connection = api_connection;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

/**
    public User login(String email, String password){
        return api_connection.api_login(email,password);
    }

    public User signUp(String first_name, String last_name, String email, String password, String telefone, String transportation){
        return api_connection.api_signUp(first_name,last_name,email,password, telefone, transportation);
    }
**/

}
