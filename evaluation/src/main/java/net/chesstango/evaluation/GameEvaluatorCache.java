package net.chesstango.evaluation;

import lombok.Getter;
import net.chesstango.board.Game;

/**
 * No se observan ganancias significativas cuando TT está habilitado y existe riesgo de colision
 *
 * @author Mauricio Coria
 */
public class GameEvaluatorCache implements GameEvaluator {

    private static final int ARRAY_SIZE = 1024 * 512;
    private final GameEvaluator imp;
    private final GameEvaluatorCacheEntry[] cache;

    @Getter
    private long cacheHitsCounter = 0;

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
            entry.hash = hash;
            entry.evaluation = imp.evaluate(game);
        } else {
            cacheHitsCounter++;
        }

        return entry.evaluation;
    }

    public void resetCacheHitsCounter() {
        cacheHitsCounter = 0;
    }

    private static class GameEvaluatorCacheEntry {
        long hash;
        int evaluation;
    }
}
