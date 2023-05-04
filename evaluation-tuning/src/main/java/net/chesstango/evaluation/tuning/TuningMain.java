package net.chesstango.evaluation.tuning;

import io.jenetics.EliteSelector;
import io.jenetics.Genotype;
import io.jenetics.IntegerGene;
import io.jenetics.Phenotype;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionResult;
import io.jenetics.engine.EvolutionStart;
import net.chesstango.evaluation.imp.GameEvaluatorSEandImp02;
import net.chesstango.evaluation.tuning.fitnessfunctions.FitnessBySearch;
import net.chesstango.evaluation.tuning.fitnessfunctions.FitnessFunction;
import net.chesstango.evaluation.tuning.geneticproviders.GeneticProvider;
import net.chesstango.evaluation.tuning.geneticproviders.GeneticProvider4FactorsGenes;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * @author Mauricio Coria
 */
public class TuningMain {
    private static final int POPULATION_SIZE = 10;
    private static final int GENERATION_LIMIT = 50;

    public static void main(String[] args) {
        GeneticProvider geneticProvider =  new GeneticProvider4FactorsGenes(GameEvaluatorSEandImp02.class);

        //FitnessFunction fitnessFunction = new FitnessByMatch(geneticProvider::createGameEvaluator);
        FitnessFunction fitnessFunction = new FitnessBySearch(geneticProvider::createGameEvaluator);

        TuningMain main = new TuningMain(fitnessFunction, geneticProvider);

        main.findGenotype();
    }

    private final GeneticProvider geneticProvider;
    private final FitnessFunction fitnessFn;
    private final Map<String, Long> fitnessMemory;

    public TuningMain(FitnessFunction fitnessFn, GeneticProvider geneticProvider) {
        this.geneticProvider = geneticProvider;
        this.fitnessFn = fitnessFn;
        this.fitnessMemory = new HashMap<>();
    }

    private void findGenotype() {
        ExecutorService executor = Executors.newFixedThreadPool(5);
        fitnessFn.start();

        Engine<IntegerGene, Long> engine = Engine.builder(this::fitness, geneticProvider.getGenotypeFactory())
                .selector(new EliteSelector<>(5))
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
        System.out.println("Y su genotipo = " + result.genotype());

        Set<Map.Entry<String, Long>> entrySet = fitnessMemory.entrySet();
        List<Map.Entry<String, Long>> entryList = entrySet.stream().collect(Collectors.toList());
        Collections.sort(entryList, Comparator.comparing(Map.Entry::getValue));
        entryList.stream().forEach(entry -> {
            System.out.println("key = [" + entry.getKey() + "]; value=[" + entry.getValue() + "]");
        });

        fitnessFn.stop();
        executor.shutdown();
    }

    private long fitness(Genotype<IntegerGene> genotype) {
        String keyGenes = geneticProvider.getKeyGenesString(genotype);

        Long points = fitnessMemory.get(keyGenes);

        if (points == null) {

            points = fitnessFn.fitness(genotype);

            geneticProvider.printGeneAndPoints(genotype, points);

            fitnessMemory.put(keyGenes, points);
        }

        return points;
    }

}
