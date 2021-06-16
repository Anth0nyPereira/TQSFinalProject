package tqs.proudpapers.client;

import org.junit.jupiter.api.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import tqs.proudpapers.ProudPapersApplication;
import tqs.proudpapers.entity.*;
import tqs.proudpapers.repository.DeliveryRepository;
import tqs.proudpapers.service.CartService;
import tqs.proudpapers.service.ClientService;
import tqs.proudpapers.service.DeliveryService;
import tqs.proudpapers.service.ProductService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author wy
 * @date 2021/6/11 12:33
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ProudPapersApplication.class)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Testcontainers
class ClientController_IT_Test {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ClientService clientService;

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    @Autowired
    private DeliveryService deliveryService;

    @Autowired
    private DeliveryRepository deliveryRepository;

    private static ClientDTO alexDTO;
    private static Client alex;

    @Container
    public static MySQLContainer container = new MySQLContainer(DockerImageName.parse("mysql:5.7"))
            .withUsername("root")
            .withPassword("12345")
            .withDatabaseName("proudpapers");

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.password", container::getPassword);
        registry.add("spring.datasource.username", container::getUsername);
    }

    @BeforeAll
    static void setUp() {
        alexDTO = new ClientDTO();
        alexDTO.setEmail("alex@ua.pt");
        alexDTO.setName("alex");
        alexDTO.setPassword("alexS3cr3t");
        alexDTO.setZip("2222-222");
        alexDTO.setCity("aveiro");
        alexDTO.setTelephone("12345678912");

        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setCardNumber("1234567891234567");
        paymentMethod.setCardExpirationMonth("11");
        paymentMethod.setCvc("123");

        alexDTO.setPaymentMethod(paymentMethod);

        alex = new Client();
        BeanUtils.copyProperties(alexDTO, alex);
        alex.setAddress(alexDTO.getZip() + "," + alexDTO.getCity());
    }

    @Order(1)
    @Test
    public void signUpWithAlex_thenLoginPageWithAlexEmail() throws Exception {
        mvc.perform(post("/signup")
                .param("name", alexDTO.getName())
                .param("email", alexDTO.getEmail())
                .param("password", alexDTO.getPassword())
                .param("city", alexDTO.getCity())
                .param("zip", alexDTO.getZip())
                .param("telephone", alexDTO.getTelephone())
                .param("cardNumber", "1234567891234567")
                .param("cardExpirationMonth", "11")
                .param("cvc", "123"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(xpath("//input[@id='email']").string(alex.getEmail()));

        ClientDTO result = clientService.getClientByEmail(alex.getEmail());
        alex.setId(result.getId());
        assertEquals(result.getEmail(), alex.getEmail());
        assertEquals(result.getName(), alex.getName());
        assertEquals(result.getTelephone(), alex.getTelephone());
    }

    @Order(2)
    @Test
    public void signUpWithUsedEmail_thenSignUpPageWithErrorMessage() throws Exception {
        ClientDTO copyed = new ClientDTO();
        BeanUtils.copyProperties(alexDTO, copyed);
        copyed.setEmail(alexDTO.getEmail());  // this email is already used by alex

        assertThrows(Exception.class, ()->mvc.perform(post("/signup")));
    }

    @Order(3)
    @Test
    public void loginWithAlex_thenIndexPageContainsUsernameAndButtons() throws Exception {
        mvc.perform(post("/login")
                .param("email", alex.getEmail())
                .param("password", alex.getPassword()))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(xpath("//div[contains(@class, 'username')]").string(alex.getName()))
                .andExpect(xpath("//div[contains(@class, 'cartDiv')]").exists());

    }

    @Order(4)
    @Test
    public void loginWithIncorrectPassword_thenLoginPageWithErrorMessage() throws Exception {
        mvc.perform(post("/login",alexDTO.getEmail(), "invalid"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(xpath("//h5[@id='error-msg']").exists());
    }

    @Order(5)
    @Test
    public void addProductToCart_thenSizeOne() throws Exception {
        Product b1 = new Product();
        b1.setName("Book A");
        b1.setPrice(10.0);
        b1.setQuantity(99);
        b1.setDescription("Test");

        Product saved = productService.save(b1);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("client", alexDTO);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                    .post("/account/{clientId}/add_to_cart/{productId}", alex.getId(), saved.getId())
                    .session(session);

        mvc.perform(builder)
                .andExpect(status().isOk());

        CartDTO cartDTO = cartService.getCartByClientID(alex.getId());
        assertEquals(1, cartDTO.getProductOfCarts().size());
        assertEquals(b1, cartDTO.getProductOfCarts().get(0).getProduct());
     }

    @Order(6)
    @Test
    public void getProductsInTheCart_thenReturnProducts() throws Exception {
        CartDTO cartDTO = cartService.getCartByClientID(alex.getId());
        Product p = cartDTO.getProductOfCarts().get(0).getProduct();

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("client", alexDTO);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .get("/account/{clientId}/cart", alex.getId())
                .session(session);

        mvc.perform(builder)
                .andExpect(status().isOk())
                .andExpect(view().name("account"))
                .andExpect(xpath("//h5[contains(@class, 'cart-product-name')]").string(p.getName()))
                .andExpect(xpath("//div[contains(@class, 'cart-product-price')]").string("€ " + p.getPrice()))
                .andExpect(xpath("//p[contains(@class, 'cart-product-desc')]    ").string(p.getDescription()));
    }


    // waiting for deployment
    public void buyProductsInTheCart_thenCartEmptyAndDeliveryWithProducts() throws Exception {
        Product atmamun = new Product();
        atmamun.setName("Atmamun");
        atmamun.setDescription("The Path To Achieving The Blis Of The Himalayan Swamis. And The Freedom Of A Living God");
        atmamun.setPrice(15.99);
        atmamun.setQuantity(13);
        atmamun = productService.save(atmamun);
        cartService.save(alex.getId(), atmamun.getId(), 1);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("client", alexDTO);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .post("/account/" + alex.getId() + "/purchase")
                .param("name", alexDTO.getName())
                .param("email", alexDTO.getEmail())
                .param("password", alexDTO.getPassword())
                .param("city", alexDTO.getCity())
                .param("zip", alexDTO.getZip())
                .param("telephone", alexDTO.getTelephone())
                .param("cardNumber", alexDTO.getPaymentMethod().getCardNumber())
                .param("cardExpirationMonth", alexDTO.getPaymentMethod().getCardExpirationMonth())
                .param("cvc", alexDTO.getPaymentMethod().getCvc())
                .session(session);

        mvc.perform(builder)
                .andExpect(status().isOk())
                .andExpect(view().name("account"))
                .andExpect(xpath("//h5[contains(@class, 'cart-product-name']").doesNotExist())
                .andExpect(xpath("//div[contains(@class, 'cart-product-price')]").doesNotExist())
                .andExpect(xpath("//p[contains(@class, 'cart-product-desc')]").doesNotExist());

        CartDTO cartDTO = cartService.getCartByClientID(alex.getId());
        assertEquals(0, cartDTO.getProductOfCarts().size());

        List<DeliveryDTO> deliveries = deliveryService.getDeliveries(alex.getId());
        assertEquals(1, deliveries.size());
        assertEquals(1, deliveries.get(0).getProductsOfDelivery().size());
        assertEquals(atmamun, deliveries.get(0).getProductsOfDelivery().get(0).getProduct());
    }


    @Test
    public void changeDeliverStateTest() throws Exception {
        Delivery delivery = new Delivery();
        delivery.setClient(alex.getId());
        delivery.setState("asd");
        delivery.setTotalPrice(10.0);
        deliveryRepository.save(delivery);

        mvc.perform(post("/update/{id}/state/{state}",
                delivery.getId(),
                        "testState"))
                .andExpect(status().isOk());

        Delivery deliveryById = deliveryService.getDeliveryById(delivery.getId());

        assertEquals("testState", deliveryById.getState());
    }

}
