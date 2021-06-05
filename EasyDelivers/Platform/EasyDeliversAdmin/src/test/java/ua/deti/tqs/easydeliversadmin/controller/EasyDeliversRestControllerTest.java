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
import ua.deti.tqs.easydeliversadmin.entities.Rider;
import ua.deti.tqs.easydeliversadmin.service.EasyDeliversService;
import ua.deti.tqs.easydeliversadmin.utils.JsonUtil;

import java.util.HashMap;
import java.util.Map;

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
        signUpRequest.put("name","hugo");
        signUpRequest.put("telephone","930921312");
        signUpRequest.put("transportation","car");

        rider = new Rider("hugo","hugo@email.com", "12345", "930921312","car");

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
                .andExpect(jsonPath("name").value("hugo"))
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
        when(service.createRider("hugo","hugo@email.com", "12345", "930921312","car"))
                .thenReturn(rider);

        mvc.perform(MockMvcRequestBuilders.post("/api/rider/account")
                .contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(signUpRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value("hugo"))
                .andExpect(jsonPath("email").value("hugo@email.com"))
                .andExpect(jsonPath("password").value("12345"))
                .andExpect(jsonPath("telephone").value("930921312"))
                .andExpect(jsonPath("transportation").value("car"));

        verify(service,times(1))
                .createRider("hugo","hugo@email.com", "12345", "930921312","car");
    }

    @Test
    @DisplayName("Tests an invalid sign up from a rider")
    void invalidSignUpTest() throws Exception {
        when(service.createRider("hugo","hugo@email.com", "12345", "930921312","car"))
                .thenReturn(null);

        mvc.perform(MockMvcRequestBuilders.post("/api/rider/account")
                .contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(signUpRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist());

        verify(service,times(1))
                .createRider("hugo","hugo@email.com", "12345", "930921312","car");
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



}