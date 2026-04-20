package net.chesstango.search.smart.alphabeta.evaluator;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.alphabeta.debug.SearchTracker;
import net.chesstango.search.smart.alphabeta.debug.model.DebugNode;

/**
 * @author Mauricio Coria
 */

@Setter
@Getter
public class EvaluatorDebug implements Evaluator, Acceptor {

    private SearchTracker searchTracker;

    private Evaluator evaluator;

    private Game game;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int evaluate() {
        int evaluation = evaluator.evaluate();
        trackEvaluation(evaluation);
        return evaluation;
    }

    public void trackEvaluation(int evaluation) {
        DebugNode currentNode = searchTracker.getCurrentNode();
        if (currentNode != null && DebugNode.NodeTopology.QUIESCENCE.equals(currentNode.getTopology())) {
            currentNode.setStandingPat(Color.WHITE.equals(game.getPosition().getCurrentTurn()) ? evaluation : -evaluation);
        }
    }

    @Override
    public void setGame(Game game) {
        this.game = game;
        this.evaluator.setGame(game);
    }
}
