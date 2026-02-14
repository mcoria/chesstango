package net.chesstango.search.smart.features.transposition;

/**
 * Interface for a Transposition Table used in chess search algorithms.
 *
 * <p>A transposition table is a cache that stores previously evaluated chess positions
 * to avoid redundant calculations. Since different move sequences can lead to the same
 * position (transpositions), this table allows the search algorithm to reuse previous
 * results, significantly improving performance.</p>
 *
 * <p>The table stores entries indexed by Zobrist hash values of chess positions, containing
 * information such as the best move found, the search depth (draft), the evaluation value,
 * and the bound type (exact, lower, or upper bound).</p>
 *
 * <p>Implementations typically separate entries for maximizing and minimizing players to
 * avoid conflicts and improve cache efficiency.</p>
 *
 * @author Mauricio Coria
 */
public interface TTable {

    /**
     * Retrieves a transposition table entry for the given position hash.
     *
     * @param hash the Zobrist hash value of the chess position to look up
     * @return the TranspositionEntry associated with the hash, or null if no entry exists
     */
    TranspositionEntry read(long hash);

    /**
     * Stores or updates a transposition table entry for the given position.
     *
     * @param hash  the Zobrist hash value of the chess position
     * @param bound the type of bound for the value (EXACT, LOWER_BOUND, or UPPER_BOUND)
     * @param draft the search depth at which this position was evaluated (higher is deeper)
     * @param move  the best move found for this position, encoded as a short
     * @param value the evaluation score for this position
     * @return the TranspositionEntry that was written to the table
     */
    TranspositionEntry write(long hash, TranspositionBound bound, int draft, short move, int value);

    /**
     * Removes all entries from the transposition table, resetting it to an empty state.
     * This is typically called at the start of a new game or search session.
     */
    void clear();
}
