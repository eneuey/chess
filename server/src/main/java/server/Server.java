package server;

import dataaccess.DataAccessException;
import dataaccess.sql.DatabaseManager;
import io.javalin.*;

public class Server {

    private final Javalin javalin;
    private final Handler handler;

    public Server() {
        try {
            DatabaseManager.createDatabase();
            DatabaseManager.createTables();
        } catch (DataAccessException e) {
            System.out.println(e.getCause().toString());
        }
        handler = new Handler();
        javalin = Javalin.create(config -> config.staticFiles.add("web"));
        javalin.delete("/db", handler::clear);
        javalin.post("/user", handler::register);
        javalin.post("/session", handler::login);
        javalin.delete("/session", handler::logout);
        javalin.get("/game", handler::listGames);
        javalin.post("/game", handler::createGame);
        javalin.put("/game", handler::joinGame);
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
