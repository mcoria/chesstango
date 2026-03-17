package net.chesstango.search.smart.alphabeta.statistics.game;

import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.SearchByDepthListener;
import net.chesstango.search.smart.alphabeta.root.RootChildEvaluationCollection;

/**
 * @author Mauricio Coria
 */

public class MaxRegularDepth implements SearchByDepthListener {

    @Setter
    private RootChildEvaluationCollection rootChildEvaluationCollection;

    private int possibleMoves;

    private float maxRegularDepth;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void setGame(Game game) {
        possibleMoves = game.getPossibleMoves().size();
    }

    @Override
    public void beforeSearchByDepth() {
    }

    @Override
    public void afterSearchByDepth() {
        int evaluatedChild = rootChildEvaluationCollection.getRootChildEvaluations().size();
        maxRegularDepth += (float) evaluatedChild / possibleMoves;
    }
}
