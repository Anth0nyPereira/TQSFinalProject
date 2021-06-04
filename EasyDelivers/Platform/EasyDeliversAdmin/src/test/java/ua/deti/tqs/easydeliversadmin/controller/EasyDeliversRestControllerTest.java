package ua.deti.tqs.easydeliversadmin.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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

    Rider rider;

    @BeforeEach
    void setUp() {
        loginRequest = new HashMap<>();
        loginRequest.put("email","hugo@email.com");
        loginRequest.put("password","12345");
        rider = new Rider("hugo","hugo@email.com", "12345", "930921312","car");
    }

    @AfterEach
    void tearDown() {
    }

    @Test
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
}