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
    private final MatchListener matchListener;

    public Tournament(List<EngineControllerPoolFactory> opponentsControllerFactories, int depth, MatchListener matchListener) {
        this.pools = opponentsControllerFactories.stream().map(GenericObjectPool::new).collect(Collectors.toList());
        this.depth = depth;
        this.matchListener = matchListener;
    }

    public void play(List<String> fenList) {
        GenericObjectPool<EngineController> mainPool = pools.get(0);

        ExecutorService executor = Executors.newFixedThreadPool(THREADS_NUMBER);

        List<Runnable> tasks = new ArrayList<>();

        for (GenericObjectPool<EngineController> pool : pools) {
            if(pool != mainPool) {
                MatchScheduler matchScheduler = new MatchScheduler(mainPool, pool, depth, matchListener);

                tasks.addAll(matchScheduler.createTasks(fenList));
            }
        }

        try {
            tasks.forEach(task -> executor.submit(task));

            executor.shutdown();

            while (executor.awaitTermination(1, TimeUnit.SECONDS) == false) ;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        for (GenericObjectPool<EngineController> pool : pools) {
            pool.close();
        }
    }

}
