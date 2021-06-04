package tqs.proudpapers.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tqs.proudpapers.entity.Client;
import tqs.proudpapers.entity.ClientDTO;
import tqs.proudpapers.entity.PaymentMethod;
import tqs.proudpapers.repository.ClientRepository;
import tqs.proudpapers.repository.PaymentMethodRepository;
import tqs.proudpapers.service.ClientService;

/**
 * @author wy
 * @date 2021/6/4 12:42
 */
@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Override
    public void saveClient(ClientDTO clientDTO) {
        Client client = new Client();
        BeanUtils.copyProperties(clientDTO, client);

        PaymentMethod saved = paymentMethodRepository.save(clientDTO.getPaymentMethod());

        client.setPaymentMethodId(saved.getId());
        client.setAddress(clientDTO.getZip() + ", " + clientDTO.getCity());
        clientRepository.save(client);
    }

    @Override
    public ClientDTO getClient(String email, String password) {
        Client client = clientRepository.getByEmailAndPassword(email, password);
        if (client == null) return null;

        ClientDTO dto = new ClientDTO();
        BeanUtils.copyProperties(client, dto);

        PaymentMethod paymentMethod = paymentMethodRepository.getById(client.getPaymentMethodId());
        dto.setPaymentMethod(paymentMethod);
        return dto;
    }

}
