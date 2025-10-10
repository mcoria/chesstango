package net.chesstango.search.smart.features.egtb;

import net.chesstango.evaluation.Evaluator;

/**
 * @author Mauricio Coria
 */
public interface EndGameTableBase extends Evaluator {
    boolean isProbeAvailable();
}

