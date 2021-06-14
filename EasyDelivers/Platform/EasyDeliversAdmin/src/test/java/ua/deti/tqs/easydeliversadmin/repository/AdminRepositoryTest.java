/*
package ua.deti.tqs.easydeliversadmin.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import ua.deti.tqs.easydeliversadmin.entities.Admin;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@Transactional
@AutoConfigureTestDatabase(replace = NONE)

public class AdminRepositoryTest {

    @Autowired
    private AdminRepository adminRepository;

    private Admin john;

    @Autowired
    private TestEntityManager em;

    @BeforeEach
    public void setUp() {
        john = new Admin("john", "aguiar", "jaguiar@gmail.com", "admin", "Senior Manager", "Hello World!");
        adminRepository.saveAndFlush(john); //ensure data is persisted at this point
    }

    @Test
    public void whenFindJohnByName_thenReturnJohnAdmin() {
        Admin found = adminRepository.findAdminByEmail(john.getEmail());
        assertThat(found).isNotNull().isEqualTo(john);
    }

    @Test
    public void whenInvalidAdminEmail_thenReturnNull() {
        Admin fromDb = adminRepository.findAdminByEmail("Does Not Exist");
        assertThat(fromDb).isNull();
    }

    @Test
    public void whenFindAdminByExistingEmail_thenReturnAdmin() {
        Admin fromDb = adminRepository.findAdminByEmail(john.getEmail());
        assertThat(fromDb).isNotNull();
        assertThat(fromDb.getFirst_name()).isEqualTo(john.getFirst_name());
    }

    @AfterEach
    public void tearDown() { // https://stackoverflow.com/questions/57235922/junit-how-to-test-method-which-deletes-entity-jpa
        final int id = em.persistAndGetId(john, Integer.class);
        adminRepository.deleteById(id);
        em.flush();
    }

}

 */
