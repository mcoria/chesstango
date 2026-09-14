package net.chesstango.search.alphabeta.debug.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.search.alphabeta.transposition.TranspositionEntry;

/**
 * @author Mauricio Coria
 */
@Getter
@Setter
@Accessors(chain = true)
public class DebugWriteTT {

    /**
     * Este field es clonado del TranspositionEntry original
     */
    private TranspositionEntry entry;

    /**
     * A que movimiento pertenece la operacion de escritura
     */
    private String move;
}
