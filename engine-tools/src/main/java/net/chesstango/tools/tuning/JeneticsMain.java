package net.chesstango.tools.tuning;

import io.jenetics.*;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionResult;
import io.jenetics.engine.EvolutionStart;
import net.chesstango.tools.tuning.factories.GameEvaluatorFactory;
import net.chesstango.tools.tuning.fitnessfunctions.FitnessByEpdBestMove;
import net.chesstango.tools.tuning.fitnessfunctions.FitnessFunction;
import net.chesstango.tools.tuning.geneticproviders.GPEvaluatorByMaterial;
import net.chesstango.tools.tuning.geneticproviders.GeneticProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executor;

/**
 * @author Mauricio Coria
 */
public class JeneticsMain extends EvalTuningAbstract {
    private static final Logger logger = LoggerFactory.getLogger(JeneticsMain.class);
    private static final int POPULATION_SIZE = 20;
    private static final int GENERATION_LIMIT = 200;
    private volatile boolean stopped;

    public static void main(String[] args) {
        //GeneticProvider geneticProvider = new GeneticProvider2FactorsGenes();
        GeneticProvider geneticProvider = new GPEvaluatorByMaterial();
        //GeneticProvider geneticProvider = new GeneticProviderNIntChromosomes(10);

        FitnessFunction fitnessFunction = new FitnessByEpdBestMove();

        JeneticsMain main = new JeneticsMain(fitnessFunction, geneticProvider);

        main.installShutdownHook(false);

        main.doWork();
    }

    private final GeneticProvider geneticProvider;

    public JeneticsMain(FitnessFunction fitnessFn, GeneticProvider geneticProvider) {
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
                .offspringFraction(0.1)
                .survivorsSelector(new EliteSelector<>())
                .offspringSelector(new TournamentSelector<>())
                //.constraint(geneticProvider.getPhenotypeConstraint())
                .populationSize(POPULATION_SIZE)
                .build();

        EvolutionStart<IntegerGene, Long> start = geneticProvider.getEvolutionStart(POPULATION_SIZE);

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

    @Override
    public void endWork() {
        this.stopped = true;
    }


    private long fitness(Genotype<IntegerGene> genotype) {
        GameEvaluatorFactory gameEvaluatorFactory = geneticProvider.createGameEvaluatorFactors(genotype);
        return fitness(gameEvaluatorFactory);
    }

    private void report(EvolutionResult<IntegerGene, Long> evolutionResult) {
        double avg = evolutionResult.population().stream().map(Phenotype::fitness).mapToDouble(Long::doubleValue).average().orElse(0);
        Long bestFitness = evolutionResult.bestFitness();
        logger.info("TotalGenerations = {}; BestFitness = {}, Avg = {}", evolutionResult.totalGenerations(), bestFitness, avg);
    }
}
