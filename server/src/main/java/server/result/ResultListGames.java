package server.result;

import model.GameData;

import java.util.Collection;

public class ResultListGames extends Result {
    Collection<GameData> games;
    public ResultListGames(String message, Collection<GameData> games) {
        super(message);
        this.games = games;
    }
    public Collection<GameData> getGames() {
        return games;
    }
}
