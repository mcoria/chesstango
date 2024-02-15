package net.chesstango.search.smart.alphabeta.debug;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.board.moves.Move;
import net.chesstango.search.smart.transposition.TranspositionBound;

/**
 * @author Mauricio Coria
 */
@Getter
@Setter
@Accessors(chain = true)
public class DebugNodeTT {
    public enum TableType {MAX_MAP, MIN_MAP, MAX_MAP_Q, MIN_MAP_Q}

    private long hashRequested;

    private TableType tableType;
    private long hash;
    private int depth;
    private long movesAndValue;
    private TranspositionBound bound;

    /**
     * En caso de sorting cual es el movimiento por el cual llegamos a esta entrada
     */
    private Move move;
}
