package tqs.proudpapers.entity;

import lombok.AllArgsConstructor;

import javax.persistence.*;

/**
 * @author wy
 * @date 2021/6/13 9:59
 */
@AllArgsConstructor
@Entity
@Table(name="products_of_delivery")
@IdClass(ProductOfDeliveryId.class)
public class ProductOfDelivery{
    @Id
    private Integer delivery;

    @Id
    @Column(name="product")
    private Integer productId;

    @Column(name="quantity")
    private Integer quantity;
}
