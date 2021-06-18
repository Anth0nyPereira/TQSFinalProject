package ua.deti.tqs.easydeliversadmin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.deti.tqs.easydeliversadmin.entities.Store;

@Repository
public interface StoreRepository extends JpaRepository<Store,Integer> {
    Store findStoreById(Integer id);
}
