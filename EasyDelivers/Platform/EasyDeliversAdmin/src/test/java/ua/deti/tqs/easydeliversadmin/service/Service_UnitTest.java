package ua.deti.tqs.easydeliversadmin.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.deti.tqs.easydeliversadmin.entities.Admin;
import ua.deti.tqs.easydeliversadmin.repository.AdminRepository;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class Service_UnitTest {

    // lenient is required because we load some expectations in the setup
    // that are not used in all the tests. As an alternative, the expectations
    // could move into each test method and be trimmed: no need for lenient
    @Mock( lenient = true)
    private AdminRepository adminRepository;

    @InjectMocks
    private EasyDeliversService service;

    @BeforeEach
    public void setUp() {
        Admin alex = new Admin("alex", "conte", "alex@deti.com", "pass", "CEO", "You can fall only if you fly");

        Mockito.when(adminRepository.findAdminByEmail(alex.getEmail())).thenReturn(alex);
        Mockito.when(adminRepository.findAdminByEmail("wrong_email")).thenReturn(null);
    }

    @Test
    public void whenValidEmail_thenAdminShouldBeFound() throws EasyDeliversService.AdminNotFoundException {
        String email = "alex@deti.com";
        Admin found = service.getAdminByEmail(email);

        assertThat(found.getEmail()).isEqualTo(email);
    }

    @Test
    public void whenInValidEmail_thenAdminShouldNotBeFound() {
        assertThrows(EasyDeliversService.AdminNotFoundException.class, () -> service.getAdminByEmail("wrong_email"));

        verifyFindByEmailIsCalledOnce("wrong_email");
    }

    private void verifyFindByEmailIsCalledOnce(String name) {
        Mockito.verify(adminRepository, VerificationModeFactory.times(1)).findAdminByEmail(name);
    }
}
