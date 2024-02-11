package net.chesstango.search;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.board.moves.Move;
import net.chesstango.search.MoveEvaluation;

import java.util.List;

/**
 * @author Mauricio Coria
 */
@Accessors(chain = true)
@Getter
@Setter
public class SearchByDepthResult {

    private int depth;

    private MoveEvaluation bestMoveEvaluation;

    private List<Move> principalVariation;

    /**
     * Evaluaciones de las posiciones que resultan de cada movimiento.
     * La lista de evaluaciones puede no estar completa !!!
     */
    private List<MoveEvaluation> moveEvaluations;

    private long timeSearchingLastDepth;

    private long timeSearching;

    public Move getBestMove() {
        return bestMoveEvaluation.move();
    }

    public int getBestEvaluation() {
        return bestMoveEvaluation.evaluation();
    }
}
