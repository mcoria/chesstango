package net.chesstango.evaluation.tunning;

import io.jenetics.*;
import io.jenetics.engine.Constraint;
import io.jenetics.engine.EvolutionStart;
import io.jenetics.util.*;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.evaluation.imp.GameEvaluatorImp01;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * @author Mauricio Coria
 */
public class GeneticProviderImp01 implements GeneticProvider {
    private static int CONSTRAINT_MAX_VALUE = 1000;

    private final IntRange geneRange = IntRange.of(0, CONSTRAINT_MAX_VALUE);

    @Override
    public Factory<Genotype<IntegerGene>>  getGenotypeFactory() {
        return new Factory<Genotype<IntegerGene>>(){
            private final Random random = RandomRegistry.random();

            @Override
            public Genotype<IntegerGene> newInstance() {
                int value1 = Math.abs(random.nextInt()) % CONSTRAINT_MAX_VALUE;

                int value2 = Math.abs(random.nextInt()) % (CONSTRAINT_MAX_VALUE - value1);

                int value3 = CONSTRAINT_MAX_VALUE - value2 -  value1;

                return Genotype.of(
                        IntegerChromosome.of(
                                IntegerGene.of(value1, geneRange ),
                                IntegerGene.of(value2, geneRange ),
                                IntegerGene.of(value3, geneRange )
                        )
                );
            }
        };
    }

    @Override
    public String getKeyGenesString(Genotype<IntegerGene> genotype) {
        GenoDecoder decodedGenotype = decodeGenotype(genotype);

        return decodedGenotype.getGene1() + "|" + decodedGenotype.getGene2() + "|" + decodedGenotype.getGene3();
    }


    @Override
    public GameEvaluator createGameEvaluator(Genotype<IntegerGene> genotype) {
        GenoDecoder decodedGenotype = decodeGenotype(genotype);

        return new GameEvaluatorImp01(decodedGenotype.getGene1(), decodedGenotype.getGene2(), decodedGenotype.getGene3());
    }

    @Override
    public void printGeneAndPoints(Genotype<IntegerGene> genotype, long points) {
        GenoDecoder decodedGenotype = decodeGenotype(genotype);

        System.out.println("Evaluacion con gene1=[" + decodedGenotype.getGene1() + "] gene2=[" + decodedGenotype.getGene2() + "] gene3=[" + decodedGenotype.getGene3() + "] ; puntos = [" + points + "]");
    }

    @Override
    public EvolutionStart<IntegerGene, Long> getEvolutionStart(int populationSize) {

        List<Phenotype<IntegerGene, Long>> phenoList = Arrays.asList(
//                  CODIFICAR
//                key = [628|288|84]; value=[6442450947]
//                key = [628|282|90]; value=[6442450947]
//                key = [630|106|264]; value=[6442450947]
//                key = [628|106|266]; value=[6442450947]
//                key = [151|106|743]; value=[6442450944]
//                key = [150|106|744]; value=[6442450944]
//                key = [332|258|410]; value=[6442450932]
//                key = [751|103|146]; value=[4294967293]
//                key = [752|106|142]; value=[2147483658]
//                key = [788|83|129]; value=[2147483658]
//                key = [879|60|61]; value=[2147483658]
//                key = [720|106|174]; value=[2147483658]
//                key = [879|53|68]; value=[2147483658]
//                key = [950|19|31]; value=[2147483658]
//                key = [783|106|111]; value=[2147483658]
//                key = [974|12|14]; value=[2147483658]
//                key = [770|110|120]; value=[2147483658]
//                key = [877|58|65]; value=[2147483658]
//                key = [780|82|138]; value=[2147483658]

                createPhenotype(979, 19, 2),
                createPhenotype(979, 8, 13),
                createPhenotype(979, 18, 3),
                createPhenotype(971, 18, 11),
                createPhenotype(958, 19, 23),
                createPhenotype(979, 7, 14),
                createPhenotype(979, 10, 11),
                createPhenotype(971, 13, 16),
                createPhenotype(979, 9, 12),
                createPhenotype(992, 3, 5),
                createPhenotype(981, 8, 11),
                createPhenotype(971, 19, 10),
                createPhenotype(968, 18, 14),
                createPhenotype(976, 19, 5),
                createPhenotype(979, 20, 1),
                createPhenotype(979, 17, 4),
                createPhenotype(580, 8, 412),
                createPhenotype(732, 60, 203),
                createPhenotype(731, 65, 204),
                createPhenotype(736, 65, 199),
                createPhenotype(731, 60, 209),
                createPhenotype(742, 65, 193),
                createPhenotype(744, 65, 191)
        );

        ISeq<Phenotype<IntegerGene, Long>> population = ISeq.of(phenoList);

        return EvolutionStart.of( population, 1);
    }

    private Phenotype<IntegerGene, Long> createPhenotype(int value1, int value2, int value3) {
        return Phenotype.of(
                Genotype.of(
                        IntegerChromosome.of(
                                IntegerGene.of(value1, geneRange ),
                                IntegerGene.of(value2, geneRange ),
                                IntegerGene.of(value3, geneRange )
                        )
                ), 1);
    }


    @Override
    public Constraint getPhenotypeConstraint() {
        return phenotypeConstraint;
    }

    protected final Constraint<IntegerGene, Long> phenotypeConstraint = new Constraint<IntegerGene, Long>() {
        @Override
        public boolean test(Phenotype<IntegerGene, Long> phenotype) {
            GenoDecoder decodedGenotype = decodeGenotype(phenotype.genotype());

            return (decodedGenotype.getGene1() +  decodedGenotype.getGene2() + decodedGenotype.getGene3()) % CONSTRAINT_MAX_VALUE == 0 ;
        }

        @Override
        public Phenotype<IntegerGene, Long> repair(Phenotype<IntegerGene, Long> phenotype, long generation) {
            Genotype<IntegerGene> genotype = phenotype.genotype();

            Chromosome<IntegerGene> chromosome = genotype.chromosome();

            IntegerChromosome integerChromosome = chromosome.as(IntegerChromosome.class);

            int[] array = integerChromosome.toArray();

            int gene1Value = array[0] % CONSTRAINT_MAX_VALUE;

            int gene2Value = array[1] % (CONSTRAINT_MAX_VALUE - gene1Value);

            int gene3Value = CONSTRAINT_MAX_VALUE - gene2Value - gene1Value;

            Phenotype<IntegerGene, Long> newPhenotype = Phenotype.of(Genotype.of(IntegerChromosome.of(
                    IntegerGene.of(gene1Value, geneRange ),
                    IntegerGene.of(gene2Value, geneRange ),
                    IntegerGene.of(gene3Value, geneRange )
            )), generation);

            return newPhenotype;
        }
    };

    private static class GenoDecoder {
        private final int gene1;
        private final int gene2;
        private final int gene3;

        public GenoDecoder(int gene1, int gene2, int gene3){
            this.gene1 = gene1;
            this.gene2 = gene2;
            this.gene3 = gene3;
        }

        public int getGene1() {
            return gene1;
        }

        public int getGene2() {
            return gene2;
        }

        public int getGene3() {
            return gene3;
        }
    }

    protected static GenoDecoder decodeGenotype(Genotype<IntegerGene> genotype){
        Chromosome<IntegerGene> chromo1 = genotype.chromosome();

        IntegerChromosome integerChromo = chromo1.as(IntegerChromosome.class);

        int[] array = integerChromo.toArray();

        return new GenoDecoder(array[0], array[1], array[2]);
    }
}