package tqs.proudpapers.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tqs.proudpapers.entity.Delivery;
import tqs.proudpapers.entity.ProductOfDelivery;

import java.util.List;

/**
 * @author wy
 * @date 2021/6/3 22:32
 */
public interface DeliveryRepository extends JpaRepository<Delivery, Integer> {

    @Query(nativeQuery = true, value = "SELECT * FROM products_of_delivery WHERE client = :client")
    List<ProductOfDelivery> getProductsOfDeliveryById(Integer client);
}
