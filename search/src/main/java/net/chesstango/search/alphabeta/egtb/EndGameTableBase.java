package net.chesstango.search.alphabeta.egtb;

import net.chesstango.evaluation.Evaluator;

/**
 * @author Mauricio Coria
 */
public interface EndGameTableBase extends Evaluator {
    boolean isProbeAvailable();
}

