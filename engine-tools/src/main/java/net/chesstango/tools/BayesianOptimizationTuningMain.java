package net.chesstango.tools;

import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.evaluation.evaluators.EvaluatorSEandImp02;
import net.chesstango.tools.tuning.fitnessfunctions.FitnessByEpdSearch;
import net.chesstango.tools.tuning.fitnessfunctions.FitnessFunction;
import py4j.GatewayServer;

import java.util.function.Supplier;

/**
 * @author Mauricio Coria
 */
public class BayesianOptimizationTuningMain {
    private final FitnessFunction fitnessFn;

    public BayesianOptimizationTuningMain(FitnessFunction fitnessFn) {
        this.fitnessFn = fitnessFn;
    }

    public long fitness(int material, int position, int expansion, int ataque) {
        Supplier<GameEvaluator> gameEvaluatorSupplier = () -> new EvaluatorSEandImp02(material, position, expansion, ataque);
        return fitnessFn.fitness(gameEvaluatorSupplier);
    }

    public static void main(String[] args) {
        FitnessFunction fitnessFn = new FitnessByEpdSearch();
        BayesianOptimizationTuningMain app = new BayesianOptimizationTuningMain(fitnessFn);

        fitnessFn.start();

        GatewayServer server = new GatewayServer(app);
        server.start();

        fitnessFn.stop();
    }
}
