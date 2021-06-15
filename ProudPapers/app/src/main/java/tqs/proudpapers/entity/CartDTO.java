package tqs.proudpapers.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

/**
 * @author wy
 * @date 2021/6/13 11:50
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDTO implements Iterable<ProductOfCartDTO>{
    private Integer cartId;

    private Integer clientId;

    private List<ProductOfCartDTO> productOfCarts;

    private Double totalPrice;

    @Override
    public Iterator<ProductOfCartDTO> iterator() {
        return productOfCarts.iterator();
    }
}
