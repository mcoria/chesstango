package net.chesstango.tools.tuning.fitnessfunctions;

import net.chesstango.evaluation.Evaluator;

import java.util.function.Supplier;

/**
 * @author Mauricio Coria
 */
public interface FitnessFunction {
    long fitness(Supplier<Evaluator> gameEvaluatorSupplier);

    void start();

    void stop();
}
