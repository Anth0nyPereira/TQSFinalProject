package tqs.proudpapers.cart;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import tqs.proudpapers.entity.*;
import tqs.proudpapers.repository.CartRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author wy
 * @date 2021/6/5 21:15
 */
@DataJpaTest
public class CartRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CartRepository repository;

    private Product atmamun;

    private ProductOfCart pcart;

    private Client alex;

    private Cart cart;

    @BeforeEach
    void setUp() {
        atmamun = new Product();
        atmamun.setName("Atmamun");
        atmamun.setDescription("The Path To Achieving The Blis Of The Himalayan Swamis. And The Freedom Of A Living God");
        atmamun.setPrice(15.99);
        atmamun.setQuantity(13);
        entityManager.persistAndFlush(atmamun);

        alex = new Client();
        alex.setEmail("alex@ua.pt");
        alex.setName("alex");
        alex.setPassword("alexS3cr3t");
        alex.setAddress("2222-222, aveiro");
        alex.setTelephone("1234567891011");
        alex = entityManager.persistAndFlush(alex);

        cart = new Cart();
        cart.setClient(alex.getId()); // we need a client id to create a cart
        cart = entityManager.persistAndFlush(cart);

        pcart = new ProductOfCart();
        pcart.setProductId(atmamun.getId());
        pcart.setQuantity(1);
        pcart.setCart(cart.getId());

        entityManager.persistAndFlush(pcart); //ensure data is persisted at this point
    }

    @Test
    public void whenCartId_thenReturnProducts() {
        List<ProductOfCart> products = repository.getProductOfCartByCart(cart.getId());
        assertEquals(1, products.size());
        assertEquals(pcart, products.get(0));
    }

    @Test
    public void whenRemove_thenReturnEmptyList() {
        repository.removeProductOfCartByCart(1);

        List<ProductOfCart> products = repository.getProductOfCartByCart(1);
        assertEquals(0, products.size());
    }

    @Test
    public void whenClientId_thenReturnHisCartId() {
        Integer cartByClientId = repository.getCartByClientId(alex.getId());

        assertEquals(cart.getId(), cartByClientId);
    }

    @Test
    public void whenAddedProduct_thenReturnTrue() {
        boolean exists = repository.existsByCartAndProductId(cart.getId(), atmamun.getId());

        assertTrue(exists);
    }

}
