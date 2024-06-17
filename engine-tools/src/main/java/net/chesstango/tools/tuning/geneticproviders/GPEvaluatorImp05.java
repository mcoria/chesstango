package net.chesstango.tools.tuning.geneticproviders;

import io.jenetics.*;
import io.jenetics.engine.EvolutionStart;
import io.jenetics.util.Factory;
import io.jenetics.util.ISeq;
import io.jenetics.util.IntRange;
import net.chesstango.tools.tuning.factories.EvaluatorImp05Factory;
import net.chesstango.tools.tuning.factories.GameEvaluatorFactory;

import java.util.Arrays;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class GPEvaluatorImp05 implements GeneticProvider {
    private static final int CONSTRAINT_MAX_VALUE = 1000;

    private final IntRange geneRange = IntRange.of(0, CONSTRAINT_MAX_VALUE);

    @Override
    public Factory<Genotype<IntegerGene>> getGenotypeFactory() {
        return Genotype.of(IntegerChromosome.of(geneRange, 3));
    }

    @Override
    public EvolutionStart<IntegerGene, Long> getEvolutionStart(int populationSize) {
        List<Phenotype<IntegerGene, Long>> phenoList = Arrays.asList(
                createPhenotype(326, 173, 7),
                createPhenotype(326, 173, 79)
        );

        ISeq<Phenotype<IntegerGene, Long>> population = ISeq.of(phenoList);

        return EvolutionStart.of(population, 1);
    }


    @Override
    public GameEvaluatorFactory createGameEvaluatorFactors(Genotype<IntegerGene> genotype) {
        Chromosome<IntegerGene> chromo1 = genotype.chromosome();

        IntegerChromosome integerChromo = chromo1.as(IntegerChromosome.class);

        int[] array = integerChromo.toArray();

        return new EvaluatorImp05Factory(array);
    }

    /**
     * Este es el proceso inverso para cargar una poblacion inicial dado los factores
     *
     * @return
     */
    private Phenotype<IntegerGene, Long> createPhenotype(int weigh1, int weigh2, int weigh3) {
        return Phenotype.of(
                Genotype.of(
                        IntegerChromosome.of(
                                IntegerGene.of(weigh1, geneRange),
                                IntegerGene.of(weigh2, geneRange),
                                IntegerGene.of(weigh3, geneRange)
                        )
                ), 1);
    }


}