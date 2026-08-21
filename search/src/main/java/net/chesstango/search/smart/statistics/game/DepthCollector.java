package net.chesstango.search.smart.statistics.game;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Bound;
import net.chesstango.search.RootMoveEvaluation;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.SearchByDepthListener;

import java.util.List;

/**
 * @author Mauricio Coria
 */

public class DepthCollector implements Acceptor, SearchByCycleListener, SearchByDepthListener {

    @Setter
    private int depth;

    @Setter
    private List<RootMoveEvaluation> rootMoveEvaluationList;

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
    public void afterSearchByDepth(boolean searchStopped) {
        long evaluatedChild = rootMoveEvaluationList
                .stream()
                .filter(rootEval -> !(rootEval.evaluation() == Evaluator.INFINITE_NEGATIVE && rootEval.bound() == Bound.UPPER_BOUND))
                .count();
        exploredDepth = (depth - 1) + (float) evaluatedChild / possibleMoves;
    }

    public void setGame(Game game) {
        possibleMoves = game.getPossibleMoves().size();
    }
}
