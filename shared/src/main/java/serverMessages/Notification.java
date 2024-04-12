package serverMessages;

import webSocketMessages.serverMessages.ServerMessage;

public class Notification extends ServerMessage {
    private final String message;

    public Notification(String msgContent) {
        super(ServerMessageType.NOTIFICATION);
        this.message= msgContent;
    }
    public String getMessage() {
        return message;
    }
}
