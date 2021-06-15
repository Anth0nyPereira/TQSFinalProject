
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
import ua.deti.tqs.easydeliversadmin.utils.PasswordEncryption;

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
    public void whenPostValidAdmin_thenVerifyAdmin() throws Exception, EasyDeliversService.AdminNotFoundException {
        PasswordEncryption encryptor = new PasswordEncryption();
        String hashedPassword = encryptor.encrypt("pass");
        Admin alex = new Admin("alex", "conte", "alex@deti.com", hashedPassword, "CEO", "You can fall only if you fly");

        when(service.getAdminByEmail("alex@deti.com")).thenReturn(alex);
        when(service.getAdminByEmail("wrong_email")).thenReturn(null);

        mvc.perform(post("/login")
                .param("email", "alex@deti.com")
                .param("password", "pass"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/dashboard"));

        verify(service, times(1)).getAdminByEmail(Mockito.any());

    }
}

