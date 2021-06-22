package tqs.proudpapers.entity;

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
    private Integer cartId;

    private Integer clientId;

    private List<ProductOfCartDTO> productOfCarts;

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
