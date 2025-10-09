package net.chesstango.search.visitors;

import net.chesstango.search.SearchResult;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.features.statistics.evaluation.EvaluatorStatisticsWrapper;

/**
 *
 * @author Mauricio Coria
 */
public class CollectSearchResultVisitor implements Visitor {

    private final SearchResult searchResult;

    public CollectSearchResultVisitor(SearchResult searchResult) {
        this.searchResult = searchResult;
    }

    @Override
    public void visit(EvaluatorStatisticsWrapper evaluatorStatisticsWrapper) {
        searchResult.setEvaluationStatistics(evaluatorStatisticsWrapper.getEvaluationStatistics());
    }
}
