package net.chesstango.search;

import net.chesstango.evaluation.Evaluator;

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
 * @param <T> the specific type of Search that this builder creates
 * @author Mauricio Coria
 * @see Search
 * @see Evaluator
 */
public interface SearchBuilder<T extends Search> {

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
    SearchBuilder<T> withGameEvaluator(Evaluator evaluator);

    /**
     * Constructs and returns the configured Search instance.
     * <p>
     * This method should be called after all desired configurations have been applied
     * through the builder's fluent interface methods.
     * </p>
     *
     * @return a fully configured Search instance of type T
     */
    T build();
}
