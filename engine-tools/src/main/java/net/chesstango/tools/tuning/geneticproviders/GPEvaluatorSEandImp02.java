package net.chesstango.tools.tuning.geneticproviders;

import io.jenetics.*;
import io.jenetics.engine.EvolutionStart;
import io.jenetics.util.Factory;
import io.jenetics.util.ISeq;
import io.jenetics.util.IntRange;
import net.chesstango.tools.tuning.factories.GameEvaluatorFactory;
import net.chesstango.tools.tuning.factories.EvaluatorSEandImp02Factory;

import java.util.Arrays;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class GPEvaluatorSEandImp02 implements GeneticProvider {

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

        return new EvaluatorSEandImp02Factory(array[0], array[1], array[2]);
    }

    /**
     * Este es el proceso inverso para cargar una poblacion inicial dado los factores
     * @param factor1
     * @param factor2
     * @param factor3
     * @return
     */
    private Phenotype<IntegerGene, Long> createPhenotype(int factor1, int factor2, int factor3) {
        var scalar1 = factor1;
        var scalar2 = (factor2 * CONSTRAINT_MAX_VALUE) / (CONSTRAINT_MAX_VALUE - factor1);
        var scalar3 = (factor3 * CONSTRAINT_MAX_VALUE) / (CONSTRAINT_MAX_VALUE - factor1 - factor2);

        return Phenotype.of(
                Genotype.of(
                        IntegerChromosome.of(
                                IntegerGene.of(scalar1, geneRange),
                                IntegerGene.of(scalar2, geneRange),
                                IntegerGene.of(scalar3, geneRange)
                        )
                ), 1);
    }


}