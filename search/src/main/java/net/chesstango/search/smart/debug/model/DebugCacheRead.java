package net.chesstango.search.smart.debug.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Represents a cache read operation performed during move sorting in the search algorithm.
 * This class tracks evaluation cache reads that occur when sorting moves, storing the
 * hash key requested, the evaluation retrieved, and the associated move.
 *
 * @author Mauricio Coria
 */
@Getter
@Setter
@Accessors(chain = true)
public class DebugCacheRead {

    /**
     * The Zobrist hash key that was requested from the evaluation cache.
     */
    private long hashRequested;

    /**
     * The evaluation value retrieved from the cache for the requested hash.
     */
    private int evaluation;

    /**
     * The move in coordinate encoding that led to this cache entry during sorting.
     * This field is populated to associate cache reads with specific moves being evaluated.
     */
    private String move;
}
