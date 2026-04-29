package net.chesstango.search;

import net.chesstango.evaluation.Evaluator;
import net.chesstango.search.builders.AlphaBetaBuilder;

/**
 * Builder interface for constructing Search instances using the builder pattern.
 * <p>
 * This interface defines the contract for building search algorithms with various configurations.
 * Implementations of this interface allow fluent configuration of search components such as
 * evaluators, transposition tables, and statistics before creating the final Search instance.
 * </p>
 * <p>
 * Example usage:
 * <pre>
 * Search search = new AlphaBetaBuilder()
 *     .withGameEvaluator(new EvaluatorByMaterial())
 *     .withTranspositionTable()
 *     .withStatistics()
 *     .build();
 * </pre>
 * </p>
 *
 * @author Mauricio Coria
 * @see Search
 * @see Evaluator
 */
public interface SearchBuilder<T extends SearchBuilder<T>> {

    /**
     * Configures the evaluator to be used for assessing chess positions during search.
     * <p>
     * The evaluator is responsible for providing static evaluation scores for positions
     * when the search reaches its depth limit or in quiescence search.
     * </p>
     *
     * @param evaluator the Evaluator instance to use for position evaluation
     * @return this builder instance for method chaining
     */
    T withGameEvaluator(Evaluator evaluator);

    /**
     * Configures the transposition table with the specified hash size.
     * <p>
     * The transposition table is used to store previously evaluated positions to avoid
     * redundant calculations during search. A larger hash size allows storing more positions
     * but requires more memory.
     * </p>
     *
     * @return this builder instance for method chaining
     */

    T withTranspositionTable();

    T withTranspositionHashSize(int hashSizeKB);

    T withTranspositionStaleAge(int staleAge);



    /**
     * Constructs and returns the configured Search instance.
     * <p>
     * This method should be called after all desired configurations have been applied
     * through the builder's fluent interface methods.
     * </p>
     *
     * @return a fully configured Search instance of type T
     */
    Search build();


    /**
     * Creates a default Search instance using the default Evaluator.
     * <p>
     * This factory method returns a builder that can be used to configure and construct a Search instance.
     * The default builder is configured with:
     * </p>
     * <ul>
     *   <li>Alpha-beta pruning algorithm</li>
     *   <li>Default evaluation function (material-based)</li>
     *   <li>Standard move ordering</li>
     * </ul>
     * <p>
     * Additional features such as transposition tables, iterative deepening, statistics collection,
     * and aspiration windows can be added by calling methods on the returned builder before calling
     * {@code build()}.
     * </p>
     * <p>
     * <b>Example:</b>
     * </p>
     * <pre>{@code
     * Search search = Search.newSearchBuilder()
     *     .withGameEvaluator(new EvaluatorByMaterial())
     *     .withTranspositionTable()
     *     .withStatistics()
     *     .build();
     * }</pre>
     *
     * @return a new SearchBuilder instance configured with default settings
     * @see SearchBuilder
     * @see net.chesstango.search.builders.AlphaBetaBuilder
     */
    static SearchBuilder<?> newSearchBuilder() {
        return AlphaBetaBuilder.createDefaultBuilderInstance();
    }
}
