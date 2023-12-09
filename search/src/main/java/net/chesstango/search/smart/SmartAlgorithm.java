package net.chesstango.search.smart;

import net.chesstango.search.MoveEvaluation;

/**
 * @author Mauricio Coria
 */
public interface SmartAlgorithm {

    /**
     * Invoked once per search depth
     */
    MoveEvaluation search();

}
