package net.chesstango.search.alphabeta.root.filters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.search.*;
import net.chesstango.search.alphabeta.AlphaBetaFilter;
import net.chesstango.search.alphabeta.pv.model.PVCalculator;
import net.chesstango.search.alphabeta.root.RootMoveEvaluationBest;
import net.chesstango.search.alphabeta.root.RootMoveEvaluationCollection;

import java.util.List;

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
    private PVCalculator pvCalculator;

    @Setter
    private Game game;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }


    @Override
    public int alphaBeta(int currentPly, int alpha, int beta) {
        int currentValue = next.alphaBeta(currentPly, alpha, beta);

        RootMoveEvaluation rootMoveEvaluation = createRootMoveEvaluation(currentValue, alpha, beta);
        rootMoveEvaluationBest.save(rootMoveEvaluation);
        rootMoveEvaluationCollection.save(rootMoveEvaluation);

        return currentValue;
    }


    final RootMoveEvaluation createRootMoveEvaluation(int currentValue, int alpha, int beta) {
        Bound moveEvaluationType = null;
        PrincipalVariation principalVariation = null;

        if (currentValue <= alpha) {
            moveEvaluationType = Bound.UPPER_BOUND;
            principalVariation = createFakePV();
        } else if (beta <= currentValue) {
            moveEvaluationType = Bound.LOWER_BOUND;
            principalVariation = createFakePV();
        } else {
            moveEvaluationType = Bound.EXACT;
            principalVariation = pvCalculator.calculatePrincipalVariation(currentValue);
        }

        Move lastMove = game.getHistory().peekLastRecord().playedMove();
        return new RootMoveEvaluation(lastMove, currentValue, moveEvaluationType, principalVariation);
    }

    PrincipalVariation createFakePV() {
        Move lastMove = game.getHistory().peekLastRecord().playedMove();
        long lastHash = game.getHistory().peekLastRecord().zobristHash().getZobristHash();
        return new PrincipalVariation(List.of(new PVMove(lastHash, lastMove)), false);
    }
}
