package net.chesstango.tools.tuning.geneticproviders;

import io.jenetics.Genotype;
import io.jenetics.IntegerGene;
import io.jenetics.engine.EvolutionStart;
import io.jenetics.util.Factory;
import net.chesstango.tools.tuning.factories.GameEvaluatorFactory;

/**
 * @author Mauricio Coria
 */
public interface GeneticProvider {
    EvolutionStart<IntegerGene, Long> getEvolutionStart(int populationSize);

    Factory<Genotype<IntegerGene>> getGenotypeFactory();

    GameEvaluatorFactory createGameEvaluatorFactors(Genotype<IntegerGene> genotype);

}
