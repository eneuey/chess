package service;

import dataaccess.*;
import server.result.*;

public class Service {
    private UserDAO userStorage;
    private GameDAO gameStorage;
    private AuthDAO authStorage;

    public Service() {
        userStorage = new MemoryUserDAO();
        gameStorage = new MemoryGameDAO();
        authStorage = new MemoryAuthDAO();
    }
    public Result clear() {
        try {
            userStorage.clear();
            gameStorage.clear();
            authStorage.clear();
            return new Result(null);
        } catch (DataAccessException e) {
            return new Result(e.getMessage());
        }
    }
}
