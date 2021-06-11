package com.example.riderapp.Classes;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("id")
    private int id;
    @SerializedName("firstname")
    private String firstname;
    @SerializedName("lastname")
    private String lastname;
    @SerializedName("email")
    private String email;
    @SerializedName("password")
    private String password;
    @SerializedName("telephone")
    private String telephone;
    @SerializedName("delivery_radius")
    private int delivery_radius;
    @SerializedName("transportation")
    private String transportation;

    public User(String firstname, String lastname, String email, String password, String telephone, String transportation) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.telephone = telephone;
        this.transportation = transportation;
        this.delivery_radius = 50;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public int getDelivery_radius() {
        return delivery_radius;
    }

    public void setDelivery_radius(int delivery_radius) {
        this.delivery_radius = delivery_radius;
    }

    public String getTransportation() {
        return transportation;
    }

    public void setTransportation(String transportation) {
        this.transportation = transportation;
    }
}
