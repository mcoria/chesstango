package net.chesstango.tools.tuning.factories;

import net.chesstango.evaluation.Evaluator;

/**
 * @author Mauricio Coria
 */
public interface GameEvaluatorFactory {
    Evaluator createGameEvaluator();

    String getKey();

    String getRepresentation();
}
