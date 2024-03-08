package dataAccessTests;

import dataAccess.DataAccessException;
import dataAccess.SqlGameDAO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import request.CreateGameRequest;
import request.JoinGameRequest;

class SqlGameDAOTest {
    SqlGameDAO gameDAO = new SqlGameDAO();
    @BeforeEach
    public void clear() throws DataAccessException{
       gameDAO.delete();
    }
    @Order(1)
    @Test
    public void successCreateGame() throws DataAccessException {
        gameDAO.createGame(new CreateGameRequest("testing1"));
        gameDAO.createGame(new CreateGameRequest("testing2"));
        Assertions.assertEquals(2, gameDAO.getGameList().games().size());
    }
    @Order(2)
    @Test
    public void successCreateGame2() throws DataAccessException {
        var id = gameDAO.createGame(new CreateGameRequest("testing"));
        JoinGameRequest info = new JoinGameRequest("WHITE", id.gameID());
        gameDAO.joinGame("Phoebe", info);
        Assertions.assertEquals("Phoebe", gameDAO.getGameList().games().get(0).whiteUsername());
    }
    @Test
    @Order(3)
    public void successJoinGame() throws DataAccessException {
        var game = gameDAO.createGame(new CreateGameRequest("testing1"));
        JoinGameRequest information = new JoinGameRequest("WHITE", game.gameID());
        gameDAO.joinGame("Phoebe", information);
        Assertions.assertEquals("Phoebe", gameDAO.getGameList().games().get(0).whiteUsername());
    }
    @Test
    @Order(4)
    public void failJoinGame() {
        JoinGameRequest information = new JoinGameRequest("WHITE", 0);
        Assertions.assertThrows(DataAccessException.class, () -> {
            gameDAO.joinGame("Phoebe", information);
        }, "gameID should never be 0");
    }
    @Test
    @Order(5)
    public void successGetGameList() throws DataAccessException {
        gameDAO.createGame(new CreateGameRequest("testing1"));
        gameDAO.createGame( new CreateGameRequest("testing2"));
        Assertions.assertEquals(2, gameDAO.getGameList().games().size());
    }

    @Test
    @Order(6)
    public void successGetGameList2() throws DataAccessException {
        Assertions.assertEquals(0, gameDAO.getGameList().games().size());
    }
    @Test
    @Order(7)
    public void successDelete() throws DataAccessException {
        gameDAO.delete();
    }
}