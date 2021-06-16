package tqs.proudpapers.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tqs.proudpapers.entity.CartDTO;
import tqs.proudpapers.entity.ClientDTO;
import tqs.proudpapers.entity.ProductOfCart;
import tqs.proudpapers.entity.ProductOfCartDTO;
import tqs.proudpapers.repository.CartRepository;
import tqs.proudpapers.service.CartService;
import tqs.proudpapers.service.DeliveryService;
import tqs.proudpapers.service.ProductService;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * @author wy
 * @date 2021/6/13 12:05
 */
@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private CartRepository repository;

    @Autowired
    private ProductService productService;

    @Autowired
    private DeliveryService deliveryService;


    @Override
    public CartDTO getCartByClientID(Integer id) {
        Integer cartId = repository.getCartByClientId(id);
        List<ProductOfCart> productsOfCart = repository.getProductOfCartByCart(cartId);

        List<ProductOfCartDTO> results = productsOfCart.stream()
                .map(p ->{
                    ProductOfCartDTO productOfCartDTO = new ProductOfCartDTO();
                    productOfCartDTO.setCart(cartId);
                    productOfCartDTO.setQuantity(p.getQuantity());
                    productOfCartDTO.setProduct(productService.searchById(p.getProductId()));
                    BeanUtils.copyProperties(p, productOfCartDTO);
                    return productOfCartDTO;
                })
                .collect(Collectors.toList());

        return new CartDTO(cartId, id, results, results.stream().map(p->p.getProduct().getPrice()).reduce(Double::sum).orElse(0.0));
    }

    @Override
    public Integer buyAllProductsInTheCart(ClientDTO clientDTO) {
        CartDTO cart = getCartByClientID(clientDTO.getId());
        repository.removeProductOfCartByCart(cart.getCartId());
        return deliveryService.addProductToDelivery(clientDTO.getId(), cart.getProductOfCarts());
    }

    @Override
    public ProductOfCart save(Integer clientId, Integer productId, Integer quantity) {
        Integer cart = repository.getCartByClientId(clientId);
        if (cart == null) return null;

        boolean added = repository.existsByCartAndProductId(cart, productId);

        if (added) return null;

        ProductOfCart productOfCart = new ProductOfCart(cart, productId, quantity);
        return repository.save(productOfCart);
    }

}
