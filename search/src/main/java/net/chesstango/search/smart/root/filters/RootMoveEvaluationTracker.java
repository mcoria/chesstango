package net.chesstango.search.smart.root.filters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Bound;
import net.chesstango.search.RootMoveEvaluation;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.AlphaBetaFilter;
import net.chesstango.search.smart.root.RootMoveEvaluationBest;
import net.chesstango.search.smart.root.RootMoveEvaluationCollection;

/**
 * Actualiza RootMoveEvaluationCollection a medida que se obtienen resultados de los movimientos de root node
 *
 * @author Mauricio Coria
 */
public class RootMoveEvaluationTracker implements AlphaBetaFilter, Acceptor {

    @Setter
    @Getter
    private AlphaBetaFilter next;

    @Setter
    private RootMoveEvaluationBest rootMoveEvaluationBest;

    @Setter
    private RootMoveEvaluationCollection rootMoveEvaluationCollection;


    @Setter
    private Game game;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }


    @Override
    public int alphaBeta(int currentPly, int alpha, int beta) {
        Move currentMove = game.getHistory().peekLastRecord().playedMove();

        int currentValue = next.alphaBeta(currentPly, alpha, beta);

        RootMoveEvaluation rootMoveEvaluation = createRootMoveEvaluation(currentMove, currentValue, alpha, beta);
        rootMoveEvaluationBest.save(rootMoveEvaluation);
        rootMoveEvaluationCollection.save(rootMoveEvaluation);

        return currentValue;
    }


    final RootMoveEvaluation createRootMoveEvaluation(Move currentMove, int currentValue, int alpha, int beta) {
        Bound moveEvaluationType = null;

        if (currentValue <= alpha) {
            moveEvaluationType = Bound.UPPER_BOUND;
        } else if (beta <= currentValue) {
            moveEvaluationType = Bound.LOWER_BOUND;
        } else {
            moveEvaluationType = Bound.EXACT;
        }

        return new RootMoveEvaluation(currentMove, currentValue, moveEvaluationType);
    }
}
