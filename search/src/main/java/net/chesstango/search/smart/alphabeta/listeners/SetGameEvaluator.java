package net.chesstango.search.smart.alphabeta.listeners;

import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.features.egtb.EndGameTableBase;

/**
 * @author Mauricio Coria
 */
@Setter
public class SetGameEvaluator implements Acceptor {

    private Evaluator evaluator;

    private EndGameTableBase endGameTableBase;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void setGame(Game game) {
        evaluator.setGame(game);
        endGameTableBase.setGame(game);
    }
}
