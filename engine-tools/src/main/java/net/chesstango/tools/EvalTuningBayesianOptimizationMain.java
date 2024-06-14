package net.chesstango.tools;

import net.chesstango.evaluation.evaluators.EvaluatorSEandImp03;
import net.chesstango.tools.tuning.factories.GameEvaluatorFactory;
import net.chesstango.tools.tuning.factories.GameEvaluatorFactory4Factors;
import net.chesstango.tools.tuning.fitnessfunctions.FitnessByMatch;
import net.chesstango.tools.tuning.fitnessfunctions.FitnessFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import py4j.GatewayServer;

/**
 *
 * Bayesian Optimization Evaluation Tuning
 *
 * @author Mauricio Coria
 */
public class EvalTuningBayesianOptimizationMain {
    private static final Logger logger = LoggerFactory.getLogger(EvalTuningBayesianOptimizationMain.class);

    private final FitnessFunction fitnessFn;

    public EvalTuningBayesianOptimizationMain(FitnessFunction fitnessFn) {
        this.fitnessFn = fitnessFn;
    }

    public long fitness(Double scalar1Dbl, Double scalar2Dbl, Double scalar3Dbl) {
        int scalar1 = scalar1Dbl.intValue();
        int scalar2 = scalar2Dbl.intValue();
        int scalar3 = scalar3Dbl.intValue();

        GameEvaluatorFactory gameEvaluatorFactory = new GameEvaluatorFactory4Factors(scalar1, scalar2, scalar3);

        logger.info("Searching con {} ", gameEvaluatorFactory);

        long points = fitnessFn.fitness(() -> gameEvaluatorFactory.createGameEvaluator(EvaluatorSEandImp03.class));

        logger.info("Evaluacion con {} ; puntos = [{}]", gameEvaluatorFactory, points);

        return points;
    }

    public static void main(String[] args) {

        FitnessFunction fitnessFn = new FitnessByMatch();

        fitnessFn.start();

        EvalTuningBayesianOptimizationMain app = new EvalTuningBayesianOptimizationMain(fitnessFn);

        GatewayServer server = new GatewayServer(app);
        server.start();

        Runtime.getRuntime().addShutdownHook(new ShutdownHook(Thread.currentThread()));

        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
            logger.info("main thread interrupted");
        }

        fitnessFn.stop();
    }

    private static class ShutdownHook extends Thread {

        private final Thread mainThread;

        private ShutdownHook(Thread mainThread) {
            this.mainThread = mainThread;
        }


        @Override
        public void run() {
            System.out.println("Shutting down....");
            mainThread.interrupt();
            try {
                mainThread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
