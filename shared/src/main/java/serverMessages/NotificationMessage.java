package serverMessages;

import webSocketMessages.serverMessages.ServerMessage;

public class NotificationMessage extends ServerMessage {
    private String messageContent;

    public NotificationMessage(String messageContent) {
        super(ServerMessageType.NOTIFICATION);
        this.messageContent = messageContent;
    }
    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }
    public String getMessage() {
        return messageContent;
    }
}
