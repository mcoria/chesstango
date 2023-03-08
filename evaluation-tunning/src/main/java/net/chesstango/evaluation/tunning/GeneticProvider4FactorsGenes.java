package net.chesstango.evaluation.tunning;

import io.jenetics.*;
import io.jenetics.engine.Constraint;
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

    private static int CONSTRAINT_MAX_VALUE = 1000;

    private final IntRange geneRange = IntRange.of(0, CONSTRAINT_MAX_VALUE);

    public GeneticProvider4FactorsGenes(Class<? extends GameEvaluator> gameEvaluatorClass) {
        this.gameEvaluatorClass = gameEvaluatorClass;
    }

    @Override
    public Factory<Genotype<IntegerGene>> getGenotypeFactory() {
        return Genotype.of(IntegerChromosome.of(geneRange, 2));
    }

    @Override
    public String getKeyGenesString(Genotype<IntegerGene> genotype) {
        GenoDecoder decodedGenotype = decodeGenotype(genotype);

        return decodedGenotype.getGene1() + "|" + decodedGenotype.getGene2();
    }

    @Override
    public GameEvaluator createGameEvaluator(Genotype<IntegerGene> genotype) {
        GenoDecoder decodedGenotype = decodeGenotype(genotype);

        try {
            return gameEvaluatorClass.getDeclaredConstructor(Integer.class, Integer.class).newInstance(decodedGenotype.getGene1(), decodedGenotype.getGene2());
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

        System.out.println("Evaluacion con gene1=[" + decodedGenotype.getGene1() + "] gene2=[" + decodedGenotype.getGene2() + "] ; puntos = [" + points + "]");
    }

    @Override
    public EvolutionStart<IntegerGene, Long> getEvolutionStart(int populationSize) {
        List<Phenotype<IntegerGene, Long>> phenoList = Arrays.asList(
                createPhenotype(415, 585),
                createPhenotype(433, 567),
                createPhenotype(516, 424),
                createPhenotype(554, 446),
                createPhenotype(600, 335),
                createPhenotype(665, 400),
                createPhenotype(737, 263),
                createPhenotype(746, 254)
        );

        ISeq<Phenotype<IntegerGene, Long>> population = ISeq.of(phenoList);

        return EvolutionStart.of(population, 1);
    }

    private Phenotype<IntegerGene, Long> createPhenotype(int value1, int value2) {
        return Phenotype.of(
                Genotype.of(
                        IntegerChromosome.of(
                                IntegerGene.of(value1, geneRange),
                                IntegerGene.of(value2, geneRange)
                        )
                ), 1);
    }

    public Constraint getPhenotypeConstraint() {
        return phenotypeConstraint;
    }

    protected final Constraint<IntegerGene, Long> phenotypeConstraint = new Constraint<IntegerGene, Long>() {
        @Override
        public boolean test(Phenotype<IntegerGene, Long> phenotype) {
            GenoDecoder decodedGenotype = decodeGenotype(phenotype.genotype());

            return decodedGenotype.getGene1() + decodedGenotype.getGene2() == CONSTRAINT_MAX_VALUE;
        }

        @Override
        public Phenotype<IntegerGene, Long> repair(Phenotype<IntegerGene, Long> phenotype, long generation) {
            Genotype<IntegerGene> genotype = phenotype.genotype();

            Chromosome<IntegerGene> chromo1 = genotype.get(0);

            IntegerChromosome integerChromo = chromo1.as(IntegerChromosome.class);

            int[] array = integerChromo.toArray();

            int gene1Value = array[0] % CONSTRAINT_MAX_VALUE;

            int gene2Value = CONSTRAINT_MAX_VALUE - gene1Value;

            if(gene1Value + gene2Value != CONSTRAINT_MAX_VALUE) {
                throw new RuntimeException("Invalid combination");
            }

            Phenotype<IntegerGene, Long> newPhenotype = Phenotype.of(Genotype.of(IntegerChromosome.of(
                    IntegerGene.of(gene1Value, geneRange),
                    IntegerGene.of(gene2Value, geneRange)
            )), generation);

            return newPhenotype;
        }
    };


    private static class GenoDecoder {
        private final int gene1;
        private final int gene2;

        public GenoDecoder(int gene1, int gene2) {
            this.gene1 = gene1;
            this.gene2 = gene2;
        }

        public int getGene1() {
            return gene1;
        }

        public int getGene2() {
            return gene2;
        }
    }

    protected static GenoDecoder decodeGenotype(Genotype<IntegerGene> genotype) {
        Chromosome<IntegerGene> chromo1 = genotype.chromosome();

        IntegerChromosome integerChromo = chromo1.as(IntegerChromosome.class);

        int[] array = integerChromo.toArray();

        return new GenoDecoder(array[0], array[1]);
    }
}