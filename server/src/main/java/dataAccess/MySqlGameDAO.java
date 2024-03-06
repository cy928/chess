package dataAccess;

import request.CreateGameRequest;
import request.JoinGameRequest;
import result.CreateGameResult;
import result.ListGameResult;

import java.sql.SQLException;

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
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement=conn.prepareStatement("TRUNCATE auth")) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
