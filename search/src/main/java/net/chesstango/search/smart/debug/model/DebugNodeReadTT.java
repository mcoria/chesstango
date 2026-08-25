package net.chesstango.search.smart.debug.model;

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
public class DebugNodeReadTT {
    public static final String NO_MOVE = "NO_MOVE";
    public static final String UNKNOWN = "UNKNOWN";


    /**
     * Este field es clonado del TranspositionEntry original
     */
    private TranspositionEntry entry;

    /**
     * Hash solicitado
     */
    private long hashRequested;

    /**
     * A que movimiento pertenece la operacion de lectura
     */
    private String move;
}
