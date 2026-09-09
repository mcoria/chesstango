package net.chesstango.search.smart.statistics.evaluator;

import java.io.Serializable;
import java.util.Set;

/**
 * @author Mauricio Coria
 */
public record EvaluatorStatistics(long evaluationsCounter,
                                  Set<EvaluatorEntry> evaluations) implements Serializable {
}
