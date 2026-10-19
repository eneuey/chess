package dataaccess;

import model.UserData;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MemoryUserDAO implements UserDAO {

    private Map<String, UserData> userStorage;

    public MemoryUserDAO() {
        userStorage = new HashMap<>();
    }

    public void clear() throws DataAccessException {
        userStorage.clear();
    }
    public void createUser(UserData userData) throws DataAccessException {
        String username = userData.username();
        userStorage.put(username, userData);
    }
    public UserData getUser(String username) throws DataAccessException {
        return userStorage.get(username);
    }
}
