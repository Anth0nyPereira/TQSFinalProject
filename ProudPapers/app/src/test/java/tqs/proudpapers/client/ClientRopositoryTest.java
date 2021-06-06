package tqs.proudpapers.client;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import tqs.proudpapers.entity.Client;
import tqs.proudpapers.repository.ClientRepository;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * @author wy
 * @date 2021/6/5 21:15
 */
@DataJpaTest
public class ClientRopositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ClientRepository repository;

    @Test
    public void whenFindAlexByEmail_thenReturnAlex() {
        Client alex = new Client();
        alex.setEmail("alex@ua.pt");
        alex.setName("alex");
        alex.setPassword("alexS3cr3t");
        alex.setAddress("2222-222, aveiro");
        alex.setTelephone("1234567891011");
        entityManager.persistAndFlush(alex); //ensure data is persisted at this point

        Client found = repository.getClientByEmail(alex.getEmail());
        assertEquals(alex, found);
    }

    @Test
    public void whenInvalidEmail_thenReturnNull() {
        Client client = repository.getClientByEmail("Does Not Exist");
        assertNull(client);
    }

    @Test
    public void givenAlexEmailAndPassword_thenReturnAlex() {

        Client alex = repository.getClientByEmailAndPassword("alex@ua.pt", "alexS3cr3t");

        assertEquals("alex@ua.pt", alex.getEmail());
        assertEquals("alexS3cr3t", alex.getPassword());
    }

    @Test
    public void givenAlexEmailAndInvalidPassword_thenReturnNull() {

        Client alex = repository.getClientByEmailAndPassword("alex@ua.pt", "abcd");

        assertNull(alex);
    }


}
