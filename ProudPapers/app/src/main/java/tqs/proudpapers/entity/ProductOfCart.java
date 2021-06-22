package tqs.proudpapers.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author wy
 * @date 2021/6/13 9:59
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name="cart_products")
@IdClass(ProductOfCartId.class)
public class ProductOfCart implements Serializable {
    @Id
    @Column(name="cart")
    @ApiModelProperty(value = "Cart's id",example = "1")
    private Integer cart;

    @Id
    @Column(name="product")
    @ApiModelProperty(value = "Product's id",example = "1")
    private Integer productId;

    @Column(name="quantity")
    @ApiModelProperty(value = "Quantity of the product in the cart",example = "1")
    private Integer quantity;

}
