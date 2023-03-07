package net.chesstango.uci.arena;

import net.chesstango.uci.gui.EngineController;
import org.apache.commons.pool2.impl.GenericObjectPool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author Mauricio Coria
 */
public class Tournament {
    private final static int THREADS_NUMBER = 5;
    private final int depth;
    private final List<GenericObjectPool<EngineController>> pools;

    public Tournament(List<EngineControllerFactory> opponentsControllerFactories, int depth) {
        this.pools = opponentsControllerFactories.stream().map(GenericObjectPool::new).collect(Collectors.toList());
        this.depth = depth;
    }

    public List<GameResult> play(List<String> fenList) {
        List<MatchScheduler> schedulers = new ArrayList<>();

        GenericObjectPool<EngineController> mainPool = pools.get(0);

        ExecutorService executor = Executors.newFixedThreadPool(THREADS_NUMBER);

        for (GenericObjectPool<EngineController> pool : pools) {
            if(pool != mainPool) {
                MatchScheduler scheduler = new MatchScheduler(mainPool, pool, depth);

                schedulers.add(scheduler);

                scheduler.enqueue(executor, fenList);
            }
        }
        executor.shutdown();
        try {
            while (executor.awaitTermination(1, TimeUnit.SECONDS) == false) ;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        for (GenericObjectPool<EngineController> pool : pools) {
            pool.close();
        }

        List<GameResult> result = new ArrayList<>();
        schedulers.forEach(scheduler -> result.addAll(scheduler.getMatchResults()));
        return result;
    }

}
