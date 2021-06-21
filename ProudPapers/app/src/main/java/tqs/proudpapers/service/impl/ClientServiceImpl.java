package tqs.proudpapers.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tqs.proudpapers.component.PasswordEncryption;
import tqs.proudpapers.entity.Client;
import tqs.proudpapers.entity.ClientDTO;
import tqs.proudpapers.entity.PaymentMethod;
import tqs.proudpapers.repository.CartRepository;
import tqs.proudpapers.repository.ClientRepository;
import tqs.proudpapers.repository.PaymentMethodRepository;
import tqs.proudpapers.service.ClientService;
import javax.transaction.Transactional;

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

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private PasswordEncryption encryption;

    @Override
    @Transactional
    public Client saveClient(ClientDTO clientDTO) {
        Client client = new Client();
        BeanUtils.copyProperties(clientDTO, client);

        if (clientDTO.getPaymentMethod() != null){
            PaymentMethod saved = paymentMethodRepository.save(clientDTO.getPaymentMethod());
            client.setPaymentMethodId(saved.getId());
        }

        client.setAddress(clientDTO.getZip() + "," + clientDTO.getCity());


        try {
            client.setPassword(encryption.encrypt(client.getPassword()));
            Client clientByEmail = clientRepository.getClientByEmail(client.getEmail());
            if (clientByEmail != null)
                 return null;

            Client saved = clientRepository.save(client);
            cartRepository.createCart(saved.getId());
            return saved;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public ClientDTO getClientByEmailAndPass(String email, String password) {
        try {
            Client client = clientRepository.getClientByEmailAndPassword(email, encryption.encrypt(password));
            return getClientDTO(client);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public ClientDTO getClientByEmail(String email) {
        return getClientDTO(clientRepository.getClientByEmail(email));
    }

    @Override
    public ClientDTO getClientById(Integer id) {
        return getClientDTO(clientRepository.getClientById(id));
    }


    private ClientDTO getClientDTO(Client client) {
        if (client == null) return null;

        ClientDTO dto = new ClientDTO();
        BeanUtils.copyProperties(client, dto);

        if (client.getPaymentMethodId() != null){
            PaymentMethod paymentMethod = paymentMethodRepository.getById(client.getPaymentMethodId());
            System.out.println(paymentMethod); //load paymentMethod
            dto.setPaymentMethod(paymentMethod);
        }

        String[] split = client.getAddress().split(",");
        dto.setZip(split[0].trim());
        dto.setCity(split[1].trim());
        return dto;
    }

}
