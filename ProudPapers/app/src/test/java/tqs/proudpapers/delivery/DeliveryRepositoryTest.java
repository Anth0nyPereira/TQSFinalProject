package tqs.proudpapers.delivery;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import tqs.proudpapers.entity.*;
import tqs.proudpapers.repository.DeliveryRepository;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author wy
 * @date 2021/6/5 21:15
 */
@DataJpaTest
public class DeliveryRepositoryTest {

    @Autowired
    private DeliveryRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    private int clientId = 1;

    private Delivery delivery;

    @BeforeEach
    void setUp(){
        delivery = new Delivery();
        delivery.setClient(clientId);
        delivery = entityManager.persistAndFlush(delivery);
    }

    @Test
    public void whenClientId_thenReturnDeliveries() {
        List<Delivery> deliveriesByClient = repository.getDeliveriesByClient(clientId);

        assertEquals(1, deliveriesByClient.size());
        assertEquals(delivery, deliveriesByClient.get(0));
    }

    @Test
    public void whenDeliveryId_thenReturnProductsInIt() {
        Product atmamun = new Product();
        atmamun.setName("Atmamun");
        atmamun.setDescription("The Path To Achieving The Blis Of The Himalayan Swamis. And The Freedom Of A Living God");
        atmamun.setPrice(15.99);
        atmamun.setQuantity(13);
        atmamun = entityManager.persistAndFlush(atmamun);

        entityManager.persistAndFlush(new ProductOfDelivery(delivery.getId(), atmamun.getId(), 1));

        List<Map<String, Integer>> productsOfDeliveryById = repository.getProductsOfDeliveryById(delivery.getId());
        Map<String, Integer> map = productsOfDeliveryById.get(0);

        assertEquals(1, productsOfDeliveryById.size());
        assertEquals(atmamun.getId(), map.get("PRODUCT"));
        assertEquals(1, map.get("QUANTITY"));
        assertEquals(delivery.getId(), map.get("DELIVERY"));
    }


    @Test
    public void whenChangeState_thenDeliveryStateChanged() {
        String state = "accepted";
        repository.changeStateOfDelivery(delivery.getId(), state);
        Delivery saved = repository.getOne(delivery.getId());

        assertEquals(state, saved.getState());
    }

    @Test
    public void whenSetDeliveryStoreId_thenDeliveryWithId() {
        int id_delivery_store = 100;
        repository.setDeliveryIdInStore(delivery.getId(), id_delivery_store);
        Delivery saved = repository.getOne(delivery.getId());

        assertEquals(id_delivery_store, saved.getIdDeliveryStore());
    }
}
