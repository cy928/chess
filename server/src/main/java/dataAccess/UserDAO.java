package dataAccess;

import request.AuthToken;
import request.LoginRequest;
import request.RegisterRequest;

import java.util.*;

public class UserDAO implements UserInterface {
    static Map<String, List<String>> userDB;
    @Override
    public void createUser(RegisterRequest information) throws DataAccessException {
        if (!userDB.isEmpty() | userDB.get(information.username()) == null) {
            List<String> info_list = new ArrayList<>(Arrays.asList(information.password(), information.email()));
            userDB.put(information.username(), info_list);
        } else {
            throw new DataAccessException("Error: already taken");
        }
    }

    @Override
    public Boolean checkCredential(LoginRequest information) throws DataAccessException {
        if (userDB.get(information.username()) != null) {
            String password = userDB.get(information.username()).get(0);
            if (Objects.equals(password, information.password())) {
                return true;
            }
            return false;
        }
        return false;
    }
}
