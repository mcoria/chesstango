package net.chesstango.search.smart.alphabeta.debug;

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
}
