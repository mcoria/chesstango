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

    enum InsertResult {
        INSERTED, // The entry was not found in the table
        UPDATED,  // The entry was found in the table and was updated
        OVER_WRITTEN  // Another entry was found in the table and was replaced
    }

    /**
     * Retrieves a transposition table entry for the given position hash.
     *
     * <p>This method searches for a previously stored evaluation of the chess position
     * identified by the given hash. If found, it populates the provided entry object
     * with the cached data, which may include the best move, evaluation value, search
     * depth, and bound type.</p>
     *
     * @param hash  the Zobrist hash value of the chess position to look up
     * @param entry the TranspositionEntry object to populate with the cached data if found
     * @return true if the entry was found, false otherwise
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
     * @return an InsertResult indicating the outcome: INSERTED if a new entry was added,
     *         UPDATED if an existing entry for the same position was modified, or
     *         OVER_WRITTEN if a different position's entry was replaced
     */
    InsertResult save(TranspositionEntry entry);

    /**
     * Removes all entries from the transposition table, resetting it to an empty state.
     * This is typically called at the start of a new game or search session.
     */
    void clear();
}
