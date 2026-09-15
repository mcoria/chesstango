package net.chesstango.search.alphabeta.quiescence;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Visitor;
import net.chesstango.search.alphabeta.AlphaBetaFilter;

/**
 * @author Mauricio Coria
 */
@Setter
public class QuiescenceStandingPat implements AlphaBetaFilter, Acceptor {

    @Getter
    private AlphaBetaFilter next;

    @Getter
    private Evaluator evaluator;

    private Move[] bestMoves;

    private Game game;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int alphaBeta(final int currentPly, final int alpha, final int beta) {
        bestMoves[currentPly] = null;
        int standingPat = Color.WHITE.equals(game.getPosition().getCurrentTurn()) ? evaluator.evaluate() : -evaluator.evaluate();
        if (standingPat >= beta) {
            return standingPat;
        }

        if(standingPat < alpha) {
            return standingPat;
        }

        int currentValue =  next.alphaBeta(currentPly, standingPat, beta);

        if(standingPat >= currentValue) {
            bestMoves[currentPly] = null;
            return standingPat;
        }

        return currentValue;
    }

}
