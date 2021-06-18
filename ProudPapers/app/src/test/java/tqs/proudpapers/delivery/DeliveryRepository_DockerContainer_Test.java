package tqs.proudpapers.delivery;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import tqs.proudpapers.entity.Client;
import tqs.proudpapers.entity.Delivery;
import tqs.proudpapers.entity.Product;
import tqs.proudpapers.repository.ClientRepository;
import tqs.proudpapers.repository.DeliveryRepository;
import tqs.proudpapers.repository.ProductRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author wy
 * @date 2021/6/5 21:15
 */
@Testcontainers
@SpringBootTest
@Transactional
public class DeliveryRepository_DockerContainer_Test {

    @Container
    public static MySQLContainer container = new MySQLContainer(DockerImageName.parse("mysql:5.7"))
            .withUsername("root")
            .withPassword("abcABC123!!!")
            .withDatabaseName("proudpapers");

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.password", container::getPassword);
        registry.add("spring.datasource.username", container::getUsername);
    }

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ProductRepository productRepository;

    private Delivery delivery;

    private Client alex;

    @BeforeEach
    void setUp(){
        alex = new Client();
        alex.setEmail("alex@ua.pt");
        alex.setName("alex");
        alex.setPassword("alexS3cr3t");
        alex.setAddress("2222-222, aveiro");
        alex.setTelephone("1234567891011");
        alex = clientRepository.save(alex); //ensure data is persisted at this point

        delivery = new Delivery();
        delivery.setClient(alex.getId());
        delivery = deliveryRepository.save(delivery);
    }

    @AfterEach
    void tearDown(){
        deliveryRepository.deleteAll();
        clientRepository.deleteAll();
    }

    @Test
    public void whenClientId_thenReturnDeliveries() {
        List<Delivery> deliveriesByClient = deliveryRepository.getDeliveriesByClient(alex.getId());

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
        atmamun = productRepository.save(atmamun);

        deliveryRepository.addProductToDelivery(delivery.getId(), atmamun.getId(), 1);

        List<Map<String, Integer>> productsOfDeliveryById = deliveryRepository.getProductsOfDeliveryById(delivery.getId());
        Map<String, Integer> map = productsOfDeliveryById.get(0);

        assertEquals(1, productsOfDeliveryById.size());
        assertEquals(atmamun.getId(), map.get("PRODUCT"));
        assertEquals(1, map.get("QUANTITY"));
        assertEquals(delivery.getId(), map.get("DELIVERY"));
    }


    @Test
    public void whenChangeState_thenDeliveryStateChanged() {
        String state = "accepted";
        deliveryRepository.changeStateOfDelivery(delivery.getId(), state);
        Delivery saved = deliveryRepository.findAllById(delivery.getId());

        assertEquals(state, saved.getState());
    }

    @Test
    public void whenSetDeliveryStoreId_thenDeliveryWithId() {
        int id_delivery_store = 100;
        deliveryRepository.setDeliveryIdInStore(delivery.getId(), id_delivery_store);
        Delivery saved = deliveryRepository.getOne(delivery.getId());

        assertEquals(id_delivery_store, saved.getIdDeliveryStore());
    }
}
