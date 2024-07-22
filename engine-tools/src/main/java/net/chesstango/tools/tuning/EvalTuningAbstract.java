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
    protected final Map<String, MemoryEntry> fitnessMemory;

    protected EvalTuningAbstract(FitnessFunction fitnessFn) {
        this.fitnessFn = fitnessFn;
        this.fitnessMemory = Collections.synchronizedMap(new HashMap<>());
    }

    public abstract void doWork();

    public abstract void endWork();

    protected long fitness(GameEvaluatorFactory gameEvaluatorFactory) {
        String evaluatorKey = gameEvaluatorFactory.getKey();

        logger.info("Computing Fitness {} ", evaluatorKey);

        MemoryEntry memoryEntry = fitnessMemory.get(evaluatorKey);

        Long points = null;

        if (memoryEntry == null) {

            points = fitnessFn.fitness(gameEvaluatorFactory::createGameEvaluator);

            String evaluatorRepresentation = gameEvaluatorFactory.getRepresentation();

            logger.info("{} - {}", evaluatorKey, evaluatorRepresentation);

            fitnessMemory.put(evaluatorKey, new MemoryEntry(points, evaluatorRepresentation));
        } else {

            points = memoryEntry.points;

            logger.info("Fitness {} in memory", evaluatorKey);
        }

        logger.info("Fitness {}; points = [{}]", evaluatorKey, points);

        return points;
    }


    private record MemoryEntry(Long points, String representation) {
    }


    protected void dumpMemory(int maxElements) {
        logger.info("Memory size = {}", fitnessMemory.size());

        List<Map.Entry<String, MemoryEntry>> entryList = fitnessMemory
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Comparator.comparingLong(o -> o.getValue().points)))
                .toList();


        entryList.stream()
                .limit(maxElements)
                .forEach(entry -> logger.info("key = [{}]; points=[{}]", entry.getKey(), entry.getValue().points));

        entryList.stream()
                .limit(maxElements)
                .forEach(entry -> System.out.printf("%s\n", entry.getValue().representation));
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
