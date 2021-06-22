package ua.deti.tqs.easydeliversadmin.entities;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "state")
public class State {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        return id == state.id && delivery == state.delivery && Objects.equals(description, state.description) && Objects.equals(timestamp, state.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, delivery, timestamp);
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
