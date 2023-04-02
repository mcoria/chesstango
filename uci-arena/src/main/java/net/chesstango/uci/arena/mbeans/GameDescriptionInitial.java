package net.chesstango.uci.arena.mbeans;

import java.io.Serializable;

/**
 * @author Mauricio Coria
 */
public class GameDescriptionInitial implements Serializable {
    protected final String initialFEN;
    protected final String white;
    protected final String black;


    public GameDescriptionInitial(String initialFEN, String white, String black) {
        this.initialFEN = initialFEN;
        this.white = white;
        this.black = black;
    }


    public String getInitialFEN() {
        return initialFEN;
    }



    public String getWhite() {
        return white;
    }


    public String getBlack() {
        return black;
    }

}
