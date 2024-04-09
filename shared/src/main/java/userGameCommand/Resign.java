package userGameCommand;

import webSocketMessages.userCommands.UserGameCommand;

public class Resign extends UserGameCommand {
    Integer gameID;
    public Resign(String authToken, Integer gameID) {
        super(authToken);
        this.gameID = gameID;
        super.commandType = CommandType.RESIGN;
    }
}