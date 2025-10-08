package net.chesstango.search.smart.alphabeta.listeners;

import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.SearchByCycleListener;

/**
 * @author Mauricio Coria
 */
@Setter
public class SetGameEvaluator implements SearchByCycleListener, Acceptor {

    private Game game;

    private Evaluator evaluator;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void beforeSearch() {
        evaluator.setGame(game);
    }

}
