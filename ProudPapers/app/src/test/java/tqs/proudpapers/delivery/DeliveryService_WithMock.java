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
import tqs.proudpapers.repository.ProductRepository;
import tqs.proudpapers.service.ProductService;
import tqs.proudpapers.service.impl.CartServiceImpl;
import tqs.proudpapers.service.impl.DeliveryServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
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
    private DeliveryRepository deliveryRepository;

    @Mock(lenient = true)
    private ProductRepository productRepository;

    @InjectMocks
    private DeliveryServiceImpl service;


    @Test
    public void whenClientId_thenReturnDeliveries() {
        Product atmamun = new Product();
        atmamun.setName("Atmamun");
        atmamun.setDescription("The Path To Achieving The Blis Of The Himalayan Swamis. And The Freedom Of A Living God");
        atmamun.setPrice(15.99);
        atmamun.setQuantity(13);
        atmamun.setId(1);

        Mockito.when(productRepository.getProductById(atmamun.getId())).thenReturn(atmamun);

        int deliveryId = 1;
        int clientId = 1;

        Delivery d = new Delivery();
        d.setId(deliveryId);
        List<Delivery> deliveries = Arrays.asList(d);
        Mockito.when(deliveryRepository.getDeliveriesByClient(clientId)).thenReturn(deliveries);

        Map<String, Integer> map = Map.of("QUANTITY", 1, "PRODUCT", atmamun.getId(), "DELIVERY", deliveryId);
        List<Map<String, Integer>> maps = Arrays.asList(map);

        Mockito.when(deliveryRepository.getProductsOfDeliveryById(deliveryId)).thenReturn(maps);

        List<Delivery> serviceDeliveries = service.getDeliveries(clientId);

        assertEquals(1, serviceDeliveries.size());
        assertEquals(1, serviceDeliveries.get(0).getProductsOfDelivery().size());
        assertEquals(atmamun, serviceDeliveries.get(0).getProductsOfDelivery().get(0).getProduct());

    }

}
