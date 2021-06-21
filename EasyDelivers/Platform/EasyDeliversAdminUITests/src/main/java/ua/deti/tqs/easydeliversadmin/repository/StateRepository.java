package ua.deti.tqs.easydeliversadmin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.deti.tqs.easydeliversadmin.entities.Delivery;
import ua.deti.tqs.easydeliversadmin.entities.State;

import java.util.List;

@Repository
public interface StateRepository extends JpaRepository<State,Integer> {
    State findStateByDelivery(Delivery delivery);
    List<State> findAll();
}
