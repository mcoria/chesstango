package net.chesstango.evaluation.tuning;

import io.jenetics.EliteSelector;
import io.jenetics.Genotype;
import io.jenetics.IntegerGene;
import io.jenetics.Phenotype;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionResult;
import io.jenetics.engine.EvolutionStart;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.evaluation.evaluators.EvaluatorSEandImp03;
import net.chesstango.evaluation.tuning.fitnessfunctions.FitnessBySearch;
import net.chesstango.evaluation.tuning.fitnessfunctions.FitnessFunction;
import net.chesstango.evaluation.tuning.geneticproviders.GeneticProvider;
import net.chesstango.evaluation.tuning.geneticproviders.GeneticProvider4FactorsGenes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * @author Mauricio Coria
 */
public class TuningMain {
    private static final Logger logger = LoggerFactory.getLogger(TuningMain.class);
    private static final int POPULATION_SIZE = 10;
    private static final int GENERATION_LIMIT = 100;

    public static void main(String[] args) {
        GeneticProvider geneticProvider = new GeneticProvider4FactorsGenes();
        //GeneticProvider geneticProvider = new GeneticProviderNIntChromosomes(10);

        //FitnessFunction fitnessFunction = new FitnessByMatch(geneticProvider::createGameEvaluator);
        FitnessFunction fitnessFunction = new FitnessBySearch();
        //FitnessFunction fitnessFunction = new FitnessByLeastSquare();

        TuningMain main = new TuningMain(fitnessFunction, geneticProvider, EvaluatorSEandImp03.class);

        main.findGenotype();
    }

    private final Class<? extends GameEvaluator> gameEvaluatorClass;
    private final GeneticProvider geneticProvider;
    private final FitnessFunction fitnessFn;
    private final Map<String, Long> fitnessMemory;

    public TuningMain(FitnessFunction fitnessFn, GeneticProvider geneticProvider, Class<? extends GameEvaluator> gameEvaluatorClass) {
        this.gameEvaluatorClass = gameEvaluatorClass;
        this.geneticProvider = geneticProvider;
        this.fitnessFn = fitnessFn;
        this.fitnessMemory = new HashMap<>();
    }

    private void findGenotype() {
        ExecutorService executor = Executors.newFixedThreadPool(5);
        fitnessFn.start();

        Engine<IntegerGene, Long> engine = Engine.builder(this::fitness, geneticProvider.getGenotypeFactory())
                .selector(new EliteSelector<>(10))
                //.constraint(geneticProvider.getPhenotypeConstraint())
                .populationSize(POPULATION_SIZE)
                .executor(executor)
                .build();

        EvolutionStart<IntegerGene, Long> start = geneticProvider.getEvolutionStart(POPULATION_SIZE);

        Phenotype<IntegerGene, Long> result = engine
                //.stream(start)
                .stream()
                .limit(GENERATION_LIMIT)
                .collect(EvolutionResult.toBestPhenotype());

        System.out.println("El mejor fenotipo encontrado = " + result.fitness());
        System.out.println("Y su genotipo = " + geneticProvider.genotypeToString(result.genotype()));


        Set<Map.Entry<String, Long>> entrySet = fitnessMemory.entrySet();
        List<Map.Entry<String, Long>> entryList = entrySet.stream().sorted(Collections.reverseOrder(Comparator.comparingLong(Map.Entry::getValue))).collect(Collectors.toList());

        entryList.stream().limit(20).forEach(entry -> {
            System.out.println("key = [" + entry.getKey() + "]; value=[" + entry.getValue() + "]");
        });


        fitnessFn.stop();
        executor.shutdown();
    }

    private long fitness(Genotype<IntegerGene> genotype) {
        String keyGenes = geneticProvider.getKeyGenesString(genotype);

        Long points = fitnessMemory.get(keyGenes);

        if (points == null) {

            GameEvaluator evaluator = geneticProvider.createGameEvaluator(gameEvaluatorClass, genotype);

            points = fitnessFn.fitness(evaluator);

            geneticProvider.genotypeToString(genotype);

            fitnessMemory.put(keyGenes, points);
        }

        logger.info("Evaluacion con {} ; puntos = [{}]", geneticProvider.genotypeToString(genotype), points);

        return points;
    }

}
