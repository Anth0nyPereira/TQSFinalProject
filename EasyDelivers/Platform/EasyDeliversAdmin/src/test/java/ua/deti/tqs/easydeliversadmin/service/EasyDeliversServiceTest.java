package ua.deti.tqs.easydeliversadmin.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.deti.tqs.easydeliversadmin.entities.Rider;
import ua.deti.tqs.easydeliversadmin.repository.RiderRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EasyDeliversServiceTest {

    @Mock(lenient = true)
    private RiderRepository riderRepository;

    @InjectMocks
    private EasyDeliversService easyDeliversService;

    Rider rider1;
    Rider invalid;
    @BeforeEach
    void setUp() {
        rider1 = new Rider("hugo","ferreira","hugo@email.com", "12345", "930921312","car");
        invalid = new Rider("firstname","lastname","email","password","telephone","transportation");
        Mockito.when(riderRepository.findRiderByEmail("hugo@email.com")).thenReturn(rider1);
        Mockito.when(riderRepository.findRiderByEmail("no@email.com")).thenReturn(null);
        Mockito.when(riderRepository.save(eq(rider1))).thenReturn(rider1);
        Mockito.when(riderRepository.save(eq(invalid))).thenReturn(null);

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("Tests a valid authentication rider")
    void whenValidAuthenticateRider(){
        Boolean x = easyDeliversService.authenticateRider("hugo@email.com","12345");
        assertEquals(true,x);
        verify(riderRepository,times(1))
                .findRiderByEmail("hugo@email.com");
    }

    @Test
    @DisplayName("Tests a invalid authentication rider (bad email)")
    void whenInvalidEmailAuthenticateRider(){
        Boolean x = easyDeliversService.authenticateRider("no@email.com","12345");
        assertEquals(false,x);
        verify(riderRepository,times(1))
                .findRiderByEmail("no@email.com");

    }

    @Test
    @DisplayName("Tests a invalid authentication rider (bad password)")
    void whenInvalidPassAuthenticateRider(){
        Boolean x = easyDeliversService.authenticateRider("hugo@email.com","54321");
        assertEquals(false,x);
        verify(riderRepository,times(1))
                .findRiderByEmail("hugo@email.com");

    }

    @Test
    @DisplayName("Tests a valid get rider")
    void whenValidGetRider(){
        Rider x = easyDeliversService.getRider("hugo@email.com");
        assertEquals(rider1.getEmail(),x.getEmail());
        assertEquals(rider1.getFirstname(),x.getFirstname());
        assertEquals(rider1.getLastname(),x.getLastname());
        assertEquals(rider1.getPassword(),x.getPassword());
        assertEquals(rider1.getTelephone(),x.getTelephone());
        assertEquals(rider1.getTransportation(),x.getTransportation());

        verify(riderRepository,times(1))
                .findRiderByEmail("hugo@email.com");

    }

    @Test
    @DisplayName("Tests a invalid get rider")
    void whenInvalidGetRider(){
        Rider x = easyDeliversService.getRider("no@email.com");
        assertEquals(null,x);
        verify(riderRepository,times(1))
                .findRiderByEmail("no@email.com");

    }

    @Test
    @DisplayName("Tests a valid create rider")
    void whenValidCreateRider(){
        Rider x = easyDeliversService.createRider("hugo","ferreira","hugo@email.com", "12345", "930921312","car");
        assertEquals(rider1.getEmail(),x.getEmail());
        assertEquals(rider1.getFirstname(),x.getFirstname());
        assertEquals(rider1.getLastname(),x.getLastname());
        assertEquals(rider1.getPassword(),x.getPassword());
        assertEquals(rider1.getTelephone(),x.getTelephone());
        assertEquals(rider1.getTransportation(),x.getTransportation());
        verify(riderRepository,times(1))
                .save(eq(rider1));


    }

    @Test
    @DisplayName("Tests a invalid create rider")
    void whenInvalidCreateRider(){
        Rider x = easyDeliversService.createRider("firstname","lastname","email","password","telephone","transportation");
        assertEquals(null,x);
        verify(riderRepository,times(1))
                .save(eq(invalid));
    }

}