package service;

import dataaccess.*;
import model.*;
import server.request.*;
import server.result.*;

import java.util.UUID;

public class Service {
    private UserDAO userStorage;
    private GameDAO gameStorage;
    private AuthDAO authStorage;

    public Service() {
        userStorage = new MemoryUserDAO();
        gameStorage = new MemoryGameDAO();
        authStorage = new MemoryAuthDAO();
    }
    public Response clear() {
        try {
            userStorage.clear();
            gameStorage.clear();
            authStorage.clear();
            return new Response(200, new Result(null));
        } catch(DataAccessException e) {
            return new Response(500, new Result(e.getMessage()));
        }
    }
    public Response register(RegisterRequest request) {
        try {
            String username = request.username();
            if (username == null || request.email() == null || request.password() == null) {
                return new Response(400, new Result("Error: bad request"));
            }
            if(userStorage.getUser(username) != null) {
                return new Response(403, new Result("Error: already taken"));
            }
            UserData userData = new UserData(username,request.password(),request.email());
            userStorage.createUser(userData);
            String authToken = generateAuthToken();
            AuthData authData = new AuthData(authToken, request.username());
            authStorage.createAuth(authData);
            return new Response(200, new ResultUsernameAndAuth(null, username, authToken));
        } catch(DataAccessException e) {
            return new Response(500, new Result(e.getMessage()));
        }
    }
    public Response login(LoginRequest request) {
        try {
            String username = request.username();
            if (username == null || request.password() == null) {
                return new Response(400, new Result("Error: bad request"));
            }
            UserData userdata = userStorage.getUser(username);
            if(userdata == null) {
                return new Response(401, new Result("Error: unauthorized"));
            }
            if(!userdata.password().equals(request.password())) {
                return new Response(401, new Result("Error: unauthorized"));
            }
            String authToken = generateAuthToken();
            AuthData authData = new AuthData(authToken, username);
            authStorage.createAuth(authData);
            return new Response(200, new ResultUsernameAndAuth(null, username, authToken));
        } catch(DataAccessException e) {
            return new Response(500, new Result(e.getMessage()));
        }
    }
    public Response logout(LogoutRequest request) {
        try {
            String authToken = request.token();
            if(authStorage.getAuth(authToken) == null) {
                return new Response(401, new Result("Error: unauthorized"));
            }
            authStorage.deleteAuth(authToken);
            return new Response(200, new Result(null));
        } catch(DataAccessException e) {
            return new Response(500, new Result(e.getMessage()));
        }
    }

    private String generateAuthToken() {
        return UUID.randomUUID().toString();
    }
}
