package dataAccess;

import dataAccessError.DataAccessException;
import request.LoginRequest;
import request.RegisterRequest;

import java.util.*;

public class MemoryUserDAO implements UserDAO {
    static Map<String, List<String>> userDB = new HashMap<>();
    @Override
    public void createUser(RegisterRequest information) throws DataAccessException {
        if (userDB.get(information.username()) != null ) {
            throw new DataAccessException("Error: already taken");
        } else {
            List<String> infoList = new ArrayList<>(Arrays.asList(information.password(), information.email()));
            userDB.put(information.username(), infoList);
        }
    }
    @Override
    public boolean checkCredential(LoginRequest information) {
        if (userDB.get(information.username()) != null) {
            String password = userDB.get(information.username()).get(0);
            return Objects.equals(password, information.password());
        }
        return false;
    }
    @Override
    public void delete() { userDB.clear(); }
}
