package dataAccessTests;

import dataAccess.DataAccessException;
import dataAccess.SqlAuthDAO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import request.AuthToken;


import java.util.UUID;


class SqlAuthDAOTest {
    SqlAuthDAO authDAO = new SqlAuthDAO();
    @BeforeEach
    public void clear() throws DataAccessException {
        authDAO.delete();
    }
    @Test
    @Order(1)
    public void successCreateAuthToken() throws DataAccessException {
        AuthToken authToken = new AuthToken(UUID.randomUUID().toString());
        authDAO.createAuthToken(authToken, "Phoebe");
        Assertions.assertFalse(authDAO.checkAuthTokenInvalid(authToken));
    }
    @Test
    @Order(2)
    public void failCreateAuthToken() throws DataAccessException {
        AuthToken authToken = new AuthToken(UUID.randomUUID().toString());
        authDAO.createAuthToken(authToken,"Phoebe");
        Assertions.assertThrows(DataAccessException.class, () -> {
            authDAO.createAuthToken(authToken, "Phoebe");
        }, "Duplicate entry");
    }
    @Test
    @Order(3)
    public void successCheckAuthTokenInvalid() throws DataAccessException {
        AuthToken authToken = new AuthToken(UUID.randomUUID().toString());
        authDAO.createAuthToken(authToken,"Phoebe");
        Assertions.assertFalse(authDAO.checkAuthTokenInvalid(authToken));
    }
    @Test
    @Order(4)
    public void failCheckAuthTokenInvalid() throws DataAccessException {
        AuthToken authToken = new AuthToken(UUID.randomUUID().toString());
        Assertions.assertTrue(authDAO.checkAuthTokenInvalid(authToken));
    }
    @Test
    @Order(5)
    public void successDeleteAuthToken() throws DataAccessException {
        AuthToken authToken = new AuthToken(UUID.randomUUID().toString());
        authDAO.createAuthToken(authToken,"Phoebe");
        authDAO.deleteAuthToken(authToken);
        Assertions.assertTrue(authDAO.checkAuthTokenInvalid(authToken));
    }

    @Test
    @Order(6)
    public void failDeleteAuthToken() throws DataAccessException {
        AuthToken authToken = new AuthToken(UUID.randomUUID().toString());
        authDAO.deleteAuthToken(authToken);
        Assertions.assertTrue(authDAO.checkAuthTokenInvalid(authToken));
    }
    @Test
    @Order(7)
    public void successGetUserName() throws DataAccessException {
        AuthToken authToken = new AuthToken(UUID.randomUUID().toString());
        authDAO.createAuthToken(authToken,"Phoebe");
        Assertions.assertEquals(authDAO.getUsername(authToken),"Phoebe");
    }
    @Test
    @Order(8)
    public void failGetUserName() throws DataAccessException {
        AuthToken authToken = new AuthToken(UUID.randomUUID().toString());
        Assertions.assertNull(authDAO.getUsername(authToken),"Username should not be null");
    }
    @Test
    @Order(9)
    public void successDelete() throws DataAccessException {
        authDAO.delete();
    }
}