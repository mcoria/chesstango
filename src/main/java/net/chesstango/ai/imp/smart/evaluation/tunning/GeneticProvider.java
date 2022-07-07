package net.chesstango.ai.imp.smart.evaluation.tunning;

import io.jenetics.Genotype;
import io.jenetics.IntegerGene;
import io.jenetics.engine.Constraint;
import io.jenetics.engine.EvolutionStart;
import io.jenetics.util.Factory;
import net.chesstango.ai.imp.smart.evaluation.GameEvaluator;

/**
 * @author Mauricio Coria
 */
public interface GeneticProvider {
    Factory<Genotype<IntegerGene>> getGenotypeFactory();

    Constraint getPhenotypeConstraint();

    String getKeyGenesString(Genotype<IntegerGene> genotype);

    GameEvaluator createGameEvaluator(Genotype<IntegerGene> genotype);

    void printGeneAndPoints(Genotype<IntegerGene> genotype, long points);

    EvolutionStart<IntegerGene, Long> getEvolutionStart(int populationSize);
}
