package ua.deti.tqs.easydeliversadmin.service;

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
import ua.deti.tqs.easydeliversadmin.entities.State;
import ua.deti.tqs.easydeliversadmin.entities.Store;
import ua.deti.tqs.easydeliversadmin.repository.DeliveryRepository;
import ua.deti.tqs.easydeliversadmin.repository.RiderRepository;
import ua.deti.tqs.easydeliversadmin.repository.StateRepository;
import ua.deti.tqs.easydeliversadmin.repository.StoreRepository;
import ua.deti.tqs.easydeliversadmin.utils.PasswordEncryption;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class EasyDeliversServiceIT {

    @Mock(lenient = true)
    private RiderRepository riderRepository;

    @Mock(lenient = true)
    private DeliveryRepository deliveryRepository;

    @Mock(lenient = true)
    private StateRepository stateRepository;

    @Mock(lenient = true)
    private StoreRepository storeRepository;

    @InjectMocks
    private EasyDeliversService easyDeliversService;

    Rider rider1;
    Rider invalid;
    Rider newRider;
    Delivery del1;
    Delivery del2;
    Delivery del3;
    List<Delivery> allDeliversAwaitingProcessing;
    State newState;
    Store newStore;

    PasswordEncryption enc;

    @BeforeEach
    void setUp() throws Exception {
        rider1 = new Rider("hugo","ferreira","hugo@email.com", PasswordEncryption.encrypt("12345"), "930921312","car");
        invalid = new Rider("firstname","lastname","email",PasswordEncryption.encrypt("password"),"telephone","transportation");
        newRider = new Rider("firstname","lastname","notfake@email.com",PasswordEncryption.encrypt("password"),"telephone","transportation");

        del1= new Delivery(1,2,"awaiting_processing","919292112","DETI","Bairro de Santiago");
        del2= new Delivery(2,2,"awaiting_processing","919292941","Staples Aveiro","Bairro do Liceu");
        del3= new Delivery(3,2,"awaiting_processing","949292921","ProudPapers","Avenida Doutor Lourenço Peixinho ");
        del1.setId(1);
        del2.setId(-1);
        allDeliversAwaitingProcessing = Arrays.asList(del1, del2, del3);

        newState = new State("awaiting_processing", del1.getId(), new Timestamp(System.currentTimeMillis()));
        newStore = new Store("PP", "localhost:8081");
        newStore.setId(1);

        Mockito.when(deliveryRepository.findDeliveryById(Integer.parseInt("1"))).thenReturn(del1);
        Mockito.when(deliveryRepository.findDeliveryById(Integer.parseInt("20"))).thenReturn(null);
        Mockito.when(riderRepository.findRiderByEmail("hugo@email.com")).thenReturn(rider1);
        Mockito.when(riderRepository.findRiderByEmail("no@email.com")).thenReturn(null);
        Mockito.when(riderRepository.findRiderByEmail("notfake@email.com")).thenReturn(null);
        Mockito.when(riderRepository.save(eq(newRider))).thenReturn(newRider);
        Mockito.when(riderRepository.save(eq(invalid))).thenReturn(null);
        Mockito.when(deliveryRepository.findDeliveriesByState("awaiting_processing")).thenReturn(allDeliversAwaitingProcessing);
    }


    @Test
    @DisplayName("Tests a Successful Assign Rider to Deliver")
    void whenSuccessfulAssignRiderDeliver(){
        Mockito.when(storeRepository.findStoreById(any())).thenReturn(newStore);
        String x = easyDeliversService.assignRiderDeliver("1","1");
        assertEquals("Delivery Assigned",x);
        verify(deliveryRepository,times(1))
                .findDeliveryById(1);
    }

    @Test
    @DisplayName("Tests a successful Update Delivery")
    void whenSuccessfulUpdateDeliveryTest(){
        Mockito.when(storeRepository.findStoreById(any())).thenReturn(newStore);
        String x = easyDeliversService.updateDeliveryStateByRider("1","1","done");
        assertEquals("Delivery State Changed",x);
        verify(deliveryRepository,times(1))
                .findDeliveryById(1);

    }

}
