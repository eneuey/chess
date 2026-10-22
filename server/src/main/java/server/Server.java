package server;

import io.javalin.*;

public class Server {

    private final Javalin javalin;
    private final Handler handler;

    public Server() {
        handler = new Handler();
        javalin = Javalin.create(config -> config.staticFiles.add("web"));
        javalin.delete("/db", handler::clear);
        javalin.post("/user", handler::register);
        javalin.post("/session", handler::login);
        javalin.delete("/session", handler::logout);
        javalin.get("/game", handler::listGames);
        javalin.post("/game", handler::createGame);
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
