package net.chesstango.tools.tuning.geneticproviders;

import io.jenetics.*;
import io.jenetics.engine.EvolutionStart;
import io.jenetics.util.Factory;
import io.jenetics.util.ISeq;
import io.jenetics.util.IntRange;
import net.chesstango.evaluation.evaluators.EvaluatorImp06;
import net.chesstango.tools.tuning.factories.EvaluatorByMaterialFactory;
import net.chesstango.tools.tuning.factories.GameEvaluatorFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;


/**
 * @author Mauricio Coria
 */
public class GPEvaluatorByMaterial implements GeneticProvider {
    private static final int CONSTRAINT_MAX_VALUE = 10;

    private static final IntRange VALUE_RANGE = IntRange.of(1, CONSTRAINT_MAX_VALUE);

    @Override
    public Factory<Genotype<IntegerGene>> getGenotypeFactory() {
        return Genotype.of(
                IntegerChromosome.of(VALUE_RANGE, 5)
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
        Chromosome<IntegerGene> chromosome = genotype.chromosome();

        IntegerChromosome integerChromosome = chromosome.as(IntegerChromosome.class);

        int[] pieceValues = integerChromosome.toArray();

        return new EvaluatorByMaterialFactory(pieceValues);
    }

    /**
     * Este es el proceso inverso para cargar una poblacion inicial dado los factores
     *
     * @return
     */
    private Phenotype<IntegerGene, Long> createPhenotype(String dump) {
        EvaluatorImp06.Tables tables = EvaluatorImp06.readValues(dump);

        List<IntegerGene> mgPawnTbl = Arrays.stream(tables.mgPawnTbl()).mapToObj(val -> IntegerGene.of(val, VALUE_RANGE)).toList();
        List<IntegerGene> mgKnightTbl = Arrays.stream(tables.mgKnightTbl()).mapToObj(val -> IntegerGene.of(val, VALUE_RANGE)).toList();
        List<IntegerGene> mgBishopTbl = Arrays.stream(tables.mgBishopTbl()).mapToObj(val -> IntegerGene.of(val, VALUE_RANGE)).toList();
        List<IntegerGene> mgRookTbl = Arrays.stream(tables.mgRookTbl()).mapToObj(val -> IntegerGene.of(val, VALUE_RANGE)).toList();
        List<IntegerGene> mgQueenTbl = Arrays.stream(tables.mgQueenTbl()).mapToObj(val -> IntegerGene.of(val, VALUE_RANGE)).toList();
        List<IntegerGene> mgKingTbl = Arrays.stream(tables.mgKingTbl()).mapToObj(val -> IntegerGene.of(val, VALUE_RANGE)).toList();
        List<IntegerGene> egPawnTbl = Arrays.stream(tables.egPawnTbl()).mapToObj(val -> IntegerGene.of(val, VALUE_RANGE)).toList();
        List<IntegerGene> egKnightTbl = Arrays.stream(tables.egKnightTbl()).mapToObj(val -> IntegerGene.of(val, VALUE_RANGE)).toList();
        List<IntegerGene> egBishopTbl = Arrays.stream(tables.egBishopTbl()).mapToObj(val -> IntegerGene.of(val, VALUE_RANGE)).toList();
        List<IntegerGene> egRookTbl = Arrays.stream(tables.egRookTbl()).mapToObj(val -> IntegerGene.of(val, VALUE_RANGE)).toList();
        List<IntegerGene> egQueenTbl = Arrays.stream(tables.egQueenTbl()).mapToObj(val -> IntegerGene.of(val, VALUE_RANGE)).toList();
        List<IntegerGene> egKingTbl = Arrays.stream(tables.egKingTbl()).mapToObj(val -> IntegerGene.of(val, VALUE_RANGE)).toList();

        return Phenotype.of(
                Genotype.of(
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