package server.result;

public class CreateGameResult extends Result{
    private final int gameID;
    public CreateGameResult(String message, int gameID) {
        super(message);
        this.gameID = gameID;
    }
    public int getGameID() {
        return gameID;
    }
}
