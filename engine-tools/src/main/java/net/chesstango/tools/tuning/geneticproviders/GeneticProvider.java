package net.chesstango.tools.tuning.geneticproviders;

import io.jenetics.Genotype;
import io.jenetics.IntegerGene;
import io.jenetics.engine.EvolutionStart;
import io.jenetics.util.Factory;
import net.chesstango.evaluation.GameEvaluator;

/**
 * @author Mauricio Coria
 */
public interface GeneticProvider {
    EvolutionStart<IntegerGene, Long> getEvolutionStart(int populationSize);

    Factory<Genotype<IntegerGene>> getGenotypeFactory();

    String getKeyGenesString(Genotype<IntegerGene> genotype);

    GameEvaluator createGameEvaluator(Class<? extends GameEvaluator> gameEvaluatorClass, Genotype<IntegerGene> genotype);

    String genotypeToString(Genotype<IntegerGene> genotype);
}
