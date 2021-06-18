package ua.deti.tqs.easydeliversadmin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.deti.tqs.easydeliversadmin.entities.Delivery;
import ua.deti.tqs.easydeliversadmin.entities.State;

import java.util.List;

public interface StateRepository extends JpaRepository<State,Integer> {
}
