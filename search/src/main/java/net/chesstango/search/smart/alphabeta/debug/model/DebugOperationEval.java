package net.chesstango.search.smart.alphabeta.debug.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author Mauricio Coria
 */
@Getter
@Setter
@Accessors(chain = true)
public class DebugOperationEval {
    private long hashRequested;
    private int evaluation;

    /**
     * En caso de sorting cual es el movimiento por el cual llegamos a esta entrada
     */
    private String move;
}
