package net.chesstango.mbeans;

import java.io.Serializable;

/**
 * @author Mauricio Coria
 */
public class GameDescriptionCurrent implements Serializable {
    private final String gameId;
    private final String currentFEN;
    private final String turn;
    private final String[] moveList;
    private final String lastMove;

    public GameDescriptionCurrent(String gameId, String currentFEN, String turn, String lastMove, String[] moveList) {
        this.gameId = gameId;
        this.currentFEN = currentFEN;
        this.turn = turn;
        this.lastMove = lastMove;
        this.moveList = moveList;
    }

    public String getGameId() {
        return gameId;
    }

    public String getCurrentFEN() {
        return currentFEN;
    }

    public String getTurn() {
        return turn;
    }

    public String getLastMove() {
        return lastMove;
    }

    public String[] getMoves() {
        return moveList;
    }

}
