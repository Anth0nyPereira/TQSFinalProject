package ua.deti.tqs.easydeliversadmin.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.deti.tqs.easydeliversadmin.entities.Delivery;
import ua.deti.tqs.easydeliversadmin.entities.State;
import ua.deti.tqs.easydeliversadmin.entities.Store;
import ua.deti.tqs.easydeliversadmin.repository.DeliveryRepository;
import ua.deti.tqs.easydeliversadmin.repository.StateRepository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StatisticService_UnitTest {

    @Mock( lenient = true)
    private DeliveryRepository deliveryRepository;

    @Mock(lenient = true)
    private StateRepository stateRepository;

    @InjectMocks
    private EasyDeliversService service;

    @BeforeEach
    public void setUp() {
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

        listOfCompletedDeliveries = Arrays.asList(del1, del2, del3);

        when(deliveryRepository.findDeliveriesByState(any())).thenReturn(listOfCompletedDeliveries);
    }

    @Test
    public void kmCoveredTest() {

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
    }


    @AfterEach
    public void tearDown() {

    }
}
