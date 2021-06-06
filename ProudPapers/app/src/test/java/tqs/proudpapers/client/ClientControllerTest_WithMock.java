package tqs.proudpapers.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tqs.proudpapers.JsonUtil;
import tqs.proudpapers.controller.ClientController;
import tqs.proudpapers.entity.Client;
import tqs.proudpapers.entity.ClientDTO;
import tqs.proudpapers.entity.PaymentMethod;
import tqs.proudpapers.service.ClientService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

/**
 * @author wy
 * @date 2021/6/5 22:17
 */
@WebMvcTest(ClientController.class)
public class ClientControllerTest_WithMock {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ClientService service;

    private static ClientDTO alexDTO;
    private static Client alex;

    @BeforeAll
    static void setUp() {
        alexDTO = new ClientDTO();
        alexDTO.setEmail("alex@ua.pt");
        alexDTO.setName("alex");
        alexDTO.setPassword("alexS3cr3t");
        alexDTO.setZip("2222-222");
        alexDTO.setCity("aveiro");
        alexDTO.setTelephone("1234567891011");

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

        Mockito.when(service.saveClient(alexDTO)).thenReturn(alex);

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

        Mockito.when(service.saveClient(copyed)).thenReturn(null);

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
    public void loginWithAlex() throws Exception {
        Mockito.when(service.getClientByEmailAndPass(alexDTO.getEmail(), alexDTO.getPassword())).thenReturn(alexDTO);

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
    public void loginWithIncorrectPassword() throws Exception {
        Mockito.when(service.getClientByEmailAndPass(alexDTO.getEmail(), "invalid")).thenReturn(null);

        mvc.perform(post("/login",alexDTO.getEmail(), "invalid"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(xpath("//h5[@id='error-msg']").exists());
    }

}
