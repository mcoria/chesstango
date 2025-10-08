package net.chesstango.search.smart.alphabeta.filters.once;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.search.MoveEvaluation;
import net.chesstango.search.MoveEvaluationType;
import net.chesstango.search.SearchResultByDepth;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.*;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFilter;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFunction;
import net.chesstango.search.smart.features.transposition.TranspositionEntry;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Funciona como una cache de resultados y es un complemento de aspiration windows
 *
 * @author Mauricio Coria
 */
public class MoveEvaluationTracker implements AlphaBetaFilter, SearchByDepthListener, SearchByWindowsListener {

    @Setter
    @Getter
    private AlphaBetaFilter next;

    private List<MoveEvaluation> currentMoveEvaluations;

    @Setter
    private Game game;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void beforeSearchByDepth() {
        this.currentMoveEvaluations = new LinkedList<>();
    }

    @Override
    public void beforeSearchByWindows(int alphaBound, int betaBound, int searchByWindowsCycle) {
        if (searchByWindowsCycle > 0) {
            /**
             * Se busca nuevamente dentro de otra ventana, esta no es la lista definitiva.
             * Dejo resultado exactos dado que no es necesario volver a explorarlos.
             * Dejo resultados no exactos y que siguen estando dentro de los limites de la ventana actual.
             */
            currentMoveEvaluations.removeIf(moveEvaluation -> MoveEvaluationType.UPPER_BOUND.equals(moveEvaluation.moveEvaluationType()) && alphaBound <= moveEvaluation.evaluation());
            currentMoveEvaluations.removeIf(moveEvaluation -> MoveEvaluationType.LOWER_BOUND.equals(moveEvaluation.moveEvaluationType()) && moveEvaluation.evaluation() <= betaBound);
        }
    }

    @Override
    public void afterSearchByDepth(SearchResultByDepth result) {
        result.setMoveEvaluations(currentMoveEvaluations);
    }


    @Override
    public long maximize(int currentPly, int alpha, int beta) {
        return process(currentPly, alpha, beta, next::maximize);
    }

    @Override
    public long minimize(int currentPly, int alpha, int beta) {
        return process(currentPly, alpha, beta, next::minimize);
    }

    public Optional<MoveEvaluation> getBestMoveEvaluation(boolean maximize) {
        Stream<MoveEvaluation> exactEvaluationStream = currentMoveEvaluations
                .stream()
                .filter(moveEvaluation -> MoveEvaluationType.EXACT.equals(moveEvaluation.moveEvaluationType()));

        return maximize ? exactEvaluationStream.max(Comparator.comparing(MoveEvaluation::evaluation)) : exactEvaluationStream.min(Comparator.comparing(MoveEvaluation::evaluation));
    }


    protected long process(int currentPly, final int alpha, final int beta, AlphaBetaFunction fn) {
        Move currentMove = game.getHistory().peekLastRecord().playedMove();

        for (MoveEvaluation evaluatedMove : currentMoveEvaluations) {
            if (evaluatedMove.move().equals(currentMove)) {
                return TranspositionEntry.encode(evaluatedMove.move(), evaluatedMove.evaluation());
            }
        }

        long bestMoveAndValue = fn.search(currentPly, alpha, beta);

        trackMoveEvaluation(currentMove, bestMoveAndValue, alpha, beta);

        return bestMoveAndValue;
    }


    protected void trackMoveEvaluation(Move currentMove, long bestMoveAndValue, int alpha, int beta) {
        int currentValue = TranspositionEntry.decodeValue(bestMoveAndValue);

        MoveEvaluationType moveEvaluationType = null;

        if (currentValue <= alpha) {
            moveEvaluationType = MoveEvaluationType.UPPER_BOUND;
        } else if (beta <= currentValue) {
            moveEvaluationType = MoveEvaluationType.LOWER_BOUND;
        } else {
            moveEvaluationType = MoveEvaluationType.EXACT;
        }

        trackMoveEvaluation(new MoveEvaluation(currentMove, currentValue, moveEvaluationType));
    }

    protected void trackMoveEvaluation(MoveEvaluation moveEvaluation) {
        currentMoveEvaluations.add(moveEvaluation);
    }
}
