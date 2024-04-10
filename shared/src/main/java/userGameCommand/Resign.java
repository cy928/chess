package userGameCommand;

import webSocketMessages.userCommands.UserGameCommand;

public class Resign extends UserGameCommand {
    public Integer gameID;
    public Resign(String authToken, Integer gameID) {
        super(authToken);
        this.gameID = gameID;
        super.commandType = CommandType.RESIGN;
    }
}