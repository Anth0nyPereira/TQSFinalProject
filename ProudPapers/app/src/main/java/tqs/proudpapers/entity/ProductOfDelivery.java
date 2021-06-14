package tqs.proudpapers.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author wy
 * @date 2021/6/13 9:59
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="products_of_delivery")
public class ProductOfDelivery {
    @Id
    private Integer delivery;

    @Column(name="product")
    private Integer productId;

    @Column(name="quantity")
    private Integer quantity;
}
