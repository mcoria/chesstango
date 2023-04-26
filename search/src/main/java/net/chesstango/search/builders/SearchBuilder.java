package net.chesstango.search.builders;

import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.SearchMove;

/**
 * @author Mauricio Coria
 */
public interface SearchBuilder {
    SearchBuilder withGameEvaluator(GameEvaluator gameEvaluator);

    SearchMove build();
}
