package ua.deti.tqs.easydeliversadmin.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import ua.deti.tqs.easydeliversadmin.entities.Store;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
@SpringBootTest
class StoreRepositoryTest {

    @Autowired
    private StoreRepository storeRepository;

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


    Store store;
    @BeforeEach
    void setUp() {
        store = new Store("ProudPapers","www.proudpapers.com/api/update/");
    }
    @AfterEach
    void tearDown() {
        storeRepository.deleteAll();
    }

    @Test
    @DisplayName("Tests invalid Find Store By ID")
    void whenInvalidFindStoreByID_thenReturnNull(){
        Store fromDB = storeRepository.findStoreById(1);
        assertThat(fromDB).isNull();
    }

    @Test
    @DisplayName("Tests Valid Find Store By ID")
    void whenValidFindStoreByID_thenReturnStore(){
        storeRepository.save(store);
        Store fromDB = storeRepository.findStoreById(store.getId());
        assertThat(fromDB).isNotNull();
        assertEquals(fromDB.getName(),store.getName());
        assertEquals(fromDB.getAddress(),store.getAddress());

    }




}