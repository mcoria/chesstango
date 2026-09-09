package net.chesstango.search.smart.statistics.evalcache;

import java.io.Serializable;

/**
 * Statistics for Evaluator Cache operations during chess position search.
 * <p>
 * </p>
 *
 * @param readNodes The total number of read attempts to get node evaluations
 * @param readNodeHits The total number of successful read attempts to get node evaluations
 * @param readComparators  The total number of read attempts done by comparators
 * @param readComparatorHits  The total number of successful read attempts done by comparators
 * @param fillPercentage Cache fill percentage
 * @author Mauricio Coria
 */
public record EvaluatorCacheStatistics(long readNodes,
                                       long readNodeHits,

                                       long readComparators,
                                       long readComparatorHits,

                                       int fillPercentage) implements Serializable {
}
