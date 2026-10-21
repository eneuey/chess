package server.result;

public class ResultUsernameAndAuth extends Result{
    private final String username;
    private final String authToken;
    public ResultUsernameAndAuth(String message, String username, String authToken) {
        super(message);
        this.username = username;
        this.authToken = authToken;
    }
}
