package net.chesstango.evaluation.tuning.fitnessfunctions;

import io.jenetics.Genotype;
import io.jenetics.IntegerGene;

/**
 * @author Mauricio Coria
 */
public interface FitnessFunction {
    long fitness(Genotype<IntegerGene> genotype);

    void start();

    void stop();
}
