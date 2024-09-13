package net.chesstango.search;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.board.moves.Move;
import net.chesstango.search.smart.features.statistics.evaluation.EvaluationStatistics;
import net.chesstango.search.smart.features.statistics.node.NodeStatistics;

import java.util.List;

/**
 * @author Mauricio Coria
 */

@Accessors(chain = true)
@Getter
@Setter
public class SearchResult {

    private final int depth;

    private String id;

    private MoveEvaluation bestMoveEvaluation;

    private Move ponderMove;

    private List<PrincipalVariation> principalVariation;

    private boolean pvComplete;

    private EvaluationStatistics evaluationStatistics;

    private NodeStatistics regularNodeStatistics;

    private NodeStatistics quiescenceNodeStatistics;

    private int executedMoves;

    private long timeSearching;

    private int searchByDepthPvCompleteCounter;

    private int searchByDepthCounter;

    private int expectedRootBestMoveCounter;

    private List<SearchByDepthResult> searchByDepthResults;

    public SearchResult(int depth) {
        this.depth = depth;
    }

    public Move getBestMove() {
        return bestMoveEvaluation.move();
    }

    public int getBestEvaluation() {
        return bestMoveEvaluation.evaluation();
    }
}
