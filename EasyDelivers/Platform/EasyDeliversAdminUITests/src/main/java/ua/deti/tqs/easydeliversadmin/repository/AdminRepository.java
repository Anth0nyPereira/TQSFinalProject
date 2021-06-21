package ua.deti.tqs.easydeliversadmin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.deti.tqs.easydeliversadmin.entities.Admin;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {
    Admin findAdminByEmail(String email);
}
