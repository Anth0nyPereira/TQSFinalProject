package tqs.proudpapers.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tqs.proudpapers.entity.ProductOfCart;

import java.util.List;

/**
 * @author wy
 * @date 2021/6/13 9:59
 */
public interface CartRepository extends JpaRepository<ProductOfCart, Integer> {
    List<ProductOfCart> getProductOfCartByCart(Integer id);

    void removeProductOfCartByCart(Integer id);

    @Query(nativeQuery = true, value = "SELECT id FROM cart WHERE client = :client")
    Integer getCartByClientId(Integer client);

    @Query(nativeQuery = true, value = "INSERT INTO cart VALUE(:client)")
    Integer createCart(Integer client);

    boolean existsByCartAndProductId(Integer cart, Integer product);
}
