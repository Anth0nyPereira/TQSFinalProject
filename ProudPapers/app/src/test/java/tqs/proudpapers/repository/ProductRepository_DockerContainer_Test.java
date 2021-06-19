package tqs.proudpapers.repository;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import tqs.proudpapers.entity.Product;
import tqs.proudpapers.repository.ProductRepository;

import javax.transaction.Transactional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author wy
 * @date 2021/6/5 21:15
 */
@Testcontainers
@SpringBootTest
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductRepository_DockerContainer_Test {
    @Container
    public static MySQLContainer container = new MySQLContainer(DockerImageName.parse("mysql:5.7"))
            .withUsername("root")
            .withPassword("123456")
            .withDatabaseName("proudpapers");

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.password", container::getPassword);
        registry.add("spring.datasource.username", container::getUsername);
    }

    @Autowired
    private ProductRepository repository;

    private Product atmamun;

    @BeforeEach
    void setUp() {
        atmamun = new Product();
        atmamun.setName("Atmamun");
        atmamun.setDescription("The Path To Achieving The Blis Of The Himalayan Swamis. And The Freedom Of A Living God");
        atmamun.setPrice(15.99);
        atmamun.setQuantity(13);
        atmamun = repository.save(atmamun);
    }

    @AfterEach
    void tearDown(){
        repository.deleteAll();
    }

    @Order(1)
    @Test
    public void whenAtmamun_thenReturnAtmamun() {
        List<Product> products = repository.getProductByNameContains(atmamun.getName());

        assertEquals(1, products.size());
        assertEquals("Atmamun", products.get(0).getName());
        assertEquals(atmamun.getId(), products.get(0).getId());
    }

    @Test
    public void whenAtmamunID_thenReturnAtmamun() {
        Product product = repository.getProductById(atmamun.getId());

        assertEquals("Atmamun", product.getName());
        assertEquals(atmamun.getId(), product.getId());
    }

    @Test
    public void whenKeyWord_thenReturnProdutsContainKeyWord() {
        Product b1 = new Product();
        b1.setName("Book A");

        Product b2 = new Product();
        b2.setName("Book B");

        Product b3 = new Product();
        b3.setName("Book C");

        repository.save(b1);
        repository.save(b2);
        repository.save(b3);

        List<Product> result = repository.getProductByNameContains("Book");

        assertEquals(3, result.size());
        assertThat(result)
                .contains(b1)
                .contains(b2)
                .contains(b3);
    }


    @Test
    public void whenInvalidKeyWord_thenEmptyList() {
        List<Product> result = repository.getProductByNameContains("invalid");
        assertEquals(0, result.size());
    }

}
