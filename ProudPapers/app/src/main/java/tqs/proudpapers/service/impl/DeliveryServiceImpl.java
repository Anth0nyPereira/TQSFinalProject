package tqs.proudpapers.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tqs.proudpapers.entity.*;
import tqs.proudpapers.repository.DeliveryRepository;
import tqs.proudpapers.repository.ProductRepository;
import tqs.proudpapers.repository.StateRepository;
import tqs.proudpapers.service.DeliveryService;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wy
 * @date 2021/6/14 14:49
 */
@Service
public class DeliveryServiceImpl implements DeliveryService {
    @Autowired
    DeliveryRepository deliveryRepository;

    @Autowired
    StateRepository stateRepository;

    @Autowired
    ProductRepository productRepository;

    @Override
    public Delivery getDeliveryById(Integer id) {
        var delivery = deliveryRepository.findAllById(id);
        fillDelivery(new DeliveryDTO(delivery));
        return delivery;
    }

    @Override
    public Integer addProductToDelivery(Integer clientId, List<ProductOfCartDTO> product) {
        var delivery = new Delivery();
        delivery.setClient(clientId);
        delivery.setState("awaiting_processing");
        double totalPrice = product.stream().map(p->p.getProduct().getPrice()).reduce(Double::sum).orElse(0.0);
        delivery.setTotalPrice(totalPrice);

        var saved = deliveryRepository.save(delivery);
        createState(saved.getId(), saved.getState());

        product.forEach(p -> deliveryRepository.addProductToDelivery(saved.getId(), p.getProduct().getId(), p.getQuantity()));
        return saved.getId();
    }

    @Override
    public void changeStateOfDelivery(Integer delivery, String state) {
        deliveryRepository.changeStateOfDelivery(delivery, state);
        createState(delivery, state);
    }

    @Override
    public void setDeliveryIdInStore(Integer delivery, Integer id) {
        deliveryRepository.setDeliveryIdInStore(delivery, id);
    }

    @Override
    public List<DeliveryDTO> getDeliveries(Integer clientId) {
        List<DeliveryDTO> deliveries = deliveryRepository.getDeliveriesByClient(clientId)
                                        .stream()
                                        .map(DeliveryDTO::new)
                                        .collect(Collectors.toList());

        deliveries.forEach(this::fillDelivery);
        return deliveries;
    }

    private void fillDelivery(DeliveryDTO delivery){
        List<ProductOfDeliveryDTO> products = new ArrayList<>();

        List<Map<String, Integer>> productsOfDeliveryArray = deliveryRepository.getProductsOfDeliveryById(delivery.getId());
        for (Map<String, Integer> m : productsOfDeliveryArray){
            var p = new ProductOfDeliveryDTO();
            p.setDelivery(delivery.getId());
            p.setQuantity(m.get("QUANTITY"));
            p.setProduct(productRepository.getProductById(m.get("PRODUCT")));
            products.add(p);
        }
        delivery.setProductsOfDelivery(products);
    }

    private void createState(Integer id, String description) {
        var state = new State();
        state.setDelivery(id);
        state.setTimestamp(new Date());
        state.setDescription(description);
        stateRepository.save(state);
    }


}
