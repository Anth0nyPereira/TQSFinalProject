package tqs.proudpapers.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import tqs.proudpapers.entity.Client;
import tqs.proudpapers.entity.ClientDTO;
import tqs.proudpapers.repository.ClientRepository;
import tqs.proudpapers.service.impl.ClientServiceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * @author wy
 * @date 2021/6/5 21:12
 */
@ExtendWith(MockitoExtension.class)
public class ClientService_WithMock_Test {

    @Mock(lenient = true)
    private ClientRepository repository;

    @InjectMocks
    private ClientServiceImpl service;

    private Client alex;

    @BeforeEach
    public void setUp() {
        alex = new Client();
        alex.setEmail("alex@ua.pt");
        alex.setName("alex");
        alex.setPassword("alexS3cr3t");
        alex.setAddress("2222-222, aveiro");
        alex.setTelephone("1234567891011");
        alex.setId(111);
    }

    @Test
    public void whenValidEmail_thenReturnAlex() {
        Mockito.when(repository.getClientByEmail(alex.getEmail())).thenReturn(alex);

        String email = "alex@ua.pt";
        ClientDTO alex = service.getClientByEmail(email);

        assertEquals("alex@ua.pt", alex.getEmail());
        assertEquals("2222-222", alex.getZip());
        assertEquals("aveiro", alex.getCity());
    }

    @Test
    public void whenInValidEmail_thenReturnNull() {
        Mockito.when(repository.getClientByEmail("invalid")).thenReturn(null);

        Client found = repository.getClientByEmail("invalid");

        assertNull(found);
    }

    @Test
    public void whenAlexEmailAndPassword_thenReturnAlex() {
        Mockito.when(repository.getClientByEmailAndPassword(alex.getEmail(), alex.getPassword())).thenReturn(alex);

        ClientDTO alex = service.getClientByEmailAndPass("alex@ua.pt", "alexS3cr3t");

        assertEquals("alex@ua.pt", alex.getEmail());
        assertEquals("2222-222", alex.getZip());
        assertEquals("aveiro", alex.getCity());
    }

    @Test
    public void whenAlexEmailAndInvalidPassword_thenReturnNull() {
        Mockito.when(repository.getClientByEmailAndPassword(alex.getEmail(), "invalid")).thenReturn(null);

        ClientDTO found = service.getClientByEmailAndPass("alex@ua.pt", "invalid");

        assertNull(found);
    }

    @Test
    public void whenInvalidEmailAndAnyPassword_thenReturnNull() {
        Mockito.when(repository.getClientByEmailAndPassword("invalid", "invalid")).thenReturn(null);

        ClientDTO found  = service.getClientByEmailAndPass("invalid", "invalid");

        assertNull(found);
    }

    @Test
    public void whenAlexId_thenReturnAlex() {
        Mockito.when(repository.getClientById(alex.getId())).thenReturn(alex);

        ClientDTO found  = service.getClientById(alex.getId());

        assertEquals(alex.getName(), found.getName());
        assertEquals(alex.getTelephone(), found.getTelephone());
        assertEquals(alex.getEmail(), found.getEmail());
        assertEquals("2222-222", found.getZip());
        assertEquals("aveiro", found.getCity());
    }
}
