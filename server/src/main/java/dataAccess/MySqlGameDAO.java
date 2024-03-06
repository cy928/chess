package dataAccess;

import request.CreateGameRequest;
import request.JoinGameRequest;
import result.CreateGameResult;
import result.ListGameResult;

public class MySqlGameDAO implements GameDAO {
  @Override
  public CreateGameResult createGame(String username, CreateGameRequest information) {
    return null;
  }

  @Override
  public void joinGame(String username, JoinGameRequest information) throws DataAccessException {

  }

  @Override
  public ListGameResult getGameList(String username) throws DataAccessException {
    return null;
  }

  @Override
  public void delete() throws DataAccessException {

  }
}
