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

    private final MoveEvaluation bestMove;

    private final Move ponderMove;

    private List<Move> principalVariation;

    /**
     * bestMoves son movimientos tan buenos como bestMove.
     * La lista puede estar vacia !!!
     */
    private List<Move> bestMoves;

    /**
     * Evaluaciones de las posiciones que resultan de cada movimiento.
     * La lista de evaluaciones puede no estar completa !!!
     */
    private List<MoveEvaluation> moveEvaluations;

    private EvaluationStatistics evaluationStatistics;

    private NodeStatistics regularNodeStatistics;

    private NodeStatistics quiescenceNodeStatistics;

    private int executedMoves;

    private String epdID;

    public SearchMoveResult(int depth, int evaluation, Move bestMove, Move ponderMove) {
        this.depth = depth;
        this.bestMove = new MoveEvaluation(bestMove, evaluation);
        this.ponderMove = ponderMove;
    }

    public Move getBestMove() {
        return bestMove.move();
    }

    public int getEvaluation() {
        return bestMove.evaluation();
    }

    public int getBestMovesCounter() {
        return bestMoves == null ? 1 : bestMoves.size();
    }

}
