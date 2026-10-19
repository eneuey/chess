package dataaccess;

import model.GameData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MemoryGameDAO implements GameDAO{

    private Map<Integer, GameData> gameStorage;

    public MemoryGameDAO() {
        gameStorage = new HashMap<>();
    }

    public void clear() throws DataAccessException {
        gameStorage.clear();
    }
    public void createGame(GameData gameData) throws DataAccessException {
        int gameID = gameData.gameID();
        gameStorage.put(gameID, gameData);
    }
    public Collection<GameData> listGames() throws DataAccessException {
        return new ArrayList<>(gameStorage.values());
    }
    public void updateGame(int gameID, GameData updatedGameData) throws DataAccessException {
        gameStorage.replace(gameID, updatedGameData);
    }
}
