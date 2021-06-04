package tqs.proudpapers.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import tqs.proudpapers.entity.Client;

/**
 * @author wy
 * @date 2021/6/3 22:31
 */
public interface ClientRepository extends PagingAndSortingRepository<Client, Integer> {
    Client getClientByEmail(String email);
    Client getByEmailAndPassword(String email, String password);
}
