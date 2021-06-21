package ua.deti.tqs.easydeliversadmin.entities;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "delivery")
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiModelProperty(value = "Delivery's id",example = "1")
    private int id;

    @Column(name="store")
    @ApiModelProperty(value = "Store's id",example = "1")
    private int store;

    @Column(name="rider_fee")
    @ApiModelProperty(value = "Rider's Fee",example = "5")
    @Min(value = 0, message = "Fee should not be less than 0")
    private int rider_fee;

    @Column(name="state")
    @NotBlank
    @ApiModelProperty(value = "Delivery's Current State",example = "awaiting_processing")
    private String state;

    @Column(name="client_telephone")
    @NotBlank
    @ApiModelProperty(value = "Client's Telephone",example = "912931231")
    private String client_telephone;

    @Column(name="start")
    @NotBlank
    @ApiModelProperty(value = "Delivery's Start",example = "Loc1")
    private String start;

    @Column(name="destination")
    @NotBlank
    @ApiModelProperty(value = "Delivery's Destination",example = "Loc2")
    private String destination;

    @Column(name="rider")
    @ApiModelProperty(value = "Rider's ID",example = "1")
    private int rider;

    @Column(name="score")
    private int score;

    public Delivery(int store,int rider_fee, String state, String client_telephone, String start, String destination) {
        this.store = store;
        this.rider_fee = rider_fee;
        this.state = state;
        this.client_telephone = client_telephone;
        this.start = start;
        this.destination = destination;
        this.score = 0;
    }

    public Delivery() {

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

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

}
