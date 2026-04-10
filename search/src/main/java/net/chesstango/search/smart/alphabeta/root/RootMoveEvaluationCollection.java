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

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * @author Mauricio Coria
 */
public class RootMoveEvaluationCollection implements SearchByCycleListener, SearchByDepthListener, SearchByWindowsListener {

    private final List<RootMoveEvaluation> rootMoveEvaluations;

    @Getter
    private RootMoveEvaluation bestRootMoveEvaluation;

    @Setter
    private Game game;

    private boolean maximize;

    public RootMoveEvaluationCollection() {
        rootMoveEvaluations = new LinkedList<>();
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
    public void beforeSearchByWindows(int alphaBound, int betaBound, int searchByWindowsCycle) {
        if (searchByWindowsCycle > 0) {
            /**
             * Se busca nuevamente dentro de otra ventana, esta no es la lista definitiva.
             * Dejo resultado exactos dado que no es necesario volver a explorarlos.
             * Dejo resultados no exactos y que siguen estando dentro de los limites de la ventana actual.
             */
            rootMoveEvaluations.removeIf(moveEvaluation -> Bound.UPPER_BOUND.equals(moveEvaluation.bound()) && alphaBound <= moveEvaluation.evaluation());
            rootMoveEvaluations.removeIf(moveEvaluation -> Bound.LOWER_BOUND.equals(moveEvaluation.bound()) && moveEvaluation.evaluation() <= betaBound);
        }
    }

    public void save(RootMoveEvaluation moveEvaluation) {
        if (Bound.EXACT == moveEvaluation.bound()) {
            if (bestRootMoveEvaluation == null || rootMoveEvaluations.isEmpty()) {
                bestRootMoveEvaluation = moveEvaluation;
            } else if (maximize && moveEvaluation.evaluation() > bestRootMoveEvaluation.evaluation()) {
                bestRootMoveEvaluation = moveEvaluation;
            } else if (!maximize && moveEvaluation.evaluation() < bestRootMoveEvaluation.evaluation()) {
                bestRootMoveEvaluation = moveEvaluation;
            }
        }
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
}
