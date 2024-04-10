package serverMessages;

import webSocketMessages.serverMessages.ServerMessage;

public class LoadGame extends ServerMessage {
    private String messageContent;

    public LoadGame(String messageContent) {
        super(ServerMessageType.ERROR);
        this.messageContent = messageContent;
    }
    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }
    public String getMessage() {
        return messageContent;
    }
}
