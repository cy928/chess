package userGameCommand;

import webSocketMessages.userCommands.UserGameCommand;

import javax.management.Notification;

public class joinPlayer extends UserGameCommand {
    private String msgContent;
    Integer gameID;

    public joinPlayer(String authToken) {
      super(authToken);
    }

    public Notification
}
