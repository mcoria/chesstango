package net.chesstango.evaluation.tunning;

import io.jenetics.*;
import io.jenetics.engine.Constraint;
import io.jenetics.engine.EvolutionStart;
import io.jenetics.util.Factory;
import io.jenetics.util.IntRange;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.evaluation.imp.GameEvaluatorImp03;

/**
 * @author Mauricio Coria
 */
public class GeneticProviderImp03 implements GeneticProvider {
    private static int CONSTRAINT_MAX_VALUE = 1000;

    private final IntRange geneRange = IntRange.of(0, CONSTRAINT_MAX_VALUE);

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

        return new GameEvaluatorImp03(decodedGenotype.getGene1(), decodedGenotype.getGene2());
    }

    @Override
    public void printGeneAndPoints(Genotype<IntegerGene> genotype, long points) {
        GenoDecoder decodedGenotype = decodeGenotype(genotype);

        System.out.println("Evaluacion con gene1=[" + decodedGenotype.getGene1() + "] gene2=[" + decodedGenotype.getGene2() + "] ; puntos = [" + points + "]");
    }

    @Override
    public EvolutionStart<IntegerGene, Long> getEvolutionStart(int populationSize) {
        return null;
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