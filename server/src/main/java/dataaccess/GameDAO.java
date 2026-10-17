package dataaccess;

import model.GameData;

import java.util.Collection;

public interface GameDAO {
    public void createGame(GameData gameData) throws DataAccessException;
    public Collection<GameData> listGames() throws DataAccessException;
    public void updateGame(String gameID, GameData updatedGameData) throws DataAccessException;
}
