package net.chesstango.search;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.board.moves.Move;
import net.chesstango.search.smart.features.statistics.evaluation.EvaluationStatistics;
import net.chesstango.search.smart.features.statistics.node.NodeStatistics;
import net.chesstango.search.smart.features.statistics.transposition.TTableStatistics;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mauricio Coria
 */

@Accessors(chain = true)
@Getter
@Setter
public class SearchResult implements Serializable {

    private String id;

    private EvaluationStatistics evaluationStatistics;

    private NodeStatistics regularNodeStatistics;

    private NodeStatistics quiescenceNodeStatistics;

    private TTableStatistics tTableStatistics;

    private long executedMoves;

    private long timeSearching;

    private int bottomMoveCounter;

    @Setter(AccessLevel.NONE)
    private List<SearchResultByDepth> searchResultByDepths = new ArrayList<>();

    public Move getBestMove() {
        return !searchResultByDepths.isEmpty() ? searchResultByDepths.getLast().getBestMove() : null;
    }

    public Integer getBestEvaluation() {
        return !searchResultByDepths.isEmpty() ? searchResultByDepths.getLast().getBestEvaluation() : null;
    }

    public List<PrincipalVariation> getPrincipalVariation() {
        return !searchResultByDepths.isEmpty() ? searchResultByDepths.getLast().getPrincipalVariation() : null;
    }

    public boolean isPvComplete() {
        return searchResultByDepths.getLast().isPvComplete();
    }

    public SearchResult addSearchResultByDepth(SearchResultByDepth searchResultByDepth) {
        searchResultByDepths.add(searchResultByDepth);
        return this;
    }

    public int getSearchesByDepthCounter() {
        return searchResultByDepths.size();
    }

    public int getSearchByDepthPvCompleteCounter() {
        return (int) searchResultByDepths.stream().filter(SearchResultByDepth::isPvComplete).count();
    }

}
