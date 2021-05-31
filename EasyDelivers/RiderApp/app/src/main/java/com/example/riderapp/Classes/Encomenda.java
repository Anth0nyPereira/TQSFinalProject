package com.example.riderapp.Classes;

public class Encomenda {
    private String name;
    private String destination;
    private String start;

    public Encomenda(String mName, String mDestination,String mStart){
        this.name = mName;
        this.destination = mDestination;
        this.start = mStart;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
