package net.chesstango.search.smart.alphabeta.evaluator.filters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.alphabeta.AlphaBetaFilter;

/**
 * @author Mauricio Coria
 */
@Setter
@Getter
public class AlphaBetaEvaluation implements AlphaBetaFilter, Acceptor {
    private Evaluator evaluator;

    private Game game;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int alphaBeta(int currentPly, int alpha, int beta) {
        return Color.WHITE.equals(game.getPosition().getCurrentTurn()) ? evaluator.evaluate() : -evaluator.evaluate();
    }
}
