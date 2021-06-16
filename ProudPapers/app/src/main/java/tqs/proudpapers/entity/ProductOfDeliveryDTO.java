package tqs.proudpapers.entity;

import lombok.*;

import javax.persistence.*;

/**
 * @author wy
 * @date 2021/6/13 9:59
 */
@Setter
@ToString
public class ProductOfDeliveryDTO {
    private Integer delivery;

    private Product product;

    private Integer quantity;

    public Product getProduct() {
        return product;
    }
}
