package dataAccess;

import request.CreateGameRequest;
import request.JoinGameRequest;
import response.CreateGameResponse;
import response.Game;
import response.ListGameResponse;


import java.util.List;
import java.util.Objects;


public class GameDAO implements GameInterface{
  static List<Game> gameDB;

  @Override
  public ListGameResponse getGameList(String username) throws DataAccessException {
    return new ListGameResponse(gameDB);
  }

  @Override
  public void joinGame(String username, JoinGameRequest information) throws DataAccessException {
    int i=0;
    for (Game game : gameDB) {
      if (Objects.equals(game.gameID(), information.gameID())) {
        if (Objects.equals(information.playerColor(), "BLACK") & game.blackUsername() == null) {
          Game game_wanted=new Game(game.gameID(), game.whiteUsername(), username, game.gameName());
          gameDB.remove(i);
          gameDB.add(i, game_wanted);
          return;
        } else if (Objects.equals(information.playerColor(), "WHITE") & game.whiteUsername() == null) {
          Game game_wanted=new Game(game.gameID(), username, game.blackUsername(), game.gameName());
          gameDB.remove(i);
          gameDB.add(i, game_wanted);
          return;
        } else {
          throw new DataAccessException("Error: already taken");
        }
      }
      i++;
      throw new DataAccessException("Error: Bad request");
    }
  }
  @Override
  public CreateGameResponse createGame(CreateGameRequest information){
    Integer gameID = gameDB.size()+1;
    Game game = new Game(gameID, null, null, information.gameName());
    gameDB.add(game);
    return new CreateGameResponse(gameID);
  }

  @Override
  public void delete() {
    gameDB.clear();
  }
}
