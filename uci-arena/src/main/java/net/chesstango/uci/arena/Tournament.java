package net.chesstango.uci.arena;

import net.chesstango.uci.arena.gui.EngineController;
import net.chesstango.uci.arena.gui.EngineControllerPoolFactory;
import net.chesstango.uci.arena.listeners.MatchListener;
import net.chesstango.uci.arena.matchtypes.MatchType;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author Mauricio Coria
 */
public class Tournament {
    private static final Logger logger = LoggerFactory.getLogger(Tournament.class);
    private final static int THREADS_NUMBER = 5;
    private final MatchType matchType;
    private final List<GenericObjectPool<EngineController>> controllersPools;
    private final MatchListener matchListener;
    private final GenericObjectPool<EngineController> mainPool;

    public Tournament(List<EngineControllerPoolFactory> controllerFactories, MatchType matchType, MatchListener matchListener) {
        this.controllersPools = controllerFactories.stream()
                .map(GenericObjectPool::new)
                .toList();
        this.matchType = matchType;
        this.matchListener = matchListener;
        this.mainPool = controllersPools.get(0);
    }

    public void play(List<String> fenList) {
        ExecutorService executor = Executors.newFixedThreadPool(THREADS_NUMBER);

        try {

            for (GenericObjectPool<EngineController> pool : controllersPools) {
                if (pool != mainPool) {
                    createPlayTasks(fenList, pool).forEach(executor::submit);
                }
            }

            executor.shutdown();

            while (!executor.awaitTermination(1, TimeUnit.SECONDS)) ;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        for (GenericObjectPool<EngineController> pool : controllersPools) {
            pool.close();
        }
    }


    public List<Runnable> createPlayTasks(List<String> fenList,
                                          GenericObjectPool<EngineController> pool) {
        return fenList.stream()
                .map(fen -> (Runnable) () -> play(fen, pool))
                .toList();
    }


    private void play(String fen,
                      GenericObjectPool<EngineController> pool) {
        EngineController controller1 = null;
        EngineController controller2 = null;

        try {
            controller1 = getControllerFromPool(mainPool);
            controller2 = getControllerFromPool(pool);

            Match match = new Match(controller1, controller2, matchType);

            match.setMatchListener(matchListener);

            match.play(fen);

            mainPool.returnObject(controller1);
            pool.returnObject(controller2);

        } catch (RuntimeException e) {
            logger.error("Error playing", e);

            invalidateObject(controller1, mainPool);
            invalidateObject(controller2, pool);

            throw e;
        }
    }

    private static EngineController getControllerFromPool(GenericObjectPool<EngineController> pool) {
        try {
            return pool.borrowObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void invalidateObject(EngineController controller, GenericObjectPool<EngineController> pool) {
        if (controller != null && pool != null) {
            try {
                pool.invalidateObject(controller);
            } catch (Exception e) {
                logger.error("invalidateObject error", e);
            }
        }
    }
}
