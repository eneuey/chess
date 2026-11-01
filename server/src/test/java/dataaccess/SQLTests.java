package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class SQLTests {
    private final UserDAO userDAO = new SQLUserDAO();
    private final AuthDAO authDAO = new SQLAuthDAO();
    private final GameDAO gameDAO = new SQLGameDAO();

    @BeforeAll
    public static void connectToDatabase() {
        try {
            DatabaseManager.createDatabase();
            DatabaseManager.createTables();
        } catch (DataAccessException e) {
            System.out.println(e.getCause().toString());
        }
    }

    @BeforeEach
    public void prepareDatabase() {
        assertDoesNotThrow(userDAO::clear);
        assertDoesNotThrow(authDAO::clear);
        assertDoesNotThrow(gameDAO::clear);
        AuthData dummyAuthData = new AuthData("secret", "teija");
        UserData dummyUserData = new UserData("teija", "password", "email");
        GameData dummyGameData = new GameData(1, null, null, "myGame", new ChessGame());
        assertDoesNotThrow(() -> userDAO.createUser(dummyUserData));
        assertDoesNotThrow(() -> authDAO.createAuth(dummyAuthData));
        assertDoesNotThrow(() -> gameDAO.createGame(dummyGameData));
    }

    @Test
    public void clearUser() {
        assertDoesNotThrow(userDAO::clear);
        assertDoesNotThrow(() -> assertNull(userDAO.getUser("teija")));
    }
    @Test
    public void createUser() {
        UserData dummyUserData = new UserData("ethan", "password", "email");
        assertDoesNotThrow(() -> userDAO.createUser(dummyUserData));
        assertDoesNotThrow(() -> assertNotNull(userDAO.getUser("ethan")));
    }
    @Test
    public void getUser() {
        assertDoesNotThrow(() -> assertEquals("teija", userDAO.getUser("teija").username()));
    }
    @Test
    public void getUserDoesNotExist() {
        assertDoesNotThrow(() -> assertNull(userDAO.getUser("ethan")));
    }

    @Test
    public void clearAuth() {
        assertDoesNotThrow(authDAO::clear);
        assertDoesNotThrow(() -> assertNull(authDAO.getAuth("secret")));
    }
    @Test
    public void deleteAuth() {
        assertDoesNotThrow(() -> authDAO.deleteAuth("secret"));
        assertDoesNotThrow(() -> assertNull(authDAO.getAuth("secret")));
    }
    @Test
    public void createAuth() {
        AuthData dummyAuthData = new AuthData("secret", "ethan");
        assertDoesNotThrow(() -> authDAO.createAuth(dummyAuthData));
        assertDoesNotThrow(() -> assertNotNull(authDAO.getAuth("secret")));
    }
    @Test
    public void getAuth() {
        assertDoesNotThrow(() -> assertEquals("teija", authDAO.getAuth("secret").username()));
    }
    @Test
    public void getAuthDoesNotExist() {
        assertDoesNotThrow(() -> assertNull(authDAO.getAuth("fakeSecret")));
    }

    @Test
    public void clearGame() {
        assertDoesNotThrow(gameDAO::clear);
        assertDoesNotThrow(() -> assertNull(gameDAO.getGame(1)));
    }
    @Test
    public void createGame() {
        GameData dummyGameData = new GameData(2, null, null, "myGame2", new ChessGame());
        assertDoesNotThrow(() -> gameDAO.createGame(dummyGameData));
        assertDoesNotThrow(() -> assertNotNull(gameDAO.getGame(2)));
    }
    @Test
    public void listGames() {
        assertDoesNotThrow(() -> assertNotNull(gameDAO.listGames()));
    }
    @Test
    public void updateGame() {
        GameData updatedGameData = new GameData(1, "teija", null, "myGame", new ChessGame());
        assertDoesNotThrow(() -> gameDAO.updateGame(1, updatedGameData));
        assertDoesNotThrow(() -> assertEquals("teija", gameDAO.getGame(1).whiteUsername()));
    }
    @Test
    public void getGame() {
        assertDoesNotThrow(() -> assertEquals("myGame", gameDAO.getGame(1).gameName()));
    }
    @Test
    public void getGameDoesNotExist() {
        assertDoesNotThrow(() -> assertNull(gameDAO.getGame(2)));
    }

}
