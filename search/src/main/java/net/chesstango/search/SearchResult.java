package net.chesstango.search;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.board.moves.Move;
import net.chesstango.search.smart.features.statistics.evaluation.EvaluationStatistics;
import net.chesstango.search.smart.features.statistics.node.NodeStatistics;

import java.util.Arrays;
import java.util.List;

/**
 * @author Mauricio Coria
 */

@Accessors(chain = true)
@Getter
@Setter
public class SearchResult {

    /**
     * Max depth searched during the entire cycle
     */
    private final int maxDepth;

    private String id;

    private EvaluationStatistics evaluationStatistics;

    private NodeStatistics regularNodeStatistics;

    private NodeStatistics quiescenceNodeStatistics;

    private int executedMoves;

    private long timeSearching;

    private int searchByDepthPvCompleteCounter;

    private int searchByDepthCounter;

    private int expectedRootBestMoveCounter;

    private List<SearchResultByDepth> searchResultByDepths;

    public SearchResult(int maxDepth) {
        this.maxDepth = maxDepth;
    }

    public Move getBestMove() {
        return searchResultByDepths.getLast().getBestMove();
    }

    public int getBestEvaluation() {
        return searchResultByDepths.getLast().getBestEvaluation();
    }

    public List<PrincipalVariation> getPrincipalVariation() {
        return searchResultByDepths.getLast().getPrincipalVariation();
    }

    public boolean isPvComplete() {
        return searchResultByDepths.getLast().isPvComplete();
    }
}
