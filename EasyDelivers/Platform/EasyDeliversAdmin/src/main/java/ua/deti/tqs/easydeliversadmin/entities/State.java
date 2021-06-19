package ua.deti.tqs.easydeliversadmin.entities;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;

@Entity
@Table(name = "state")
public class State {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;


    @Column(name="description")
    @NotBlank
    private String description;

    @Column(name="delivery")
    private int delivery;

    @Column(name="timestamp")
    private Timestamp timestamp;

    public State() {

    }

    public State(String description, int delivery, Timestamp timestamp) {
        this.description = description;
        this.delivery = delivery;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDelivery() {
        return delivery;
    }

    public void setDelivery(int delivery) {
        this.delivery = delivery;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "State{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", delivery=" + delivery +
                ", timestamp=" + timestamp +
                '}';
    }
}
