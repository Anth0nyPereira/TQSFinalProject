package tqs.proudpapers.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import tqs.proudpapers.entity.*;
import tqs.proudpapers.repository.CartRepository;
import tqs.proudpapers.service.DeliveryService;
import tqs.proudpapers.service.ProductService;
import tqs.proudpapers.service.impl.CartServiceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wy
 * @date 2021/6/5 21:12
 */
@ExtendWith(MockitoExtension.class)
public class CartService_WithMock_Test {

    @Mock(lenient = true)
    private CartRepository cartRepository;

    @Mock(lenient = true)
    private ProductService productService;

    @Mock(lenient = true)
    private DeliveryService deliveryService;

    @InjectMocks
    private CartServiceImpl cartService;


    @Test
    public void whenClientId_thenReturnCart() {
        int clientId = 1;
        int cartId = 2;
        int quantity = 1;

        Product b1 = new Product();
        b1.setName("Book A");
        b1.setPrice(11.0);
        b1.setId(1);
        ProductOfCartDTO dto1 = new ProductOfCartDTO(cartId, b1, quantity);

        Product b2 = new Product();
        b2.setName("Book B");
        b2.setPrice(12.0);
        b2.setId(2);
        ProductOfCartDTO dto2 = new ProductOfCartDTO(cartId, b2, quantity);

        Product b3 = new Product();
        b3.setName("Book C");
        b3.setPrice(13.0);
        b3.setId(3);
        ProductOfCartDTO dto3 = new ProductOfCartDTO(cartId, b3, quantity);

        CartDTO cart = new CartDTO();
        cart.setCartId(cartId);
        cart.setProductOfCarts(Arrays.asList(dto1, dto2, dto3));

        List<ProductOfCart> list = Arrays.asList(b1, b2, b3).stream()
                .map(b -> new ProductOfCart(cartId, b.getId(), 1))
                .collect(Collectors.toList());

        Mockito.when(cartRepository.getProductOfCartByCart(cartId)).thenReturn(list);
        Mockito.when(cartRepository.getCartByClientId(clientId)).thenReturn(cartId);

        Mockito.when(productService.searchById(1)).thenReturn(b1);
        Mockito.when(productService.searchById(2)).thenReturn(b2);
        Mockito.when(productService.searchById(3)).thenReturn(b3);

        CartDTO result = cartService.getCartByClientID(clientId);

        assertEquals(3, result.getProductOfCarts().size());
        assertEquals(clientId, result.getClientId());
        assertThat(result.getProductOfCarts())
                .contains(dto1)
                .contains(dto2)
                .contains(dto3);
    }

    @Test
    void buyAllProductInTheCart_thenReturnIdDeliveryStore(){
        int clientId = 1;
        int idDelivryStore = 1;
        Mockito.when(deliveryService.addProductToDelivery(clientId, new ArrayList<>())).thenReturn(idDelivryStore);

        ClientDTO dto = new ClientDTO();
        dto.setId(clientId);
        Integer returnedId = cartService.buyAllProductsInTheCart(dto);
        assertEquals(idDelivryStore, returnedId);
    }

}
