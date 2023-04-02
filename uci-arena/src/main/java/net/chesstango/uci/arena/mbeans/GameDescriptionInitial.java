package net.chesstango.uci.arena.mbeans;

import java.io.Serializable;

/**
 * @author Mauricio Coria
 */
public class GameDescriptionInitial implements Serializable {
    private final int gameId;
    protected final String initialFEN;
    protected final String white;
    protected final String black;



    public GameDescriptionInitial(int gameId, String initialFEN, String white, String black) {
        this.gameId = gameId;
        this.initialFEN = initialFEN;
        this.white = white;
        this.black = black;
    }

    public int getGameId() {
        return gameId;
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
