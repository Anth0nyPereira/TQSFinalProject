package tqs.proudpapers.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.junit.jupiter.MockitoExtension;
import tqs.proudpapers.entity.Client;
import tqs.proudpapers.entity.ClientDTO;
import tqs.proudpapers.repository.ClientRepository;
import tqs.proudpapers.service.impl.ClientServiceImpl;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;

/**
 * @author wy
 * @date 2021/6/5 21:12
 */
@ExtendWith(MockitoExtension.class)
public class ClientService_WithMock {

    @Mock(lenient = true)
    private ClientRepository repository;

    @InjectMocks
    private ClientServiceImpl service;


    @BeforeEach
    public void setUp() {
        Client alex = new Client();
        alex.setEmail("alex@ua.pt");
        alex.setName("alex");
        alex.setPassword("alexS3cr3t");
        alex.setAddress("2222-222, aveiro");
        alex.setTelephone("1234567891011");

        Mockito.when(repository.getClientByEmail(alex.getEmail())).thenReturn(alex);
        Mockito.when(repository.getClientByEmail("invalid")).thenReturn(null);
        Mockito.when(repository.getClientByEmailAndPassword(alex.getEmail(), alex.getPassword())).thenReturn(alex);
        Mockito.when(repository.getClientByEmailAndPassword(alex.getEmail(), "invalid")).thenReturn(null);
        Mockito.when(repository.getClientByEmailAndPassword("invalid", "invalid")).thenReturn(null);
    }

    @Test
    public void whenValidEmail_thenReturnAlex() {
        String email = "alex@ua.pt";
        ClientDTO alex = service.getClientByEmail(email);

        assertEquals("alex@ua.pt", alex.getEmail());
        assertEquals("2222-222", alex.getZip());
        assertEquals("aveiro", alex.getCity());
    }

    @Test
    public void whenInValidEmail_thenReturnNull() {
        Client found = repository.getClientByEmail("invalid");

        assertNull(found);
    }

    @Test
    public void whenAlexEmailAndPassword_thenReturnAlex() {
        ClientDTO alex = service.getClientByEmailAndPass("alex@ua.pt", "alexS3cr3t");

        assertEquals("alex@ua.pt", alex.getEmail());
        assertEquals("2222-222", alex.getZip());
        assertEquals("aveiro", alex.getCity());
    }

    @Test
    public void whenAlexEmailAndInvalidPassword_thenReturnNull() {
        ClientDTO found = service.getClientByEmailAndPass("alex@ua.pt", "invalid");

        assertNull(found);
    }

    @Test
    public void whenInvalidEmailAndAnyPassword_thenReturnNull() {
        ClientDTO found  = service.getClientByEmailAndPass("invalid", "invalid");
        assertNull(found);
    }

}
