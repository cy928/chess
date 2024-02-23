package service;

import request.AuthToken;

public class GameService {
    public boolean getGameList(AuthToken auth) {
        if (gameDAO.checkAuthToken(suth)) {
            gameList = gameDAO.getGameList(auth);
            return gameList;
        }
    }
}
