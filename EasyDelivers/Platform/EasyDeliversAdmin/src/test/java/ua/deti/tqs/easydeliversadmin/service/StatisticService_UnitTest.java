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
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StatisticService_UnitTest {
    private List<State> listOfStatesByDescriptionCompletedAndTimestamp;
    List<Delivery> listOfCompletedDeliveries;

    Rider r1;
    Rider r2;
    Rider r3;

    private long actualLong;

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
        actualLong = System.currentTimeMillis();
        listOfStatesByDescriptionCompletedAndTimestamp = new ArrayList<>();
        listOfStatesByDescriptionCompletedAndTimestamp.add(new State("completed", 2, new Timestamp(actualLong - 2000)));
        listOfStatesByDescriptionCompletedAndTimestamp.add(new State("completed", 3, new Timestamp(actualLong - 200000)));
        listOfStatesByDescriptionCompletedAndTimestamp.add(new State("completed", 4, new Timestamp(actualLong - 9000000)));
        listOfStatesByDescriptionCompletedAndTimestamp.add(new State("completed", 6, new Timestamp(actualLong - 86300000)));
        when(stateRepository.findStatesByDescriptionAndTimestampBetween(eq("completed"), any(Timestamp.class), any(Timestamp.class))).thenReturn(listOfStatesByDescriptionCompletedAndTimestamp);


        List<State> listOfStatesByDescriptionAcceptedAndTimestamp = new ArrayList<>();
        listOfStatesByDescriptionAcceptedAndTimestamp.add(new State("accepted", 2, new Timestamp(actualLong - 7000)));
        listOfStatesByDescriptionAcceptedAndTimestamp.add(new State("accepted", 3, new Timestamp(actualLong - 205000)));
        listOfStatesByDescriptionAcceptedAndTimestamp.add(new State("accepted", 4, new Timestamp(actualLong - 9010000)));
        listOfStatesByDescriptionAcceptedAndTimestamp.add(new State("accepted", 6, new Timestamp(actualLong - 86304000)));
        when(stateRepository.findStatesByDescriptionAndTimestampBetween(eq("accepted"), any(Timestamp.class), any(Timestamp.class))).thenReturn(listOfStatesByDescriptionAcceptedAndTimestamp);

        when(stateRepository.findStateByDeliveryAndDescription(eq(2), eq("accepted"))).thenReturn(listOfStatesByDescriptionAcceptedAndTimestamp.get(0));
        when(stateRepository.findStateByDeliveryAndDescription(eq(3), eq("accepted"))).thenReturn(listOfStatesByDescriptionAcceptedAndTimestamp.get(1));
        when(stateRepository.findStateByDeliveryAndDescription(eq(4), eq("accepted"))).thenReturn(listOfStatesByDescriptionAcceptedAndTimestamp.get(2));
        when(stateRepository.findStateByDeliveryAndDescription(eq(6), eq("accepted"))).thenReturn(listOfStatesByDescriptionAcceptedAndTimestamp.get(3));

        listOfCompletedDeliveries = new ArrayList<>();

        Delivery del1= new Delivery(1,2,"completed","919292112","DETI","Bairro de Santiago");
        Delivery del2= new Delivery(2,4,"completed","919292941","Staples Aveiro","Bairro do Liceu");
        Delivery del3= new Delivery(3,4,"completed","949292921","ProudPapers","Avenida Doutor Lourenço Peixinho ");
        Delivery del4= new Delivery(3,4,"completed","949292921","ProudPapers","Avenida Doutor Lourenço Peixinho ");

        del1.setScore(5);
        del2.setScore(3);
        del3.setScore(3);
        del4.setScore(3);

        listOfCompletedDeliveries = Arrays.asList(del1, del2, del3);

        when(deliveryRepository.findDeliveriesByState(any())).thenReturn(listOfCompletedDeliveries);

        List<Rider> listOfAllRiders = new ArrayList<>();
        PasswordEncryption enc = new PasswordEncryption();
        r1= new Rider("hugo","ferreira","hugo@email.com", enc.encrypt("12345"), "930921312","car");
        r2= new Rider("clara","sousa","csousa@email.com", enc.encrypt("anvd"), "911551312","car");
        r3= new Rider("pedro","teixeira","teixeira@email.com", enc.encrypt("12345"), "930921312","car");
        r1.setId(0);
        r2.setId(1);
        r3.setId(2);
        listOfAllRiders = Arrays.asList(r1, r2, r3);

        del1.setRider(r1.getId());
        del2.setRider(r2.getId());
        del3.setRider(r3.getId());
        del4.setRider(r2.getId());

        when(riderRepository.findAll()).thenReturn(listOfAllRiders);
        when(riderRepository.findRiderById(r1.getId())).thenReturn(r1);
        when(riderRepository.findRiderById(r2.getId())).thenReturn(r2);
        when(riderRepository.findRiderById(r3.getId())).thenReturn(r3);

        when(deliveryRepository.findDeliveryById(2)).thenReturn(del1);
        when(deliveryRepository.findDeliveryById(3)).thenReturn(del2);
        when(deliveryRepository.findDeliveryById(4)).thenReturn(del3);
        when(deliveryRepository.findDeliveryById(6)).thenReturn(del4);

        when(geocoder.getDistanceInKmsBetweenTwoAddressesWithExternalApi("DETI","Bairro de Santiago")).thenReturn(4.2);
        when(geocoder.getDistanceInKmsBetweenTwoAddressesWithExternalApi("Bairro do Liceu", "Glicínias Plaza")).thenReturn(2.8);
        when(geocoder.getDistanceInKmsBetweenTwoAddressesWithExternalApi("ProudPapers","Avenida Doutor Lourenço Peixinho ")).thenReturn(3.0);
        when(geocoder.getDistanceInKmsBetweenTwoAddressesWithExternalApi("DETI", "DECA")).thenReturn(1.2);
        when(geocoder.getDistanceInKmsBetweenTwoAddressesWithExternalApi("Staples Aveiro","Bairro do Liceu")).thenReturn(7.8);
        when(geocoder.getDistanceInKmsBetweenTwoAddressesWithExternalApi("EasyDelivers", "ProudPapers")).thenReturn(5.0);
    }

    @Test
    public void kmCoveredLast24HoursTest() {
        double kmsCovered = service.sumOfKmCoveredInLast24Hours();
        assertEquals(18.0, kmsCovered);

        verify(stateRepository, times(1)).findStatesByDescriptionAndTimestampBetween(eq("completed"), any(Timestamp.class), any(Timestamp.class));
        verify(deliveryRepository, times(4)).findDeliveryById(any(Integer.class));
        verify(geocoder, times(4)).getDistanceInKmsBetweenTwoAddressesWithExternalApi(any(String.class), any(String.class));
    }

    @Test
    public void nrDeliveriesLast24HoursTest() {
        int numberDeliveriesMadeForLast24Hours = service.numberDeliveriesMadeForLast24Hours();
        assertEquals(4, numberDeliveriesMadeForLast24Hours);
        verify(stateRepository, times(1))
                .findStatesByDescriptionAndTimestampBetween(eq("completed"), any(Timestamp.class), any(Timestamp.class));
    }

    @Test
    public void averageTimeLast24HoursTest() {
        double averageTime = service.averageTimeDeliveries();
        assertEquals(TimeUnit.MILLISECONDS.toMinutes(6000), averageTime);
        verify(stateRepository, times(1))
                .findStatesByDescriptionAndTimestampBetween(eq("completed"), any(Timestamp.class), any(Timestamp.class));
    }

    @Test
    public void averageScoreTest() {
        double averageScore = service.averageRidersScore();
        assertEquals(Double.valueOf(11.0/3.0),averageScore);
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
    public void getAllCompletedDeliveriesTest(){
        List<Delivery> allCompletedDeliveriesList = service.getAllCompletedDeliveries();
        assertThat(allCompletedDeliveriesList.size()).isEqualTo(3);
        verify(deliveryRepository, times(1)).findDeliveriesByState("completed");
    }

    @Test
    public void kmCoveredInTheLast13DaysTest() {
        List<Double> kmsCoveredList = service.sumOfKmCoveredForLast13Days();
        assertEquals(13, kmsCoveredList.size());
        int counter = 0;
        for (double d: kmsCoveredList) {
            assertEquals(18.0, kmsCoveredList.get(counter));
            counter += 1;
        }


        verify(stateRepository, times(13)).findStatesByDescriptionAndTimestampBetween(eq("completed"), any(Timestamp.class), any(Timestamp.class));
        verify(deliveryRepository, times(4*13)).findDeliveryById(any(Integer.class));
        verify(geocoder, times(4*13)).getDistanceInKmsBetweenTwoAddressesWithExternalApi(any(String.class), any(String.class));
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
        List<Double> averageDeliveryTimesIn13days = service.averageDeliveryTimeForLast13Days();
        assertThat(averageDeliveryTimesIn13days.size()).isEqualTo(13);
        verify(stateRepository, times(13))
                .findStatesByDescriptionAndTimestampBetween(eq("completed"), any(), any());
    }

    @Test
    public void getAllRidersNamesWhenPassDeliveriesTest(){
        Map<Integer,String> allRidersNames = service.allRidersNamesByDeliveries(listOfCompletedDeliveries);
        assertThat(allRidersNames.values().contains(r1.getFirstname() + " " + r1.getLastname()));
        assertThat(allRidersNames.values().contains(r2.getFirstname() + " " + r2.getLastname()));
        assertThat(allRidersNames.values().contains(r3.getFirstname() + " " + r3.getLastname()));
    }

    // PERSONALS



    @Test
    public void whenValidId_ReturnKmCoveredTest() {
        int id = 0;
        double kmsCovered = service.personalSumOfKmCoveredInLast24Hours(id);
        assertEquals(4.2, kmsCovered);

        verify(stateRepository, times(1)).findStatesByDescriptionAndTimestampBetween(eq("completed"), any(Timestamp.class), any(Timestamp.class));
        verify(deliveryRepository, times(4)).findDeliveryById(any(Integer.class));
        verify(geocoder, times(1)).getDistanceInKmsBetweenTwoAddressesWithExternalApi(any(String.class), any(String.class));
    }

    @Test
    public void whenValidId_ReturnKmsCoveredInTheLast13DaysTest() {
        int id = 0;
        List<Double> kmsCoveredList = service.personalsumOfKmCoveredForLast13Days(id);
        int counter = 0;
        for (double d: kmsCoveredList) {
            assertEquals(4.2, kmsCoveredList.get(counter));
            counter += 1;
        }

        verify(stateRepository, times(13)).findStatesByDescriptionAndTimestampBetween(eq("completed"), any(Timestamp.class), any(Timestamp.class));
        verify(deliveryRepository, times(4*13)).findDeliveryById(any(Integer.class));
        verify(geocoder, times(1*13)).getDistanceInKmsBetweenTwoAddressesWithExternalApi(any(String.class), any(String.class));
    }

    @Test
    public void whenValidId_ReturnNrDeliveriesTest() {
        int id = 0;
        int numberDeliveriesMadeForLast24Hours = service.personalDeliveriesMadeForLast24Hours(id);
        assertEquals(1, numberDeliveriesMadeForLast24Hours);
        verify(stateRepository, times(1))
                .findStatesByDescriptionAndTimestampBetween(eq("completed"), any(Timestamp.class), any(Timestamp.class));
    }

    @Test
    public void whenValidId_ReturnNumberDeliveriesInTheLast13DaysTest(){
        int id = 0;
        List<Integer> alldeliveriesin13days = service.personalDeliveriesMadeForLast13Days(id);
        assertThat(alldeliveriesin13days.size()).isEqualTo(13);
        verify(stateRepository, times(13))
                .findStatesByDescriptionAndTimestampBetween(eq("completed"), any(Timestamp.class), any(Timestamp.class));
    }

    @Test
    public void whenValidId_ReturnAverageTimeTest() {
        int id = 1;
        double averageTime = service.personalAverageTimeDeliveries(id);
        assertEquals(TimeUnit.MILLISECONDS.toMinutes(5000), averageTime);
        verify(stateRepository, times(1))
                .findStatesByDescriptionAndTimestampBetween(eq("completed"), any(Timestamp.class), any(Timestamp.class));
    }

    @Test
    public void whenValidId_ReturnAverageDeliveryTimesInTheLast13DaysTest(){
        int id = 1;
        List<Double> averageDeliveryTimesIn13days = service.personalAverageDeliveryTimeForLast13Days(id);
        assertThat(averageDeliveryTimesIn13days.size()).isEqualTo(13);
        verify(stateRepository, times(13))
                .findStatesByDescriptionAndTimestampBetween(eq("completed"), any(), any());
    }

    @AfterEach
    public void tearDown() {
        listOfStatesByDescriptionCompletedAndTimestamp.clear();
    }
}
