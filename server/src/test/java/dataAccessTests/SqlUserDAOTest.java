package dataAccessTests;
import dataAccessError.DataAccessException;
import dataAccess.SqlUserDAO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import request.LoginRequest;
import request.RegisterRequest;


class SqlUserDAOTest {
    SqlUserDAO userDAO = new SqlUserDAO();
    private final String username = "Phoebe";
    private final String password = "0928";
    private final String email = "cy928@byu.edu";
    @BeforeEach
    public void clear() throws DataAccessException {
        userDAO.delete();
    }
    @Test
    @Order(1)
    public void successCreateUser() throws DataAccessException {
        RegisterRequest registerInfo = new RegisterRequest(username, password, email);
        userDAO.createUser(registerInfo);
        LoginRequest info = new LoginRequest(username, password);
        Assertions.assertTrue(userDAO.checkCredential(info));
    }
    @Test
    @Order(2)
    public void failCreateUser() throws DataAccessException {
        RegisterRequest registerInfo = new RegisterRequest(username, password, email);
        userDAO.createUser(registerInfo);
        Assertions.assertThrows(DataAccessException.class, () -> {
          userDAO.createUser(registerInfo);
        }, "already exist");
    }
    @Test
    @Order(3)
    public void successCheckCredential() throws DataAccessException {
        RegisterRequest registerInfo = new RegisterRequest(username, password, email);
        userDAO.createUser(registerInfo);
        LoginRequest information = new LoginRequest(username, password);
        Assertions.assertTrue(userDAO.checkCredential(information));
    }
    @Test
    @Order(4)
    public void failCheckCredential() throws DataAccessException {
        LoginRequest information = new LoginRequest(username, password);
        Assertions.assertFalse(userDAO.checkCredential(information));
    }
    @Test
    @Order(5)
    public void successDelete() throws DataAccessException {
        userDAO.delete();
    }
}