package net.chesstango.search.smart.statics;

import java.util.Set;

/**
 * @author Mauricio Coria
 */
public record EvaluationStatics(long evaluatedGamesCounter, Set<EvaluationEntry> evaluations) {
}
