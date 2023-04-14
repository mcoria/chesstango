package net.chesstango.mbeans;

import java.io.Serializable;

/**
 * @author Mauricio Coria
 */
public class GameDescriptionInitial implements Serializable {
    private final String gameId;
    protected final String initialFEN;
    protected final String white;
    protected final String black;
    private final String turn;

    public GameDescriptionInitial(String gameId, String initialFEN, String white, String black, String turn) {
        this.gameId = gameId;
        this.initialFEN = initialFEN;
        this.white = white;
        this.black = black;
        this.turn = turn;
    }
    public String getGameId() {
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

    public String getTurn() {
        return turn;
    }

}