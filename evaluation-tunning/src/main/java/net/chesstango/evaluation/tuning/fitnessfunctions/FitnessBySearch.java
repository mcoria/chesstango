package net.chesstango.evaluation.tuning.fitnessfunctions;

import io.jenetics.Genotype;
import io.jenetics.IntegerGene;

/**
 * @author Mauricio Coria
 */
public class FitnessBySearch implements FitnessFunction {
    @Override
    public long fitness(Genotype<IntegerGene> genotype) {
        return 0;
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }
}
