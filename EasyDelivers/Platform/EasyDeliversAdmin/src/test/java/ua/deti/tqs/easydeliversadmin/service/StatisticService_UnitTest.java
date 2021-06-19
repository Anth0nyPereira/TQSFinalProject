package ua.deti.tqs.easydeliversadmin.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.deti.tqs.easydeliversadmin.entities.State;
import ua.deti.tqs.easydeliversadmin.repository.DeliveryRepository;
import ua.deti.tqs.easydeliversadmin.repository.StateRepository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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
        Mockito.when(stateRepository.findStatesByDescriptionAndTimestampBetween("completed", Mockito.any(), Mockito.any())).thenReturn(listOfStatesByDescriptionCompletedAndTimestamp);

        List<State> listOfStatesByDescriptionAcceptedAndTimestamp = new ArrayList<>();
        listOfStatesByDescriptionCompletedAndTimestamp.add(new State("accepted", 2, new Timestamp(actualLong - 7000)));
        listOfStatesByDescriptionCompletedAndTimestamp.add(new State("accepted", 3, new Timestamp(actualLong - 205000)));
        listOfStatesByDescriptionCompletedAndTimestamp.add(new State("accepted", 4, new Timestamp(actualLong - 9010000)));
        listOfStatesByDescriptionCompletedAndTimestamp.add(new State("accepted", 6, new Timestamp(actualLong - 86304000)));
        Mockito.when(stateRepository.findStatesByDescriptionAndTimestampBetween("accepted", Mockito.any(), Mockito.any())).thenReturn(listOfStatesByDescriptionAcceptedAndTimestamp);
    }

    @Test
    public void kmCoveredTest() {

    }

    @Test
    public void nrDeliveriesTest() {
        int numberDeliveriesMadeForLast24Hours = service.numberDeliveriesMadeForLast24Hours();
        assertEquals(4, numberDeliveriesMadeForLast24Hours);
        verify(stateRepository, times(1))
                .findStatesByDescriptionAndTimestampBetween("completed", Mockito.any(), Mockito.any());
    }

    @Test
    public void averageTimeTest() {
        double averageTime = service.averageTimeDeliveries();
        assertEquals(6000, averageTime);
        verify(stateRepository, times(1))
                .findStatesByDescriptionAndTimestampBetween("accepted", Mockito.any(), Mockito.any());
        verify(stateRepository, times(1))
                .findStatesByDescriptionAndTimestampBetween("completed", Mockito.any(), Mockito.any());
    }

    @AfterEach
    public void tearDown() {

    }
}
