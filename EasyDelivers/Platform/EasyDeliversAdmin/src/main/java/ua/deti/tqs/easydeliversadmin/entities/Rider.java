package ua.deti.tqs.easydeliversadmin.entities;

import javax.persistence.*;

@Entity
@Table(name = "Rider")
public class Rider {
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private int id;
        private String name;
        private String email;
        private String password;
        private String telephone;
        private int deleviry_radius;
        private String transportation;

        public Rider(String name, String email, String password, String telephone, String transportation) {
                this.name = name;
                this.email = email;
                this.password = password;
                this.telephone = telephone;
                this.transportation = transportation;
                this.deleviry_radius = 50;
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

        public int getDeleviry_radius() {
                return deleviry_radius;
        }

        public void setDeleviry_radius(int deleviry_radius) {
                this.deleviry_radius = deleviry_radius;
        }

        public String getTransportation() {
                return transportation;
        }

        public void setTransportation(String transportation) {
                this.transportation = transportation;
        }
}
