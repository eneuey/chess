package client;

import client.exception.ResponseException;
import com.google.gson.Gson;
import result.Result;
import ui.EscapeSequences;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public class ServerFacade {
    private final String serverURL;
    private final HttpClient client;
    private final Gson gson;

    public ServerFacade(String serverURL) {
        this.serverURL = serverURL;
        client = HttpClient.newHttpClient();
        gson = new Gson();
    }

    public void register(String username, String password, String email) throws ResponseException {
        String jsonBody = gson.toJson(Map.of(
                "username", username,
                "password", password,
                "email", email));
        makePostRequest(jsonBody, "user");
    }

    public void login(String username, String password) {
        String jsonBody = gson.toJson(Map.of(
                "username", username,
                "password", password));
        makePostRequest(jsonBody, "session");
    }

    private void makePostRequest(String jsonBody, String path) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(serverURL + "/" + path))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Throwable ex) {
            throw new ResponseException(ResponseException.Code.ServerError, EscapeSequences.generateErrorText("Error: Could not connect to server"));
        }
        errorHandler(response);
    }

    private void errorHandler(HttpResponse<String> response) throws ResponseException {
        int statusCode = response.statusCode();
        String jsonBody = response.body();
        if(statusCode != 200) {
            throw new ResponseException(ResponseException.Code.ServerError,
                    EscapeSequences.generateErrorText(deserializeErrorMessage(jsonBody)));
        }
    }
    private String deserializeErrorMessage(String jsonBody) {
        Result result = gson.fromJson(jsonBody, Result.class);
        return result.getMessage();
    }
}
