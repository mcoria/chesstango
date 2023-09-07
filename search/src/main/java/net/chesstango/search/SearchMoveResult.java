package net.chesstango.search;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.board.moves.Move;
import net.chesstango.search.smart.statistics.EvaluationStatistics;
import net.chesstango.search.smart.statistics.NodeStatistics;

import java.util.List;

/**
 * @author Mauricio Coria
 */

@Accessors(chain = true)
@Getter
@Setter
public class SearchMoveResult {

    private final int depth;

    private final MoveEvaluation bestMoveEvaluation;

    private final Move ponderMove;

    private List<Move> principalVariation;

    /**
     * bestMoves son movimientos tan buenos como bestMove
     */
    private List<Move> bestMoves;

    /**
     * Evaluaciones de las posiciones que resultan de cada movimiento
     */
    private List<MoveEvaluation> moveEvaluations;

    private EvaluationStatistics evaluationStatistics;

    private NodeStatistics regularNodeStatistics;

    private NodeStatistics quiescenceNodeStatistics;

    private int executedMoves;

    private String epdID;

    public SearchMoveResult(int depth, int evaluation, Move bestMove, Move ponderMove) {
        this.depth = depth;
        this.bestMoveEvaluation = new MoveEvaluation(bestMove, evaluation);
        this.ponderMove = ponderMove;
    }

    public Move getBestMove() {
        return bestMoveEvaluation.move();
    }

    public int getEvaluation() {
        return bestMoveEvaluation.evaluation();
    }

    public int getBestMovesCounter() {
        return bestMoves.size();
    }

}
