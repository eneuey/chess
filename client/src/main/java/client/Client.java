package client;

import chess.*;

public class Client {
    private final ServerFacade server;
    private final BoardRenderer renderer;

    public Client(String serverURL) {
        server = new ServerFacade(serverURL);
        renderer = new BoardRenderer();
    }
    public void run() {//throws ResponseException{
        ChessGame testGame = new ChessGame();
        try {
            testGame.makeMove(new ChessMove(new ChessPosition(2,6), new ChessPosition(4, 6), null));
        } catch (Exception ex) {}
        renderer.render(testGame.getBoard(), ChessGame.TeamColor.WHITE);
    }
}
