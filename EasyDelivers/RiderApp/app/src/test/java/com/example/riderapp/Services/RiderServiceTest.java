package com.example.riderapp.Services;


import com.example.riderapp.Classes.User;
import com.example.riderapp.Connections.API_Connection;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.mock.Calls;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class RiderServiceTest {

    API_Connection api_connection;

    User user;
    JsonObject login;
    JsonObject badlogin;

    @BeforeEach
    void setUp() {
        api_connection=mock(API_Connection.class);
        user = new User("user","name","rider@email.com", "pass1234", "930921312","car");
        login = new JsonObject();
        login.addProperty("email","rider@email.com");
        login.addProperty("password","pass1234");
        badlogin = new JsonObject();
        badlogin.addProperty("email","cvc@email.com");
        badlogin.addProperty("password","paassass1234");
    }

    @AfterEach
    void tearDown() {

    }
    //In Tests we can use Execute

    @Test
    @DisplayName("Tests a successful login")
    void successfulLoginTest() throws Exception {
        when(api_connection.api_login(login)).thenReturn(Calls.response(user));
        Call<User> call = api_connection.api_login(login);
        final AtomicReference<Response<User>> responseRef = new AtomicReference<>();
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                responseRef.set(response);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                fail();
            }
        });
        User x = responseRef.get().body();
        assertEquals("rider@email.com",x.getEmail());
        assertEquals("user",x.getFirstname());
        assertEquals("name",x.getLastname());
        assertEquals("pass1234",x.getPassword());
        assertEquals("930921312",x.getTelephone());
        assertEquals("car",x.getTransportation());

        verify(api_connection,times(1))
                .api_login(login);

    }

    @Test
    @DisplayName("Tests a invalid login")
    void invalidLoginTest() throws Exception {
        User nullUser=null;
        when(api_connection.api_login(badlogin)).thenReturn(Calls.response(nullUser));
        Call<User> call = api_connection.api_login(badlogin);
        final AtomicReference<Response<User>> responseRef = new AtomicReference<>();
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                responseRef.set(response);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                fail();
            }
        });
        assertNull(responseRef.get().body());
        verify(api_connection,times(1))
                .api_login(badlogin);

    }

    @Test
    @DisplayName("Tests a successful sign up")
    void successfulSignUpTest() throws Exception {
        when(api_connection.api_signUp(user)).thenReturn(Calls.response(user));
        Call<User> call = api_connection.api_signUp(user);
        final AtomicReference<Response<User>> responseRef = new AtomicReference<>();
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                responseRef.set(response);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
        User x = responseRef.get().body();
        assertEquals("rider@email.com",x.getEmail());
        assertEquals("user",x.getFirstname());
        assertEquals("name",x.getLastname());
        assertEquals("pass1234",x.getPassword());
        assertEquals("930921312",x.getTelephone());
        assertEquals("car",x.getTransportation());
        verify(api_connection, times(1))
                .api_signUp(user);

    }

    @Test
    @DisplayName("Tests an invalid sign up")
    void invalidSignUpTest() throws Exception {
        User nullUser=null;
        when(api_connection.api_signUp(user)).thenReturn(Calls.response(nullUser));
        Call<User> call = api_connection.api_signUp(user);
        final AtomicReference<Response<User>> responseRef = new AtomicReference<>();
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                responseRef.set(response);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
        assertNull(responseRef.get().body());
        verify(api_connection, times(1))
                .api_signUp(user);

    }

}