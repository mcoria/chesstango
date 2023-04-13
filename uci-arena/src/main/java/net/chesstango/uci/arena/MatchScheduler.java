package net.chesstango.uci.arena;

import net.chesstango.uci.gui.EngineController;
import org.apache.commons.pool2.impl.GenericObjectPool;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author Mauricio Coria
 */
public class MatchScheduler {
    private static final int BATCH_SIZE = 10;
    private final int depth;
    private final GenericObjectPool<EngineController> pool1;
    private final GenericObjectPool<EngineController> pool2;
    private final MatchListener matchListener;

    public MatchScheduler(GenericObjectPool<EngineController> pool1, GenericObjectPool<EngineController> pool2, int depth, MatchListener matchListener) {
        this.pool1 = pool1;
        this.pool2 = pool2;
        this.depth = depth;
        this.matchListener = matchListener;
    }

    public void enqueue(ExecutorService executor, List<String> fenList) {
        final AtomicInteger counter = new AtomicInteger();

        final Collection<List<String>> batches = fenList
                .stream()
                .collect(Collectors.groupingBy(it -> counter.getAndIncrement() / BATCH_SIZE))
                .values();

        batches.forEach(batch -> executor.submit( () -> play(batch) ));
    }


    private void play(List<String> fenList) {
        EngineController controller1 = null;
        EngineController controller2 = null;

        try {
            controller1 = getControllerFromPool(pool1);
            controller2 = getControllerFromPool(pool2);

            Match match = new Match(controller1, controller2, depth);
            match.setMatchListener(matchListener);

            match.play(fenList);

            pool1.returnObject(controller1);
            pool2.returnObject(controller2);

        } catch (RuntimeException e) {
            e.printStackTrace(System.err);

            invalidateObject(controller1, pool1);
            invalidateObject(controller2, pool2);

            throw e;
        }
    }

    private EngineController getControllerFromPool(GenericObjectPool<EngineController> pool) {
        try {
            return pool.borrowObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void invalidateObject(EngineController controller, GenericObjectPool<EngineController> pool) {
        if (controller != null && pool != null) {
            try {
                pool.invalidateObject(controller);
            } catch (Exception e) {
                e.printStackTrace(System.err);
            }
        }
    }

}
