package dataAccess;

import response.ListGameResponse;

public interface UserInterface {
    public void createUser(String username, String password, String email) throws DataAccessException;
    public void checkCredential(String username, String password) throws DataAccessException;

}
