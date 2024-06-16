package net.chesstango.tools.tuning;

import net.chesstango.tools.tuning.factories.EvaluatorSEandImp02Factory;
import net.chesstango.tools.tuning.fitnessfunctions.FitnessByMatch;
import net.chesstango.tools.tuning.fitnessfunctions.FitnessFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import py4j.GatewayServer;

/**
 * Bayesian Optimization Evaluation Tuning
 *
 * @author Mauricio Coria
 */
public class EvalTuningBayesianOptimizationMain extends EvalTuningAbstract {
    private static final Logger logger = LoggerFactory.getLogger(EvalTuningBayesianOptimizationMain.class);

    public static void main(String[] args) {

        FitnessFunction fitnessFn = new FitnessByMatch();

        EvalTuningBayesianOptimizationMain app = new EvalTuningBayesianOptimizationMain(fitnessFn);

        Runtime.getRuntime().addShutdownHook(new ShutdownHook(app, Thread.currentThread()));

        app.doWork();
    }


    public EvalTuningBayesianOptimizationMain(FitnessFunction fitnessFn) {
        super(fitnessFn);
    }

    @Override
    public void doWork() {
        GatewayServer server = new GatewayServer(this);
        server.start();
        fitnessFn.start();

        try {
            synchronized (this) {
                wait(Long.MAX_VALUE);
            }
        } catch (InterruptedException e) {
            logger.info("main thread interrupted");
        }

        dumpMemory();

        fitnessFn.stop();
        server.shutdown();
    }

    public void endWork() {
        synchronized (this) {
            notify();
        }
    }

    public long fitness(Double scalar1Dbl, Double scalar2Dbl, Double scalar3Dbl) {
        int scalar1 = scalar1Dbl.intValue();
        int scalar2 = scalar2Dbl.intValue();
        int scalar3 = scalar3Dbl.intValue();
        return fitness(new EvaluatorSEandImp02Factory(scalar1, scalar2, scalar3));
    }


    private static class ShutdownHook extends Thread {

        private final EvalTuningBayesianOptimizationMain app;
        private final Thread mainThread;

        private ShutdownHook(EvalTuningBayesianOptimizationMain app, Thread mainThread) {
            this.app = app;
            this.mainThread = mainThread;
        }


        @Override
        public void run() {
            logger.info("Shutting down....");

            app.endWork();

            mainThread.interrupt();
            try {
                mainThread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
