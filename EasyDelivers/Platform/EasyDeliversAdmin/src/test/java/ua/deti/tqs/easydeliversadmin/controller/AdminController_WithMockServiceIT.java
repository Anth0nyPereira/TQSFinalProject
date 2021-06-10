package ua.deti.tqs.easydeliversadmin.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ua.deti.tqs.easydeliversadmin.entities.Admin;
import ua.deti.tqs.easydeliversadmin.service.EasyDeliversService;
import ua.deti.tqs.easydeliversadmin.utils.JsonUtil;

import static org.hamcrest.CoreMatchers.is;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EasyDeliversController.class)
public class AdminController_WithMockServiceIT {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private EasyDeliversService service;

    @BeforeEach
    public void setUp() throws Exception {
    }

    @Test
    public void whenPostValidAdmin_thenVerifyAdmin( ) throws Exception, EasyDeliversService.AdminNotFoundException {
        Admin alex = new Admin("alex", "conte", "alex@deti.com", "pass", "CEO", "You can fall only if you fly");

        //given(service.save(Mockito.any())).willReturn(alex);
        when(service.getAdminByEmail("alex@deti.com")).thenReturn(alex);
        when(service.getAdminByEmail("wrong_email")).thenReturn(null);

        mvc.perform(post("/dashboard").contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(alex))).andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("alex")));

        verify(service, times(1)).getAdminByEmail(Mockito.any());

    }
}
