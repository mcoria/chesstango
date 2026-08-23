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
public class DebugNodeTT {
    public static final String NO_MOVE = "NO_MOVE";
    public static final String UNKNOWN = "UNKNOWN";

    public enum Operation {
        READ, WRITE
    }

    private Operation operation;


    /**
     * Este field es clonado del TranspositionEntry original
     */
    private TranspositionEntry entry;

    /**
     * En caso de sorting cual es el movimiento por el cual llegamos a esta entrada
     */
    private String move;
}
