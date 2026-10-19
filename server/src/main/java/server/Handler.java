package server;

import io.javalin.http.Context;
import service.*;

public class Handler {
    private final Service service;

    public Handler() {
        service = new Service();
    }
    public String clear(Context ctx) {
        //Result clearResult = new result();
        return "";
    }
}
