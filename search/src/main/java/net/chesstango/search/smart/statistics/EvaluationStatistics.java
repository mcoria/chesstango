package net.chesstango.search.smart.statistics;

import java.util.Set;

/**
 * @author Mauricio Coria
 */
public record EvaluationStatistics(long evaluatedGamesCounter, Set<EvaluationEntry> evaluations) {
}
