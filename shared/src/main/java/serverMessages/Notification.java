package serverMessages;

import webSocketMessages.serverMessages.ServerMessage;

public class Notification extends ServerMessage {
    private String message;

    public Notification(String msgContent) {
        super(ServerMessageType.NOTIFICATION);
        this.message= msgContent;
    }
    public void setMessage(String message) {
        this.message=message;
    }
    public String getMessage() {
        return message;
    }
}
