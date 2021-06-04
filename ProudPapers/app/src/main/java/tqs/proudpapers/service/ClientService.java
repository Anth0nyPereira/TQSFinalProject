package tqs.proudpapers.service;

import tqs.proudpapers.entity.Client;
import tqs.proudpapers.entity.ClientDTO;

/**
 * @author wy
 * @date 2021/6/4 12:41
 */
public interface ClientService {
    void saveClient(ClientDTO clientDTO);

    ClientDTO getClient(String email, String password);
}
