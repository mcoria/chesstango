package net.chesstango.search;

import net.chesstango.evaluation.Evaluator;

/**
 * @author Mauricio Coria
 */
public interface SearchBuilder<T extends Search> {

    SearchBuilder<T> withGameEvaluator(Evaluator evaluator);

    T build();
}
