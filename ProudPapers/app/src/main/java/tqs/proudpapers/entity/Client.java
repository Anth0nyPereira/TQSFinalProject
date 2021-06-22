package tqs.proudpapers.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author wy
 * @date 2021/6/3 15:50
 */
@Data
@Entity
@Table(name= "client")
public class Client implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "Client's id",example = "1")
    private Integer id;

    @Column(name="name")
    @NotBlank
    @ApiModelProperty(value = "Client's name",example = "Alex")
    private String name;

    @Column(name="email")
    @Email
    @ApiModelProperty(value = "Client's email",example = "alex@ua.pt")
    private String email;

    @Column(name="password")
    @NotBlank
    @ApiModelProperty(value = "Client's password")
    private String password;

    @Column(name="address")
    @NotBlank
    @ApiModelProperty(value = "Client's address (zip and city).", example = "2222-222, aveiro")
    private String address;

    @Column(name="telephone")
    @NotBlank
    @ApiModelProperty(value = "Client's telephone.", example = "915 111 111")
    private String telephone;

    @Column(name="payment_method")
    @ApiModelProperty(value = "Client's payment method id.", example = "1")
    private Integer paymentMethodId;

    public void setPaymentMethodId(Integer id) {
        this.paymentMethodId = id;
    }

    public void setAddress(String s) {
        this.address = s;
    }
}
