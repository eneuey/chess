package dataaccess;

import chess.ChessGame;
import model.GameData;
import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class SQLGameDAO implements GameDAO {
    public void clear() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement("TRUNCATE TABLE gameData")) {
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException("failed to clear", ex);
        }
    }
    public void createGame(GameData gameData) throws DataAccessException {
        int gameID = gameData.gameID();
        String whiteUsername = gameData.whiteUsername();
        String blackUsername = gameData.blackUsername();
        String gameName = gameData.gameName();
        String game = serialize(gameData.game());
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement
                     ("INSERT INTO gameData (gameID, whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?, ?)")) {
            preparedStatement.setInt(1, gameID);
            preparedStatement.setString(2, whiteUsername);
            preparedStatement.setString(3, blackUsername);
            preparedStatement.setString(4, gameName);
            preparedStatement.setString(5, game);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException("failed to create game", ex);
        }
    }
    public Collection<GameData> listGames() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM gameData")) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                Collection<GameData> gameList = new ArrayList<>();
                while(resultSet.next()) {
                    int gameID = resultSet.getInt(1);
                    String whiteUsername = resultSet.getString(2);
                    String blackUsername = resultSet.getString(3);
                    String gameName = resultSet.getString(4);
                    Gson gson = new Gson();
                    ChessGame game = gson.fromJson(resultSet.getString(5), ChessGame.class);
                    gameList.add(new GameData(gameID, whiteUsername, blackUsername, gameName, game));
                }
                return gameList;
            }
        } catch (SQLException ex) {
            throw new DataAccessException("failed to list games", ex);
        }
    }
    public void updateGame(int gameID, GameData updatedGameData) throws DataAccessException {
        String whiteUsername = updatedGameData.whiteUsername();
        String blackUsername = updatedGameData.blackUsername();
        String gameName = updatedGameData.gameName();
        String game = serialize(updatedGameData.game());
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement
                     ("UPDATE gameData SET whiteUsername = ?, blackUsername = ?, gameName = ?, game = ? WHERE gameID = ?")) {
            preparedStatement.setString(1, whiteUsername);
            preparedStatement.setString(2, blackUsername);
            preparedStatement.setString(3, gameName);
            preparedStatement.setString(4, game);
            preparedStatement.setInt(5, gameID);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException("failed to update game", ex);
        }
    }
    public GameData getGame(int gameID) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM gameData WHERE gameID = ?")) {
            preparedStatement.setInt(1, gameID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String whiteUsername = resultSet.getString(2);
                    String blackUsername = resultSet.getString(3);
                    String gameName = resultSet.getString(4);
                    Gson gson = new Gson();
                    ChessGame game = gson.fromJson(resultSet.getString(5), ChessGame.class);
                    return new GameData(gameID, whiteUsername, blackUsername, gameName, game);
                }
                else {
                    return null;
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("failed to get game", ex);
        }
    }

    private String serialize(Object input) {
        Gson gson = new Gson();
        return gson.toJson(input);
    }
}
