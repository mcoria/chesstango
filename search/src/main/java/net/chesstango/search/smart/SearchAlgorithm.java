package net.chesstango.search.smart;

import net.chesstango.search.MoveEvaluation;

/**
 * @author Mauricio Coria
 */
public interface SearchAlgorithm {

    /**
     * Invoked once per search depth
     */
    MoveEvaluation search();

}
