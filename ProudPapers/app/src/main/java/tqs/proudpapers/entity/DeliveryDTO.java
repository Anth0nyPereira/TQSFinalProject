package tqs.proudpapers.entity;

import lombok.Data;
import org.springframework.beans.BeanUtils;

import javax.persistence.*;
import java.util.List;

/**
 * @author wy
 * @date 2021/6/3 16:05
 */
@Data
public class DeliveryDTO {

    public DeliveryDTO(Delivery delivery){
        BeanUtils.copyProperties(delivery, this);
    }

    private Integer id;

    private Double totalPrice = 0.0;

    private Integer client;

    private String state = "awaiting_processing";

    private Integer idDeliveryStore;

    private List<ProductOfDeliveryDTO> productsOfDelivery;
}
