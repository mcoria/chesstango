package net.chesstango.uci.arena;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.chesstango.uci.arena.gui.EngineController;
import net.chesstango.uci.protocol.requests.CmdGo;
import org.apache.commons.pool2.impl.GenericObjectPool;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Mauricio Coria
 */
public class MatchScheduler {
    private static final Logger logger = LoggerFactory.getLogger(MatchScheduler.class);
    private final CmdGo cmdGo;
    private final GenericObjectPool<EngineController> pool1;
    private final GenericObjectPool<EngineController> pool2;
    private final MatchListener matchListener;

    public MatchScheduler(GenericObjectPool<EngineController> pool1,
                          GenericObjectPool<EngineController> pool2,
                          CmdGo cmdGo,
                          MatchListener matchListener) {
        this.pool1 = pool1;
        this.pool2 = pool2;
        this.cmdGo = cmdGo;
        this.matchListener = matchListener;
    }

    public List<Runnable> createTasks(List<String> fenList) {
        return fenList.stream().map( fen -> (Runnable) () -> play(fen)).collect(Collectors.toList());
    }


    private void play(String fen) {
        EngineController controller1 = null;
        EngineController controller2 = null;

        try {
            controller1 = getControllerFromPool(pool1);
            controller2 = getControllerFromPool(pool2);

            Match match = new Match(controller1, controller2, cmdGo);
            match.setMatchListener(matchListener);

            match.play(fen);

            pool1.returnObject(controller1);
            pool2.returnObject(controller2);

        } catch (RuntimeException e) {
            logger.error("Error playing", e);

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
                logger.error("invalidateObject error", e);
            }
        }
    }

}
