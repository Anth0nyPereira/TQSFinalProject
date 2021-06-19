package ua.deti.tqs.easydeliversadmin.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.deti.tqs.easydeliversadmin.component.Geocoder;
import ua.deti.tqs.easydeliversadmin.entities.Delivery;
import ua.deti.tqs.easydeliversadmin.entities.Rider;
import ua.deti.tqs.easydeliversadmin.entities.State;
import ua.deti.tqs.easydeliversadmin.repository.DeliveryRepository;
import ua.deti.tqs.easydeliversadmin.repository.RiderRepository;
import ua.deti.tqs.easydeliversadmin.repository.StateRepository;
import ua.deti.tqs.easydeliversadmin.utils.PasswordEncryption;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StatisticService_UnitTest {

    @Mock( lenient = true)
    private DeliveryRepository deliveryRepository;

    @Mock(lenient = true)
    private StateRepository stateRepository;

    @Mock(lenient = true)
    private RiderRepository riderRepository;

    @Mock(lenient = true)
    private Geocoder geocoder;

    @InjectMocks
    private EasyDeliversService service;

    @BeforeEach
    public void setUp() throws Exception {
        long actualLong = System.currentTimeMillis();
        List<State> listOfStatesByDescriptionCompletedAndTimestamp = new ArrayList<>();
        listOfStatesByDescriptionCompletedAndTimestamp.add(new State("completed", 2, new Timestamp(actualLong - 2000)));
        listOfStatesByDescriptionCompletedAndTimestamp.add(new State("completed", 3, new Timestamp(actualLong - 200000)));
        listOfStatesByDescriptionCompletedAndTimestamp.add(new State("completed", 4, new Timestamp(actualLong - 9000000)));
        listOfStatesByDescriptionCompletedAndTimestamp.add(new State("completed", 6, new Timestamp(actualLong - 86300000)));
        when(stateRepository.findStatesByDescriptionAndTimestampBetween(eq("completed"), any(Timestamp.class), any(Timestamp.class))).thenReturn(listOfStatesByDescriptionCompletedAndTimestamp);

        List<State> listOfStatesByDescriptionAcceptedAndTimestamp = new ArrayList<>();
        listOfStatesByDescriptionCompletedAndTimestamp.add(new State("accepted", 2, new Timestamp(actualLong - 7000)));
        listOfStatesByDescriptionCompletedAndTimestamp.add(new State("accepted", 3, new Timestamp(actualLong - 205000)));
        listOfStatesByDescriptionCompletedAndTimestamp.add(new State("accepted", 4, new Timestamp(actualLong - 9010000)));
        listOfStatesByDescriptionCompletedAndTimestamp.add(new State("accepted", 6, new Timestamp(actualLong - 86304000)));
        when(stateRepository.findStatesByDescriptionAndTimestampBetween(eq("accepted"), any(Timestamp.class), any(Timestamp.class))).thenReturn(listOfStatesByDescriptionAcceptedAndTimestamp);

        List<Delivery> listOfCompletedDeliveries = new ArrayList<>();

        Delivery del1= new Delivery(1,2,"completed","919292112","DETI","Bairro de Santiago", 5);
        Delivery del2= new Delivery(2,4,"completed","919292941","Staples Aveiro","Bairro do Liceu", 3);
        Delivery del3= new Delivery(3,4,"completed","949292921","ProudPapers","Avenida Doutor Lourenço Peixinho ", 3);
        Delivery del4= new Delivery(3,4,"completed","949292921","ProudPapers","Avenida Doutor Lourenço Peixinho ", 3);

        listOfCompletedDeliveries = Arrays.asList(del1, del2, del3);

        when(deliveryRepository.findDeliveriesByState(any())).thenReturn(listOfCompletedDeliveries);

        List<Rider> listOfAllRiders = new ArrayList<>();
        PasswordEncryption enc = new PasswordEncryption();
        Rider r1= new Rider("hugo","ferreira","hugo@email.com", enc.encrypt("12345"), "930921312","car", 1000.00);
        Rider r2= new Rider("clara","sousa","csousa@email.com", enc.encrypt("anvd"), "911551312","car", 1000.00);
        Rider r3= new Rider("pedro","teixeira","teixeira@email.com", enc.encrypt("12345"), "930921312","car", 1000.00);

        listOfAllRiders = Arrays.asList(r1, r2, r3);

        when(riderRepository.findAll()).thenReturn(listOfAllRiders);

        when(deliveryRepository.findDeliveryById(2)).thenReturn(del1);
        when(deliveryRepository.findDeliveryById(3)).thenReturn(del2);
        when(deliveryRepository.findDeliveryById(4)).thenReturn(del3);
        when(deliveryRepository.findDeliveryById(5)).thenReturn(del4);
        when(geocoder.getDistanceBetweenTwoAddressesWithExternalApi("Bairro de Santiago", "Estação de Aveiro")).thenReturn(4.2);
        when(geocoder.getDistanceBetweenTwoAddressesWithExternalApi("Bairro do Liceu", "Glicínias Plaza")).thenReturn(2.8);
        when(geocoder.getDistanceBetweenTwoAddressesWithExternalApi("ProudPapers", "Avenida Doutor Lourenço Peixinho")).thenReturn(3.0);
        when(geocoder.getDistanceBetweenTwoAddressesWithExternalApi("DETI", "DECA")).thenReturn(1.2);
        when(geocoder.getDistanceBetweenTwoAddressesWithExternalApi("Staples Aveiro", "DETI")).thenReturn(7.8);
        when(geocoder.getDistanceBetweenTwoAddressesWithExternalApi("EasyDelivers", "ProudPapers")).thenReturn(5.0);

    }

    @Test
    public void kmCoveredTest() {
        double kmsCovered = service.sumOfKmCoveredInLast24Hours();
        assertEquals(24.0, kmsCovered);

        verify(stateRepository, times(1)).findStatesByDescriptionAndTimestampBetween(eq("completed"), any(Timestamp.class), any(Timestamp.class));
        verify(deliveryRepository, times(4)).findDeliveryById(any(Integer.class));
        verify(geocoder, times(1)).getDistanceBetweenTwoAddressesWithExternalApi(any(String.class), any(String.class));
    }

    @Test
    public void nrDeliveriesTest() {
        int numberDeliveriesMadeForLast24Hours = service.numberDeliveriesMadeForLast24Hours();
        assertEquals(4, numberDeliveriesMadeForLast24Hours);
        verify(stateRepository, times(1))
                .findStatesByDescriptionAndTimestampBetween(eq("completed"), any(Timestamp.class), any(Timestamp.class));
    }

    @Test
    public void averageTimeTest() {
        double averageTime = service.averageTimeDeliveries();
        assertEquals(6000, averageTime);
        verify(stateRepository, times(1))
                .findStatesByDescriptionAndTimestampBetween(eq("accepted"), any(Timestamp.class), any(Timestamp.class));
        verify(stateRepository, times(1))
                .findStatesByDescriptionAndTimestampBetween(eq("completed"), any(Timestamp.class), any(Timestamp.class));
    }

    @Test
    public void averageScoreTest() {
        double averageScore = service.averageRidersScore();
        assertEquals((11/3),averageScore);
        verify(deliveryRepository, times(1))
                .findDeliveriesByState(eq("completed"));
    }

    @Test
    public void getAllRidersTest(){
        List<Rider> allRidersList = service.getAllRiders();
        assertThat(allRidersList.size()).isEqualTo(3);
        verify(riderRepository, times(1))
                .findAll();
    }

    @Test
    public void getNumberDeliveriesInTheLast13DaysTest(){
        List<Integer> alldeliveriesin13days = service.numberDeliveriesMadeForLast13Days();
        assertThat(alldeliveriesin13days.size()).isEqualTo(13);
        verify(stateRepository, times(13))
                .findStatesByDescriptionAndTimestampBetween(eq("completed"), any(Timestamp.class), any(Timestamp.class));
    }

    @Test
    public void getAverageDeliveryTimesInTheLast13DaysTest(){
        List<Integer> alldeliveryTimesin13days = service.numberDeliveriesMadeForLast13Days();
        assertThat(alldeliveryTimesin13days.size()).isEqualTo(13);
        // First, we'll fetch all deliveries that were completed in the day we want.
        // We want to do this 13 times (one for each day)
        verify(stateRepository, times(13))
                .findStatesByDescriptionAndTimestampBetween(eq("completed"), any(), any());
    }

    @AfterEach
    public void tearDown() {

    }
}
