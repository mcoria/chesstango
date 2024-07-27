package net.chesstango.tools.tuning.geneticproviders;

import io.jenetics.*;
import io.jenetics.engine.EvolutionStart;
import io.jenetics.util.Factory;
import io.jenetics.util.ISeq;
import io.jenetics.util.IntRange;
import net.chesstango.evaluation.evaluators.EvaluatorImp06;
import net.chesstango.tools.tuning.factories.EvaluatorImp06Factory;
import net.chesstango.tools.tuning.factories.GameEvaluatorFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
                IntegerChromosome.of(weighRange, 3),  // weighs
                IntegerChromosome.of(tableRange, 64), // mgPawnTbl
                IntegerChromosome.of(tableRange, 64), // mgKnightTbl
                IntegerChromosome.of(tableRange, 64), // mgBishopTbl
                IntegerChromosome.of(tableRange, 64), // mgRookTbl
                IntegerChromosome.of(tableRange, 64), // mgQueenTbl
                IntegerChromosome.of(tableRange, 64), // mgKingTbl
                IntegerChromosome.of(tableRange, 64), // egPawnTbl
                IntegerChromosome.of(tableRange, 64), // egKnightTbl
                IntegerChromosome.of(tableRange, 64), // egBishopTbl
                IntegerChromosome.of(tableRange, 64), // egRookTbl
                IntegerChromosome.of(tableRange, 64), // egQueenTbl
                IntegerChromosome.of(tableRange, 64)  // egKingTbl
        );
    }

    @Override
    public EvolutionStart<IntegerGene, Long> getEvolutionStart(int populationSize) {

        try {
            List<String> dumps = Files.readAllLines(Paths.get("C:\\java\\projects\\chess\\chess-utils\\testing\\training\\Tango\\EvaluatorImp06-2024-07-22.txt"));

            List<Phenotype<IntegerGene, Long>> phenoList = dumps
                    .stream()
                    .limit(populationSize)
                    .map(this::createPhenotype)
                    .toList();

            ISeq<Phenotype<IntegerGene, Long>> population = ISeq.of(phenoList);

            return EvolutionStart.of(population, 1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
    private Phenotype<IntegerGene, Long> createPhenotype(String dump) {
        EvaluatorImp06.Tables tables = EvaluatorImp06.readValues(dump);

        List<IntegerGene> weighs = Arrays.stream(tables.weighs()).mapToObj(val -> IntegerGene.of(val, weighRange)).toList();
        List<IntegerGene> mgPawnTbl = Arrays.stream(tables.mgPawnTbl()).mapToObj(val -> IntegerGene.of(val, tableRange)).toList();
        List<IntegerGene> mgKnightTbl = Arrays.stream(tables.mgKnightTbl()).mapToObj(val -> IntegerGene.of(val, tableRange)).toList();
        List<IntegerGene> mgBishopTbl = Arrays.stream(tables.mgBishopTbl()).mapToObj(val -> IntegerGene.of(val, tableRange)).toList();
        List<IntegerGene> mgRookTbl = Arrays.stream(tables.mgRookTbl()).mapToObj(val -> IntegerGene.of(val, tableRange)).toList();
        List<IntegerGene> mgQueenTbl = Arrays.stream(tables.mgQueenTbl()).mapToObj(val -> IntegerGene.of(val, tableRange)).toList();
        List<IntegerGene> mgKingTbl = Arrays.stream(tables.mgKingTbl()).mapToObj(val -> IntegerGene.of(val, tableRange)).toList();
        List<IntegerGene> egPawnTbl = Arrays.stream(tables.egPawnTbl()).mapToObj(val -> IntegerGene.of(val, tableRange)).toList();
        List<IntegerGene> egKnightTbl = Arrays.stream(tables.egKnightTbl()).mapToObj(val -> IntegerGene.of(val, tableRange)).toList();
        List<IntegerGene> egBishopTbl = Arrays.stream(tables.egBishopTbl()).mapToObj(val -> IntegerGene.of(val, tableRange)).toList();
        List<IntegerGene> egRookTbl = Arrays.stream(tables.egRookTbl()).mapToObj(val -> IntegerGene.of(val, tableRange)).toList();
        List<IntegerGene> egQueenTbl = Arrays.stream(tables.egQueenTbl()).mapToObj(val -> IntegerGene.of(val, tableRange)).toList();
        List<IntegerGene> egKingTbl = Arrays.stream(tables.egKingTbl()).mapToObj(val -> IntegerGene.of(val, tableRange)).toList();

        return Phenotype.of(
                Genotype.of(
                        IntegerChromosome.of(weighs),
                        IntegerChromosome.of(mgPawnTbl),
                        IntegerChromosome.of(mgKnightTbl),
                        IntegerChromosome.of(mgBishopTbl),
                        IntegerChromosome.of(mgRookTbl),
                        IntegerChromosome.of(mgQueenTbl),
                        IntegerChromosome.of(mgKingTbl),
                        IntegerChromosome.of(egPawnTbl),
                        IntegerChromosome.of(egKnightTbl),
                        IntegerChromosome.of(egBishopTbl),
                        IntegerChromosome.of(egRookTbl),
                        IntegerChromosome.of(egQueenTbl),
                        IntegerChromosome.of(egKingTbl)
                ), 1);
    }


}