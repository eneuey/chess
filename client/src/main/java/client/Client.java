package client;

import chess.*;
import client.exception.ResponseException;
import ui.EscapeSequences;

import java.util.Arrays;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Client {
    private final ServerFacade server;
    private final BoardRenderer renderer;
    private State state = State.LOGGEDOUT;
    private String playerUsername;

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
                System.out.print(generateErrorText(ex.getMessage()));
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
                case "login" -> login(parameters);
                case "logout" -> logout();
                case "listgames" -> listGames();
                case "creategame" -> createGame(parameters);
                case "help" -> help();
                case "quit" -> quit();
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
            server.register(username, password, email);
            state = State.LOGGEDIN;
            playerUsername = username;
            return generateResponseText(String.format("Successfully registered user: %s", username));
        }
        throw new ResponseException(ResponseException.Code.ClientError,
                generateErrorText("Expected: register <username> <password> <email>"));
    }

    private String login(String... params) throws ResponseException {
        if(params.length == 2) {
            String username = params[0];
            String password = params[1];
            server.login(username, password);
            state = State.LOGGEDIN;
            playerUsername = username;
            return generateResponseText(String.format("Successfully logged in. Welcome %s.", username));
        }
        throw new ResponseException(ResponseException.Code.ClientError,
                generateErrorText("Expected: login <username> <password>"));
    }

    private String logout() throws ResponseException {
        assertSignedIn();
        server.logout();
        state = State.LOGGEDOUT;
        return generateResponseText(String.format("Successfully logged out. Come back soon %s.", playerUsername));
    }

    private String createGame(String... params) throws ResponseException {
        assertSignedIn();
        if(params.length == 1) {
            String gameName = params[0];
            server.createGame(gameName);
            return generateResponseText(String.format("Successfully created game: %s", gameName));
        }
        throw new ResponseException(ResponseException.Code.ClientError,
                generateErrorText("Expected: createGame <game name>"));
    }

    private String listGames() throws ResponseException {
        assertSignedIn();
        String games = server.listGames();
        return "not implemented fool";
    }

     private String quit() {
        if(state == State.LOGGEDIN) {
            logout();
        }
        return generateResponseText("bye");
     }

    private String help() {
        if(state == State.LOGGEDOUT) {
            return SET_TEXT_COLOR_BLUE + """
                    - help
                    - quit
                    - login <username> <password>
                    - register <username> <password> <email>"""
                    + RESET_TEXT_COLOR;
        }
        return SET_TEXT_COLOR_BLUE + """
                - help
                - logout
                - createGame <game name>
                - listGames
                - playGame <game ID> <WHITE/BLACK>
                - observeGame <game ID>"""
                + RESET_TEXT_COLOR;
    }

    private void printPrompt() {
        String currentState = (state == State.LOGGEDIN) ? "LOGGED_IN" : "LOGGED_OUT";
        System.out.print("\n" + "[" + currentState + "] " + ">>> ");
    }

    private void assertSignedIn() throws ResponseException {
        if (state == State.LOGGEDOUT) {
            throw new ResponseException(ResponseException.Code.ClientError, generateErrorText("You must sign in"));
        }
    }

    private void test() {
        ChessGame testGame = new ChessGame();
        try {
            testGame.makeMove(new ChessMove(new ChessPosition(2,6), new ChessPosition(4, 6), null));
        } catch (Exception ex) {}
        renderer.render(testGame.getBoard(), ChessGame.TeamColor.WHITE);
    }
}
