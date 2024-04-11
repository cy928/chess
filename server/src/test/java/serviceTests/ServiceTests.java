package serviceTests;

import request.*;
import result.ListGameResult;
import result.CreateGameResult;
import dataAccessError.DataAccessException;
import org.junit.jupiter.api.*;
import service.ClearService;
import service.GameService;
import service.UserService;

import java.sql.SQLException;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ServiceTests {
  private final String username = "Phoebe";
  private final String password = "0928";
  private final String email = "cy928@byu.edu";

  static AuthToken authToken;
  static CreateGameResult gameID;
  UserService userService= new UserService();
  GameService gameService= new GameService();
  ClearService clearService= new ClearService();
  @Test
  @Order(1)
  public void successRegister() throws DataAccessException {
    RegisterRequest registerInfo = new RegisterRequest(username, password, email);
    var auth = userService.register(registerInfo);
    Assertions.assertNotNull(auth, "The authToken that registration returned should not be null");

  }
  @Test
  @Order(2)
  public void failRegister() {
    RegisterRequest registerInfo = new RegisterRequest(null, password, email);
    Assertions.assertThrows(DataAccessException.class, () -> {
      userService.register(registerInfo);
    }, "Register with a null name is a bad request");
  }
  @Order(3)
  @Test
  public void successLogin() throws DataAccessException {
    LoginRequest userInfo = new LoginRequest(username, password);
    authToken = userService.login(userInfo);
    Assertions.assertNotNull(authToken, "The authToken that login returned should not be null");
  }
  @Order(4)
  @Test
  public void failLogin() {
    String password = "20020928";
    LoginRequest userInfo = new LoginRequest(username, password);
    Assertions.assertThrows(DataAccessException.class, () -> {
      userService.login(userInfo);
    }, "Wrong password");
  }
  @Order(5)
  @Test
  public void successLogout() throws DataAccessException, SQLException {
    boolean result = userService.logout(authToken);
    Assertions.assertTrue(result, "Logout should return true");
  }

  @Order(6)
  @Test
  public void failLogout() {
    AuthToken auth = new AuthToken("Incorrect");
    Assertions.assertThrows(DataAccessException.class, () -> {
      userService.logout(auth);
    }, "Incorrect authToken should throw an error");
  }

  @Order(7)
  @Test
  public void successCreateGame() throws DataAccessException {
    LoginRequest userInfo = new LoginRequest(username, password);
    authToken = userService.login(userInfo);
    CreateGameRequest gameName = new CreateGameRequest("FirstGame");
    gameID = gameService.createGame(authToken, gameName);
    Assertions.assertSame(gameID.getClass(), CreateGameResult.class, "They should be the same type");

  }
  @Order(8)
  @Test
  public void failCreateGame() {
    CreateGameRequest gameName = new CreateGameRequest(null);
    Assertions.assertThrows(DataAccessException.class, () -> {
      gameService.createGame(authToken, gameName);
    }, "CreatGame with a null gameName should throw an error");
  }
  @Order(9)
  @Test
  public void successJoinGame() throws DataAccessException {
    JoinGameRequest info = new JoinGameRequest("WHITE", gameID.gameID());
    boolean result = gameService.joinGame(authToken, info);
    Assertions.assertTrue(result,"JoinGame should return True");
  }
  @Order(10)
  @Test
  public void failJoinGame() {
    JoinGameRequest info = new JoinGameRequest("WHITE", 0);
    Assertions.assertThrows(DataAccessException.class, () -> {
      gameService.joinGame(authToken,info);
    }, "0 is not a valid game ID");
  }
  @Order(11)
  @Test
  public void successListGame() throws DataAccessException {
    CreateGameRequest gameName1 = new CreateGameRequest("FirstGame");
    var ID1 = gameService.createGame(authToken,gameName1);
    CreateGameRequest gameName2 = new CreateGameRequest("SecondGame");
    var ID2 = gameService.createGame(authToken,gameName2);
    JoinGameRequest info1 = new JoinGameRequest("WHITE", ID1.gameID());
    gameService.joinGame(authToken, info1);
    JoinGameRequest info2 = new JoinGameRequest("WHITE", ID2.gameID());
    gameService.joinGame(authToken, info2);
    ListGameResult gameList = gameService.getGameList(authToken);
    Assertions.assertEquals(4, gameList.games().size());
  }
  @Order(12)
  @Test
  public void failListGame() throws DataAccessException, SQLException {
    userService.logout(authToken);
    Assertions.assertThrows(DataAccessException.class, () -> {
      gameService.getGameList(authToken);
    }, "Invalid authToken should not return gameList");
  }
  @Order(13)
  @Test
  public void successClearApplication() throws DataAccessException, SQLException {
    AuthToken authToken1 = userService.register(new RegisterRequest("Seb","0605","seb605gmail.com"));
    AuthToken authToken2 = userService.register(new RegisterRequest("Amy","0325","amy325@gmail.com"));
    authToken = userService.login(new LoginRequest(username, password));
    gameService.createGame(authToken, new CreateGameRequest("Testing1"));
    gameService.createGame(authToken1, new CreateGameRequest("Testing2"));
    gameService.createGame(authToken2, new CreateGameRequest("Testing3"));
    clearService.delete();
    Assertions.assertThrows(DataAccessException.class, () -> {
      userService.login(new LoginRequest("Seb","0605"));
    }, "No userdata in the database");
  }

}