package net.chesstango.uci.arena;

import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.uci.arena.gui.EngineController;
import net.chesstango.uci.arena.gui.EngineControllerPoolFactory;
import net.chesstango.uci.arena.listeners.MatchListener;
import net.chesstango.uci.arena.matchtypes.MatchType;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Mauricio Coria
 */
public class MatchMultiple {
    private static final Logger logger = LoggerFactory.getLogger(MatchMultiple.class);

    private final GenericObjectPool<EngineController> pool1;

    private final GenericObjectPool<EngineController> pool2;

    private final MatchType matchType;

    private final List<MatchResult> result = Collections.synchronizedList(new LinkedList<>());

    @Setter
    @Accessors(chain = true)
    private boolean debugEnabled;

    @Setter
    @Accessors(chain = true)
    private boolean switchChairs;

    @Setter
    @Accessors(chain = true)
    private MatchListener matchListener;


    public MatchMultiple(EngineControllerPoolFactory controllerFactory1, EngineControllerPoolFactory controllerFactory2, MatchType matchType) {
        this.pool1 = new GenericObjectPool<>(controllerFactory1);
        this.pool2 = new GenericObjectPool<>(controllerFactory2);
        this.matchType = matchType;
        this.switchChairs = true;
    }

    public List<MatchResult> play(List<String> fenList) {
        int availableCores = Runtime.getRuntime().availableProcessors();

        try (ExecutorService executor = Executors.newFixedThreadPool(availableCores)) {

            createPlayTasks(fenList, pool1, pool2)
                    .forEach(executor::submit);

            if (switchChairs) {
                createPlayTasks(fenList, pool2, pool1)
                        .forEach(executor::submit);
            }
        }

        pool1.close();
        pool2.close();

        return result;
    }

    private List<Runnable> createPlayTasks(List<String> fenList,
                                           GenericObjectPool<EngineController> thePool1,
                                           GenericObjectPool<EngineController> thePool2) {
        return fenList.stream()
                .map(fen -> (Runnable) () -> play(fen, thePool1, thePool2))
                .toList();
    }

    private void play(String fen,
                      GenericObjectPool<EngineController> thePool1,
                      GenericObjectPool<EngineController> thePool2) {

        EngineController controller1 = null;
        EngineController controller2 = null;

        try {
            controller1 = getControllerFromPool(thePool1);
            controller2 = getControllerFromPool(thePool2);

            Match match = new Match(controller1, controller2, matchType)
                    .setDebugEnabled(debugEnabled)
                    .setMatchListener(matchListener);

            match.setMatchListener(matchListener);

            result.add(match.play(fen));

            thePool1.returnObject(controller1);
            thePool2.returnObject(controller2);

        } catch (RuntimeException e) {
            logger.error("Error playing", e);

            invalidateObject(controller1, thePool1);
            invalidateObject(controller2, thePool2);
        }
    }

    private static EngineController getControllerFromPool(GenericObjectPool<EngineController> pool) {
        try {
            return pool.borrowObject();
        } catch (Exception e) {
            logger.error("getControllerFromPool error", e);
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
