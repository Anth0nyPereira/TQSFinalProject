package com.example.riderapp.Classes;

public class Encomenda {
    private String name;
    private int price;

    public Encomenda(String mName, int mPrice){
        this.name = mName;
        this.price = mPrice;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
