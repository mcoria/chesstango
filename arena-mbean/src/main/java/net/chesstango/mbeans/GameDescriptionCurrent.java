package net.chesstango.mbeans;

import lombok.Getter;

import java.io.Serializable;

/**
 * @author Mauricio Coria
 */
@Getter
public class GameDescriptionCurrent implements Serializable {
    private final String gameId;
    private final String currentFEN;
    private final String turn;
    private final String[] moves;
    private final String lastMove;

    public GameDescriptionCurrent(String gameId, String currentFEN, String turn, String lastMove, String[] moveList) {
        this.gameId = gameId;
        this.currentFEN = currentFEN;
        this.turn = turn;
        this.lastMove = lastMove;
        this.moves = moveList;
    }

}
