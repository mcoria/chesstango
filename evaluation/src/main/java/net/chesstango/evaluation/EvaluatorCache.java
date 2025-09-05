package net.chesstango.evaluation;

import lombok.Getter;
import net.chesstango.board.Game;

/**
 * No se observan ganancias significativas cuando TT está habilitado y existe riesgo de colision
 *
 * @author Mauricio Coria
 */
public class EvaluatorCache implements Evaluator, EvaluatorCacheRead {

    private static final int ARRAY_SIZE = 1024 * 512;

    @Getter
    private final Evaluator imp;

    private final GameEvaluatorCacheEntry[] cache;

    @Getter
    private long cacheHitsCounter = 0;

    private Game game;


    public EvaluatorCache(Evaluator imp) {
        this.imp = imp;
        this.cache = new GameEvaluatorCacheEntry[ARRAY_SIZE];
        for (int i = 0; i < ARRAY_SIZE; i++) {
            this.cache[i] = new GameEvaluatorCacheEntry();
        }
    }

    @Override
    public void setGame(Game game) {
        this.game = game;
        imp.setGame(game);
    }

    @Override
    public int evaluate() {
        long hash = game.getPosition().getZobristHash();

        int idx = (int) Math.abs(hash % ARRAY_SIZE);

        GameEvaluatorCacheEntry entry = cache[idx];

        if (entry.hash != hash) {
            entry.hash = hash;
            entry.evaluation = imp.evaluate();
        } else {
            cacheHitsCounter++;
        }

        return entry.evaluation;
    }

    @Override
    public Integer readFromCache(long hash) {
        int idx = (int) Math.abs(hash % ARRAY_SIZE);

        GameEvaluatorCacheEntry entry = cache[idx];

        return entry.hash == hash ? entry.evaluation : null;
    }

    public void resetCacheHitsCounter() {
        cacheHitsCounter = 0;
    }

    private static class GameEvaluatorCacheEntry {
        long hash;
        int evaluation;
    }
}
