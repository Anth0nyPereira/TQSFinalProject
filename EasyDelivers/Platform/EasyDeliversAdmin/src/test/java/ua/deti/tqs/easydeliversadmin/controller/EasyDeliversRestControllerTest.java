package ua.deti.tqs.easydeliversadmin.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ua.deti.tqs.easydeliversadmin.entities.Delivery;
import ua.deti.tqs.easydeliversadmin.entities.Rider;
import ua.deti.tqs.easydeliversadmin.service.EasyDeliversService;
import ua.deti.tqs.easydeliversadmin.utils.JsonUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class EasyDeliversRestControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    EasyDeliversService service;

    Map<String, Object> loginRequest;
    Map<String, Object> signUpRequest;
    Map<String, Object> newDeliveryRequest;

    Rider rider;

    @BeforeEach
    void setUp() {
        loginRequest = new HashMap<>();
        loginRequest.put("email","hugo@email.com");
        loginRequest.put("password","12345");

        signUpRequest = new HashMap<>();
        signUpRequest.put("email","hugo@email.com");
        signUpRequest.put("password","12345");
        signUpRequest.put("firstname","hugo");
        signUpRequest.put("lastname","ferreira");
        signUpRequest.put("telephone","930921312");
        signUpRequest.put("transportation","car");

        rider = new Rider("hugo","ferreira","hugo@email.com", "12345", "930921312","car");

        newDeliveryRequest = new HashMap<>();
        newDeliveryRequest.put("store",1);
        newDeliveryRequest.put("client_telephone","9393920");
        newDeliveryRequest.put("start","DETI Aveiro");
        newDeliveryRequest.put("destination","Bairro de Santiago Aveiro");

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("Tests a successful login with a rider")
    void successfulLoginTest() throws Exception {
        when(service.authenticateRider("hugo@email.com","12345"))
                .thenReturn(true);
        when(service.getRider("hugo@email.com")).thenReturn(rider);

        mvc.perform(MockMvcRequestBuilders.post("/api/rider/login")
                .contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("firstname").value("hugo"))
                .andExpect(jsonPath("lastname").value("ferreira"))
                .andExpect(jsonPath("email").value("hugo@email.com"))
                .andExpect(jsonPath("password").value("12345"))
                .andExpect(jsonPath("telephone").value("930921312"))
                .andExpect(jsonPath("transportation").value("car"));

        verify(service,times(1))
                .authenticateRider("hugo@email.com","12345");

        verify(service,times(1))
                .getRider("hugo@email.com");
    }

    @Test
    @DisplayName("Tests an invalid login with a rider")
    void invalidLoginTest() throws Exception {
        when(service.authenticateRider("hugo@email.com","12345"))
                .thenReturn(false);

        mvc.perform(MockMvcRequestBuilders.post("/api/rider/login")
                .contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist());

        verify(service,times(1))
                .authenticateRider("hugo@email.com","12345");

        verify(service,times(0))
                .getRider("hugo@email.com");
    }

    @Test
    @DisplayName("Tests a successful sign up from a rider")
    void successfulSignUpTest() throws Exception {
        when(service.createRider("hugo","ferreira","hugo@email.com", "12345", "930921312","car"))
                .thenReturn(rider);

        mvc.perform(MockMvcRequestBuilders.post("/api/rider/account")
                .contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(signUpRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("firstname").value("hugo"))
                .andExpect(jsonPath("lastname").value("ferreira"))
                .andExpect(jsonPath("email").value("hugo@email.com"))
                .andExpect(jsonPath("password").value("12345"))
                .andExpect(jsonPath("telephone").value("930921312"))
                .andExpect(jsonPath("transportation").value("car"));

        verify(service,times(1))
                .createRider("hugo","ferreira","hugo@email.com", "12345", "930921312","car");
    }

    @Test
    @DisplayName("Tests an invalid sign up from a rider")
    void invalidSignUpTest() throws Exception {
        when(service.createRider("hugo","ferreira","hugo@email.com", "12345", "930921312","car"))
                .thenReturn(null);

        mvc.perform(MockMvcRequestBuilders.post("/api/rider/account")
                .contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(signUpRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist());

        verify(service,times(1))
                .createRider("hugo","ferreira","hugo@email.com", "12345", "930921312","car");
    }

    @Test
    @DisplayName("Tests receiving a new successful delivery")
    void successfulReceiveNewDeliveryTest() throws Exception {
        when(service.createDelivery(1, "9393920", "DETI Aveiro", "Bairro de Santiago Aveiro"))
                .thenReturn("Delivery accepted");

        mvc.perform(MockMvcRequestBuilders.post("/api/delivery")
                .contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(newDeliveryRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Delivery accepted"));

        verify(service,times(1))
                .createDelivery(1, "9393920", "DETI Aveiro", "Bairro de Santiago Aveiro");
    }

    @Test
    @DisplayName("Tests receiving a new invalid delivery")
    void invalidReceiveNewDeliveryTest() throws Exception {
        when(service.createDelivery(1, "9393920", "DETI Aveiro", "Bairro de Santiago Aveiro"))
                .thenReturn("Delivery refused");

        mvc.perform(MockMvcRequestBuilders.post("/api/delivery")
                .contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(newDeliveryRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Delivery refused"));

        verify(service,times(1))
                .createDelivery(1, "9393920", "DETI Aveiro", "Bairro de Santiago Aveiro");
    }

    @Test
    @DisplayName("Tests request all available Deliveries")
    void getAllAvailableDeliveriesTest() throws Exception{
        Delivery del1= new Delivery(1,2,"awaiting_processing","919292112","DETI","Bairro de Santiago");
        Delivery del2= new Delivery(2,4,"awaiting_processing","919292941","Staples Aveiro","Bairro do Liceu");
        Delivery del3= new Delivery(3,4,"awaiting_processing","949292921","ProudPapers","Avenida Doutor Lourenço Peixinho ");
        List<Delivery> allDeliversAwaitingProcessing = Arrays.asList(del1, del2, del3);

        when(service.getAvailableDeliveries()).thenReturn(allDeliversAwaitingProcessing);

        mvc.perform(MockMvcRequestBuilders.get("/api/rider/deliveries").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].store", is(del1.getStore())))
                .andExpect(jsonPath("$[1].start", is(del2.getStart())))
                .andExpect(jsonPath("$[2].destination", is(del3.getDestination())));
        verify(service, times(1)).getAvailableDeliveries();
    }

    @Test
    @DisplayName("Test successful Assign delivery to rider")
    void successfulAssignDeliveryTest() throws Exception {
        when(service.assignRiderDeliver("1", "1")).thenReturn("Delivery Assigned");

        mvc.perform(MockMvcRequestBuilders.put("/api/rider/deliveries/1/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Delivery Assigned"));

        verify(service,times(1))
                .assignRiderDeliver("1","1");
    }

    @Test
    @DisplayName("Test invalid Assign delivery to rider")
    void invalidAssignDeliveryTest() throws Exception {
        when(service.assignRiderDeliver("1", "1")).thenReturn("error");

        mvc.perform(MockMvcRequestBuilders.put("/api/rider/deliveries/1/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("error"));

        verify(service,times(1))
                .assignRiderDeliver("1","1");
    }

    @Test
    @DisplayName("Test successful Update to  Delivery State")
    void successfulUpdateDeliveryTest() throws Exception {
        when(service.updateDeliveryStateByRider("1", "1", "done")).thenReturn("Delivery Updated");
        mvc.perform(MockMvcRequestBuilders.put("/api/rider/deliveries/update/1/1/done"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Delivery Updated"));

        verify(service,times(1))
                .updateDeliveryStateByRider("1", "1", "done");
    }

    @Test
    @DisplayName("Test invalid Update to  Delivery State")
    void invalidUpdateDeliveryTest() throws Exception {
        when(service.updateDeliveryStateByRider("1", "1", "done")).thenReturn("error");
        mvc.perform(MockMvcRequestBuilders.put("/api/rider/deliveries/update/1/1/done"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("error"));
        verify(service,times(1))
                .updateDeliveryStateByRider("1", "1", "done");
    }



}