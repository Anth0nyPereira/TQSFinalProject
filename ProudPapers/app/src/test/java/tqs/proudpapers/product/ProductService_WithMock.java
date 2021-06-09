package tqs.proudpapers.product;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import tqs.proudpapers.entity.Client;
import tqs.proudpapers.entity.ClientDTO;
import tqs.proudpapers.entity.Product;
import tqs.proudpapers.repository.ClientRepository;
import tqs.proudpapers.repository.ProductRepository;
import tqs.proudpapers.service.impl.ClientServiceImpl;
import tqs.proudpapers.service.impl.ProductServiceImpl;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * @author wy
 * @date 2021/6/5 21:12
 */
@ExtendWith(MockitoExtension.class)
public class ProductService_WithMock {

    @Mock(lenient = true)
    private ProductRepository repository;

    @InjectMocks
    private ProductServiceImpl service;

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
    public void whenAtmamun_thenReturnAtmamun() {
        Mockito.when(repository.getProductByNameContains(atmamun.getName())).thenReturn(List.of(atmamun));
        List<Product> products = service.searchByKeyWord(atmamun.getName());

        assertEquals(1, products.size());
        assertEquals("Atmamun", products.get(0).getName());
        assertEquals(1, products.get(0).getId());
    }

    @Test
    public void whenAtmamunID_thenReturnAtmamun() {
        Mockito.when(repository.getProductById(atmamun.getId())).thenReturn(atmamun);
        Product product = service.searchById(atmamun.getId());

        assertEquals("Atmamun", product.getName());
        assertEquals(1, product.getId());

    }

    @Test
    public void whenKeyWord_thenReturnProdutsContainKeyWord() {
        Product b1 = new Product();
        b1.setName("Book A");

        Product b2 = new Product();
        b2.setName("Book B");

        Product b3 = new Product();
        b3.setName("Book C");

        List<Product> products = List.of(b1, b2, b3);

        Mockito.when(repository.getProductByNameContains("Book")).thenReturn(products);

        List<Product> result = service.searchByKeyWord("Book");

        assertEquals(3, result.size());
        assertThat(result).contains(b1);
        assertThat(result).contains(b2);
        assertThat(result).contains(b3);
    }

    @Test
    public void whenInvalidId_thenReturnNull() {
        Mockito.when(repository.getProductById(123456)).thenReturn(null);

        Product result = service.searchById(123456);

        assertNull(result);
    }

    @Test
    public void whenInvalidKeyWord_thenEmptyList() {
        Mockito.when(repository.getProductByNameContains("invalid")).thenReturn(new ArrayList<>());

        List<Product> result = service.searchByKeyWord("invalid");

        assertEquals(0, result.size());
    }

}
