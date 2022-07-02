package net.chesstango.ai.imp.smart.evaluation.tunning;

import io.jenetics.Genotype;
import io.jenetics.IntegerGene;
import io.jenetics.engine.Constraint;
import io.jenetics.util.Factory;
import net.chesstango.uci.arbiter.EngineController;

public interface GeneticProvider {
    Factory getGenotypeFactory();

    Constraint getPhenotypeConstraint();

    String getKeyGenesString(Genotype<IntegerGene> genotype);

    EngineController createTango(Genotype<IntegerGene> genotype);

    void printGeneAndPoints(Genotype<IntegerGene> genotype, long points);
}
