package ua.deti.tqs.easydeliversadmin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.deti.tqs.easydeliversadmin.entities.Admin;

public interface AdminRepository extends JpaRepository<Admin, Integer> {
    Admin findAdminByEmail(String email);
}
