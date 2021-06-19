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
import ua.deti.tqs.easydeliversadmin.entities.Rider;
import java.awt.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest
class RiderRepositoryTest {

    @Autowired
    private RiderRepository riderRepository;

    // container {
    @Container
    public static MySQLContainer container = new MySQLContainer(DockerImageName.parse("mysql:5.7"))
            .withUsername("admin")
            .withPassword("admin")
            .withDatabaseName("test");

    // }

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.password", container::getPassword);
        registry.add("spring.datasource.username", container::getUsername);
    }

    Rider rider;

    @BeforeEach
    void setUp() {
        rider = new Rider("hugo","ferreira","hugo@email.com", "12345", "930921312","car");
    }

    @AfterEach
    void tearDown() {
        riderRepository.deleteAll();
    }

    @Test
    @DisplayName("Tests invalid Find Rider By Email")
    void whenInvalidFindRiderByEmail_thenReturnNull(){
        rider = new Rider("hugo","ferreira","hugo@email.com", "12345", "930921312","car");

        Rider fromDB = riderRepository.findRiderByEmail("hugo@email.com");

        assertThat(fromDB).isNull();
    }



    @Test
    @DisplayName("Tests a valid Find Rider By Email")
    void whenValidFindRiderByEmail_thenReturnRider(){
        riderRepository.save(rider);

        Rider fromDB = riderRepository.findRiderByEmail("hugo@email.com");
        assertThat(fromDB).isNotNull();
        assertEquals(fromDB.getEmail(),rider.getEmail());
        assertEquals(fromDB.getFirstname(),rider.getFirstname());
        assertEquals(fromDB.getLastname(),rider.getLastname());
        assertEquals(fromDB.getPassword(),rider.getPassword());
        assertEquals(fromDB.getTelephone(),rider.getTelephone());
        assertEquals(fromDB.getTransportation(),rider.getTransportation());
    }


}