package ua.deti.tqs.easydeliversadmin.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import ua.deti.tqs.easydeliversadmin.entities.Delivery;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
class DeliveryRepositoryTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private DeliveryRepository deliveryRepository;

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

    Delivery del1;
    Delivery del2;
    Delivery del3;

    @BeforeEach
    void setUp() {
        del1= new Delivery(1,2,"awaiting_processing","919292112","DETI","Bairro de Santiago");
        del2= new Delivery(2,4,"awaiting_processing","919292941","Staples Aveiro","Bairro do Liceu");
        del3= new Delivery(3,4,"awaiting_processing","949292921","ProudPapers","Avenida Doutor Lourenço Peixinho ");
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("Given Deliveries awaiting_processing return status 200")
    void givenDeliveriesWhenGetDeliveriesStatus_thenReturn200(){
        deliveryRepository.save(del1);
        deliveryRepository.save(del2);
        deliveryRepository.save(del3);

        ResponseEntity<List<Delivery>> response = restTemplate
                .exchange("/api/rider/deliveries", HttpMethod.GET, null, new ParameterizedTypeReference<List<Delivery>>() {
                });
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).extracting(Delivery::getStore).containsExactly(1, 2, 3);
    }
}