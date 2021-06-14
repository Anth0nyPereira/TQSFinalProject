/*
package ua.deti.tqs.easydeliversadmin.controller;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
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

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EasyDeliversController.class)
public class AdminController_WithMockServiceIT {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private EasyDeliversService service;

    @Test
    public void whenPostValidAdmin_thenVerifyAdmin( ) throws Exception, EasyDeliversService.AdminNotFoundException {
        Admin alex = new Admin("alex", "conte", "alex@deti.com", "pass", "CEO", "You can fall only if you fly");

        when(service.getAdminByEmail("alex@deti.com")).thenReturn(alex);
        when(service.getAdminByEmail("wrong_email")).thenReturn(null);

        mvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\": \"alex@deti.com\", \"password\": \"pass\"}"))
                .andExpect(status().is3xxRedirection())
                .andExpect(content().string(containsString(alex.toString())));


        mvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(EntityUtils.toString(new UrlEncodedFormEntity(Arrays.asList(
                        new BasicNameValuePair("email", alex.getEmail()),
                        new BasicNameValuePair("password", alex.getPassword())
                )))))
                .andExpect(status().is3xxRedirection())
                .andExpect(content().string(containsString(alex.getFirst_name())));

        mvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(alex))).andExpect(status().is3xxRedirection()).andDo(print())
                .andExpect(jsonPath("first_name", is("alex")));


        //mvc.perform(post("/login").contentType(MediaType.ALL_VALUE).content(alex.toString())).andExpect(content().string(org.hamcrest.Matchers.containsString(alex.getFirst_name())));
        //.andExpect(jsonPath("first_name", is(alex.getFirst_name())));

        //verify(service, times(1)).getAdminByEmail(Mockito.any());

    }
}
 */
