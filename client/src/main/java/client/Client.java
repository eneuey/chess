package client;

import chess.*;
import client.exception.ResponseException;
import ui.EscapeSequences;

import java.util.Arrays;
import java.util.Objects;
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
        while(!result.equals("bye")) {
            printPrompt();
            String line = scanner.nextLine();
            try {
                result = eval(line);
                System.out.print(generateResponseText(result));
            } catch (ResponseException ex) {
                System.out.print(generateErrorText(ex.getMessage()));
            } catch (Throwable ex) {
                System.out.print(generateErrorText(String.format("Internal Error %s",ex.getMessage())));
            }
        }
    }

    private String eval(String input) throws ResponseException {
        String[] tokens = input.toLowerCase().split(" ");
        String command = (tokens.length > 0) ? tokens[0] : "unknown";
        String[] parameters = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch (command) {
            case "register" -> register(parameters);
            case "login" -> login(parameters);
            case "logout" -> logout();
            case "listgames" -> listGames();
            case "creategame" -> createGame(parameters);
            case "playgame" -> playGame(parameters);
            case "observegame" -> observeGame(parameters);
            case "help" -> help();
            case "quit" -> quit();
            default -> throw new ResponseException(ResponseException.Code.ClientError,
                    "Unknown command. Type \"help\" for a list of commands");
        };
    }

    private String register(String... params) throws ResponseException {
        if(params.length == 3) {
            String username = params[0];
            String password = params[1];
            String email = params[2];
            server.register(username, password, email);
            state = State.LOGGEDIN;
            playerUsername = username;
            return String.format("Successfully registered user: %s", username);
        }
        throw new ResponseException(ResponseException.Code.ClientError, "Expected: register <username> <password> <email>");
    }

    private String login(String... params) throws ResponseException {
        if(params.length == 2) {
            String username = params[0];
            String password = params[1];
            server.login(username, password);
            state = State.LOGGEDIN;
            playerUsername = username;
            return String.format("Successfully logged in. Welcome %s.", username);
        }
        throw new ResponseException(ResponseException.Code.ClientError, "Expected: login <username> <password>");
    }

    private String logout() throws ResponseException {
        assertSignedIn();
        server.logout();
        state = State.LOGGEDOUT;
        return String.format("Successfully logged out. Come back soon %s.", playerUsername);
    }

    private String createGame(String... params) throws ResponseException {
        assertSignedIn();
        if(params.length == 1) {
            String gameName = params[0];
            server.createGame(gameName);
            return String.format("Successfully created game: %s", gameName);
        }
        throw new ResponseException(ResponseException.Code.ClientError, "Expected: createGame <game name>");
    }

    private String listGames() throws ResponseException {
        assertSignedIn();
        return server.listGames();
    }

    private String playGame(String... params) throws ResponseException {
        assertSignedIn();
        if(params.length == 2 && (Objects.equals(params[1], "white") || Objects.equals(params[1], "black"))) {
            Integer gameListOrder = getGameNumber(params[0]);
            String playerColor = params[1].toUpperCase();
            ChessGame.TeamColor perspective = Objects.equals(params[1], "white")
                    ? ChessGame.TeamColor.WHITE
                    : ChessGame.TeamColor.BLACK;
            server.playGame(gameListOrder, playerColor);
            ChessGame testGame = new ChessGame();
            renderer.render(testGame.getBoard(), perspective);
            Scanner scanner = new Scanner(System.in);
            String result = "";
            while(!result.equals("exit")) {
                System.out.print("Type \"exit\" to leave game: ");
                result = scanner.nextLine();
            }
            return "";
        }
        throw new ResponseException(ResponseException.Code.ClientError, "Expected: playGame <game number> <WHITE/BLACK>");
    }

    private String observeGame(String... params) {
        assertSignedIn();
        if(params.length == 1) {
            Integer gameListOrder = getGameNumber(params[0]);
            server.observeGame(gameListOrder);
            ChessGame testGame = new ChessGame();
            renderer.render(testGame.getBoard(), ChessGame.TeamColor.WHITE);
            Scanner scanner = new Scanner(System.in);
            String result = "";
            while(!result.equals("exit")) {
                System.out.print("Type \"exit\" to leave game: ");
                result = scanner.nextLine();
            }
            return "";
        }
        throw new ResponseException(ResponseException.Code.ClientError, "Expected: observeGame <game number>");
    }

     private String quit() {
        if(state == State.LOGGEDIN) {
            logout();
        }
        return "bye";
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
                - playGame <game number> <WHITE/BLACK>
                - observeGame <game number>"""
                + RESET_TEXT_COLOR;
    }

    private Integer getGameNumber(String input) throws ResponseException {
        try {
            return Integer.valueOf(input);
        } catch (Throwable ex) {
            throw new ResponseException(ResponseException.Code.ClientError, "Must enter a valid game number");
        }
    }

    private void printPrompt() {
        String currentState = (state == State.LOGGEDIN) ? "LOGGED_IN" : "LOGGED_OUT";
        System.out.print("\n" + "[" + currentState + "] " + ">>> ");
    }

    private void assertSignedIn() throws ResponseException {
        if(state == State.LOGGEDOUT) {
            throw new ResponseException(ResponseException.Code.ClientError, "You must sign in");
        }
    }

    private void test() {
        ChessGame testGame = new ChessGame();
        try {
            testGame.makeMove(new ChessMove(new ChessPosition(2,6), new ChessPosition(4, 6), null));
        } catch(Exception ex) {}
        renderer.render(testGame.getBoard(), ChessGame.TeamColor.WHITE);
    }
}
