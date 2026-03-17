package net.chesstango.search.smart.alphabeta.root;

import lombok.Getter;
import net.chesstango.board.moves.Move;
import net.chesstango.search.RootChildEvaluation;
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
public class RootChildEvaluationCollection implements SearchByDepthListener, SearchByWindowsListener {
    @Getter
    private List<RootChildEvaluation> moveEvaluations;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void beforeSearchByDepth() {
        this.moveEvaluations = new LinkedList<>();
    }

    @Override
    public void beforeSearchByWindows(int alphaBound, int betaBound, int searchByWindowsCycle) {
        if (searchByWindowsCycle > 0) {
            /**
             * Se busca nuevamente dentro de otra ventana, esta no es la lista definitiva.
             * Dejo resultado exactos dado que no es necesario volver a explorarlos.
             * Dejo resultados no exactos y que siguen estando dentro de los limites de la ventana actual.
             */
            moveEvaluations.removeIf(moveEvaluation -> Bound.UPPER_BOUND.equals(moveEvaluation.moveEvaluationType()) && alphaBound <= moveEvaluation.evaluation());
            moveEvaluations.removeIf(moveEvaluation -> Bound.LOWER_BOUND.equals(moveEvaluation.moveEvaluationType()) && moveEvaluation.evaluation() <= betaBound);
        }
    }

    public void add(RootChildEvaluation moveEvaluation) {
        this.moveEvaluations.add(moveEvaluation);
    }

    public Optional<RootChildEvaluation> getBestMoveEvaluation(boolean maximize) {
        Stream<RootChildEvaluation> moveEvaluationStream = moveEvaluations
                .stream()
                .filter(moveEvaluation -> Bound.EXACT.equals(moveEvaluation.moveEvaluationType()));

        return maximize ? moveEvaluationStream.max(Comparator.comparing(RootChildEvaluation::evaluation)) : moveEvaluationStream.min(Comparator.comparing(RootChildEvaluation::evaluation));
    }

    public Optional<RootChildEvaluation> get(Move currentMove) {
        for (RootChildEvaluation evaluatedMove : moveEvaluations) {
            if (evaluatedMove.move().equals(currentMove)) {
                return Optional.of(evaluatedMove);

            }
        }
        return Optional.empty();
    }
}
