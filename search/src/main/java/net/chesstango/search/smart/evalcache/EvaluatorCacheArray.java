package net.chesstango.search.smart.evalcache;

import lombok.Getter;
import net.chesstango.search.smart.Constants;

/**
 *
 * @author Mauricio Coria
 */
public class EvaluatorCacheArray implements EvaluatorCache {

    @Getter
    private final EvaluatorCacheEntry[] cache;

    @Getter
    private int currentAge;

    public EvaluatorCacheArray() {
        this.cache = new EvaluatorCacheEntry[Constants.CACHE_ARRAY_SIZE];
        for (int i = 0; i < Constants.CACHE_ARRAY_SIZE; i++) {
            this.cache[i] = new EvaluatorCacheEntry();
        }
        this.currentAge = Integer.MIN_VALUE;
    }

    @Override
    public EvaluatorCacheEntry read(long hash) {
        int idx = (int) Math.abs(hash % Constants.CACHE_ARRAY_SIZE);

        EvaluatorCacheEntry entry = cache[idx];

        return entry.hash == hash && entry.age == currentAge ? entry : null;
    }

    @Override
    public EvaluatorCacheEntry write(long hash, int evaluation) {
        int idx = (int) Math.abs(hash % Constants.CACHE_ARRAY_SIZE);

        EvaluatorCacheEntry entry = cache[idx];
        entry.hash = hash;
        entry.evaluation = evaluation;
        entry.age = currentAge;

        return entry;
    }

    public void increaseAge() {
        if (currentAge < Integer.MAX_VALUE) {
            this.currentAge++;
        } else {
            clear();
            this.currentAge++;
        }
    }

    public void clear() {
        for (int i = 0; i < Constants.CACHE_ARRAY_SIZE; i++) {
            this.cache[i].age = Integer.MIN_VALUE;
        }
        this.currentAge = Integer.MIN_VALUE;
    }

    public int getFillPercentage() {
        int filled = 0;
        for (int i = 0; i < Constants.CACHE_ARRAY_SIZE; i++) {
            EvaluatorCacheEntry entry = cache[i];
            if (entry.age == currentAge) {
                filled++;
            }
        }
        return (filled * 100 / Constants.CACHE_ARRAY_SIZE);
    }
}
