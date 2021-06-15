package tqs.proudpapers.service;

import tqs.proudpapers.entity.*;

import java.util.List;

/**
 * @author wy
 * @date 2021/6/14 14:45
 */
public interface DeliveryService {
    Delivery getDeliveryById(Integer id);

    Integer addProductToDelivery(Integer clientId, List<ProductOfCartDTO> product);

    void changeStateOfDelivery(Integer delivery, String state);

    void setDeliveryIdInStore(Integer delivery, Integer id);

    List<Delivery> getDeliveries(Integer clientId);
}
