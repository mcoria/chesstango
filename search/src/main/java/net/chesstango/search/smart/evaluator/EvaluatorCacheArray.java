package net.chesstango.search.smart.evaluator;

import lombok.Getter;

/**
 *
 * @author Mauricio Coria
 */
public class EvaluatorCacheArray implements EvaluatorCache {

    public static final int ARRAY_SIZE = 1024 * 512;

    public static final int STALE_AGE = 3;

    @Getter
    private final EvaluatorCacheEntry[] cache;

    @Getter
    private int currentAge;

    @Getter
    private long evaluationsCacheHitsCounter;

    @Getter
    private long readFromCacheCounter;

    @Getter
    private long readFromCacheHitsCounter;

    public EvaluatorCacheArray() {
        this.cache = new EvaluatorCacheEntry[ARRAY_SIZE];
        for (int i = 0; i < ARRAY_SIZE; i++) {
            this.cache[i] = new EvaluatorCacheEntry();
        }
        this.currentAge = Integer.MIN_VALUE + STALE_AGE;
        this.evaluationsCacheHitsCounter = 0;
        this.readFromCacheCounter = 0;
        this.readFromCacheHitsCounter = 0;
    }

    @Override
    public EvaluatorCacheEntry readFromCache(long hash) {
        int idx = (int) Math.abs(hash % ARRAY_SIZE);

        EvaluatorCacheEntry entry = cache[idx];

        EvaluatorCacheEntry result = entry.hash == hash && !(entry.age > currentAge || currentAge - entry.age >= STALE_AGE) ? entry : null;

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

    public int getFillPercentage() {
        int filled = 0;
        for (int i = 0; i < ARRAY_SIZE; i++) {
            EvaluatorCacheEntry entry = cache[i];
            if (!(entry.age > currentAge || currentAge - entry.age >= STALE_AGE)) {
                filled++;
            }
        }
        return (filled * 100 / ARRAY_SIZE);
    }
}
