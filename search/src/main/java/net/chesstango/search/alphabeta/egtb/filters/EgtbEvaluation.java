package net.chesstango.search.alphabeta.egtb.filters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Visitor;
import net.chesstango.search.alphabeta.AlphaBetaFilter;
import net.chesstango.search.alphabeta.egtb.EndGameTableBase;

/**
 * @author Mauricio Coria
 */
@Getter
@Setter
public class EgtbEvaluation implements AlphaBetaFilter, Acceptor {

    private EndGameTableBase endGameTableBase;

    private Game game;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int alphaBeta(int currentPly, int alpha, int beta) {
        return Color.WHITE.equals(game.getPosition().getCurrentTurn()) ? endGameTableBase.evaluate() : -endGameTableBase.evaluate();
    }
}
