package tqs.proudpapers.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.Iterator;
import java.util.List;

/**
 * @author wy
 * @date 2021/6/13 11:50
 */
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CartDTO implements Iterable<ProductOfCartDTO>{
    @ApiModelProperty(value = "Cart's id", example = "1")
    private Integer cartId;

    @ApiModelProperty(value = "Id of the owner of the cart" , example = "1")
    private Integer clientId;

    @ApiModelProperty(value = "Products in the cart")
    private List<ProductOfCartDTO> productOfCarts;

    @ApiModelProperty(value = "Total price", example = "1.0")
    private Double totalPrice;

    @Override
    public Iterator<ProductOfCartDTO> iterator() {
        return productOfCarts.iterator();
    }

    public Integer getCartId() {
        return cartId;
    }

    public List<ProductOfCartDTO> getProductOfCarts() {
        return productOfCarts;
    }

    public int getClientId() {
        return clientId;
    }

}
