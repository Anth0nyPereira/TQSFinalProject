package com.example.riderapp.Services;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


class RiderServiceTest {
/***
    API_Connection api_connection;

    RiderService riderService;

    User user;

    @BeforeEach
    void setUp() {
        api_connection=mock(API_Connection.class);
        user = new User("user","name","rider@email.com", "pass1234", "930921312","car");
        riderService = new RiderService(api_connection);

    }

    @AfterEach
    void tearDown() {

    }

    @Test
    @DisplayName("Tests a successful login")
    void successfulLoginTest() throws Exception {
        when(api_connection.api_login("rider@email.com","pass1234")).thenReturn(user);
        User x = riderService.login("rider@email.com","pass1234");
        assertEquals("rider@email.com",x.getEmail());
        assertEquals("user",x.getFirstname());
        assertEquals("name",x.getLastname());
        assertEquals("pass1234",x.getPassword());
        assertEquals("930921312",x.getTelephone());
        assertEquals("car",x.getTransportation());
        verify(api_connection,times(1))
                .api_login("rider@email.com","pass1234");

    }

    @Test
    @DisplayName("Tests a invalid login")
    void invalidLoginTest() throws Exception {
        when(api_connection.api_login("rider@email.com","pass1234")).thenReturn(null);
        User x = riderService.login("rider@email.com","pass1234");
        assertNull(x);
        verify(api_connection,times(1))
                .api_login("rider@email.com","pass1234");

    }

    @Test
    @DisplayName("Tests a successful sign up")
    void successfulSignUpTest() throws Exception {
        when(api_connection.api_signUp("user","name","rider@email.com", "pass1234", "930921312","car")).thenReturn(user);
        User x = riderService.signUp("user","name","rider@email.com", "pass1234", "930921312","car");
        assertEquals("rider@email.com",x.getEmail());
        assertEquals("user",x.getFirstname());
        assertEquals("name",x.getLastname());
        assertEquals("pass1234",x.getPassword());
        assertEquals("930921312",x.getTelephone());
        assertEquals("car",x.getTransportation());
        verify(api_connection,times(1))
                .api_signUp("user","name","rider@email.com", "pass1234", "930921312","car");

    }

    @Test
    @DisplayName("Tests an invalid sign up")
    void invalidSignUpTest() throws Exception {
        when(api_connection.api_signUp("user","name","rider@email.com", "pass1234", "930921312","car")).thenReturn(null);
        User x = riderService.signUp("user","name","rider@email.com", "pass1234", "930921312","car");
        assertNull(x);
        verify(api_connection,times(1))
                .api_signUp("user","name","rider@email.com", "pass1234", "930921312","car");

    }
***/
}