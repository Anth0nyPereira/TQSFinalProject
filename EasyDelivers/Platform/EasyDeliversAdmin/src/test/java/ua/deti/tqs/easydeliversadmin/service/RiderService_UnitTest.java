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
import ua.deti.tqs.easydeliversadmin.entities.Delivery;
import ua.deti.tqs.easydeliversadmin.entities.Rider;
import ua.deti.tqs.easydeliversadmin.entities.Store;
import ua.deti.tqs.easydeliversadmin.repository.DeliveryRepository;
import ua.deti.tqs.easydeliversadmin.repository.RiderRepository;
import ua.deti.tqs.easydeliversadmin.repository.StateRepository;
import ua.deti.tqs.easydeliversadmin.repository.StoreRepository;
import ua.deti.tqs.easydeliversadmin.utils.PasswordEncryption;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RiderService_UnitTest {

    @Mock(lenient = true)
    private RiderRepository riderRepository;

    @Mock(lenient = true)
    private DeliveryRepository deliveryRepository;

    @Mock(lenient = true)
    private StoreRepository storeRepository;

    @Mock(lenient = true)
    private StateRepository stateRepository;

    @InjectMocks
    private EasyDeliversService easyDeliversService;

    Rider rider1;
    Rider invalid;
    Rider newRider;
    Delivery del1;
    Delivery del2;
    Delivery del3;
    List<Delivery> allDeliversAwaitingProcessing;
    Store newStore;

    PasswordEncryption enc;

    @BeforeEach
    void setUp() throws Exception {
        enc = new PasswordEncryption();
        rider1 = new Rider("hugo","ferreira","hugo@email.com", enc.encrypt("12345"), "930921312","car");
        invalid = new Rider("firstname","lastname","email","password","telephone","transportation");
        newRider = new Rider("firstname","lastname","notfake@email.com","password","telephone","transportation");

        del1= new Delivery(1,2,"awaiting_processing","919292112","DETI","Bairro de Santiago");
        del2= new Delivery(2,4,"awaiting_processing","919292941","Staples Aveiro","Bairro do Liceu");
        del3= new Delivery(3,4,"awaiting_processing","949292921","ProudPapers","Avenida Doutor Lourenço Peixinho ");

        newStore = new Store("PP", "localhost:8081");
        newStore.setId(1);

        allDeliversAwaitingProcessing = Arrays.asList(del1, del2, del3);
        Mockito.when(deliveryRepository.findDeliveryById(Integer.parseInt("1"))).thenReturn(del1);
        Mockito.when(deliveryRepository.findDeliveryById(Integer.parseInt("20"))).thenReturn(null);
        Mockito.when(riderRepository.findRiderByEmail("hugo@email.com")).thenReturn(rider1);
        Mockito.when(riderRepository.findRiderByEmail("no@email.com")).thenReturn(null);
        Mockito.when(riderRepository.findRiderByEmail("notfake@email.com")).thenReturn(null);
        Mockito.when(riderRepository.save(eq(newRider))).thenReturn(newRider);
        Mockito.when(riderRepository.save(eq(invalid))).thenReturn(null);
        Mockito.when(deliveryRepository.findDeliveriesByState("awaiting_processing")).thenReturn(allDeliversAwaitingProcessing);
        Mockito.when(storeRepository.findStoreById(any())).thenReturn(newStore);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("Tests a valid authentication rider")
    void whenValidAuthenticateRider() throws Exception {
        Boolean x = easyDeliversService.authenticateRider("hugo@email.com","12345");
        assertEquals(true,x);
        verify(riderRepository,times(1))
                .findRiderByEmail("hugo@email.com");
    }

    @Test
    @DisplayName("Tests a invalid authentication rider (bad email)")
    void whenInvalidEmailAuthenticateRider() throws Exception {
        Boolean x = easyDeliversService.authenticateRider("no@email.com","12345");
        assertEquals(false,x);
        verify(riderRepository,times(1))
                .findRiderByEmail("no@email.com");

    }

    @Test
    @DisplayName("Tests a invalid authentication rider (bad password)")
    void whenInvalidPassAuthenticateRider() throws Exception {
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
        Rider x = easyDeliversService.createRider("firstname","lastname","notfake@email.com","password","telephone","transportation");
        assertEquals(newRider.getEmail(),x.getEmail());
        assertEquals(newRider.getFirstname(),x.getFirstname());
        assertEquals(newRider.getLastname(),x.getLastname());
        assertEquals(newRider.getPassword(),x.getPassword());
        assertEquals(newRider.getTelephone(),x.getTelephone());
        assertEquals(newRider.getTransportation(),x.getTransportation());
        verify(riderRepository,times(1))
                .save(eq(newRider));
        verify(riderRepository,times(1))
                .findRiderByEmail(newRider.getEmail());


    }

    @Test
    @DisplayName("Tests a invalid create rider")
    void whenInvalidCreateRider(){
        Rider x = easyDeliversService.createRider("firstname","lastname","email","password","telephone","transportation");
        assertNull(x);
        verify(riderRepository,times(1))
                .save(eq(invalid));
    }

    @Test
    @DisplayName("Tests request all available Deliveries")
     void whenGetAllAvailableDeliveries() {
        List<Delivery> dels = deliveryRepository.findDeliveriesByState("awaiting_processing");
        assertThat(dels).hasSize(3).extracting(Delivery::getStart).contains(del1.getStart(), del1.getStart(), del3.getStart());
        Mockito.verify(deliveryRepository,times(1)).findDeliveriesByState("awaiting_processing");
    }


    @Test
    @DisplayName("Tests a successful Update Delivery")
    void whenSuccessfulUpdateDeliveryTest(){
        String x = easyDeliversService.updateDeliveryStateByRider("1","1","done");
        assertEquals("Delivery State Changed",x);
        verify(deliveryRepository,times(1))
                .findDeliveryById(1);

    }

    @Test
    @DisplayName("Tests a invalid Update Delivery")
    void whenInvalidUpdateDeliveryTest(){
        String x = easyDeliversService.updateDeliveryStateByRider("20","1","done");
        assertEquals("error",x);
        verify(deliveryRepository,times(1))
                .findDeliveryById(20);
    }

    @Test
    @DisplayName("Tests a Successful Assign Rider to Deliver")
    void whenSuccessfulAssignRiderDeliver(){
        String x = easyDeliversService.assignRiderDeliver("1","1");
        assertEquals("Delivery Assigned",x);
        verify(deliveryRepository,times(1))
                .findDeliveryById(1);
    }

    @Test
    @DisplayName("Tests a invalid Assign Rider to Deliver")
    void whenInvalidAssignRiderDeliver(){
        String x = easyDeliversService.assignRiderDeliver("20","1");
        assertEquals("error",x);
        verify(deliveryRepository,times(1))
                .findDeliveryById(20);
    }


}