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
    private final GenericObjectPool<EngineController> pool;

    private final List<GenericObjectPool<EngineController>> opponentsPools;

    public Tournament(EngineControllerFactory mainControllerFactory, List<EngineControllerFactory> opponentsControllerFactories) {
        this.pool = new GenericObjectPool<>(mainControllerFactory);
        this.opponentsPools = opponentsControllerFactories.stream().map(GenericObjectPool::new).collect(Collectors.toList());
    }

    public List<GameResult> play(List<String> fenList) {
        ExecutorService executor = Executors.newFixedThreadPool(4);
        List<MatchScheduler> schedulers = new ArrayList<>();

        for (GenericObjectPool<EngineController> opponentsPool : opponentsPools) {
            MatchScheduler scheduler = new MatchScheduler(executor, pool, opponentsPool);
            schedulers.add(scheduler);

            scheduler.enqueue(fenList);
        }

        executor.shutdown();

        try {
            while (executor.awaitTermination(1, TimeUnit.SECONDS) == false) ;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        pool.close();
        for (GenericObjectPool<EngineController> opponentsPool : opponentsPools) {
            opponentsPool.close();
        }

        List<GameResult> result = new ArrayList<>();

        schedulers.forEach(scheduler -> result.addAll(scheduler.getMatchResults()));


        return result;
    }

}
