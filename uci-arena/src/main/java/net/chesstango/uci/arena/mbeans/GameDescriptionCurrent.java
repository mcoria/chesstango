package net.chesstango.uci.arena.mbeans;

import java.io.Serializable;

/**
 * @author Mauricio Coria
 */
public class GameDescriptionCurrent implements Serializable {
    protected final String currentFEN;
    protected final String turn;
    protected final String[] moveList;

    public GameDescriptionCurrent(String currentFEN, String turn, String[] moveList) {
        this.currentFEN = currentFEN;
        this.turn = turn;
        this.moveList = moveList;
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
