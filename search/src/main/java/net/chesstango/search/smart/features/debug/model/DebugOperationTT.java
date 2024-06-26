package net.chesstango.search.smart.features.debug.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.search.smart.features.transposition.TranspositionEntry;

/**
 * @author Mauricio Coria
 */
@Getter
@Setter
@Accessors(chain = true)
public class DebugOperationTT {
    public enum TableType {MAX_MAP, MIN_MAP, MAX_MAP_Q, MIN_MAP_Q}

    private TableType tableType;

    TranspositionEntry entry;

    /**
     * En caso de sorting cual es el movimiento por el cual llegamos a esta entrada
     */
    private String move;
}
