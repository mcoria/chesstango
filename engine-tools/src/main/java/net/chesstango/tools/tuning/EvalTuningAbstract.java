package net.chesstango.tools.tuning;

import net.chesstango.evaluation.GameEvaluator;
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

    protected final Class<? extends GameEvaluator> gameEvaluatorClass;
    protected final FitnessFunction fitnessFn;
    protected final Map<String, Long> fitnessMemory;

    protected EvalTuningAbstract(FitnessFunction fitnessFn, Class<? extends GameEvaluator> gameEvaluatorClass) {
        this.fitnessFn = fitnessFn;
        this.fitnessMemory = Collections.synchronizedMap(new HashMap<>());
        this.gameEvaluatorClass = gameEvaluatorClass;
    }

    public abstract void doWork();

    protected long fitness(GameEvaluatorFactory gameEvaluatorFactory) {
        String keyGenes = gameEvaluatorFactory.getKey();

        logger.info("Searching {} ", gameEvaluatorFactory);

        Long points = fitnessMemory.get(keyGenes);

        if (points == null) {

            points = fitnessFn.fitness(() -> gameEvaluatorFactory.createGameEvaluator(gameEvaluatorClass));

            fitnessMemory.put(keyGenes, points);
        } else {
            logger.info("Fitness {} in memory", gameEvaluatorFactory);
        }

        logger.info("Fitness {} ; points = [{}]", gameEvaluatorFactory, points);

        return points;
    }


    protected void dumpMemory() {
        Set<Map.Entry<String, Long>> entrySet = fitnessMemory.entrySet();
        List<Map.Entry<String, Long>> entryList = entrySet.stream()
                .sorted(Collections.reverseOrder(Comparator.comparingLong(Map.Entry::getValue)))
                .toList();

        entryList.stream().limit(20).forEach(entry -> {
            System.out.println("key = [" + entry.getKey() + "]; value=[" + entry.getValue() + "]");
        });
    }
}
