package userGameCommand;

import chess.ChessGame;
import webSocketMessages.userCommands.UserGameCommand;

public class JoinPlayer extends UserGameCommand {
    public ChessGame.TeamColor playerColor;
    public Integer gameID;
    private String msgContent;
    public JoinPlayer(String authToken, Integer gameID, ChessGame.TeamColor teamColor ) {
        super(authToken);
        super.commandType = CommandType.JOIN_PLAYER;
        this.gameID = gameID;
        this.playerColor= teamColor;

    }
}
