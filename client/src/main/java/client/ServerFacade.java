package client;

import client.exception.ResponseException;
import com.google.gson.Gson;
import result.Result;
import result.ResultUsernameAndAuth;
import ui.EscapeSequences;

import java.awt.desktop.SystemEventListener;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public class ServerFacade {
    private final String serverURL;
    private final HttpClient client;
    private final Gson gson;
    private String authToken;

    public ServerFacade(String serverURL) {
        this.serverURL = serverURL;
        client = HttpClient.newHttpClient();
        gson = new Gson();
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
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Throwable ex) {
            throw new ResponseException(ResponseException.Code.ServerError, EscapeSequences.generateErrorText("Error: Could not connect to server"));
        }
        errorHandler(response);
        authToken = "";
    }

    private String makePostRequest(String jsonBody, String path) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(serverURL + "/" + path))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + authToken)
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Throwable ex) {
            throw new ResponseException(ResponseException.Code.ServerError, EscapeSequences.generateErrorText("Error: Could not connect to server"));
        }
        errorHandler(response);
        return response.body();
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
