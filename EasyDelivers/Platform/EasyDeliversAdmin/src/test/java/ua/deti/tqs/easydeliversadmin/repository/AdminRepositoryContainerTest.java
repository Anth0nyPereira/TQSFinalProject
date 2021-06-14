package ua.deti.tqs.easydeliversadmin.repository;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import ua.deti.tqs.easydeliversadmin.entities.Admin;
import ua.deti.tqs.easydeliversadmin.entities.Rider;
import java.awt.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest
class AdminRepositoryContainerTest {

    @Autowired
    private AdminRepository repository;

    private Admin admin;

    // container {
    @Container
    public static MySQLContainer container = new MySQLContainer(DockerImageName.parse("mysql:5.7"))
            .withUsername("admin")
            .withPassword("admin")
            .withDatabaseName("easydeliversadmin");

    // }

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.password", container::getPassword);
        registry.add("spring.datasource.username", container::getUsername);
    }

    @BeforeEach
    void setUp() {
        admin = new Admin("anthony","pereira","anthonypereira@ua.pt", "admin", "Senior Manager","I fight for a future where everyone has the same rights");
        repository.save(admin);
    }

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("If an admin exists, checks if it is returned")
    void whenValidFindRiderByEmail_thenReturnRider(){
        Admin adminFromDatabase = repository.findAdminByEmail("anthonypereira@ua.pt");
        assertThat(adminFromDatabase).isNotNull().isEqualTo(admin);
        assertThat(adminFromDatabase.getEmail()).isEqualTo(admin.getEmail());
        assertThat(adminFromDatabase.getFirst_name()).isEqualTo(admin.getFirst_name());
        assertThat(adminFromDatabase.getLast_name()).isEqualTo(admin.getLast_name());
        assertThat(adminFromDatabase.getPassword()).isEqualTo(admin.getPassword());
        assertThat(adminFromDatabase.getPosition()).isEqualTo(admin.getPosition());
        assertThat(adminFromDatabase.getDescription()).isEqualTo(admin.getDescription());
    }


    @Test
    @DisplayName("If an admin does not exist, null should be returned instead")
    void whenInvalidFindRiderByEmail_thenReturnRider(){
        Admin inexistingAdmin = repository.findAdminByEmail("tonypereira@hotmail.com");

        assertThat(inexistingAdmin).isNull();
    }




}