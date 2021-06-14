package tqs.proudpapers.product;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import tqs.proudpapers.controller.ProductController;
import tqs.proudpapers.entity.Product;
import tqs.proudpapers.service.ProductService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
        atmamun.setId(1);
    }

    @Test
    public void whenSearchAtmamunId_thenReturnAtmamun() throws Exception {

        Mockito.when(service.searchById(atmamun.getId())).thenReturn(atmamun);

        mvc.perform(get("/search/{id}", atmamun.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("search"))
                .andExpect(xpath("//h3[contains(@class, 'product-name')]").string(atmamun.getName()))
                .andExpect(xpath("//div[contains(@class, 'product-price')]").string("€ " + atmamun.getPrice()))
                .andExpect(xpath("//p[contains(@class, 'product-description')]").string(atmamun.getDescription()));
    }

    @Test
    public void whenSearchAtmamunName_thenReturnAtmamun() throws Exception {
        Mockito.when(service.searchByKeyWord(atmamun.getName())).thenReturn(Arrays.asList(atmamun));

        mvc.perform(get("/search/{name}", atmamun.getName()))
                .andExpect(status().isOk())
                .andExpect(view().name("search"))
                .andExpect(xpath("//h3[contains(@class, 'product-name')]").string(atmamun.getName()))
                .andExpect(xpath("//div[contains(@class, 'product-price')]").string("€ " + atmamun.getPrice()))
                .andExpect(xpath("//p[contains(@class, 'product-description')]").string(atmamun.getDescription()));
    }


    @Test
    public void whenSearchInvalidName_thenNothingFound() throws Exception {
        Mockito.when(service.searchByKeyWord("invalid")).thenReturn(new ArrayList<>());

        mvc.perform(get("/search/{name}", "invalid"))
                .andExpect(status().isOk())
                .andExpect(view().name("search"))
                .andExpect(xpath("//h3[contains(@class, 'no-products')]").exists());
    }

    @Test
    public void whenGetAtmamunId_thenReturnDetail() throws Exception {
        Mockito.when(service.searchById(atmamun.getId())).thenReturn(atmamun);

        mvc.perform(get("/product/{id}", atmamun.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("productDetail"))
                .andExpect(xpath("//div[@id='descr']").string(atmamun.getDescription()))
                .andExpect(xpath("//h2[@id='product-name']").string(atmamun.getName()))
                .andExpect(xpath("//span[@id='product-price']").string(String.valueOf(atmamun.getPrice())));
    }
}
