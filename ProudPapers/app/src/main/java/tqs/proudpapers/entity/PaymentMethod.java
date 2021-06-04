package tqs.proudpapers.entity;

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
    private Integer id;

    @Column(name = "card_number")
    private String cardNumber;

    @Column(name = "card_expiration_month")
    private Integer cardExpirationMonth;

    @Column(name = "cvc")
    private String cvc;
}
