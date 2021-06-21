package tqs.proudpapers.entity;

import lombok.*;

import java.util.Date;

/**
 * @author wy
 * @date 2021/6/13 9:59
 */
@Setter
@Getter
@ToString
public class ProductOfDeliveryDTO {
    private Integer delivery;

    private Product product;

    private Integer quantity;

    private Date lastUpdate;
}
