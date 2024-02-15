package net.chesstango.search.smart.sorters.comparators;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.evaluation.GameEvaluatorCache;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.transposition.TranspositionEntry;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Mauricio Coria
 */
public class GameEvaluatorComparator implements MoveComparator, SearchByCycleListener {

    @Getter
    @Setter
    private MoveComparator next;

    @Getter
    @Setter
    private GameEvaluatorCache gameEvaluatorCache;

    private Game game;
    private Map<Short, Optional<Integer>> moveToEvaluation;
    private Color currentTurn;

    @Override
    public void beforeSearch(SearchByCycleContext context) {
        this.game = context.getGame();

    }

    @Override
    public void beforeSort() {
        moveToEvaluation = new HashMap<>();

        currentTurn = game.getChessPosition().getCurrentTurn();

        next.beforeSort();
    }

    @Override
    public void afterSort() {
        next.afterSort();
    }

    @Override
    public int compare(Move o1, Move o2) {
        int result = 0;

        final Optional<Integer> moveEntry1 = getTranspositionEntry(o1);
        final Optional<Integer> moveEntry2 = getTranspositionEntry(o2);

        if (moveEntry1.isPresent() && moveEntry2.isPresent()) {
            int evaluation1 = TranspositionEntry.decodeValue(moveEntry1.get());
            int evaluation2 = TranspositionEntry.decodeValue(moveEntry2.get());
            result = Color.WHITE.equals(currentTurn) ? Integer.compare(evaluation1, evaluation2) : Integer.compare(evaluation2, evaluation1);
        } else if (moveEntry1.isPresent()) {
            return 1;
        } else if (moveEntry2.isPresent()) {
            return -1;
        }

        return result == 0 ? next.compare(o1, o2) : result;
    }

    private Optional<Integer> getTranspositionEntry(Move move) {
        final short key = move.binaryEncoding();
        return moveToEvaluation.computeIfAbsent(key, k -> {
            long zobristHashMove = game.getChessPosition().getZobristHash(move);
            Integer evaluation = gameEvaluatorCache.readFromCache(zobristHashMove);
            return Objects.nonNull(evaluation) ? Optional.of(evaluation) : Optional.empty();
        });
    }

}
