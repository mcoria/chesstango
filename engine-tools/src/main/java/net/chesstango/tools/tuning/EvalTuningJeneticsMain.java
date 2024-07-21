package net.chesstango.tools.tuning;

import io.jenetics.*;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionResult;
import net.chesstango.tools.tuning.factories.GameEvaluatorFactory;
import net.chesstango.tools.tuning.fitnessfunctions.FitnessByEpdSearch;
import net.chesstango.tools.tuning.fitnessfunctions.FitnessFunction;
import net.chesstango.tools.tuning.geneticproviders.GPEvaluatorImp06;
import net.chesstango.tools.tuning.geneticproviders.GeneticProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executor;

/**
 * @author Mauricio Coria
 */
public class EvalTuningJeneticsMain extends EvalTuningAbstract {
    private static final Logger logger = LoggerFactory.getLogger(EvalTuningJeneticsMain.class);
    private static final int POPULATION_SIZE = 10;
    private static final int GENERATION_LIMIT = 20;
    private volatile boolean stopped;

    public static void main(String[] args) {
        //GeneticProvider geneticProvider = new GeneticProvider2FactorsGenes();
        GeneticProvider geneticProvider = new GPEvaluatorImp06();
        //GeneticProvider geneticProvider = new GeneticProviderNIntChromosomes(10);

        //FitnessFunction fitnessFunction = new FitnessByMatch();
        FitnessFunction fitnessFunction = new FitnessByEpdSearch();
        //FitnessFunction fitnessFunction = new FitnessByLeastSquare();

        EvalTuningJeneticsMain main = new EvalTuningJeneticsMain(fitnessFunction, geneticProvider);

        main.installShutdownHook(false);

        main.doWork();
    }

    private final GeneticProvider geneticProvider;

    public EvalTuningJeneticsMain(FitnessFunction fitnessFn, GeneticProvider geneticProvider) {
        super(fitnessFn);
        this.geneticProvider = geneticProvider;
    }

    @Override
    public void doWork() {
        fitnessFn.start();

        Executor executor = Runnable::run;

        Engine<IntegerGene, Long> engine = Engine
                .builder(this::fitness, geneticProvider.getGenotypeFactory())
                .executor(executor)
                .offspringFraction(0.8)
                .survivorsSelector(new EliteSelector<>())
                .offspringSelector(new TournamentSelector<>())
                //.constraint(geneticProvider.getPhenotypeConstraint())
                .populationSize(POPULATION_SIZE)
                .build();

        //EvolutionStart<IntegerGene, Long> start = geneticProvider.getEvolutionStart(POPULATION_SIZE);

        EvolutionResult<IntegerGene, Long> result = engine
                //.stream(start)
                .stream()
                .limit(evolutionResult -> !stopped)
                .limit(GENERATION_LIMIT)
                .peek(this::report)
                .collect(EvolutionResult.toBestEvolutionResult());

        if (result != null) {
            logger.info("El mejor fitness encontrado = {}", result.bestFitness());

            Phenotype<IntegerGene, Long> bestPhenotype = result.bestPhenotype();

            logger.info("Y su genotipo = {}", geneticProvider.createGameEvaluatorFactors(bestPhenotype.genotype()).getKey());
        }

        dumpMemory(POPULATION_SIZE);

        fitnessFn.stop();
    }

    private void report(EvolutionResult<IntegerGene, Long> evolutionResult) {
        logger.info("TotalGenerations = {}", evolutionResult.totalGenerations());
    }

    @Override
    public void endWork() {
        this.stopped = true;
    }


    private long fitness(Genotype<IntegerGene> genotype) {
        GameEvaluatorFactory gameEvaluatorFactory = geneticProvider.createGameEvaluatorFactors(genotype);
        return fitness(gameEvaluatorFactory);
    }
}
