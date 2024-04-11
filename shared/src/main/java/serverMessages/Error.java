package serverMessages;

import webSocketMessages.serverMessages.ServerMessage;

public class Error extends ServerMessage {
    private String errorMessage;

    public Error(String msgContent) {
        super(ServerMessageType.ERROR);
        this.errorMessage = msgContent;
    }
    public String getMessage() {
        return errorMessage;
    }
}
