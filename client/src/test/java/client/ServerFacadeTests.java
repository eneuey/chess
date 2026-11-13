package client;

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

    @Test
    public void createGameSuccess() {
        assertDoesNotThrow(() -> facade.createGame("facadeTestGame"));
    }

    @Test
    public void listGamesSuccess() {
        assertDoesNotThrow(() -> facade.createGame("facadeTestGame"));
        assertDoesNotThrow(() -> {
            String response = facade.listGames();
            String expected = String.format("1) facadeTestGame%n" +
                    "   White Player: Not assigned%n" +
                    "   Black Player: Not assigned");
            assertEquals(expected, response);
        });
    }

    @Test
    public void listGamesEmptyList() {
        assertDoesNotThrow(() -> {
            String response = facade.listGames();
            assertEquals("No current games", response);
        });
    }

    @Test
    public void playGameSuccess() {
        assertDoesNotThrow(() -> facade.createGame("facadeTestGame"));
        assertDoesNotThrow(() -> facade.listGames());
        assertDoesNotThrow(() -> facade.playGame(1, "WHITE"));
    }

    @Test
    public void playGameBadRequest() {
        assertDoesNotThrow(() -> facade.createGame("facadeTestGame"));
        assertDoesNotThrow(() -> facade.listGames());
        ResponseException ex = assertThrows(ResponseException.class,
                () -> facade.playGame(1, "badTeamColor"));
        assertEquals("Error: bad request", ex.getMessage());
    }

    @Test
    public void playGameDoesNotExist() {
        assertDoesNotThrow(() -> facade.createGame("facadeTestGame"));
        assertDoesNotThrow(() -> facade.listGames());
        ResponseException ex = assertThrows(ResponseException.class,
                () -> facade.playGame(8, "WHITE"));
        assertEquals("No game number 8 exists", ex.getMessage());
    }

    @Test
    public void playGameAlreadyTaken() {
        assertDoesNotThrow(() -> facade.createGame("facadeTestGame"));
        assertDoesNotThrow(() -> facade.listGames());
        assertDoesNotThrow(() -> facade.playGame(1, "WHITE"));
        ResponseException ex = assertThrows(ResponseException.class,
                () -> facade.playGame(1, "WHITE"));
        assertEquals("Error: already taken", ex.getMessage());
    }

    @Test
    public void observeGameSuccess() {
        assertDoesNotThrow(() -> facade.createGame("facadeTestGame"));
        assertDoesNotThrow(() -> facade.listGames());
        assertDoesNotThrow(() -> facade.observeGame(1));
    }

    @Test
    public void observeGameDoesNotExist() {
        assertDoesNotThrow(() -> facade.createGame("facadeTestGame"));
        assertDoesNotThrow(() -> facade.listGames());
        ResponseException ex = assertThrows(ResponseException.class,
                () -> facade.observeGame(8));
        assertEquals("No game number 8 exists", ex.getMessage());
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
