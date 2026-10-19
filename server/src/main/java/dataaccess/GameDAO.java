package dataaccess;

import model.GameData;

import java.util.Collection;

public interface GameDAO {
    public void clear() throws DataAccessException;
    public void createGame(GameData gameData) throws DataAccessException;
    public Collection<GameData> listGames() throws DataAccessException;
    public void updateGame(int gameID, GameData updatedGameData) throws DataAccessException;
}
