package tqs.proudpapers.entity;

import lombok.Data;
import javax.persistence.*;

/**
 * @author wy
 * @date 2021/6/3 16:05
 */
@Data
@Entity
@Table(name="delivery")
public class Delivery{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="total_price")
    private Double totalPrice;

    @Column(name="client")
    private Integer client;

    @Column(name="state")
    private String state;

    @Column(name="id_delivery_store")
    private Integer idDeliveryStore;
}
