package net.chesstango.evaluation;

import net.chesstango.board.Game;

/**
 * No se observan ganancias significativas cuando TT está habilitado y existe riesgo de colision
 *
 * @author Mauricio Coria
 */
public class GameEvaluatorCache implements GameEvaluator {

    private final GameEvaluator imp;
    private final GameEvaluatorCacheEntry[] cache;

    private long statisticCounter = 0;

    private static final int ARRAY_SIZE = 1024 * 512;

    public GameEvaluatorCache(GameEvaluator imp) {
        this.imp = imp;
        this.cache = new GameEvaluatorCacheEntry[ARRAY_SIZE];
        for (int i = 0; i < ARRAY_SIZE; i++) {
            this.cache[i] = new GameEvaluatorCacheEntry();
        }
    }

    @Override
    public int evaluate(Game game) {
        long hash = game.getChessPosition().getZobristHash();

        int idx = (int) Math.abs(hash % ARRAY_SIZE);

        GameEvaluatorCacheEntry entry = cache[idx];

        if (entry.hash != hash) {
            statisticCounter++;
            entry.hash = hash;
            entry.evaluation = imp.evaluate(game);
        }

        return entry.evaluation;
    }

    public void resetStatisticCounter() {
        statisticCounter = 0;
    }

    public long getStatisticCounter() {
        return statisticCounter;
    }

    private static class GameEvaluatorCacheEntry {
        long hash;
        int evaluation;
    }
}
