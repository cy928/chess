package serverMessages;

import webSocketMessages.serverMessages.ServerMessage;

public class LoadGameMessage extends ServerMessage {
    private String messageContent;

    public LoadGameMessage(String messageContent) {
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
