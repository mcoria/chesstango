package net.chesstango.search.alphabeta.statistics.evaluator;

import java.io.Serializable;
import java.util.Set;

/**
 * @author Mauricio Coria
 */
public record EvaluatorStatistics(long evaluationsCounter,
                                  Set<EvaluatorEntry> evaluations) implements Serializable {
}
