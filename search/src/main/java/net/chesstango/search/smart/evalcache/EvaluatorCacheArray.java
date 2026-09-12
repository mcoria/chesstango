package net.chesstango.search.smart.evalcache;

import lombok.AccessLevel;
import lombok.Getter;

/**
 *
 * @author Mauricio Coria
 */
public class EvaluatorCacheArray implements EvaluatorCache {

    @Getter
    private final EvaluatorCacheEntry[] cache;

    @Getter
    private int currentAge;

    @Getter
    private int arraySize;

    public EvaluatorCacheArray(int hashSizeKB) {
        if (hashSizeKB <= 0) {
            throw new IllegalArgumentException("HashSize must be at least 1 KB");
        }
        // Suponiendo que el hashSizeKB es en KB convertirlo a bytes
        this.arraySize = (hashSizeKB * 1024)  / 16;
        this.cache = new EvaluatorCacheEntry[this.arraySize];
        for (int i = 0; i < this.arraySize; i++) {
            this.cache[i] = new EvaluatorCacheEntry();
        }
        this.currentAge = Integer.MIN_VALUE;
    }

    @Override
    public EvaluatorCacheEntry read(long hash) {
        int idx = (int) Math.abs(hash % this.arraySize);

        EvaluatorCacheEntry entry = cache[idx];

        return entry.hash == hash && entry.age == currentAge ? entry : null;
    }

    @Override
    public EvaluatorCacheEntry write(long hash, int evaluation) {
        int idx = (int) Math.abs(hash % this.arraySize);

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
        for (int i = 0; i < this.arraySize; i++) {
            this.cache[i].age = Integer.MIN_VALUE;
        }
        this.currentAge = Integer.MIN_VALUE;
    }

    public int getFillPercentage() {
        int filled = 0;
        for (int i = 0; i < this.arraySize; i++) {
            EvaluatorCacheEntry entry = cache[i];
            if (entry.age == currentAge) {
                filled++;
            }
        }
        return (filled * 100 / this.arraySize);
    }
}
