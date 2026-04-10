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
    private final RootMoveEvaluationComparator whiteRootMoveEvaluationComparator;
    private final RootMoveEvaluationComparator blackRootMoveEvaluationComparator;

    @Getter
    @Setter
    private MoveSorter next;

    @Setter
    private Game game;

    @Setter
    private List<RootMoveEvaluation> lastRootMoveEvaluations;


    private boolean maximize;

    private int numberOfMove;

    public RootMoveSorter() {
        this.whiteRootMoveEvaluationComparator = new RootMoveEvaluationComparator(Color.WHITE);
        this.blackRootMoveEvaluationComparator = new RootMoveEvaluationComparator(Color.BLACK);
    }

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

        if (lastRootMoveEvaluations.size() != numberOfMove) {
            throw new RuntimeException("Not all move were explorer during last iteration");
        }

        lastRootMoveEvaluationsCopy.sort(maximize ? whiteRootMoveEvaluationComparator : blackRootMoveEvaluationComparator);

        if (Bound.EXACT != lastRootMoveEvaluationsCopy.getFirst().bound()) {
            throw new RuntimeException("First move bound is not exact after sorting");
        }

        return lastRootMoveEvaluations.stream().map(RootMoveEvaluation::move).toList();
    }

    /**
     * @author Mauricio Coria
     */
    public static class RootMoveEvaluationComparator implements Comparator<RootMoveEvaluation> {
        private final Comparator<RootMoveEvaluation> rootMoveEvaluationComparator;

        public RootMoveEvaluationComparator(Color color) {
            DefaultMoveComparator defaultMoveComparator = new DefaultMoveComparator();
            this.rootMoveEvaluationComparator = Color.WHITE.equals(color)
                    ? Comparator
                      .comparing(RootMoveEvaluation::bound, Comparator.reverseOrder())
                      .thenComparing(RootMoveEvaluation::evaluation, Comparator.reverseOrder())         // De mayor a menor
                      .thenComparing((o1, o2) -> defaultMoveComparator.reversed().compare(o1.move(), o2.move()))
                    : Comparator
                      .comparing(RootMoveEvaluation::bound)
                      .thenComparing(RootMoveEvaluation::evaluation)                                   // De menor a mayor: natural order
                      .thenComparing((o1, o2) -> defaultMoveComparator.reversed().compare(o1.move(), o2.move()));

        }

        @Override
        public int compare(RootMoveEvaluation o1, RootMoveEvaluation o2) {
            return rootMoveEvaluationComparator.compare(o1, o2);
        }
    }
}
