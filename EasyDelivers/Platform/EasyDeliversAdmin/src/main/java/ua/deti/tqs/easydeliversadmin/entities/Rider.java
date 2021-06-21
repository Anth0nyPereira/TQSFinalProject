package ua.deti.tqs.easydeliversadmin.entities;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Objects;

@Entity
@Table(name = "Rider")
public class Rider {
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        @ApiModelProperty(value = "Rider's id",example = "1")
        private int id;
        @Column(name="first_name")
        @NotBlank
        @ApiModelProperty(value = "Rider's First Name",example = "Ana")
        private String firstname;

        @Column(name="last_name")
        @ApiModelProperty(value = "Rider's Last Name",example = "Pereira")
        private String lastname;
        @Column(name="email")
        @Email
        @ApiModelProperty(value = "Rider's Email",example = "ana@email.pt")
        private String email;
        @Column(name="password")
        @NotBlank
        @ApiModelProperty(value = "Rider's Password",example = "pass")
        private String password;
        @Column(name="telephone")
        @NotBlank
        @ApiModelProperty(value = "Rider's Telephone",example = "912931231")
        private String telephone;
        @Column(name="delivery_radius")
        @ApiModelProperty(value = "Rider's Delivery Radius",example = "50")
        private int delivery_radius;
        @Column(name="transportation")
        @NotBlank
        @ApiModelProperty(value = "Rider's Transportation",example = "5")
        private String transportation;
        @Column(name="salary")
        @NotBlank
        private Double salary;
        @Column(name="score")
        @NotBlank
        private Double score;

        public Rider(String firstname, String lastname, String email, String password, String telephone, String transportation) {
                this.firstname = firstname;
                this.lastname = lastname;
                this.email = email;
                this.password = password;
                this.telephone = telephone;
                this.transportation = transportation;
                this.delivery_radius = 50;
                this.salary = 0.0;
                this.score = 0.0;
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

        public Double getSalary() {
                return salary;
        }

        public void setSalary(Double salary) {
                this.salary = salary;
        }

        public Double getScore() {
                return score;
        }

        public void setScore(Double score) {
                this.score = score;
        }

        @Override
        public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                Rider rider = (Rider) o;
                return delivery_radius == rider.delivery_radius && Objects.equals(firstname, rider.firstname) && Objects.equals(lastname, rider.lastname) && Objects.equals(email, rider.email) && Objects.equals(password, rider.password) && Objects.equals(telephone, rider.telephone) && Objects.equals(transportation, rider.transportation) && Objects.equals(salary, rider.salary);
        }

        @Override
        public int hashCode() {
                return Objects.hash(firstname, lastname, email, password, telephone, delivery_radius, transportation, salary);
        }
}
