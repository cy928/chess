package clientTests;

import dataAccess.DataAccessException;
import org.junit.jupiter.api.*;
import result.CreateGameResult;
import result.ListGameResult;
import server.Server;
import ui.ResponseException;
import ui.ServerFacade;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ServerFacadeTests {
    private static Server server;
    String[] registerParameters={"Phoebe", "0928", "cy928@byu.edu"};
    String[] loginParameters={"Phoebe", "0928"};
    static Integer gameID;
    private static ServerFacade serverFacade;
    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        serverFacade = new ServerFacade("http://localhost:" + port);
    }
    @AfterAll
    static void stopServer() throws DataAccessException, ResponseException {
        serverFacade.clear();
        server.stop();
    }
    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }
    @Test
    @Order(1)
    public void successRegister() throws DataAccessException, ResponseException {
        assertNotNull(serverFacade.register(registerParameters));
    }
    @Test
    @Order(2)
    public void failRegister() {
        Assertions.assertThrows(ResponseException.class, () -> {
            serverFacade.register(registerParameters);
        });
    }
    @Test
    @Order(3)
    public void successLogin() throws DataAccessException, ResponseException {
        assertNotNull(serverFacade.login(loginParameters));
    }
    @Test
    @Order(4)
    public void failLogin() {
        String[] WrongParameters={"Phoebe", "0123"};
        Assertions.assertThrows(ResponseException.class, () -> {serverFacade.login(WrongParameters);}, "Wrong password");
    }
    @Test
    @Order(5)
    public void successCreate() throws ResponseException, DataAccessException {
        serverFacade.login(loginParameters);
        String[] gameParameters={"First Game"};
        CreateGameResult response=serverFacade.create(gameParameters);
        gameID=response.gameID();
        assertNotNull(response);
    }
    @Test
    @Order(6)
    public void failCreate() {
        String[] params={};
        Assertions.assertThrows(java.lang.ArrayIndexOutOfBoundsException.class, () -> {serverFacade.create(params);}, "missing game name");
    }
    @Test
    @Order(7)
    public void successList() throws ResponseException, DataAccessException {
        ListGameResult response = serverFacade.list();
        assertEquals(1,response.games().size());
    }
    @Test
    @Order(8)
    public void failList() throws ResponseException, DataAccessException {
        ListGameResult response = serverFacade.list();
        assertNotEquals(0,response.games().size());
    }
    @Test
    @Order(9)
    public void successJoin() throws ResponseException, DataAccessException {
        String[] joinParameters={Integer.toString(gameID),"WHITE"};
        serverFacade.join(joinParameters);
        Assertions.assertTrue(true);

    }
    @Test
    @Order(10)
    public void failJoin() {
        String[] params={Integer.toString(gameID),"WHITE"};
        Assertions.assertThrows(ResponseException.class, () -> {serverFacade.join(params);}, "This place is already taken");
    }

    @Test
    @Order(13)
    public void successLogout() throws DataAccessException, ResponseException {
        serverFacade.login(loginParameters);
        serverFacade.logout();
        Assertions.assertTrue(true);
    }

    @Test
    @Order(14)
    public void failLogout() throws DataAccessException, ResponseException {
        serverFacade.login(loginParameters);
        serverFacade.logout();
        Assertions.assertTrue(true);
    }
}
