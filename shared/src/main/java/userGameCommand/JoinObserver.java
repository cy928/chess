package userGameCommand;

import webSocketMessages.userCommands.UserGameCommand;

public class JoinObserver extends UserGameCommand {
    Integer gameID;
    private String msgContent;
    public JoinObserver(String authToken, Integer gameID) {
        super(authToken);
        this.gameID = gameID;
        super.commandType = UserGameCommand.CommandType.JOIN_OBSERVER;
    }
}
