package net.chesstango.tools.tuning;

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
public class BayesianOptimizationMain extends EvalTuningAbstract {
    private static final Logger logger = LoggerFactory.getLogger(BayesianOptimizationMain.class);

    public static void main(String[] args) {

        FitnessFunction fitnessFn = new FitnessByMatch();

        BayesianOptimizationMain main = new BayesianOptimizationMain(fitnessFn);

        main.installShutdownHook(true);

        main.doWork();
    }


    public BayesianOptimizationMain(FitnessFunction fitnessFn) {
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

        dumpMemory(20);

        fitnessFn.stop();
        server.shutdown();
    }

    @Override
    public void endWork() {
        synchronized (this) {
            notify();
        }
    }

    public long fitness(Double scalar1Dbl, Double scalar2Dbl, Double scalar3Dbl) {
        int scalar1 = scalar1Dbl.intValue();
        int scalar2 = scalar2Dbl.intValue();
        int scalar3 = scalar3Dbl.intValue();
        //return fitness(new EvaluatorImp0Factory(new int[]{scalar1, scalar2, scalar3}));
        return 0;
    }
}
