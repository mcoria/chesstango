package net.chesstango.search.smart.alphabeta.evaluator.listeners;

import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Visitor;

/**
 * @author Mauricio Coria
 */
@Setter
public class SetGameToEvaluator implements Acceptor {

    private Evaluator evaluator;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void setGame(Game game) {
        evaluator.setGame(game);
    }
}
