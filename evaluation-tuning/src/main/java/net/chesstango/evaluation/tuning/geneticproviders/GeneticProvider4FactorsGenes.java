package net.chesstango.evaluation.tuning.geneticproviders;

import io.jenetics.*;
import io.jenetics.engine.EvolutionStart;
import io.jenetics.util.Factory;
import io.jenetics.util.ISeq;
import io.jenetics.util.IntRange;
import net.chesstango.evaluation.GameEvaluator;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class GeneticProvider4FactorsGenes implements GeneticProvider {
    private final Class<? extends GameEvaluator> gameEvaluatorClass;

    private static final int CONSTRAINT_MAX_VALUE = 1000;

    private final IntRange geneRange = IntRange.of(0, CONSTRAINT_MAX_VALUE);

    public GeneticProvider4FactorsGenes(Class<? extends GameEvaluator> gameEvaluatorClass) {
        this.gameEvaluatorClass = gameEvaluatorClass;
    }

    @Override
    public Factory<Genotype<IntegerGene>> getGenotypeFactory() {
        return Genotype.of(IntegerChromosome.of(geneRange, 3));
    }

    @Override
    public String getKeyGenesString(Genotype<IntegerGene> genotype) {
        GenoDecoder decodedGenotype = decodeGenotype(genotype);

        return decodedGenotype.getFactor1() + "|" + decodedGenotype.getFactor2() + "|" + decodedGenotype.getFactor3() + "|" + decodedGenotype.getFactor4();
    }

    @Override
    public GameEvaluator createGameEvaluator(Genotype<IntegerGene> genotype) {
        GenoDecoder decodedGenotype = decodeGenotype(genotype);

        try {
            return gameEvaluatorClass.getDeclaredConstructor(Integer.class, Integer.class, Integer.class, Integer.class).newInstance(decodedGenotype.getFactor1(), decodedGenotype.getFactor2(), decodedGenotype.getFactor3(), decodedGenotype.getFactor4());
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void printGeneAndPoints(Genotype<IntegerGene> genotype, long points) {
        GenoDecoder decodedGenotype = decodeGenotype(genotype);

        System.out.println("Evaluacion con factor1=[" + decodedGenotype.getFactor1() + "] factor2=[" + decodedGenotype.getFactor2() +  "] factor3=[" + decodedGenotype.getFactor3() + "] factor4=[" + decodedGenotype.getFactor4() +"] ; puntos = [" + points + "]");
    }

    @Override
    public EvolutionStart<IntegerGene, Long> getEvolutionStart(int populationSize) {
        List<Phenotype<IntegerGene, Long>> phenoList = Arrays.asList(
                //createPhenotype(415, 585),
        );

        ISeq<Phenotype<IntegerGene, Long>> population = ISeq.of(phenoList);

        return EvolutionStart.of(population, 1);
    }

    private Phenotype<IntegerGene, Long> createPhenotype(int value1, int value2, int value3) {
        return Phenotype.of(
                Genotype.of(
                        IntegerChromosome.of(
                                IntegerGene.of(value1, geneRange),
                                IntegerGene.of(value2, geneRange),
                                IntegerGene.of(value3, geneRange)
                        )
                ), 1);
    }


    private static class GenoDecoder {
        private final int factor1;
        private final int factor2;
        private final int factor3;
        private final int factor4;

        public GenoDecoder(int gene1, int gene2, int gene3) {
            this.factor1 = gene1;
            this.factor2 = gene2 * (CONSTRAINT_MAX_VALUE - factor1) / CONSTRAINT_MAX_VALUE;
            this.factor3 = gene3 * (CONSTRAINT_MAX_VALUE - factor1 - factor2) / CONSTRAINT_MAX_VALUE;
            this.factor4 = CONSTRAINT_MAX_VALUE - factor1 - factor2 - factor3;
        }

        public int getFactor1() {
            return factor1;
        }

        public int getFactor2() {
            return factor2;
        }

        public int getFactor3() {
            return factor3;
        }

        public int getFactor4() {
            return factor4;
        }
    }

    protected static GenoDecoder decodeGenotype(Genotype<IntegerGene> genotype) {
        Chromosome<IntegerGene> chromo1 = genotype.chromosome();

        IntegerChromosome integerChromo = chromo1.as(IntegerChromosome.class);

        int[] array = integerChromo.toArray();

        return new GenoDecoder(array[0], array[1], array[2]);
    }
}