package tqs.proudpapers.entity;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
