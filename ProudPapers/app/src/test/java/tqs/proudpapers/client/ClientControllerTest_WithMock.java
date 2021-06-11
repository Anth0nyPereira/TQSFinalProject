package tqs.proudpapers.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import tqs.proudpapers.controller.ClientController;
import tqs.proudpapers.entity.Client;
import tqs.proudpapers.entity.ClientDTO;
import tqs.proudpapers.entity.PaymentMethod;
import tqs.proudpapers.entity.Product;
import tqs.proudpapers.service.ClientService;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author wy
 * @date 2021/6/5 22:17
 */
@WebMvcTest(ClientController.class)
public class ClientControllerTest_WithMock {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ClientService clientService;

    @MockBean
    private CartService cartService;

    private static ClientDTO alexDTO;
    private Client alex;

    @BeforeEach
    void setUp() {
        alexDTO = new ClientDTO();
        alexDTO.setEmail("alex@ua.pt");
        alexDTO.setName("alex");
        alexDTO.setPassword("alexS3cr3t");
        alexDTO.setZip("2222-222");
        alexDTO.setCity("aveiro");
        alexDTO.setTelephone("1234567891011");
        alexDTO.setId(11);

        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setCardNumber("1234567891234567");
        paymentMethod.setCardExpirationMonth("11");
        paymentMethod.setCvc("123");

        alexDTO.setPaymentMethod(paymentMethod);

        alex = new Client();
        BeanUtils.copyProperties(alexDTO, alex);
        alex.setAddress(alexDTO.getZip() + "," + alexDTO.getCity());
    }

    @Test
    public void signUpWithAlex_thenLoginPageWithAlexEmail() throws Exception {

        Mockito.when(clientService.saveClient(alexDTO)).thenReturn(alex);

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
    }

    @Test
    public void signUpWithUsedEmail_thenSignUpPageWithErrorMessage() throws Exception {
        ClientDTO copyed = new ClientDTO();
        BeanUtils.copyProperties(alexDTO, copyed);
        copyed.setEmail("used@ua.pt");

        Mockito.when(clientService.saveClient(copyed)).thenReturn(null);

        mvc.perform(post("/signup")
                .param("name", copyed.getName())
                .param("email", copyed.getEmail())
                .param("password", copyed.getPassword())
                .param("city", copyed.getCity())
                .param("zip", copyed.getZip())
                .param("telephone", copyed.getTelephone())
                .param("cardNumber", "1234567891234567")
                .param("cardExpirationMonth", "11")
                .param("cvc", "123")).andExpect(status().isOk())
                .andExpect(view().name("signUp"))
                .andExpect(xpath("//h5[@id='error-msg']").exists());
  }


    @Test
    public void loginWithAlex_thenIndexPageContainsUsernameAndButtons() throws Exception {
        Mockito.when(clientService.getClientByEmailAndPass(alexDTO.getEmail(), alexDTO.getPassword())).thenReturn(alexDTO);

        mvc.perform(post("/login")
                    .param("email", alex.getEmail())
                    .param("password", alex.getPassword()))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(xpath("//div[contains(@class, 'username')]").string(alex.getName()))
                .andExpect(xpath("//div[contains(@class, 'loveDiv')]").exists())
                .andExpect(xpath("//div[contains(@class, 'cartDiv')]").exists());
    }

    @Test
    public void loginWithIncorrectPassword_thenLoginPageWithErrorMessage() throws Exception {
        Mockito.when(clientService.getClientByEmailAndPass(alexDTO.getEmail(), "invalid")).thenReturn(null);

        mvc.perform(post("/login",alexDTO.getEmail(), "invalid"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(xpath("//h5[@id='error-msg']").exists());
    }

    @Test
    public void getProductsInTheCart_thenReturnProducts() throws Exception {
        Product b1 = new Product();
        b1.setName("Book A");
        b1.setPrice(10.0);
        b1.setDescription("Test Book");

        int clientId = 1;

        CartDTO cart = new CartDTO();
        cart.setClientId(clientId);
        cart.setProducts(Arrays.asList(b1));

        Mockito.when(cartService.getCart(alex.getId())).thenReturn(cart);

        mvc.perform(get("/cart"))
                .andExpect(status().isOk())
                .andExpect(view().name("account"))
                .andExpect(xpath("//h5[contains(@class, 'cart-product-name']").string(b1.getName()))
                .andExpect(xpath("//div[contains(@class, 'cart-product-price')]").string("€ " + b1.getPrice()))
                .andExpect(xpath("//p[contains(@class, 'cart-product-desc')]").string(b1.getDescription()));
    }

    @Test
    public void buyProductsInTheCart_thenCartEmpty() throws Exception {
        Product b1 = new Product();
        b1.setName("Book A");
        b1.setPrice(10.0);
        b1.setName("Test Book");

        CartDTO cart = new CartDTO();
        cart.setProducts(Arrays.asList(b1));

        Mockito.when(cartService.buyAllProdutsInTheCart(alex.getId())).thenReturn(null);

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
    }
}
