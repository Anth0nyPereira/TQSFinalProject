package com.example.riderapp.Classes;

import com.google.gson.annotations.SerializedName;

public class Encomenda {

    @SerializedName("destination")
    private String destination;
    @SerializedName("start")
    private String start;
    @SerializedName("rider_fee")
    private int rider_fee;

    @SerializedName("client_telephone")
    private String client_telephone;



    public Encomenda(String mDestination, String mStart, int rider_fee, String client_telephone){
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
