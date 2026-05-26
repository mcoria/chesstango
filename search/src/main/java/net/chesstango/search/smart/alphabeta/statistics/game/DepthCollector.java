package net.chesstango.search.smart.alphabeta.statistics.game;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.SearchByDepthListener;
import net.chesstango.search.smart.alphabeta.root.RootMoveEvaluationCollection;

/**
 * @author Mauricio Coria
 */

public class DepthCollector implements Acceptor, SearchByCycleListener, SearchByDepthListener {

    @Setter
    private RootMoveEvaluationCollection rootMoveEvaluationCollection;

    @Setter
    private int depth;

    @Getter
    private float exploredDepth;

    private int possibleMoves;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void beforeSearch() {
        exploredDepth = 0;
    }

    @Override
    public void beforeSearchByDepth() {
    }

    @Override
    public void afterSearchByDepth() {
        int evaluatedChild = rootMoveEvaluationCollection.getRootMoveEvaluations().size();
        exploredDepth = (depth - 1) + (float) evaluatedChild / possibleMoves;
    }

    public void setGame(Game game) {
        possibleMoves = game.getPossibleMoves().size();
    }
}
