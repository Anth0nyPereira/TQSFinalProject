package tqs.proudpapers.client;

import org.junit.jupiter.api.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import tqs.proudpapers.ProudPapersApplication;
import tqs.proudpapers.entity.*;
import tqs.proudpapers.repository.ClientRepository;
import tqs.proudpapers.repository.PaymentMethodRepository;
import tqs.proudpapers.service.CartService;
import tqs.proudpapers.service.ClientService;
import tqs.proudpapers.service.ProductService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author wy
 * @date 2021/6/11 12:33
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ProudPapersApplication.class)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ClientController_IT {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ClientService clientService;

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    private ClientDTO alexDTO;
    private Client alex;

    @BeforeEach
    void setUp() {
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

        mvc.perform(post("/signup")
                .param("name", copyed.getName())
                .param("email", copyed.getEmail())
                .param("password", copyed.getPassword())
                .param("city", copyed.getCity())
                .param("zip", copyed.getZip())
                .param("telephone", copyed.getTelephone())
                .param("cardNumber", "12345678912")
                .param("cardExpirationMonth", "11")
                .param("cvc", "123"))
                .andExpect(status().isOk())
                .andExpect(view().name("signUp"))
                .andExpect(xpath("//h5[@id='error-msg']").exists());

    }

    @Order(3)
    @Test
    public void getCartWithoutLogin_thenLoginPage() throws Exception {
        mvc.perform(get("/account/{id}/cart", 2))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));

    }

    @Order(4)
    @Test
    public void loginWithAlex_thenIndexPageContainsUsernameAndButtons() throws Exception {
        mvc.perform(post("/login")
                .param("email", alex.getEmail())
                .param("password", alex.getPassword()))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(xpath("//div[contains(@class, 'username')]").string(alex.getName()))
                .andExpect(xpath("//div[contains(@class, 'loveDiv')]").exists())
                .andExpect(xpath("//div[contains(@class, 'cartDiv')]").exists());

    }

    @Order(5)
    @Test
    public void loginWithIncorrectPassword_thenLoginPageWithErrorMessage() throws Exception {
        mvc.perform(post("/login",alexDTO.getEmail(), "invalid"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(xpath("//h5[@id='error-msg']").exists());

    }

    @Order(6)
    @Test
    public void addProductToCart_thenSizeOne() throws Exception {
        Product b1 = new Product();
        b1.setName("Book A");
        b1.setPrice(10.0);
        b1.setQuantity(99);
        b1.setDescription("Test");

        Product saved = productService.save(b1);

        mvc.perform(post("/account/{clientId}/add_to_cart/{productId}",
                2,
                        saved.getId() + ""))
                .andExpect(status().isOk());

        CartDTO cartDTO = cartService.getCartByClientID(2);
        assertEquals(1, cartDTO.getProductOfCarts().size());
        assertEquals(b1, cartDTO.getProductOfCarts().get(0).getProduct());
     }

    @Order(7)
    @Test
    public void getProductsInTheCart_thenReturnProducts() throws Exception {
        CartDTO cartDTO = cartService.getCartByClientID(alex.getId());
        Product p = cartDTO.getProductOfCarts().get(0).getProduct();

        mvc.perform(get("/cart"))
                .andExpect(status().isOk())
                .andExpect(view().name("account"))
                .andExpect(xpath("//h5[contains(@class, 'cart-product-name']").string(p.getName()))
                .andExpect(xpath("//div[contains(@class, 'cart-product-price')]").string("€ " + p.getPrice()))
                .andExpect(xpath("//p[contains(@class, 'cart-product-desc')]").string(p.getDescription()));
    }

    @Order(8)
    @Test
    public void buyProductsInTheCart_thenCartEmpty() throws Exception {
        mvc.perform(post("/purchase")
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
                .andExpect(view().name("account"))
                .andExpect(xpath("//h5[contains(@class, 'cart-product-name']").doesNotExist())
                .andExpect(xpath("//div[contains(@class, 'cart-product-price')]").doesNotExist())
                .andExpect(xpath("//p[contains(@class, 'cart-product-desc')]").doesNotExist());

        CartDTO cartDTO = cartService.getCartByClientID(alex.getId());
        assertEquals(0, cartDTO.getProductOfCarts().size());
    }
}
