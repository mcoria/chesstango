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
public class SearchMoveResult {

    private String id;

    private final int depth;

    private final MoveEvaluation bestMoveEvaluation;

    private final Move ponderMove;

    private List<PrincipalVariation> principalVariation;

    private boolean pvComplete;

    private EvaluationStatistics evaluationStatistics;

    private NodeStatistics regularNodeStatistics;

    private NodeStatistics quiescenceNodeStatistics;

    private int executedMoves;

    private long timeSearching;

    private int searchByDepthPvCompleteCounter;

    private int searchByDepthCounter;

    private List<SearchByDepthResult> searchByDepthResults;

    public SearchMoveResult(int depth, MoveEvaluation bestMoveEvaluation, Move ponderMove) {
        this.depth = depth;
        this.bestMoveEvaluation = bestMoveEvaluation;
        this.ponderMove = ponderMove;
    }

    public Move getBestMove() {
        return bestMoveEvaluation.move();
    }

    public int getBestEvaluation() {
        return bestMoveEvaluation.evaluation();
    }
}
