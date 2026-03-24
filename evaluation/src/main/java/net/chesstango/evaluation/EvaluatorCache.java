package net.chesstango.evaluation;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;

/**
 * No se observan ganancias significativas cuando TT está habilitado y existe riesgo de colision
 *
 * @author Mauricio Coria
 */
public class EvaluatorCache implements Evaluator, EvaluatorCacheRead {

    private static final int ARRAY_SIZE = 1024 * 512;

    private static final int STALE_AGE = 3;

    private int currentAge;


    private static class GameEvaluatorCacheEntry {
        long hash;
        int evaluation;
        int age;

        GameEvaluatorCacheEntry() {
            hash = 0;
            evaluation = 0;
            age = Integer.MIN_VALUE;
        }
    }

    private final GameEvaluatorCacheEntry[] cache;

    @Getter
    @Setter
    private Evaluator imp;

    @Getter
    private long evaluationsCacheHitsCounter;

    @Getter
    private long readFromCacheCounter;

    @Getter
    private long readFromCacheHitsCounter;

    private Game game;

    public EvaluatorCache() {
        this.cache = new GameEvaluatorCacheEntry[ARRAY_SIZE];
        for (int i = 0; i < ARRAY_SIZE; i++) {
            this.cache[i] = new GameEvaluatorCacheEntry();
        }
        this.currentAge = Integer.MIN_VALUE + STALE_AGE;
        this.evaluationsCacheHitsCounter = 0;
        this.readFromCacheCounter = 0;
        this.readFromCacheHitsCounter = 0;
    }

    @Override
    public void setGame(Game game) {
        this.game = game;
        this.imp.setGame(game);
    }

    @Override
    public int evaluate() {
        long hash = game.getPosition().getZobristHash();

        int idx = (int) Math.abs(hash % ARRAY_SIZE);

        GameEvaluatorCacheEntry entry = cache[idx];

        if (entry.hash != hash || entry.age > currentAge || currentAge - entry.age >= STALE_AGE) {
            entry.hash = hash;
            entry.evaluation = imp.evaluate();
            entry.age = currentAge;
        } else {
            evaluationsCacheHitsCounter++;
        }

        return entry.evaluation;
    }

    @Override
    public Integer readFromCache(long hash) {
        int idx = (int) Math.abs(hash % ARRAY_SIZE);

        GameEvaluatorCacheEntry entry = cache[idx];

        Integer result = entry.hash == hash && !(entry.age > currentAge || currentAge - entry.age >= STALE_AGE) ? entry.evaluation : null;

        readFromCacheCounter++;
        if (result != null) {
            readFromCacheHitsCounter++;
        }

        return result;
    }

    public void increaseAge() {
        if (currentAge < Integer.MAX_VALUE) {
            this.currentAge++;
        } else {
            clear();
            this.currentAge++;
        }
        this.evaluationsCacheHitsCounter = 0;
        this.readFromCacheCounter = 0;
        this.readFromCacheHitsCounter = 0;
    }

    public void clear() {
        for (int i = 0; i < ARRAY_SIZE; i++) {
            this.cache[i].age = Integer.MIN_VALUE;
        }
        this.currentAge = Integer.MIN_VALUE + STALE_AGE;
    }
}
