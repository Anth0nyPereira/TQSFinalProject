package tqs.proudpapers.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author wy
 * @date 2021/6/3 15:50
 */
@Getter
@Setter
@Entity
@Table(name= "client")
public class Client implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="name")
    @NotBlank
    private String name;

    @Column(name="email")
    @Email
    private String email;

    @Column(name="password")
    @NotBlank
    private String password;

    @Column(name="address")
    @NotBlank
    private String address;

    @Column(name="telephone")
    @NotBlank
    private String telephone;

    @Column(name="payment_method")
    private Integer paymentMethodId;

    public void setPaymentMethodId(Integer id) {
        this.paymentMethodId = id;
    }

    public void setAddress(String s) {
        this.address = s;
    }
}
