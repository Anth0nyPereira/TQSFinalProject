package tqs.proudpapers.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tqs.proudpapers.entity.Delivery;

/**
 * @author wy
 * @date 2021/6/3 22:32
 */
public interface DeliveryRepository extends JpaRepository<Delivery, Integer> {

}
