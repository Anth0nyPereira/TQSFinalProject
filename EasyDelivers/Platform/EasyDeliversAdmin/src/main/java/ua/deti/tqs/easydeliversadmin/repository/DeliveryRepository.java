package ua.deti.tqs.easydeliversadmin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.deti.tqs.easydeliversadmin.entities.Delivery;
import ua.deti.tqs.easydeliversadmin.entities.Rider;

import java.util.List;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery,Integer> {
    List<Delivery> findDeliveriesByState (String State);
}
