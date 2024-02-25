package dataAccess;

import request.CreateGameRequest;
import request.JoinGameRequest;
import response.CreateGameResponse;
import response.Game;
import response.ListGameResponse;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class GameDAO implements GameInterface{
    static List<Game> gameDB = new ArrayList<>();

    @Override
    public ListGameResponse getGameList(String username) throws DataAccessException {
        List<Game> gameList = new ArrayList<Game>();
        for (Game game : gameDB){
            if (game.blackUsername() != null){
                if(game.blackUsername().equals(username)){
                    gameList.add(game);
                }
            } else if(game.whiteUsername() != null){
                if (game.whiteUsername().equals(username)){
                    gameList.add(game);
                }
            }
        }
        if(gameList.isEmpty()){
            return new ListGameResponse(gameDB);
        }
        return new ListGameResponse(gameList);
    }

    @Override
    public void joinGame(String username, JoinGameRequest information) throws DataAccessException {
        if(information.playerColor() == null){
            for (Game game : gameDB) {
                if (Objects.equals(game.gameID(), information.gameID())) {
                   return;
                }
            }
            throw new DataAccessException( "Error: bad request");
        }
        String playerColor = information.playerColor();
        int i=0;
        for (Game game : gameDB) {
            if (Objects.equals(game.gameID(), information.gameID())) {
                if (Objects.equals(information.playerColor(), "WHITE") & game.whiteUsername() == null) {
                    Game game_wanted=new Game(game.gameID(), username, game.blackUsername(), game.gameName());
                    gameDB.remove(gameDB.get(i));
                    gameDB.add(i, game_wanted);
                    return;
                } else if (Objects.equals(information.playerColor(), "BLACK") & game.blackUsername() == null) {
                    Game game_wanted = new Game(game.gameID(), game.whiteUsername(), username, game.gameName());
                    gameDB.remove(gameDB.get(i));
                    gameDB.add(i, game_wanted);
                    return;
                } else {
                    throw new DataAccessException("Error: already taken");
                }
            }
            i++;
        }
        throw new DataAccessException("Error: Bad request");
    }
    @Override
    public CreateGameResponse createGame(String username, CreateGameRequest information){
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
