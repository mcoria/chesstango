package net.chesstango.evaluation.tuning.geneticproviders;

import io.jenetics.*;
import io.jenetics.engine.EvolutionStart;
import io.jenetics.util.Factory;
import io.jenetics.util.ISeq;
import io.jenetics.util.IntRange;
import net.chesstango.evaluation.GameEvaluator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class GeneticProviderNIntChromosomes implements GeneticProvider {

    private static final Logger logger = LoggerFactory.getLogger(GeneticProviderNIntChromosomes.class);

    private static final int CONSTRAINT_MAX_VALUE = 1000;
    private static final IntRange geneRange = IntRange.of(0, CONSTRAINT_MAX_VALUE);

    private final int intChromosomes;

    public GeneticProviderNIntChromosomes(int intChromosomes) {
        this.intChromosomes = intChromosomes;
    }

    @Override
    public Factory<Genotype<IntegerGene>> getGenotypeFactory() {
        return Genotype.of(IntegerChromosome.of(geneRange, intChromosomes));
    }

    @Override
    public String getKeyGenesString(Genotype<IntegerGene> genotype) {
        Chromosome<IntegerGene> chromo1 = genotype.chromosome();

        IntegerChromosome integerChromo = chromo1.as(IntegerChromosome.class);

        int[] array = integerChromo.toArray();

        StringBuilder stringBuilder = new StringBuilder();

        for (int factor : integerChromo.toArray()) {
            stringBuilder.append(String.format("%d|", factor));
        }

        return stringBuilder.toString();
    }

    @Override
    public GameEvaluator createGameEvaluator(Class<? extends GameEvaluator> gameEvaluatorClass, Genotype<IntegerGene> genotype) {
        return null;
    }

    @Override
    public String genotypeToString(Genotype<IntegerGene> genotype) {
        //logger.info(String.format("Evaluacion con %s ; puntos = [%d]", getKeyGenesString(genotype), points));
        return "";
    }

    @Override
    public EvolutionStart<IntegerGene, Long> getEvolutionStart(int populationSize) {
        List<Phenotype<IntegerGene, Long>> phenoList = Arrays.asList(
                /*
                key = [83|826|70|21]; value=[-1149]
                key = [272|658|31|39]; value=[-1147]
                key = [290|639|31|40]; value=[-1143]
                key = [185|734|65|16]; value=[-1135]
                key = [185|779|23|13]; value=[-1134]
                key = [161|701|110|28]; value=[-1133]
                key = [185|734|36|45]; value=[-1123]
                key = [213|688|56|43]; value=[-1106]
                key = [185|734|31|50]; value=[-1100]
                key = [185|734|45|36]; value=[-1093]
                createPhenotype(415, 585),
                 */
        );

        ISeq<Phenotype<IntegerGene, Long>> population = ISeq.of(phenoList);

        return EvolutionStart.of(population, 1);
    }

}