package userGameCommand;

import chess.ChessGame;
import webSocketMessages.userCommands.UserGameCommand;

public class JoinPlayer extends UserGameCommand {
    Integer gameID;
    private String msgContent;
    ChessGame.TeamColor teamColor;
    public JoinPlayer(String authToken, Integer gameID, ChessGame.TeamColor teamColor ) {
        super(authToken);
        this.gameID = gameID;
        this.teamColor = teamColor;
        super.commandType = CommandType.JOIN_PLAYER;
    }
}
