package com.example.riderapp.Services;

import com.example.riderapp.Classes.User;
import com.example.riderapp.Connections.API_Connection;

public class RiderService {

    private API_Connection api_connection;
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setApi_connection(API_Connection api_connection) {
        this.api_connection = api_connection;
    }

    public User login(String email, String password){
        return null;
    }

    public User signUp(String first_name, String last_name, String email, String password, String telefone, String transportation){
        return null;
    }


}
