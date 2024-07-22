package net.chesstango.tools.tuning;

import net.chesstango.tools.tuning.factories.GameEvaluatorFactory;
import net.chesstango.tools.tuning.fitnessfunctions.FitnessFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author Mauricio Coria
 */
public abstract class EvalTuningAbstract {
    private static final Logger logger = LoggerFactory.getLogger(EvalTuningAbstract.class);

    protected final FitnessFunction fitnessFn;
    protected final Map<String, Long> fitnessMemory;

    protected EvalTuningAbstract(FitnessFunction fitnessFn) {
        this.fitnessFn = fitnessFn;
        this.fitnessMemory = Collections.synchronizedMap(new HashMap<>());
    }

    public abstract void doWork();

    public abstract void endWork();

    protected long fitness(GameEvaluatorFactory gameEvaluatorFactory) {
        String evaluatorKey = gameEvaluatorFactory.getKey();

        logger.info("Computing Fitness {} ", evaluatorKey);

        Long points = fitnessMemory.get(evaluatorKey);

        if (points == null) {

            points = fitnessFn.fitness(gameEvaluatorFactory::createGameEvaluator);

            String dump = gameEvaluatorFactory.getRepresentation();

            logger.info("{} - {}", evaluatorKey, dump);

            fitnessMemory.put(evaluatorKey, points);
        } else {
            logger.info("Fitness {} in memory", evaluatorKey);
        }

        logger.info("Fitness {} ; points = [{}]", evaluatorKey, points);

        return points;
    }


    protected void dumpMemory(int maxElements) {
        logger.info("Memory size = {}", fitnessMemory.size());

        Set<Map.Entry<String, Long>> entrySet = fitnessMemory.entrySet();

        List<Map.Entry<String, Long>> entryList = entrySet
                .stream()
                .sorted(Collections.reverseOrder(Comparator.comparingLong(Map.Entry::getValue)))
                .toList();

        entryList.stream()
                .limit(maxElements)
                .forEach(entry -> logger.info("key = [{}]; value=[{}]", entry.getKey(), entry.getValue()));
    }

    protected void installShutdownHook(boolean interruptBeforeJoin) {
        final Thread mainThread = Thread.currentThread();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Shutting down....");

            endWork();

            if (interruptBeforeJoin) {
                mainThread.interrupt();
            }
            try {
                mainThread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }));
    }
}
