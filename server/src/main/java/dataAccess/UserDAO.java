package dataAccess;

import request.LoginRequest;
import request.RegisterRequest;

public interface UserDAO {
    void createUser(RegisterRequest information) throws DataAccessException;
    boolean checkCredential(LoginRequest information) throws DataAccessException;
    void delete();
}
