package net.chesstango.evaluation.tuning.fitnessfunctions;

import net.chesstango.evaluation.GameEvaluator;

/**
 * @author Mauricio Coria
 */
public interface FitnessFunction {
    long fitness(GameEvaluator gameEvaluator);

    void start();

    void stop();
}
