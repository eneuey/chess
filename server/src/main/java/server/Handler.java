package server;

import com.google.gson.Gson;
import dataaccess.AlreadyTakenException;
import io.javalin.http.Context;
import server.request.RegisterRequest;
import server.result.*;
import service.*;

public class Handler {
    private final Service service;

    public Handler() {
        service = new Service();
    }
    public void clear(Context ctx) {
        ctx.contentType("application/json");
        Response clearResponse = service.clear();
        ctx.status(clearResponse.status());
        ctx.result(serialize(clearResponse.result()));
    }
    public void register(Context ctx) {
        ctx.contentType("application/json");
        Gson gson = new Gson();
        RegisterRequest request = gson.fromJson(ctx.body(), RegisterRequest.class);
        Response registerResponse = service.register(request);
        ctx.status(registerResponse.status());
        ctx.result(serialize(registerResponse.result()));
    }

    private String serialize(Object input) {
        Gson gson = new Gson();
        return gson.toJson(input);
    }

}
