package server.result;

public class CreateGameResult extends Result{
    private final String gameName;
    public CreateGameResult(String message, String gameName) {
        super(message);
        this.gameName = gameName;
    }
    public String getGameName() {
        return gameName;
    }
}
