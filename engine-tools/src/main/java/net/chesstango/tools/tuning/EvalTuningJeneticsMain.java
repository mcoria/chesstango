package net.chesstango.tools.tuning;

import io.jenetics.EliteSelector;
import io.jenetics.Genotype;
import io.jenetics.IntegerGene;
import io.jenetics.Phenotype;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionResult;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.evaluation.evaluators.EvaluatorSEandImp03;
import net.chesstango.tools.tuning.fitnessfunctions.FitnessByMatch;
import net.chesstango.tools.tuning.fitnessfunctions.FitnessFunction;
import net.chesstango.tools.tuning.geneticproviders.GeneticProvider;
import net.chesstango.tools.tuning.geneticproviders.GeneticProvider4Factors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Mauricio Coria
 */
public class EvalTuningJeneticsMain extends EvalTuningAbstract {
    private static final Logger logger = LoggerFactory.getLogger(EvalTuningJeneticsMain.class);
    private static final int POPULATION_SIZE = 5;
    private static final int GENERATION_LIMIT = 5;

    public static void main(String[] args) {
        //GeneticProvider geneticProvider = new GeneticProvider2FactorsGenes();
        GeneticProvider geneticProvider = new GeneticProvider4Factors();
        //GeneticProvider geneticProvider = new GeneticProviderNIntChromosomes(10);

        FitnessFunction fitnessFunction = new FitnessByMatch();
        //FitnessFunction fitnessFunction = new FitnessByEpdSearch();
        //FitnessFunction fitnessFunction = new FitnessByLeastSquare();

        EvalTuningJeneticsMain main = new EvalTuningJeneticsMain(fitnessFunction, geneticProvider, EvaluatorSEandImp03.class);

        main.doWork();
    }

    private final GeneticProvider geneticProvider;

    public EvalTuningJeneticsMain(FitnessFunction fitnessFn, GeneticProvider geneticProvider, Class<? extends GameEvaluator> gameEvaluatorClass) {
        super(fitnessFn, gameEvaluatorClass);
        this.geneticProvider = geneticProvider;
    }

    @Override
    public void doWork() {
        ExecutorService executor = Executors.newFixedThreadPool(5);
        fitnessFn.start();

        Engine<IntegerGene, Long> engine = Engine.builder(this::fitness, geneticProvider.getGenotypeFactory())
                .selector(new EliteSelector<>(POPULATION_SIZE / 5))
                //.constraint(geneticProvider.getPhenotypeConstraint())
                .populationSize(POPULATION_SIZE)
                .executor(executor)
                .build();

        //EvolutionStart<IntegerGene, Long> start = geneticProvider.getEvolutionStart(POPULATION_SIZE);

        Phenotype<IntegerGene, Long> result = engine
                //.stream(start)
                .stream()
                .limit(GENERATION_LIMIT)
                .collect(EvolutionResult.toBestPhenotype());

        System.out.println("El mejor fenotipo encontrado = " + result.fitness());
        System.out.println("Y su genotipo = " + geneticProvider.createGameEvaluatorFactors(result.genotype()));

        dumpMemory();


        fitnessFn.stop();
        executor.shutdown();
    }


    private long fitness(Genotype<IntegerGene> genotype) {
        return fitness(geneticProvider.createGameEvaluatorFactors(genotype));
    }
}
