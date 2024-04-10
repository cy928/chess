package userGameCommand;

import webSocketMessages.userCommands.UserGameCommand;

public class Leave extends UserGameCommand {
    public Integer gameID;
    public Leave(String authToken, Integer gameID) {
        super(authToken);
        this.gameID = gameID;
        super.commandType = CommandType.LEAVE;
    }
}
