package net.chesstango.evaluation.tuning.geneticproviders;

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

    void printGeneAndPoints(Genotype<IntegerGene> genotype, long points);
}
