package tqs.proudpapers.cart;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import tqs.proudpapers.entity.Product;
import static org.junit.jupiter.api.Assertions.assertEquals;
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

    private ProductOfCart pcart;

    @BeforeEach
    void setUp() {
        Product atmamun = new Product();
        atmamun.setName("Atmamun");
        atmamun.setDescription("The Path To Achieving The Blis Of The Himalayan Swamis. And The Freedom Of A Living God");
        atmamun.setPrice(15.99);
        atmamun.setQuantity(13);

        pcart.setProduct(atmamun);
        pcart.setCartId(1);

        entityManager.persistAndFlush(pcart); //ensure data is persisted at this point
    }


    @Test
    public void whenCartId_thenReturnProducts() {
        List<ProductOfCart> products = repository.getProductsByCartID(1);
        assertEquals(1, products.size());
        assertEquals(pcart, products.get(0));
    }

    @Test
    public void whenClear_thenReturnEmptyList() {
        repository.clear(1);

        List<ProductOfCart> products = repository.getProductsByCartID(1);
        assertEquals(0, products.size());
    }


}
