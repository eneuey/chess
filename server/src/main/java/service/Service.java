package service;

import chess.ChessGame;
import dataaccess.*;
import model.*;
import server.request.*;
import server.result.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

public class Service {
    private UserDAO userStorage;
    private GameDAO gameStorage;
    private AuthDAO authStorage;
    private int gameIDCounter = 0;

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
    public Response logout(String authToken) {
        try {
            if(authStorage.getAuth(authToken) == null) {
                return new Response(401, new Result("Error: unauthorized"));
            }
            authStorage.deleteAuth(authToken);
            return new Response(200, new Result(null));
        } catch(DataAccessException e) {
            return new Response(500, new Result(e.getMessage()));
        }
    }
    public Response listGames(String authToken) {
        try {
            if(authStorage.getAuth(authToken) == null) {
                return new Response(401, new Result("Error: unauthorized"));
            }
            Collection<GameDataResponse> gameList = ConvertGameDataToGameDataResponse(gameStorage.listGames());
            return new Response(200, new ResultListGames(null, gameList));
        } catch(DataAccessException e) {
            return new Response(500, new Result(e.getMessage()));
        }
    }
    public Response createGame(CreateGameRequest request, String authToken) {
        try {
            String gameName = request.gameName();
            if(gameName == null) {
                return new Response(400, new Result("Error: bad request"));
            }
            if(authStorage.getAuth(authToken) == null) {
                return new Response(401, new Result("Error: unauthorized"));
            }
            GameData gameData = new GameData(generateGameID(),"", "", gameName, new ChessGame());
            gameStorage.createGame(gameData);
            return new Response(200, new CreateGameResult(null, gameIDCounter));
        } catch(DataAccessException e) {
            return new Response(500, new Result(e.getMessage()));
        }
    }

    private Collection<GameDataResponse> ConvertGameDataToGameDataResponse(Collection<GameData> gameDataList) {
        Collection<GameDataResponse> output = new ArrayList<>();
        for(GameData gameData : gameDataList) {
            output.add(new GameDataResponse(gameData.gameID(), gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName()));
        }
        return output;
    }
    private String generateAuthToken() {
        return UUID.randomUUID().toString();
    }
    private int generateGameID() {
        gameIDCounter++;
        return gameIDCounter;
    }
}
