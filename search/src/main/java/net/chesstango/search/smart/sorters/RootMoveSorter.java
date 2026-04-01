package net.chesstango.search.smart.sorters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.search.Bound;
import net.chesstango.search.RootMoveEvaluation;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.sorters.comparators.DefaultMoveComparator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class RootMoveSorter implements MoveSorter, SearchByCycleListener {

    private final RootMoveEvaluationComparator rootMoveEvaluationComparator = new RootMoveEvaluationComparator();

    @Getter
    @Setter
    private MoveSorter next;

    @Setter
    private Game game;

    @Setter
    private List<RootMoveEvaluation> lastRootMoveEvaluations;

    private boolean maximize;

    private int numberOfMove;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void beforeSearch() {
        this.maximize = Color.WHITE.equals(game.getPosition().getCurrentTurn());
        this.numberOfMove = game.getPossibleMoves().size();
        this.lastRootMoveEvaluations = null;
    }

    @Override
    public Iterable<Move> getOrderedMoves(int currentPly) {
        if (lastRootMoveEvaluations == null) {
            return next.getOrderedMoves(currentPly);
        } else {
            return getSortedMovesByLastMoveEvaluations();
        }
    }


    private List<Move> getSortedMovesByLastMoveEvaluations() {
        List<RootMoveEvaluation> lastRootMoveEvaluationsCopy = new ArrayList<>(lastRootMoveEvaluations);

        if (lastRootMoveEvaluationsCopy.size() != numberOfMove) {
            throw new RuntimeException("Not all move were explorer during last iteration");
        }

        lastRootMoveEvaluationsCopy.sort(maximize ? rootMoveEvaluationComparator.reversed() : rootMoveEvaluationComparator);

        if (Bound.EXACT != lastRootMoveEvaluationsCopy.getFirst().bound()) {
            throw new RuntimeException("First move bound is not exact after sorting");
        }

        return lastRootMoveEvaluationsCopy.stream().map(RootMoveEvaluation::move).toList();
    }

    static final class RootMoveEvaluationComparator implements Comparator<RootMoveEvaluation> {
        private final Comparator<Move> defaultMoveComparator = new DefaultMoveComparator();

        @Override
        public int compare(RootMoveEvaluation o1, RootMoveEvaluation o2) {
            int result = o1.compareTo(o2);
            if (result == 0) {
                return defaultMoveComparator.compare(o1.move(), o2.move());
            }
            return result;
        }
    }
}
