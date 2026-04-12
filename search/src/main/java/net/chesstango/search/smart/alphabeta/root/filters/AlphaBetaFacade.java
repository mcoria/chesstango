package net.chesstango.search.smart.alphabeta.root.filters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.SearchAlgorithm;
import net.chesstango.search.smart.alphabeta.AlphaBetaFilter;

/**
 * @author Mauricio Coria
 */
public class AlphaBetaFacade implements SearchAlgorithm, Acceptor {

    @Setter
    @Getter
    private AlphaBetaFilter next;

    @Setter
    private Game game;


    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void search() {
        final Color currentTurn = game.getPosition().getCurrentTurn();

        final long bestMoveAndValue = Color.WHITE.equals(currentTurn) ?
                next.maximize(0, Evaluator.INFINITE_NEGATIVE, Evaluator.INFINITE_POSITIVE) :
                next.minimize(0, Evaluator.INFINITE_NEGATIVE, Evaluator.INFINITE_POSITIVE);

    }

}
