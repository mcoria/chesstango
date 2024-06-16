package net.chesstango.tools.tuning.factories;

import net.chesstango.evaluation.GameEvaluator;

/**
 * @author Mauricio Coria
 */
public interface GameEvaluatorFactory {
    GameEvaluator createGameEvaluator();

    String getKey();
}
