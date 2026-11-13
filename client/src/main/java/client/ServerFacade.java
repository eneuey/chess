package client;

import client.exception.ResponseException;
import com.google.gson.Gson;
import model.GameDataResponse;
import result.*;
import ui.EscapeSequences;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ServerFacade {
    private final String serverURL;
    private final HttpClient client;
    private final Gson gson;
    private String authToken;
    private HashMap<Integer, Integer> currentGames;

    public ServerFacade(String serverURL) {
        this.serverURL = serverURL;
        client = HttpClient.newHttpClient();
        gson = new Gson();
        currentGames = new HashMap<>();
        authToken = "";
    }

    public void register(String username, String password, String email) throws ResponseException {
        String jsonRequestBody = gson.toJson(Map.of(
                "username", username,
                "password", password,
                "email", email));
        String jsonResponseBody = makePostRequest(jsonRequestBody, "user");
        authToken = deserializeJsonBody(jsonResponseBody, ResultUsernameAndAuth.class).getAuthToken();
    }

    public void login(String username, String password) throws ResponseException {
        String jsonRequestBody = gson.toJson(Map.of(
                "username", username,
                "password", password));
        String jsonResponseBody = makePostRequest(jsonRequestBody, "session");
        authToken = deserializeJsonBody(jsonResponseBody, ResultUsernameAndAuth.class).getAuthToken();
    }

    public void logout() throws ResponseException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(serverURL + "/session"))
                .header("Authorization", authToken)
                .DELETE()
                .build();
        HttpResponse<String> response = sendHTTPRequest(request);
        authToken = "";
    }

    public void createGame(String gameName) throws ResponseException {
        String jsonRequestBody = gson.toJson(Map.of("gameName", gameName));
        makePostRequest(jsonRequestBody, "game");
    }

    public String listGames() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(serverURL + "/game"))
                .header("Authorization", authToken)
                .GET()
                .build();
        HttpResponse<String> response = sendHTTPRequest(request);
        ArrayList<GameDataResponse> games = (ArrayList<GameDataResponse>)deserializeJsonBody(response.body(), ResultListGames.class).getGames();
        if(!games.isEmpty()) {
            return createGameList(games);
        }
        return "No current games";
    }

    private String createGameList(ArrayList<GameDataResponse> games) {
        StringBuilder gameList = new StringBuilder();
        for(int i = 0; i < games.size(); i++) {
            GameDataResponse currentGame = games.get(i);
            int gameListOrder = i + 1;
            int gameID = currentGame.gameID();
            String whiteUsername = (currentGame.whiteUsername() != null) ? currentGame.whiteUsername() : "Not assigned";
            String blackUsername = (currentGame.blackUsername() != null) ? currentGame.blackUsername() : "Not assigned";
            String gameName = currentGame.gameName();
            gameList.append(String.format("%d) %s\n" +
                    "   White Player: %s\n" +
                    "   Black Player: %s",
                    gameListOrder, gameName, whiteUsername, blackUsername));
            gameList.append(gameListOrder < games.size() ? "\n" : "");
            currentGames.put(gameListOrder, gameID);
        }
        return gameList.toString();
    }

    private String makePostRequest(String jsonBody, String path) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(serverURL + "/" + path))
                .header("Content-Type", "application/json")
                .header("Authorization", authToken)
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();
        HttpResponse<String> response = sendHTTPRequest(request);
        return response.body();
    }

    private HttpResponse<String> sendHTTPRequest(HttpRequest request) throws ResponseException {
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Throwable ex) {
            throw new ResponseException(ResponseException.Code.ServerError, EscapeSequences.generateErrorText("Error: Could not connect to server"));
        }
        errorHandler(response);
        return response;
    }

    private void errorHandler(HttpResponse<String> response) throws ResponseException {
        int statusCode = response.statusCode();
        String jsonBody = response.body();
        if(statusCode != 200) {
            throw new ResponseException(ResponseException.Code.ServerError,
                    EscapeSequences.generateErrorText(deserializeErrorMessage(jsonBody)));
        }
    }

    private <T> T deserializeJsonBody(String jsonBody, Class<T> responseType) {
        return gson.fromJson(jsonBody, responseType);
    }

    private String deserializeErrorMessage(String jsonBody) {
        Result result = gson.fromJson(jsonBody, Result.class);
        return result.getMessage();
    }
}
