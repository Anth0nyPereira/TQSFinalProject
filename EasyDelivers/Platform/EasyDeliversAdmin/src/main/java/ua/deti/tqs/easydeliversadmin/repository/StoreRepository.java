package ua.deti.tqs.easydeliversadmin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.deti.tqs.easydeliversadmin.entities.Store;

public interface StoreRepository extends JpaRepository<Store,Integer> {
    Store findStoreById(Integer id);
}
