package tqs.proudpapers.product;

import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import tqs.proudpapers.entity.Client;
import tqs.proudpapers.entity.Product;
import tqs.proudpapers.repository.ClientRepository;
import tqs.proudpapers.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * @author wy
 * @date 2021/6/5 21:15
 */
@DataJpaTest
public class ProductRopositoryTest {

    @Autowired
    private TestEntityManager entityManager;

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
        entityManager.persistAndFlush(atmamun);
    }

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

        entityManager.persistAndFlush(b1);
        entityManager.persistAndFlush(b2);
        entityManager.persistAndFlush(b3);

        List<Product> result = repository.getProductByNameContains("Book");

        assertEquals(3, result.size());
        assertThat(result).contains(b1);
        assertThat(result).contains(b2);
        assertThat(result).contains(b3);
    }


    @Test
    public void whenInvalidKeyWord_thenEmptyList() {
        List<Product> result = repository.getProductByNameContains("invalid");
        assertEquals(0, result.size());
    }

}
