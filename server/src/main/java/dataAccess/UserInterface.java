package dataAccess;

import request.LoginRequest;
import request.RegisterRequest;
import response.ListGameResponse;

public interface UserInterface {
    void createUser(RegisterRequest information) throws DataAccessException;
    boolean checkCredential(LoginRequest information) throws DataAccessException;
    void delete();
}
