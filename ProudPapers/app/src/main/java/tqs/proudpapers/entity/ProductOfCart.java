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
@Table(name="cart_products")
public class ProductOfCart {
    @Id
    private Integer cart;

    @Column(name="product")
    private Integer productId;

    @Column(name="quantity")
    private Integer quantity;
}
