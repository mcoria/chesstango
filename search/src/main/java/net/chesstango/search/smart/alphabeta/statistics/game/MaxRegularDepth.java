package net.chesstango.search.smart.alphabeta.statistics.game;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.SearchByDepthListener;
import net.chesstango.search.smart.alphabeta.root.RootChildEvaluationCollection;

/**
 * @author Mauricio Coria
 */

public class MaxRegularDepth implements SearchByCycleListener, SearchByDepthListener {

    @Setter
    private RootChildEvaluationCollection rootChildEvaluationCollection;

    @Getter
    private float maxRegularDepth;

    private int possibleMoves;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void setGame(Game game) {
        possibleMoves = game.getPossibleMoves().size();
    }

    @Override
    public void beforeSearch() {
        maxRegularDepth = 0;
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
