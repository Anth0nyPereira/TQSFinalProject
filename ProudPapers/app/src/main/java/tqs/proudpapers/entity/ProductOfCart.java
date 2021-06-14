package tqs.proudpapers.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author wy
 * @date 2021/6/13 9:59
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="cart_products")
@IdClass(ProductOfCartId.class)
public class ProductOfCart implements Serializable {
    @Id
    @Column(name="cart")
    private Integer cart;

    @Id
    @Column(name="product")
    private Integer productId;

    @Column(name="quantity")
    private Integer quantity;

}
