package net.chesstango.tools.tuning.geneticproviders;

import io.jenetics.*;
import io.jenetics.engine.EvolutionStart;
import io.jenetics.util.Factory;
import io.jenetics.util.ISeq;
import io.jenetics.util.IntRange;
import net.chesstango.tools.tuning.factories.EvaluatorImp06Factory;
import net.chesstango.tools.tuning.factories.GameEvaluatorFactory;

import java.util.Arrays;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class GPEvaluatorImp06 implements GeneticProvider {
    private static final int CONSTRAINT_MAX_VALUE = 1000;

    private static final IntRange weighRange = IntRange.of(0, CONSTRAINT_MAX_VALUE);
    private static final IntRange tableRange = IntRange.of(-CONSTRAINT_MAX_VALUE, CONSTRAINT_MAX_VALUE);

    @Override
    public Factory<Genotype<IntegerGene>> getGenotypeFactory() {
        return Genotype.of(
                IntegerChromosome.of(weighRange, 3),
                IntegerChromosome.of(tableRange, 64),
                IntegerChromosome.of(tableRange, 64),
                IntegerChromosome.of(tableRange, 64),
                IntegerChromosome.of(tableRange, 64),
                IntegerChromosome.of(tableRange, 64),
                IntegerChromosome.of(tableRange, 64),
                IntegerChromosome.of(tableRange, 64),
                IntegerChromosome.of(tableRange, 64),
                IntegerChromosome.of(tableRange, 64),
                IntegerChromosome.of(tableRange, 64),
                IntegerChromosome.of(tableRange, 64),
                IntegerChromosome.of(tableRange, 64)
        );
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
        int[] weighs = chromosomeToArray(genotype.get(0));

        int[] mgPawnTbl = chromosomeToArray(genotype.get(1));
        int[] mgKnightTbl = chromosomeToArray(genotype.get(2));
        int[] mgBishopTbl = chromosomeToArray(genotype.get(3));
        int[] mgRookTbl = chromosomeToArray(genotype.get(4));
        int[] mgQueenTbl = chromosomeToArray(genotype.get(5));
        int[] mgKingTbl = chromosomeToArray(genotype.get(6));

        int[] egPawnTbl = chromosomeToArray(genotype.get(7));
        int[] egKnightTbl = chromosomeToArray(genotype.get(8));
        int[] egBishopTbl = chromosomeToArray(genotype.get(9));
        int[] egRookTbl = chromosomeToArray(genotype.get(10));
        int[] egQueenTbl = chromosomeToArray(genotype.get(11));
        int[] egKingTbl = chromosomeToArray(genotype.get(12));

        return new EvaluatorImp06Factory(weighs,
                mgPawnTbl, mgKnightTbl, mgBishopTbl, mgRookTbl, mgQueenTbl, mgKingTbl,
                egPawnTbl, egKnightTbl, egBishopTbl, egRookTbl, egQueenTbl, egKingTbl);
    }

    private int[] chromosomeToArray(Chromosome<IntegerGene> chromosome) {
        IntegerChromosome intChromosome = chromosome.as(IntegerChromosome.class);
        return intChromosome.toArray();
    }

    /**
     * Este es el proceso inverso para cargar una poblacion inicial dado los factores
     *
     * @return
     */
    private Phenotype<IntegerGene, Long> createPhenotype(int weigh1, int weigh2, int weigh3) {
        /*
        return Phenotype.of(
                Genotype.of(
                        IntegerChromosome.of(
                                IntegerGene.of(weigh1, geneRange),
                                IntegerGene.of(weigh2, geneRange),
                                IntegerGene.of(weigh3, geneRange)
                        )
                ), 1);
        */
        return null;
    }


}