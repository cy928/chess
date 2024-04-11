package dataAccess;

import chess.ChessGame;
import dataAccessError.DataAccessException;
import request.CreateGameRequest;
import request.JoinGameRequest;
import result.CreateGameResult;
import result.Game;
import result.ListGameResult;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class MemoryGameDAO{
    static List<Game> gameDB = new ArrayList<>();
    public CreateGameResult createGame(CreateGameRequest information){
        Integer gameID = gameDB.size()+1;
        Game game = new Game(gameID, null, null, information.gameName());
        gameDB.add(game);
        return new CreateGameResult(gameID);
    }

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
                    Game gameWanted=new Game(game.gameID(), username, game.blackUsername(), game.gameName());
                    gameDB.remove(gameDB.get(i));
                    gameDB.add(i, gameWanted);
                } else if (Objects.equals(playerColor, "BLACK") & game.blackUsername() == null) {
                    Game gameWanted = new Game(game.gameID(), game.whiteUsername(), username, game.gameName());
                    gameDB.remove(gameDB.get(i));
                    gameDB.add(i, gameWanted);
                } else {
                    throw new DataAccessException("Error: already taken");
                }
                return;
            }
        }
        throw new DataAccessException("Error: bad request");
    }

    public ListGameResult getGameList() {
        return new ListGameResult(gameDB);
    }

    public void delete() { gameDB.clear(); }
}
