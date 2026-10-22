package service;

import org.junit.jupiter.api.*;
import server.request.RegisterRequest;
import server.result.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ServiceTests {
    private final Service service = new Service();
    private Response registerResponse;

    @BeforeEach
    public void registerUser() {
        RegisterRequest registerRequest = new RegisterRequest("teija", "pswd", "trab2@byu.edu");
        registerResponse = service.register(registerRequest);
    }

    @AfterEach
    public void clear() {
        service.clear();
    }

    @Test
    public void registerSuccess() {
        ResultUsernameAndAuth result = (ResultUsernameAndAuth) registerResponse.result();
        assertEquals("teija", result.getUsername());
    }
}
