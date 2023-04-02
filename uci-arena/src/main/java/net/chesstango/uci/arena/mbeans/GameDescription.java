package net.chesstango.uci.arena.mbeans;

import java.beans.ConstructorProperties;
import java.io.Serializable;

public class GameDescription implements Serializable {

    protected final String initialFEN;
    protected final String currentFEN;
    protected final String white;
    protected final String black;
    protected final String turn;
    protected final String[] moveList;

    @ConstructorProperties({"initialFEN", "currentFEN", "white", "black", "turn", "moveList"})
    public GameDescription(String initialFEN, String currentFEN, String white, String black, String turn, String[] moveList) {
        this.initialFEN = initialFEN;
        this.currentFEN = currentFEN;
        this.white = white;
        this.black = black;
        this.turn = turn;
        this.moveList = moveList;
    }


    public String getInitialFEN() {
        return initialFEN;
    }


    public String getCurrentFEN() {
        return currentFEN;
    }


    public String getWhite() {
        return white;
    }


    public String getBlack() {
        return black;
    }


    public String getTurn() {
        return turn;
    }


    public String[] getMoves() {
        return moveList;
    }

}
