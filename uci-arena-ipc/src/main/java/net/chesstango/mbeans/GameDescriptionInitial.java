package net.chesstango.mbeans;

import lombok.Getter;

import java.io.Serializable;

/**
 * @author Mauricio Coria
 */
@Getter
public class GameDescriptionInitial implements Serializable {
    private final String gameId;
    private final String initialFEN;
    private final String white;
    private final String black;
    private final String turn;

    public GameDescriptionInitial(String gameId, String initialFEN, String white, String black,
                           String turn){
        this.gameId = gameId;
        this.initialFEN = initialFEN;
        this.white = white;
        this.black = black;
        this.turn = turn;
    }

}
