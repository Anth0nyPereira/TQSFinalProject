package tqs.proudpapers.cart;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import tqs.proudpapers.entity.Cart;
import tqs.proudpapers.entity.Client;
import tqs.proudpapers.entity.Product;
import tqs.proudpapers.entity.ProductOfCart;
import tqs.proudpapers.repository.CartRepository;
import tqs.proudpapers.repository.ClientRepository;
import tqs.proudpapers.repository.ProductRepository;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author wy
 * @date 2021/6/16 17:54
 */
@Testcontainers
@SpringBootTest
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CarRepository_DockerContainers_Test {
    @Container
    public static MySQLContainer container = new MySQLContainer(DockerImageName.parse("mysql:5.7"))
            .withUsername("proudpapers")
            .withPassword("abcABC123!!!")
            .withDatabaseName("proudpapers");

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.password", container::getPassword);
        registry.add("spring.datasource.username", container::getUsername);
    }

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ClientRepository clientRepository;

    private static Product atmamun;

    private static ProductOfCart pcart;

    private static Client alex;

    private static Cart cart;

    @BeforeAll
    static void setUp() {
        atmamun = new Product();
        atmamun.setName("Atmamun");
        atmamun.setDescription("The Path To Achieving The Blis Of The Himalayan Swamis. And The Freedom Of A Living God");
        atmamun.setPrice(15.99);
        atmamun.setQuantity(13);

        alex = new Client();
        alex.setEmail("alex@ua.pt");
        alex.setName("alex");
        alex.setPassword("alexS3cr3t");
        alex.setAddress("2222-222, aveiro");
        alex.setTelephone("1234567891011");

        cart = new Cart();
        pcart = new ProductOfCart();

    }

    @Order(1)
    @Test
    public void whenCartId_thenReturnProducts() {
        atmamun = productRepository.save(atmamun);
        alex = clientRepository.save(alex);

        cart.setClient(alex.getId()); // we need a client id to create a cart
        cartRepository.createCart(alex.getId());
        cart.setId(cartRepository.getCartByClientId(alex.getId()));

        pcart.setProductId(atmamun.getId());
        pcart.setQuantity(1);
        pcart.setCart(cart.getId());
        pcart = cartRepository.save(pcart);


        // test
        List<ProductOfCart> products = cartRepository.getProductOfCartByCart(cart.getId());
        assertEquals(1, products.size());
        assertEquals(pcart, products.get(0));
    }

    @Order(2)
    @Test
    public void whenAddedProduct_thenReturnTrue() {
        boolean exists = cartRepository.existsByCartAndProductId(cart.getId(), atmamun.getId());
        assertTrue(exists);
    }

    @Order(3)
    @Test
    public void whenRemove_thenReturnEmptyList() {
        cartRepository.removeProductOfCartByCart(1);

        List<ProductOfCart> products = cartRepository.getProductOfCartByCart(1);
        assertEquals(0, products.size());
    }

    @Order(4)
    @Test
    public void whenClientId_thenReturnHisCartId() {
        Integer cartByClientId = cartRepository.getCartByClientId(alex.getId());

        assertEquals(cart.getId(), cartByClientId);
    }
}
