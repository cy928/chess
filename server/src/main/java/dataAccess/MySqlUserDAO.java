package dataAccess;

import request.LoginRequest;
import request.RegisterRequest;

public class MySqlUserDAO implements UserDAO {
  @Override
  public void createUser(RegisterRequest information) throws DataAccessException {

  }

  @Override
  public boolean checkCredential(LoginRequest information) throws DataAccessException {
    return false;
  }

  @Override
  public void delete() {

  }
}
