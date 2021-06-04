package ua.deti.tqs.easydeliversadmin.entities;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "delivery")
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(name="store")
    @NotBlank
    private int store;
    @Column(name="rider_fee")
    @NotBlank
    private int rider_fee;
    @Column(name="state")
    @NotBlank
    private String state;
    @Column(name="client_telephone")
    @NotBlank
    private String client_telephone;
    @Column(name="start")
    @NotBlank
    private String start;

    public Delivery(@NotBlank int store, @NotBlank int rider_fee, @NotBlank String state, @NotBlank String client_telephone, @NotBlank String start, @NotBlank String destination) {
        this.store = store;
        this.rider_fee = rider_fee;
        this.state = state;
        this.client_telephone = client_telephone;
        this.start = start;
        this.destination = destination;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStore() {
        return store;
    }

    public void setStore(int store) {
        this.store = store;
    }

    public int getRider_fee() {
        return rider_fee;
    }

    public void setRider_fee(int rider_fee) {
        this.rider_fee = rider_fee;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getClient_telephone() {
        return client_telephone;
    }

    public void setClient_telephone(String client_telephone) {
        this.client_telephone = client_telephone;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public int getRider() {
        return rider;
    }

    public void setRider(int rider) {
        this.rider = rider;
    }

    @Column(name="destination")
    @NotBlank
    private String destination;
    @Column(name="rider")
    private int rider;
}
