package net.chesstango.tools.tuning.fitnessfunctions;

import net.chesstango.evaluation.GameEvaluator;

import java.util.function.Supplier;

/**
 * @author Mauricio Coria
 */
public interface FitnessFunction {
    long fitness(Supplier<GameEvaluator> gameEvaluatorSupplier);

    void start();

    void stop();
}
