package userGameCommand;

import chess.ChessMove;
import webSocketMessages.userCommands.UserGameCommand;

public class MakeMove extends UserGameCommand {
    ChessMove move;
    Integer gameID;
    public MakeMove(String authToken, Integer gameID, ChessMove move) {
        super(authToken);
        this.gameID = gameID;
        this.move = move;
        super.commandType = CommandType.MAKE_MOVE;
    }
}

