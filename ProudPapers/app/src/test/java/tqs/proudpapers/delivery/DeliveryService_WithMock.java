package tqs.proudpapers.delivery;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import tqs.proudpapers.entity.*;
import tqs.proudpapers.repository.CartRepository;
import tqs.proudpapers.repository.DeliveryRepository;
import tqs.proudpapers.service.ProductService;
import tqs.proudpapers.service.impl.CartServiceImpl;
import tqs.proudpapers.service.impl.DeliveryServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author wy
 * @date 2021/6/5 21:12
 */
@ExtendWith(MockitoExtension.class)
public class DeliveryService_WithMock {

    @Mock(lenient = true)
    private DeliveryRepository repository;

    @InjectMocks
    private DeliveryServiceImpl service;


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

        Mockito.when(repository.getProductsOfDeliveryById(deliveryId)).thenReturn(list);

        Delivery delivery = service.getDeliveryById(deliveryId);

        assertEquals("awaiting_processing", delivery.getState());
        assertEquals(deliveryId, delivery.getId());
        assertEquals(1, delivery.getProducts().size());
        assertThat(delivery.getProducts())
                .contains(b1);
    }

}
