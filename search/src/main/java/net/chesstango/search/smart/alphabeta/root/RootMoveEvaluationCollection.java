package net.chesstango.search.smart.alphabeta.root;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.search.Bound;
import net.chesstango.search.RootMoveEvaluation;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.SearchByDepthListener;
import net.chesstango.search.smart.SearchByWindowsListener;
import net.chesstango.search.smart.sorters.comparators.DefaultMoveComparator;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * @author Mauricio Coria
 */
public class RootMoveEvaluationCollection implements SearchByCycleListener, SearchByDepthListener, SearchByWindowsListener {

    private final List<RootMoveEvaluation> rootMoveEvaluations;

    private final RootMoveEvaluationComparator whiteRootMoveEvaluationComparator;
    private final RootMoveEvaluationComparator blackRootMoveEvaluationComparator;

    @Getter
    private RootMoveEvaluation bestRootMoveEvaluation;

    @Setter
    private Game game;

    private boolean maximize;


    public RootMoveEvaluationCollection() {
        rootMoveEvaluations = new LinkedList<>();
        whiteRootMoveEvaluationComparator = new RootMoveEvaluationCollection.RootMoveEvaluationComparator(Color.WHITE);
        blackRootMoveEvaluationComparator = new RootMoveEvaluationCollection.RootMoveEvaluationComparator(Color.BLACK);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void beforeSearch() {
        rootMoveEvaluations.clear();
        bestRootMoveEvaluation = null;
        maximize = Color.WHITE.equals(game.getPosition().getCurrentTurn());
    }

    @Override
    public void beforeSearchByDepth() {
        rootMoveEvaluations.clear();
    }

    @Override
    public void afterSearchByDepth() {
        //En caso de stop inmediatamente se completó DEPTH = 1
        if (!rootMoveEvaluations.isEmpty()) {
            rootMoveEvaluations.sort(maximize ? whiteRootMoveEvaluationComparator : blackRootMoveEvaluationComparator);
            bestRootMoveEvaluation = rootMoveEvaluations.getFirst();
        }
    }

    @Override
    public void beforeSearchByWindows(int alpha, int beta, int searchByWindowsCycle) {
        if (searchByWindowsCycle > 0) {
            /**
             * Se busca nuevamente dentro de otra ventana, esta no es la lista definitiva.
             * Dejo resultado exactos dado que no es necesario volver a explorarlos.
             * Dejo resultados no exactos y que siguen estando dentro de los limites de la ventana actual.
             */
            rootMoveEvaluations.removeIf(moveEvaluation -> Bound.UPPER_BOUND.equals(moveEvaluation.bound()) && alpha <= moveEvaluation.evaluation());
            rootMoveEvaluations.removeIf(moveEvaluation -> Bound.LOWER_BOUND.equals(moveEvaluation.bound()) && moveEvaluation.evaluation() <= beta);
        }
    }

    public void save(RootMoveEvaluation moveEvaluation) {
        rootMoveEvaluations.add(moveEvaluation);
    }

    public Optional<RootMoveEvaluation> get(Move currentMove) {
        for (RootMoveEvaluation evaluatedMove : rootMoveEvaluations) {
            if (evaluatedMove.move().equals(currentMove)) {
                return Optional.of(evaluatedMove);
            }
        }
        return Optional.empty();
    }

    public List<RootMoveEvaluation> getRootMoveEvaluations() {
        return List.copyOf(rootMoveEvaluations);
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
