package server.result;

import model.GameDataResponse;

import java.util.Collection;

public class ResultListGames extends Result {
    Collection<GameDataResponse> games;
    public ResultListGames(String message, Collection<GameDataResponse> games) {
        super(message);
        this.games = games;
    }
    public Collection<GameDataResponse> getGames() {
        return games;
    }
}
