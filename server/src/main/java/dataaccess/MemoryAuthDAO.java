package dataaccess;

import model.AuthData;

import java.util.HashMap;
import java.util.Map;

public class MemoryAuthDAO implements AuthDAO {

    private Map<String, AuthData> authStorage;

    public MemoryAuthDAO() {
        authStorage = new HashMap<>();
    }

    public void clear() throws DataAccessException {
        authStorage.clear();
    }
    public void createAuth(AuthData authData) throws DataAccessException {
        String authToken = authData.authToken();
        authStorage.put(authToken, authData);
    }
    public AuthData getAuth(String authToken) throws DataAccessException {
        return authStorage.get(authToken);
    }
    public void deleteAuth(String authToken) throws DataAccessException {
        authStorage.remove(authToken);
    }
}
