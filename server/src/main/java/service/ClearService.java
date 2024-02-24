package service;

public class ClearService {
    public static boolean deleteall() {
        gameDAO.deleteall();
        return true;
    }
}
