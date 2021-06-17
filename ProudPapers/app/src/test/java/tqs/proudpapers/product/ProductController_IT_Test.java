package tqs.proudpapers.product;

import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import tqs.proudpapers.ProudPapersApplication;
import tqs.proudpapers.entity.Product;
import tqs.proudpapers.service.ProductService;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

/**
 * @author wy
 * @date 2021/6/16 17:49
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ProudPapersApplication.class)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Testcontainers
@Transactional
public class ProductController_IT_Test {
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
    private MockMvc mvc;

    @Autowired
    private ProductService service;

    private static Product atmamun;

    @BeforeAll
    static void setUp() {
        atmamun = new Product();
        atmamun.setName("Atmamun");
        atmamun.setDescription("The Path To Achieving The Blis Of The Himalayan Swamis. And The Freedom Of A Living God");
        atmamun.setPrice(15.99);
        atmamun.setQuantity(13);
    }

    @Test
    public void whenSearchAtmamunId_thenReturnAtmamun() throws Exception {
        atmamun = service.save(atmamun);
        mvc.perform(get("/search/{id}", atmamun.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("search"))
                .andExpect(xpath("//h3[contains(@class, 'product-name')]").string(atmamun.getName()))
                .andExpect(xpath("//div[contains(@class, 'product-price')]").string("€ " + atmamun.getPrice()))
                .andExpect(xpath("//p[contains(@class, 'product-description')]").string(atmamun.getDescription()));
    }

    @Test
    public void whenSearchAtmamunName_thenReturnAtmamun() throws Exception {

        mvc.perform(get("/search/{name}", atmamun.getName()))
                .andExpect(status().isOk())
                .andExpect(view().name("search"))
                .andExpect(xpath("//h3[contains(@class, 'product-name')]").string(atmamun.getName()))
                .andExpect(xpath("//div[contains(@class, 'product-price')]").string("€ " + atmamun.getPrice()))
                .andExpect(xpath("//p[contains(@class, 'product-description')]").string(atmamun.getDescription()));
    }


    @Test
    public void whenSearchInvalidName_thenNothingFound() throws Exception {

        mvc.perform(get("/search/{name}", "invalid"))
                .andExpect(status().isOk())
                .andExpect(view().name("search"))
                .andExpect(xpath("//h3[contains(@class, 'no-products')]").exists());
    }

    @Test
    public void whenGetAtmamunId_thenReturnDetail() throws Exception {

        mvc.perform(get("/product/{id}", atmamun.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("productDetail"))
                .andExpect(xpath("//div[@id='descr']").string(atmamun.getDescription()))
                .andExpect(xpath("//h2[@id='product-name']").string(atmamun.getName()))
                .andExpect(xpath("//span[@id='product-price']").string(String.valueOf(atmamun.getPrice())));
    }
}
