package net.chesstango.search.smart.alphabeta.root;

import lombok.Getter;
import net.chesstango.board.moves.Move;
import net.chesstango.search.RootMoveEvaluation;
import net.chesstango.search.Bound;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.SearchByDepthListener;
import net.chesstango.search.smart.SearchByWindowsListener;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author Mauricio Coria
 */
public class RootMoveEvaluationCollection implements SearchByDepthListener, SearchByWindowsListener {
    @Getter
    private List<RootMoveEvaluation> rootMoveEvaluations;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void beforeSearchByDepth() {
        this.rootMoveEvaluations = new LinkedList<>();
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

    public void add(RootMoveEvaluation moveEvaluation) {
        this.rootMoveEvaluations.add(moveEvaluation);
    }

    /**
     * Con Aspiration Windows habilitado, si la ventana actual falla:
     * - Si maximizo, debiera retornar LOWER_BOUND o EXACT ?
     * - Si minimizo, debiera retornar UPPER_BOUND o EXACT ?
     */
    public Optional<RootMoveEvaluation> getBestMoveEvaluation(boolean maximize) {
        Stream<RootMoveEvaluation> moveEvaluationStream = rootMoveEvaluations
                .stream()
                .filter(moveEvaluation -> Bound.EXACT.equals(moveEvaluation.bound()));

        return maximize ? moveEvaluationStream.max(Comparator.comparing(RootMoveEvaluation::evaluation)) : moveEvaluationStream.min(Comparator.comparing(RootMoveEvaluation::evaluation));
    }

    public Optional<RootMoveEvaluation> get(Move currentMove) {
        for (RootMoveEvaluation evaluatedMove : rootMoveEvaluations) {
            if (evaluatedMove.move().equals(currentMove)) {
                return Optional.of(evaluatedMove);

            }
        }
        return Optional.empty();
    }
}
