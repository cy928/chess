package dataAccess;

import request.LoginRequest;
import request.RegisterRequest;
import response.ListGameResponse;

public interface UserInterface {
    public void createUser(RegisterRequest information) throws DataAccessException;
    public Boolean checkCredential(LoginRequest information) throws DataAccessException;
    public void delete();
}
