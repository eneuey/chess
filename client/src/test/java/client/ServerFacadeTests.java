import client.ServerFacade;
import client.exception.ResponseException;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import server.Server;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;
    private static int port;

    @BeforeAll
    public static void init() {
        server = new Server();
        port = server.run(0);
        facade = new ServerFacade("http://localhost:" + port);
        System.out.println("Started test HTTP server on " + port);
        clear();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
        clear();
    }

    @BeforeEach
    public void register() {
        assertDoesNotThrow(() -> facade.register("facadeTestUser", "1", "email"));
    }

    @AfterEach
    public void clearDatabase() {
        clear();
    }

    @Test
    public void registerSuccess() {
        assertDoesNotThrow(() -> facade.register("facadeTestUser2", "1", "email"));
    }

    @Test
    public void registerFailAlreadyTaken() {
        ResponseException exception = assertThrows(ResponseException.class,
                () -> facade.register("facadeTestUser", "1", "email"));
        assertEquals("Error: already taken", exception.getMessage());
    }

    @Test
    public void loginFailWrongPassword() {
        ResponseException exception = assertThrows(ResponseException.class,
                () -> facade.login("facadeTestUser", "wrongPassword"));
        assertEquals("Error: unauthorized", exception.getMessage());
    }

    @Test
    public void loginFailWrongUsername() {
        ResponseException exception = assertThrows(ResponseException.class,
                () -> facade.login("wrongUsername", "wrongPassword"));
        assertEquals("Error: unauthorized", exception.getMessage());
    }

    @Test
    public void loginSuccess() {
        assertDoesNotThrow(() -> facade.login("facadeTestUser", "1"));
    }

    @Test
    public void logoutSuccess() {
        assertDoesNotThrow(() -> facade.logout());
    }

    private static void clear() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + "/db"))
                .DELETE()
                .build();
        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println((response.statusCode() != 200)
                    ? "Could not clear database"
                    : "database cleared *evil malicious laugh*");
        } catch (Throwable ex) {
            System.out.println("Connection error");
        }
    }
}
