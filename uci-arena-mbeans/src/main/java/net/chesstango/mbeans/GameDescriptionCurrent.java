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

    public GameDescriptionCurrent(String gameId, String currentFEN, String turn, String[] moveList) {
        this.gameId = gameId;
        this.currentFEN = currentFEN;
        this.turn = turn;
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

    public String[] getMoves() {
        return moveList;
    }

}
