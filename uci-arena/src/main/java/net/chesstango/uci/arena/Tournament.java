package net.chesstango.uci.arena;

import net.chesstango.uci.gui.EngineController;
import org.apache.commons.pool2.impl.GenericObjectPool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author Mauricio Coria
 */
public class Tournament {
    private final List<EngineController> engineControllerList;
    private final GenericObjectPool<EngineController> pool;


    public Tournament(EngineControllerFactory controllerFactory, List<EngineController> engineControllerList) {
        this.engineControllerList = engineControllerList;
        this.pool = new GenericObjectPool<>(controllerFactory);
    }

    public List<GameResult> play(List<String> fenList) {
        ExecutorService executor = Executors.newFixedThreadPool(4);

        List<GameResult> matchResults = Collections.synchronizedList(new ArrayList<>());

        for (EngineController engineController : engineControllerList) {
            executor.submit(() -> matchResults.addAll(play(fenList, engineController)));
        }

        executor.shutdown();

        try {
            while (executor.awaitTermination(1, TimeUnit.SECONDS) == false) ;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        pool.close();

        return matchResults;
    }

    private List<GameResult> play(List<String> fenList, EngineController engineController) {
        EngineController primaryEngine = null;
        try {
            primaryEngine = pool.borrowObject();
        } catch (Exception e) {
            e.printStackTrace(System.err);
            throw new RuntimeException(e);
        }

        Match match = new Match(primaryEngine, engineController, 1);

        List<GameResult> thisMatchResults = match.play(fenList);

        pool.returnObject(primaryEngine);

        return thisMatchResults;
    }

}
