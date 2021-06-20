package ua.deti.tqs.easydeliversadmin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.deti.tqs.easydeliversadmin.entities.Delivery;
import ua.deti.tqs.easydeliversadmin.entities.Rider;

import java.util.List;

@Repository
public interface RiderRepository extends JpaRepository<Rider,Integer> {
    Rider findRiderByEmail(String email);
    Rider findRiderById(int id);
    List<Rider> findAll();
}
