package dataAccess;

import request.CreateGameRequest;
import request.JoinGameRequest;
import result.CreateGameResult;
import result.Game;
import result.ListGameResult;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class MemoryGameDAO implements GameDAO {
    static List<Game> gameDB = new ArrayList<>();
    @Override
    public CreateGameResult createGame(String username, CreateGameRequest information){
        Integer gameID = gameDB.size()+1;
        Game game = new Game(gameID, null, null, information.gameName());
        gameDB.add(game);
        return new CreateGameResult(gameID);
    }

    @Override
    public void joinGame(String username, JoinGameRequest information) throws DataAccessException {
        if (information.playerColor() == null) {
            for (Game game : gameDB) {
                if (Objects.equals(game.gameID(), information.gameID())) {
                   return;
                }
            }
            throw new DataAccessException("Error: bad request");
        }
        String playerColor = information.playerColor();
        for (int i = 0; i < gameDB.size(); i++) {
            Game game = gameDB.get(i);
            if (Objects.equals(game.gameID(), information.gameID())) {
                if (Objects.equals(playerColor, "WHITE") & game.whiteUsername() == null) {
                    Game game_wanted=new Game(game.gameID(), username, game.blackUsername(), game.gameName());
                    gameDB.remove(gameDB.get(i));
                    gameDB.add(i, game_wanted);
                } else if (Objects.equals(playerColor, "BLACK") & game.blackUsername() == null) {
                    Game game_wanted = new Game(game.gameID(), game.whiteUsername(), username, game.gameName());
                    gameDB.remove(gameDB.get(i));
                    gameDB.add(i, game_wanted);
                } else {
                    throw new DataAccessException("Error: already taken");
                }
                return;
            }
        }
        throw new DataAccessException("Error: bad request");
    }
    @Override
    public ListGameResult getGameList() {
        return new ListGameResult(gameDB);
    }
    @Override
    public void delete() { gameDB.clear(); }
}
