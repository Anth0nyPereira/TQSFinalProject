package tqs.proudpapers.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author wy
 * @date 2021/6/13 9:59
 */
@AllArgsConstructor
@NoArgsConstructor
@Setter
@EqualsAndHashCode
public class ProductOfCartDTO {
    @ApiModelProperty(notes = "Cart's id",example = "1")
    private Integer cart;

    @ApiModelProperty(notes = "Product's info")
    private Product product;

    @ApiModelProperty(notes = "Quantity of the products in the cart", example = "1")
    private Integer quantity;

    public Product getProduct() {
        return product;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
