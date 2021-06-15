package tqs.proudpapers.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author wy
 * @date 2021/6/14 16:35
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductOfCartId implements Serializable {
    private Integer cart;

    private Integer productId;
}