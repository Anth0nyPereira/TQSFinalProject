package tqs.proudpapers.client;

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
import tqs.proudpapers.entity.Client;
import tqs.proudpapers.repository.ClientRepository;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * @author wy
 * @date 2021/6/5 21:15
 */
@Testcontainers
@SpringBootTest
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ClientRepository_DockerContainers_Test {

    @Container
    public static MySQLContainer container = new MySQLContainer(DockerImageName.parse("mysql:5.7"))
            .withUsername("proudpapers")
            .withPassword("abcABC123!!!")
            .withDatabaseName("proudpapers");

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.password", container::getPassword);
        registry.add("spring.datasource.username", container::getUsername);
    }

    @Autowired
    private ClientRepository repository;

    private static Client alex;

    @BeforeAll
    static void setUp(){
        alex = new Client();
        alex.setEmail("alex@ua.pt");
        alex.setName("alex");
        alex.setPassword("alexS3cr3t");
        alex.setAddress("2222-222, aveiro");
        alex.setTelephone("1234567891011");
    }

    @Order(1)
    @Test
    public void saveALex_getAlexByEmail_thenReturnAlex(){
        alex = repository.save(alex);
        Client found = repository.getClientByEmail(alex.getEmail());
        assertEquals(alex, found);
    }

    @Test
    public void whenInvalidEmail_thenReturnNull() {
        Client client = repository.getClientByEmail("Does Not Exist");
        assertNull(client);
    }

    @Test
    public void whenAlexEmailAndPassword_thenReturnAlex() {
         Client found = repository.getClientByEmailAndPassword("alex@ua.pt", "alexS3cr3t");

        assertEquals("alex@ua.pt", found.getEmail());
        assertEquals("alexS3cr3t", found.getPassword());
    }

    @Test
    public void whenAlexEmailAndInvalidPassword_thenReturnNull() {

        Client found = repository.getClientByEmailAndPassword("alex@ua.pt", "abcd");

        assertNull(found);
    }

    @Test
    public void whenAlexId_thenReturnAlex() {

        Client found = repository.getClientById(alex.getId());

        assertEquals(alex, found);
    }

}
