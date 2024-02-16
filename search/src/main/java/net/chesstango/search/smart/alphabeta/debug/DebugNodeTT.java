package net.chesstango.search.smart.alphabeta.debug;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.search.smart.transposition.TranspositionEntry;

/**
 * @author Mauricio Coria
 */
@Getter
@Setter
@Accessors(chain = true)
public class DebugNodeTT {
    public enum TableType {MAX_MAP, MIN_MAP, MAX_MAP_Q, MIN_MAP_Q}
    private TableType tableType;

    private long hashRequested;

    TranspositionEntry entry;

    /**
     * En caso de sorting cual es el movimiento por el cual llegamos a esta entrada
     */
    private String move;
}
