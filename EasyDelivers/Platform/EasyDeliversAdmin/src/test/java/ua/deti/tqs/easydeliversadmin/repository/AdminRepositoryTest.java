package ua.deti.tqs.easydeliversadmin.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ua.deti.tqs.easydeliversadmin.entities.Admin;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class AdminRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AdminRepository adminRepository;

    @Test
    public void whenFindJohnByName_thenReturnJohnAdmin() {
        Admin john = new Admin("john", "aguiar", "jaguiar@gmail.com", "admin", "Senior Manager", "Hello World!");
        entityManager.persistAndFlush(john); //ensure data is persisted at this point

        Admin found = adminRepository.findAdminByEmail(john.getEmail());
        assertThat( found ).isEqualTo(john);
    }

    @Test
    public void whenInvalidAdminEmail_thenReturnNull() {
        Admin fromDb = adminRepository.findAdminByEmail("Does Not Exist");
        assertThat(fromDb).isNull();
    }

    @Test
    public void whenFindAdminByExistingEmail_thenReturnAdmin() {
        Admin john = new Admin("john", "aguiar", "jaguiar@gmail.com", "admin", "Senior Manager", "Hello World!");
        entityManager.persistAndFlush(john);

        Admin fromDb = adminRepository.findAdminByEmail(john.getEmail());
        assertThat(fromDb).isNotNull();
        assertThat(fromDb.getFirst_name()).isEqualTo(john.getFirst_name());
    }

    @Test
    public void whenInvalidId_thenReturnNull() {
        Admin fromDb = adminRepository.findAdminByEmail("jaguiar@gmail.com");
        assertThat(fromDb).isNull();
    }

}
