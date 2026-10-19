package server;

import com.google.gson.Gson;
import io.javalin.http.Context;
import server.result.*;
import service.*;

public class Handler {
    private final Service service;

    public Handler() {
        service = new Service();
    }
    public void clear(Context ctx) {
        ctx.contentType("application/json");
        Result clearResult = service.clear();
        ctx.result(serialize(clearResult));
    }

    private String serialize(Object input) {
        Gson serializer = new Gson();
        return serializer.toJson(input);
    }
}
