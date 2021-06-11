package tqs.proudpapers.cart;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import tqs.proudpapers.entity.Product;
import tqs.proudpapers.repository.ClientRepository;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wy
 * @date 2021/6/5 21:12
 */
@ExtendWith(MockitoExtension.class)
public class CartService_WithMock {

    @Mock(lenient = true)
    private ClientRepository repository;

    @InjectMocks
    private CartServiceImpl service;

    @Test
    public void whenClientId_thenReturnCart() {
        Product b1 = new Product();
        b1.setName("Book A");

        Product b2 = new Product();
        b2.setName("Book B");

        Product b3 = new Product();
        b3.setName("Book C");

        int clientId = 1;
        int cartId = 2;

        CartDTO cart = new CartDTO();
        cart.setId(cartId);
        cart.setProducts(products);

        List<ProductOfCart> list = Arrays.asList(b1, b2, b3).stream()
                .map(b -> new ProductOfCart(cartId, b))
                .collect(Collectors.toList());

        Mockito.when(repository.getProductsByCartID(cartId)).thenReturn(list);

        CartDTO result = service.getCartByClientID(clientId);

        assertEquals(3, result.getProducts.size());
        assertEquals(clientId, result.getClientId());
        assertThat(result.getProducts())
                .contains(b1)
                .contains(b2)
                .contains(b3);
    }

}
