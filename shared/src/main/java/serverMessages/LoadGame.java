package serverMessages;

import chess.ChessGame;
import com.google.gson.Gson;
import webSocketMessages.serverMessages.ServerMessage;

public class LoadGame extends ServerMessage {
    public ChessGame game;
    public LoadGame(ChessGame chessGame) {
        super(ServerMessageType.LOAD_GAME);
        this.game= chessGame;
    }
    public String getMessage() {
        return new Gson().toJson(game.getBoard());
    }
}
