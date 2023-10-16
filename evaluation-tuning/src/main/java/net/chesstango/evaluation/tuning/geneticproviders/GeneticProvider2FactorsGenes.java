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
public class GeneticProvider2FactorsGenes implements GeneticProvider {

    private static final int CONSTRAINT_MAX_VALUE = 1000;

    private final IntRange geneRange = IntRange.of(0, CONSTRAINT_MAX_VALUE);


    @Override
    public Factory<Genotype<IntegerGene>> getGenotypeFactory() {
        return Genotype.of(IntegerChromosome.of(geneRange, 1));
    }

    @Override
    public String getKeyGenesString(Genotype<IntegerGene> genotype) {
        GenoDecoder decodedGenotype = decodeGenotype(genotype);

        return decodedGenotype.getFactor1() + "|" + decodedGenotype.getFactor2();
    }

    @Override
    public GameEvaluator createGameEvaluator(Class<? extends GameEvaluator> gameEvaluatorClass, Genotype<IntegerGene> genotype) {
        GenoDecoder decodedGenotype = decodeGenotype(genotype);

        try {
            return gameEvaluatorClass.getDeclaredConstructor(Integer.class, Integer.class).newInstance(decodedGenotype.getFactor1(), decodedGenotype.getFactor2());
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
    public String genotypeToString(Genotype<IntegerGene> genotype) {
        GenoDecoder decodedGenotype = decodeGenotype(genotype);

        return String.format("[factor1=[%d] factor2=[%d]]", decodedGenotype.getFactor1(), decodedGenotype.getFactor2());
    }

    @Override
    public EvolutionStart<IntegerGene, Long> getEvolutionStart(int populationSize) {
        List<Phenotype<IntegerGene, Long>> phenoList = Arrays.asList(
                createPhenotype(415),
                createPhenotype(433),
                createPhenotype(516),
                createPhenotype(554),
                createPhenotype(600),
                createPhenotype(665),
                createPhenotype(737),
                createPhenotype(746)
        );

        ISeq<Phenotype<IntegerGene, Long>> population = ISeq.of(phenoList);

        return EvolutionStart.of(population, 1);
    }

    private Phenotype<IntegerGene, Long> createPhenotype(int value1) {
        return Phenotype.of(
                Genotype.of(
                        IntegerChromosome.of(
                                IntegerGene.of(value1, geneRange)
                        )
                ), 1);
    }


    private static class GenoDecoder {
        private final int factor1;
        private final int factor2;

        public GenoDecoder(int gene1) {
            this.factor1 = gene1;
            this.factor2 = CONSTRAINT_MAX_VALUE - factor1;
        }

        public int getFactor1() {
            return factor1;
        }

        public int getFactor2() {
            return factor2;
        }
    }

    protected static GenoDecoder decodeGenotype(Genotype<IntegerGene> genotype) {
        Chromosome<IntegerGene> chromosome = genotype.chromosome();

        IntegerChromosome intChromosome = chromosome.as(IntegerChromosome.class);

        int[] array = intChromosome.toArray();

        return new GenoDecoder(array[0]);
    }
}