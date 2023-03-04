package net.chesstango.uci.arena;

import net.chesstango.uci.gui.EngineController;
import net.chesstango.uci.proxy.EngineProxy;
import org.apache.commons.pool2.impl.GenericObjectPool;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class Tournament {
    private final EngineControllerProxyFactory controllerFactory;
    private final List<EngineController> engineControllerList;

    public Tournament(EngineControllerProxyFactory controllerFactory, List<EngineController> engineControllerList) {
        this.controllerFactory = controllerFactory;
        this.engineControllerList = engineControllerList;
    }

    public List<GameResult> play(List<String> fenList) {
        GenericObjectPool<EngineController> pool = new GenericObjectPool<>(controllerFactory);
        List<GameResult> matchResults = new ArrayList<>();
        for (EngineController engineController : engineControllerList) {
            try {
                EngineController primaryEngine = pool.borrowObject();
                Match match = new Match(primaryEngine, engineController, 1);
                matchResults.addAll(match.play(fenList));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        pool.close();
        return matchResults;
    }

}
