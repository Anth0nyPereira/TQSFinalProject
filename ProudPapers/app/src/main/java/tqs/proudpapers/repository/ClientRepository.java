package tqs.proudpapers.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tqs.proudpapers.entity.Client;

/**
 * @author wy
 * @date 2021/6/3 22:31
 */
public interface ClientRepository extends JpaRepository<Client, Integer> {
    Client getClientByEmail(String email);
    Client getClientByEmailAndPassword(String email, String password);
}
