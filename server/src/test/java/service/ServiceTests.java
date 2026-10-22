package service;

import chess.ChessGame;
import org.junit.jupiter.api.*;
import server.request.CreateGameRequest;
import server.request.JoinGameRequest;
import server.request.LoginRequest;
import server.request.RegisterRequest;
import server.result.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

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
    public void clearSuccess() {
        service.clear();
        Response response = service.login(new LoginRequest("teija", "pswd"));
        int status = response.status();
        assertEquals(401, status);
    }
    @Test
    public void registerSuccess() {
        ResultUsernameAndAuth result = (ResultUsernameAndAuth) registerResponse.result();
        assertEquals("teija", result.getUsername());
    }
    @Test
    public void registerBadRequest() {
        Response response = service.register(new RegisterRequest(null, null, null));
        int status = response.status();
        assertEquals(400, status);
    }
    @Test
    public void registerAlreadyTaken() {
        Response response = service.register(new RegisterRequest("teija", "hi", "hello"));
        int status = response.status();
        assertEquals(403, status);
    }

    @Test
    public void loginSuccess() {
        Response response = service.login(new LoginRequest("teija", "pswd"));
        int status = response.status();
        assertEquals(200, status);
    }
    @Test
    public void loginBadRequest() {
        Response response = service.login(new LoginRequest(null, null));
        int status = response.status();
        assertEquals(400, status);
    }
    @Test
    public void loginUnauthorized() {
        Response response = service.login(new LoginRequest("teija", "pswd1"));
        int status = response.status();
        assertEquals(401, status);
    }

    @Test
    public void logoutSuccess() {
        Response loginResponse = service.login(new LoginRequest("teija", "pswd"));
        ResultUsernameAndAuth result = (ResultUsernameAndAuth) registerResponse.result();
        Response logoutResponse = service.logout(result.getAuthToken());
        assertEquals(200, logoutResponse.status());
    }
    @Test
    public void logoutUnauthorized() {
        service.login(new LoginRequest("teija", "pswd"));
        Response logoutResponse = service.logout(":)");
        assertEquals(401, logoutResponse.status());
    }

    @Test
    public void createGameSuccess() {
        Response loginResponse = service.login(new LoginRequest("teija", "pswd"));
        ResultUsernameAndAuth result = (ResultUsernameAndAuth) registerResponse.result();
        Response createGamesResponse = service.createGame(new CreateGameRequest("bresler"), result.getAuthToken());
        assertEquals(200, createGamesResponse.status());
    }
    @Test
    public void createGameBadRequest() {
        Response loginResponse = service.login(new LoginRequest("teija", "pswd"));
        ResultUsernameAndAuth result = (ResultUsernameAndAuth) registerResponse.result();
        Response createGamesResponse = service.createGame(new CreateGameRequest(null), result.getAuthToken());
        assertEquals(400, createGamesResponse.status());
    }
    @Test
    public void createGameUnauthorized() {
        Response createGamesResponse = service.createGame(new CreateGameRequest("hello"), ":)");
        assertEquals(401, createGamesResponse.status());
    }

    @Test
    public void listGamesSuccess() {
        Response loginResponse = service.login(new LoginRequest("teija", "pswd"));
        ResultUsernameAndAuth result = (ResultUsernameAndAuth) registerResponse.result();
        Response listGamesResponse = service.listGames(result.getAuthToken());
        assertEquals(200, listGamesResponse.status());
    }
    @Test
    public void listGamesUnauthorized() {
        Response listGamesResponse = service.listGames(":)");
        assertEquals(401, listGamesResponse.status());
    }

    @Test
    public void joinGameSuccessWhite() {
        Response loginResponse = service.login(new LoginRequest("teija", "pswd"));
        ResultUsernameAndAuth registerResult = (ResultUsernameAndAuth) registerResponse.result();
        String authToken = registerResult.getAuthToken();
        Response createGamesResponse = service.createGame(new CreateGameRequest("bresler"), authToken);
        CreateGameResult createGameResult = (CreateGameResult) createGamesResponse.result();
        int gameID = createGameResult.getGameID();
        Response joinGameResponse = service.joinGame(new JoinGameRequest(ChessGame.TeamColor.WHITE, gameID), authToken);
        assertEquals(200, joinGameResponse.status());
    }
    @Test
    public void joinGameSuccessBlack() {
        Response loginResponse = service.login(new LoginRequest("teija", "pswd"));
        ResultUsernameAndAuth registerResult = (ResultUsernameAndAuth) registerResponse.result();
        String authToken = registerResult.getAuthToken();
        Response createGamesResponse = service.createGame(new CreateGameRequest("bresler"), authToken);
        CreateGameResult createGameResult = (CreateGameResult) createGamesResponse.result();
        int gameID = createGameResult.getGameID();
        Response joinGameResponse = service.joinGame(new JoinGameRequest(ChessGame.TeamColor.BLACK, gameID), authToken);
        assertEquals(200, joinGameResponse.status());
    }
    @Test
    public void joinGameBadRequest() {
        Response loginResponse = service.login(new LoginRequest("teija", "pswd"));
        ResultUsernameAndAuth registerResult = (ResultUsernameAndAuth) registerResponse.result();
        String authToken = registerResult.getAuthToken();
        Response createGamesResponse = service.createGame(new CreateGameRequest("bresler"), authToken);
        Response joinGameResponse = service.joinGame(new JoinGameRequest(ChessGame.TeamColor.BLACK, 0), authToken);
        assertEquals(400, joinGameResponse.status());
    }
    @Test
    public void joinGameUnauthorized() {
        Response loginResponse = service.login(new LoginRequest("teija", "pswd"));
        ResultUsernameAndAuth registerResult = (ResultUsernameAndAuth) registerResponse.result();
        String authToken = registerResult.getAuthToken();
        Response createGamesResponse = service.createGame(new CreateGameRequest("bresler"), authToken);
        CreateGameResult createGameResult = (CreateGameResult) createGamesResponse.result();
        int gameID = createGameResult.getGameID();
        Response joinGameResponse = service.joinGame(new JoinGameRequest(ChessGame.TeamColor.BLACK, gameID), ":)");
        assertEquals(401, joinGameResponse.status());
    }
    @Test
    public void joinGameWhiteAlreadyTaken() {
        Response loginResponse = service.login(new LoginRequest("teija", "pswd"));
        ResultUsernameAndAuth registerResult = (ResultUsernameAndAuth) registerResponse.result();
        String authToken = registerResult.getAuthToken();
        Response createGamesResponse = service.createGame(new CreateGameRequest("bresler"), authToken);
        CreateGameResult createGameResult = (CreateGameResult) createGamesResponse.result();
        int gameID = createGameResult.getGameID();
        service.joinGame(new JoinGameRequest(ChessGame.TeamColor.WHITE, gameID), authToken);

        RegisterRequest registerRequest = new RegisterRequest("ethan", "hi", "trab2@byu.edu");
        Response registerSecondUserResponse = service.register(registerRequest);
        ResultUsernameAndAuth registerSecondUserResult = (ResultUsernameAndAuth) registerSecondUserResponse.result();
        String authTokenSecondUser = registerSecondUserResult.getAuthToken();
        Response joinGameResponse = service.joinGame(new JoinGameRequest(ChessGame.TeamColor.WHITE, gameID), authTokenSecondUser);
        assertEquals(403, joinGameResponse.status());
    }
    @Test
    public void joinGameBlackAlreadyTaken() {
        Response loginResponse = service.login(new LoginRequest("teija", "pswd"));
        ResultUsernameAndAuth registerResult = (ResultUsernameAndAuth) registerResponse.result();
        String authToken = registerResult.getAuthToken();
        Response createGamesResponse = service.createGame(new CreateGameRequest("bresler"), authToken);
        CreateGameResult createGameResult = (CreateGameResult) createGamesResponse.result();
        int gameID = createGameResult.getGameID();
        service.joinGame(new JoinGameRequest(ChessGame.TeamColor.BLACK, gameID), authToken);

        RegisterRequest registerRequest = new RegisterRequest("ethan", "hi", "trab2@byu.edu");
        Response registerSecondUserResponse = service.register(registerRequest);
        ResultUsernameAndAuth registerSecondUserResult = (ResultUsernameAndAuth) registerSecondUserResponse.result();
        String authTokenSecondUser = registerSecondUserResult.getAuthToken();
        Response joinGameResponse = service.joinGame(new JoinGameRequest(ChessGame.TeamColor.BLACK, gameID), authTokenSecondUser);
        assertEquals(403, joinGameResponse.status());
    }
}
