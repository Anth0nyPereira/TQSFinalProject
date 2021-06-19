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
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StatisticService_UnitTest {
    private List<State> listOfStatesByDescriptionCompletedAndTimestamp;
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
        when(geocoder.convertFromAddressToCoordinatesWithApi("Bairro de Santiago")).thenReturn("40.6278521,-8.6526136");
        when(geocoder.convertFromAddressToCoordinatesWithApi("Bairro do Liceu")).thenReturn("40.6345766,-8.6487619");
        when(geocoder.convertFromAddressToCoordinatesWithApi("Avenida Doutor Lourenço Peixinho")).thenReturn("40.6430859,-8.6486171");
        when(geocoder.convertFromAddressToCoordinatesWithApi("DETI")).thenReturn("40.6331731,-8.661682");
        when(geocoder.convertFromAddressToCoordinatesWithApi("Staples Aveiro")).thenReturn("40.6435735,-8.6076039");
        when(geocoder.convertFromAddressToCoordinatesWithApi("ProudPapers")).thenReturn("40.6409327,-8.6540674");

    }

    /*@Test
    public void kmCoveredTest() {
        // findStateByDescription (completed) andTimestamp (last 24h)
        // findDelivery by id -> findRider by id
        // api get (delivery.end) e api get (delivery.start)
        // conta(operacao) qualquer(??) delivery.end - delivery.start
        //double kmsCovered = service.sumOfKmCoveredInLast24Hours();

        //http://dev.virtualearth.net/REST/v1/Routes/{travelMode}?wayPoint.1={wayPoint1}&viaWaypoint.2={viaWaypoint2}&waypoint.3={waypoint3}&wayPoint.n={waypointN}&heading={heading}&optimize={optimize}&avoid={avoid}&distanceBeforeFirstTurn={distanceBeforeFirstTurn}&routeAttributes={routeAttributes}&timeType={timeType}&dateTime={dateTime}&maxSolutions={maxSolutions}&tolerances={tolerances}&distanceUnit={distanceUnit}&key={BingMapsKey}


        //√((x_2-x_1)²+(y_2-y_1)²)
        // 40.6278521,-8.6526136
        // 40.6331731,-8.661682
        //double distance_del1 = Math.sqrt(Math.pow(40.6331731 - 40.6278521) + Math.pow(-8.661682, -8.6526136));
        //assertThat();
        //verify(stateRepository, times(1))
         //       .findStatesByDescriptionAndTimestampBetween(eq("completed"), any(Timestamp.class), any(Timestamp.class));
    }        */

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

    @AfterEach
    public void tearDown() {
        listOfStatesByDescriptionCompletedAndTimestamp.clear();
    }
}
