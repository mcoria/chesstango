package net.chesstango.tools.tuning.geneticproviders;

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

    private static final int CONSTRAINT_MAX_VALUE = 1000;

    private final IntRange geneRange = IntRange.of(0, CONSTRAINT_MAX_VALUE);

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
    public String genotypeToString(Genotype<IntegerGene> genotype) {
        GenoDecoder decodedGenotype = decodeGenotype(genotype);

        return String.format("[factor1=[%d] factor2=[%d] factor3=[%d] factor4=[%d]]", decodedGenotype.getFactor1(), decodedGenotype.getFactor2(), decodedGenotype.getFactor3(), decodedGenotype.getFactor4());
    }

    @Override
    public EvolutionStart<IntegerGene, Long> getEvolutionStart(int populationSize) {
        List<Phenotype<IntegerGene, Long>> phenoList = Arrays.asList(
                /*
                    CODIFICAR
                    key = [545|423|17|15]; value=[51635]
                    key = [545|422|18|15]; value=[51599]
                    key = [562|406|19|13]; value=[51557]
                    key = [562|404|20|14]; value=[51551]
                    key = [562|406|17|15]; value=[51480]
                    key = [725|255|3|17]; value=[51456]
                    key = [380|597|4|19]; value=[51435]
                    key = [590|380|5|25]; value=[51433]
                    key = [725|255|6|14]; value=[51430]
                    key = [545|422|5|28]; value=[51411]
                    key = [725|255|4|16]; value=[51406]
                    key = [534|432|24|10]; value=[51403]
                    key = [582|387|5|26]; value=[51396]
                    key = [562|406|7|25]; value=[51385]
                    key = [536|428|15|21]; value=[51282]
                    key = [545|422|11|22]; value=[51259]
                    key = [545|422|24|9]; value=[51234]
                    key = [725|252|4|19]; value=[51151]
                    key = [488|441|57|14]; value=[51145]
                    key = [725|255|11|9]; value=[51132]
                 */
                createPhenotype(205, 753, 17),
                createPhenotype(205, 753, 9),
                createPhenotype(294, 669, 10),
                createPhenotype(294, 669, 15),
                createPhenotype(294, 669, 8),
                createPhenotype(294, 669, 29),
                createPhenotype(290, 673, 15),
                createPhenotype(189, 768, 17),
                createPhenotype(133, 821, 19),
                createPhenotype(161, 795, 18)
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


    @Override
    public GameEvaluator createGameEvaluator(Class<? extends GameEvaluator> gameEvaluatorClass, Genotype<IntegerGene> genotype) {
        GenoDecoder decodedGenotype = decodeGenotype(genotype);

        try {
            return gameEvaluatorClass
                    .getDeclaredConstructor(Integer.class, Integer.class, Integer.class, Integer.class)
                    .newInstance(decodedGenotype.getFactor1(),
                                decodedGenotype.getFactor2(),
                                decodedGenotype.getFactor3(),
                                decodedGenotype.getFactor4());
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

    private static GenoDecoder decodeGenotype(Genotype<IntegerGene> genotype) {
        Chromosome<IntegerGene> chromo1 = genotype.chromosome();

        IntegerChromosome integerChromo = chromo1.as(IntegerChromosome.class);

        int[] array = integerChromo.toArray();

        return new GenoDecoder(array[0], array[1], array[2]);
    }

}