package server;

import com.google.gson.Gson;
import dataaccess.AlreadyTakenException;
import io.javalin.http.Context;
import server.request.*;
import server.result.*;
import service.*;

public class Handler {
    private final Service service;

    public Handler() {
        service = new Service();
    }
    public void clear(Context ctx) {
        ctx.contentType("application/json");
        sendResponse(service.clear(), ctx);
    }
    public void register(Context ctx) {
        ctx.contentType("application/json");
        Gson gson = new Gson();
        RegisterRequest request = gson.fromJson(ctx.body(), RegisterRequest.class);
        sendResponse(service.register(request), ctx);
    }
    public void login(Context ctx) {
        ctx.contentType("application/json");
        Gson gson = new Gson();
        LoginRequest request = gson.fromJson(ctx.body(), LoginRequest.class);
        sendResponse(service.login(request), ctx);
    }
    public void logout(Context ctx) {
        ctx.contentType("application/json");
        Gson gson = new Gson();
        String authToken = ctx.header("Authorization");
        sendResponse(service.logout(authToken), ctx);
    }
    public void listGames(Context ctx) {
        ctx.contentType("application/json");
        Gson gson = new Gson();
        String authToken = ctx.header("Authorization");
        sendResponse(service.listGames(authToken), ctx);
    }
    public void createGame(Context ctx) {
        ctx.contentType("application/json");
        Gson gson = new Gson();
        String authToken = ctx.header("Authorization");
        CreateGameRequest request = gson.fromJson(ctx.body(), CreateGameRequest.class);
        sendResponse(service.createGame(request, authToken), ctx);
    }
    public void joinGame(Context ctx) {
        ctx.contentType("application/json");
        Gson gson = new Gson();
        String authToken = ctx.header("Authorization");
        JoinGameRequest request = gson.fromJson(ctx.body(), JoinGameRequest.class);
        sendResponse(service.joinGame(request, authToken), ctx);
    }

    private void sendResponse(Response response, Context ctx) {
        ctx.status(response.status());
        ctx.result(serialize(response.result()));
    }
    private String serialize(Object input) {
        Gson gson = new Gson();
        return gson.toJson(input);
    }

}
