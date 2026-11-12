package client;

import chess.*;
import client.exception.ResponseException;

import java.util.Arrays;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Client {
    private final ServerFacade server;
    private final BoardRenderer renderer;
    private State state = State.LOGGEDOUT;

    public Client(String serverURL) {
        server = new ServerFacade(serverURL);
        renderer = new BoardRenderer();
    }
    public void run() {//throws ResponseException{
        System.out.println(DISPLAY_WHITE_QUEEN + "Welcome to Ethan's Chess Dojo. Type \"help\" to get started." + DISPLAY_WHITE_QUEEN);

        Scanner scanner = new Scanner(System.in);
        String result = "";
        while(!result.equals(generateResponseText("bye"))) {
            printPrompt();
            String line = scanner.nextLine();
            try {
                result = eval(line);
                System.out.print(result);
            } catch (Throwable ex) {
                System.out.print(ex.getMessage());
            }
        }
    }

    private String eval(String input) {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String command = (tokens.length > 0) ? tokens[0] : "unknown";
            String[] parameters = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (command) {
                case "register" -> register(parameters);
                case "help" -> help();
                case "quit" -> generateResponseText("bye");
                default -> generateErrorText("Unknown command. Type \"help\" for a list of commands");
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }
    private String register(String... params) throws ResponseException {
        if(params.length == 3) {
            String username = params[0];
            String password = params[1];
            String email = params[2];
            state = State.LOGGEDIN;
            return generateResponseText(String.format("Successfully registered user: %s", username));
        }
        throw new ResponseException(ResponseException.Code.ClientError,
                generateErrorText("Expected: register <username> <password> <email>"));
    }
    private String help() {
        if(state == State.LOGGEDOUT) {
            return """
                    - help
                    - quit
                    - login
                    - register
                    """;
        }
        return """
                - help
                - logout
                - createGame
                - listGames
                - playGame
                - observeGame
                """;
    }

    private void printPrompt() {
        String currentState = (state == State.LOGGEDIN) ? "LOGGED_IN" : "LOGGED_OUT";
        System.out.print("\n" + "[" + currentState + "] " + ">>> ");
    }
    private String generateErrorText(String message) {
        return SET_TEXT_COLOR_RED + message + RESET_TEXT_COLOR;
    }
    private String generateResponseText(String message) {
        return SET_TEXT_COLOR_MAGENTA + message + RESET_TEXT_COLOR;
    }




    private void test() {
        ChessGame testGame = new ChessGame();
        try {
            testGame.makeMove(new ChessMove(new ChessPosition(2,6), new ChessPosition(4, 6), null));
        } catch (Exception ex) {}
        renderer.render(testGame.getBoard(), ChessGame.TeamColor.WHITE);
    }
}
