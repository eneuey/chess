package server;

import io.javalin.*;
import io.javalin.http.Context;

public class Server {

    private final Javalin javalin;
    private final Handler handler;

    public Server() {
        handler = new Handler();
        javalin = Javalin.create(config -> config.staticFiles.add("web"));
        javalin.delete("/db", handler::clear);
        // Register your endpoints and exception handlers here.

    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
