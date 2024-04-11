package userGameCommand;

import chess.ChessGame;
import webSocketMessages.userCommands.UserGameCommand;

public class Leave extends UserGameCommand {
    public ChessGame.TeamColor color;
    public Integer gameID;
    public Leave(String authToken, Integer gameID, ChessGame.TeamColor teamColor) {
        super(authToken);
        this.color= teamColor;
        this.gameID = gameID;
        super.commandType = CommandType.LEAVE;
    }
}
