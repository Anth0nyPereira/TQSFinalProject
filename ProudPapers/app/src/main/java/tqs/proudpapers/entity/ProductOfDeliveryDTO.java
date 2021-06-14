package tqs.proudpapers.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @author wy
 * @date 2021/6/13 9:59
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductOfDeliveryDTO {
    private Integer delivery;

    private Product product;

    private Integer quantity;
}
