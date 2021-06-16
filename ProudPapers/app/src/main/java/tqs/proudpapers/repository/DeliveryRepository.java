package tqs.proudpapers.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import tqs.proudpapers.entity.Delivery;
import tqs.proudpapers.entity.ProductOfDelivery;

import java.util.List;
import java.util.Map;

/**
 * @author wy
 * @date 2021/6/3 22:32
 */
public interface DeliveryRepository extends JpaRepository<Delivery, Integer> {

    @Query(nativeQuery = true, value = "SELECT * FROM products_of_delivery WHERE delivery=:delivery")
    List<Map<String, Integer>> getProductsOfDeliveryById(Integer delivery);

    @Modifying
    @Query(nativeQuery = true, value = "INSERT INTO products_of_delivery VALUE(:product, :delivery, :quantity) ")
    void addProductToDelivery(Integer delivery, Integer product, Integer quantity);

    @Modifying(clearAutomatically = true)
    @Query(nativeQuery = true, value = "UPDATE delivery SET state=:state WHERE id=:delivery")
    void changeStateOfDelivery(Integer delivery, String state);

    @Modifying(clearAutomatically = true)
    @Query(nativeQuery = true, value = "UPDATE delivery SET id_delivery_store=:id WHERE id=:delivery")
    void setDeliveryIdInStore(Integer delivery, Integer id);

    List<Delivery> getDeliveriesByClient(Integer client);

    Delivery findAllById(Integer id);
}
