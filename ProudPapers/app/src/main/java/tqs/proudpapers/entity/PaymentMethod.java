package tqs.proudpapers.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author wy
 * @date 2021/6/3 17:56
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "payment_method")
public class PaymentMethod implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(notes = "PaymentMethod's city", example = "1")
    private Integer id;

    @Column(name = "card_number")
    @ApiModelProperty(value = "Bank card number", example = "1234567891234567")
    private String cardNumber;

    @Column(name = "card_expiration_month")
    @ApiModelProperty(value = "Expiration month of the card", example = "12")
    private String cardExpirationMonth;

    @Column(name = "cvc")
    @ApiModelProperty(value = "CVC of the card", example = "123")
    private String cvc;
}
