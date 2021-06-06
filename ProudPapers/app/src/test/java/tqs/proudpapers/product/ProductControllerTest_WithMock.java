package tqs.proudpapers.product;

import org.junit.jupiter.api.BeforeAll;
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
import tqs.proudpapers.service.ProductService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author wy
 * @date 2021/6/5 22:17
 */
@WebMvcTest(ProductController.class)
public class ProductControllerTest_WithMock {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ProductService service;

    private static Product atmamun;

    @BeforeAll
    static void setUp() {
        atmamun = new Product();
        atmamun.setName("Atmamun");
        atmamun.setDescription("The Path To Achieving The Blis Of The Himalayan Swamis. And The Freedom Of A Living God");
        atmamun.setPrice(15.99);
        atmamun.setQuantity(13);
        atmamun.setImg("/img/Atmamun.jpg");
        atmamun.setId(1);
    }

    @Test
    public void whenAtmamunId_thenReturnAtmamun() throws Exception {

        Mockito.when(service.getProduct(atmamun.getId())).thenReturn(atmamun);

        mvc.perform(post("/product/{id}", atmamun.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("search"))
                .andExpect(xpath("//h3[contains(@class, 'product-name')]").string(atmamun.getName()))
                .andExpect(xpath("//h3[contains(@class, 'product-price')]").string(String.valueOf(atmamun.getPrice())))
                .andExpect(xpath("//h3[contains(@class, 'product-description)]").string(atmamun.getDescription()));
    }

    @Test
    public void whenAtmamunName_thenReturnAtmamun() throws Exception {
        Mockito.when(service.getProduct(atmamun.getName())).thenReturn(atmamun);

        mvc.perform(post("/product/{name}", atmamun.getName()))
                .andExpect(status().isOk())
                .andExpect(view().name("search"))
                .andExpect(xpath("//h3[contains(@class, 'product-name')]").string(atmamun.getName()))
                .andExpect(xpath("//h3[contains(@class, 'product-price')]").string(String.valueOf(atmamun.getPrice())))
                .andExpect(xpath("//h3[contains(@class, 'product-description)]").string(atmamun.getDescription()));
    }


    @Test
    public void whenInvalidName_thenNothingFound() throws Exception {
        Mockito.when(service.getProduct("invalid")).thenReturn(atmamun);

        mvc.perform(post("/product/{name}", "invalid"))
                .andExpect(status().isOk())
                .andExpect(view().name("search"))
                .andExpect(xpath("//h3[contains(@class, 'no-products')]").exists());
    }

}
