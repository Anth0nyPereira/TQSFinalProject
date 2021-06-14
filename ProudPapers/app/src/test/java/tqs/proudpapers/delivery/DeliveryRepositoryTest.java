package tqs.proudpapers.delivery;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import tqs.proudpapers.entity.*;
import tqs.proudpapers.repository.CartRepository;
import tqs.proudpapers.repository.DeliveryRepository;
import tqs.proudpapers.service.impl.DeliveryServiceImpl;

import java.util.Arrays;
import java.util.List;

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


    @Test
    public void whenDeliveryId_thenReturnDelivery() {
        int bookId = 1;
        int quantity = 1;
        int deliveryId = 1;

        Product b1 = new Product();
        b1.setName("Book A");
        b1.setPrice(11.0);
        b1.setId(bookId);

        ProductOfDelivery product = new ProductOfDelivery();
        product.setProductId(b1.getId());
        product.setQuantity(quantity);
        product.setDelivery(deliveryId);

        List<ProductOfDelivery> list = Arrays.asList(product);

        List<ProductOfDelivery> products = repository.getProductsOfDeliveryById(deliveryId);
        assertEquals(1, products.size());

    }

}
