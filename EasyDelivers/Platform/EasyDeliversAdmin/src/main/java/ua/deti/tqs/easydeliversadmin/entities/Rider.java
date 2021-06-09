package ua.deti.tqs.easydeliversadmin.entities;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Objects;

@Entity
@Table(name = "Rider")
public class Rider {
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private int id;
        @Column(name="first_name")
        @NotBlank
        private String firstname;
        @Column(name="last_name")
        private String lastname;
        @Column(name="email")
        @Email
        private String email;
        @Column(name="password")
        @NotBlank
        private String password;
        @Column(name="telephone")
        @NotBlank
        private String telephone;
        @Column(name="delivery_radius")
        @NotBlank
        private int delivery_radius;
        @Column(name="transportation")
        @NotBlank
        private String transportation;

        public Rider(String firstname, String lastname, String email, String password, String telephone, String transportation) {
                this.firstname = firstname;
                this.lastname = lastname;
                this.email = email;
                this.password = password;
                this.telephone = telephone;
                this.transportation = transportation;
                this.delivery_radius = 50;
        }

        public Rider() {

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

        @Override
        public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                Rider rider = (Rider) o;
                return delivery_radius == rider.delivery_radius && Objects.equals(firstname, rider.firstname) && Objects.equals(lastname, rider.lastname) && Objects.equals(email, rider.email) && Objects.equals(password, rider.password) && Objects.equals(telephone, rider.telephone) && Objects.equals(transportation, rider.transportation);
        }

        @Override
        public int hashCode() {
                return Objects.hash(firstname, lastname, email, password, telephone, delivery_radius, transportation);
        }
}
