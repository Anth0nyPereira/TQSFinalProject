package com.example.riderapp.Connections;

import com.example.riderapp.Classes.User;

public interface API_Connection {

    User api_login(String email, String password);

    User api_signUp(String first_name, String last_name, String email, String password, String telefone, String transportation);



}
