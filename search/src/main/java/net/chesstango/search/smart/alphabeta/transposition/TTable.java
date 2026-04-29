package net.chesstango.search.smart.alphabeta.transposition;

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
     * <p>This method searches for a previously stored evaluation of the chess position
     * identified by the given hash.
     * It populates the provided entry object with the cached data, which may include the best move,
     * evaluation value, search depth, and bound type.</p>
     *
     * @param hash  the Zobrist hash value of the chess position to look up
     * @param entry the TranspositionEntry object to populate with the cached data if found
     * @return true if there is an entry that maps to the given hash, false otherwise
     */
    boolean load(long hash, TranspositionEntry entry);

    /**
     * Stores or updates a transposition table entry for the given position.
     *
     * <p>This method inserts a new entry or replaces an existing one in the transposition table.
     * The replacement strategy typically depends on factors such as search depth (draft) and
     * entry age, with deeper searches generally taking priority over shallower ones.</p>
     *
     * @param entry the TranspositionEntry containing the evaluation data to save
     */
    void save(TranspositionEntry entry);
}
