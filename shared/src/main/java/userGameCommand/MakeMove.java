package userGameCommand;

import chess.ChessMove;
import webSocketMessages.userCommands.UserGameCommand;

public class MakeMove extends UserGameCommand {
    public ChessMove move;
    public Integer gameID;
    public MakeMove(String authToken, Integer gameID, ChessMove chessMove) {
        super(authToken);
        this.gameID = gameID;
        this.move = chessMove;
        super.commandType = CommandType.MAKE_MOVE;
    }
}

