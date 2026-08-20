package net.chesstango.search.smart.core.filters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.AlphaBetaFilter;

/**
 * @author Mauricio Coria
 */
@Setter
public class QuiescenceStandingPat implements AlphaBetaFilter, Acceptor {

    @Getter
    private AlphaBetaFilter next;

    @Getter
    private Evaluator evaluator;

    private Game game;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int alphaBeta(final int currentPly, final int alpha, final int beta) {
        int bestValue = Color.WHITE.equals(game.getPosition().getCurrentTurn()) ? evaluator.evaluate() : -evaluator.evaluate();
        if (bestValue >= beta) {
            return bestValue;
        }

        int currentValue =  next.alphaBeta(currentPly, Math.max(bestValue, alpha), beta);

        return Math.max(bestValue, currentValue);
    }

}
