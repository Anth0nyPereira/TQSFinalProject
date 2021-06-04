package ua.deti.tqs.easydeliversadmin.entities;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "Rider")
public class Rider {
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private int id;
        @Column(name="name")
        @NotBlank
        private String name;
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

        public Rider(String name, String email, String password, String telephone, String transportation) {
                this.name = name;
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

        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
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
