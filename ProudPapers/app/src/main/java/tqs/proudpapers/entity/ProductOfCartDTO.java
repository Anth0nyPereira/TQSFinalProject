package tqs.proudpapers.entity;

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
    private Integer cart;

    private Product product;

    private Integer quantity;

    public Product getProduct() {
        return product;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
