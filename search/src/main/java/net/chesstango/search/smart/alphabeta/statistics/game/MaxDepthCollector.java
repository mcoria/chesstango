package net.chesstango.search.smart.alphabeta.statistics.game;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.SearchByDepthListener;
import net.chesstango.search.smart.alphabeta.root.RootMoveEvaluationCollection;

/**
 * @author Mauricio Coria
 */

public class MaxDepthCollector implements SearchByCycleListener, SearchByDepthListener {

    @Setter
    private RootMoveEvaluationCollection rootMoveEvaluationCollection;

    @Getter
    private float maxDepth;

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
        maxDepth = 0;
    }

    @Override
    public void beforeSearchByDepth() {
    }

    @Override
    public void afterSearchByDepth() {
        int evaluatedChild = rootMoveEvaluationCollection.getRootMoveEvaluations().size();
        maxDepth += (float) evaluatedChild / possibleMoves;
    }
}
