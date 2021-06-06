package tqs.proudpapers.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * @author wy
 * @date 2021/6/3 16:05
 */
@Data
@NoArgsConstructor
@Entity
@Table(name="delivery")
public class Delivery implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="total_price")
    private Double total_price;

    @Column(name="client")
    private Integer client;

    @Column(name="state")
    private String state;

    @Transient
    private List<Product> products;
}
