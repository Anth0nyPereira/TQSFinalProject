package com.example.riderapp.Classes;

import com.google.gson.annotations.SerializedName;

public class Encomenda {

    @SerializedName("id")
    private int id;

    @SerializedName("destination")
    private String destination;
    @SerializedName("start")
    private String start;
    @SerializedName("rider_fee")
    private int rider_fee;


    @SerializedName("client_telephone")
    private String client_telephone;



    public Encomenda(int id,String mDestination, String mStart, int rider_fee, String client_telephone){
        this.id=id;
        this.destination = mDestination;
        this.start = mStart;
        this.rider_fee = rider_fee;
        this.client_telephone = client_telephone;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public int getRider_fee() {
        return rider_fee;
    }

    public void setRider_fee(int rider_fee) {
        this.rider_fee = rider_fee;
    }

    public String getClient_telephone() {
        return client_telephone;
    }

    public void setClient_telephone(String client_telephone) {
        this.client_telephone = client_telephone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Encomenda{" +
                "destination='" + destination + '\'' +
                ", start='" + start + '\'' +
                ", rider_fee=" + rider_fee +
                ", client_telephone='" + client_telephone + '\'' +
                '}';
    }
}
