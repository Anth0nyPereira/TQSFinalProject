package tqs.proudpapers.service;

import tqs.proudpapers.entity.CartDTO;
import tqs.proudpapers.entity.Product;
import tqs.proudpapers.entity.ProductOfCart;

/**
 * @author wy
 * @date 2021/6/13 11:49
 */
public interface CartService {
    CartDTO getCartByClientID(Integer clientId);

    void buyAllProductsInTheCart(Integer cartId);

    ProductOfCart save(Integer clientId, Integer productId, Integer quantity);

}
