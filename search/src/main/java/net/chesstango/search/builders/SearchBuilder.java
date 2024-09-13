package net.chesstango.search.builders;

import net.chesstango.evaluation.Evaluator;
import net.chesstango.search.Search;

/**
 * @author Mauricio Coria
 */
public interface SearchBuilder {

    <T extends SearchBuilder> T withGameEvaluator(Evaluator evaluator);

    Search build();

}
