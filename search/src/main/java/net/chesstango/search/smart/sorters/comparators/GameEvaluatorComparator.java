package net.chesstango.search.smart.sorters.comparators;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.evaluation.GameEvaluatorCacheRead;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.transposition.TranspositionEntry;

import java.util.Map;

/**
 * @author Mauricio Coria
 */
public class GameEvaluatorComparator implements MoveComparator, SearchByCycleListener {

    @Getter
    @Setter
    private MoveComparator next;

    @Getter
    @Setter
    private GameEvaluatorCacheRead gameEvaluatorCacheRead;

    private Game game;
    private Map<Short, Long> moveToZobrist;
    private Color currentTurn;

    @Override
    public void beforeSearch(SearchByCycleContext context) {
        this.game = context.getGame();

    }

    @Override
    public void beforeSort(Map<Short, Long> moveToZobrist) {
        this.moveToZobrist = moveToZobrist;

        this.currentTurn = game.getChessPosition().getCurrentTurn();

        next.beforeSort(moveToZobrist);
    }

    @Override
    public void afterSort(Map<Short, Long> moveToZobrist) {
        next.afterSort(moveToZobrist);
    }

    @Override
    public int compare(Move o1, Move o2) {
        int result = 0;

        final Integer moveEvaluation1 = getTranspositionEntry(o1);
        final Integer moveEvaluation2 = getTranspositionEntry(o2);

        if (moveEvaluation1 != null && moveEvaluation2 != null) {
            int evaluation1 = TranspositionEntry.decodeValue(moveEvaluation1);
            int evaluation2 = TranspositionEntry.decodeValue(moveEvaluation2);
            result = Color.WHITE.equals(currentTurn) ? Integer.compare(evaluation1, evaluation2) : Integer.compare(evaluation2, evaluation1);
        } else if (moveEvaluation1 != null) {
            return 1;
        } else if (moveEvaluation2 != null) {
            return -1;
        }

        return result == 0 ? next.compare(o1, o2) : result;
    }

    private Integer getTranspositionEntry(Move move) {

        final short moveEncoded = move.binaryEncoding();

        final long zobristHashMove = moveToZobrist.computeIfAbsent(moveEncoded, k -> game.getChessPosition().getZobristHash(move));

        return gameEvaluatorCacheRead.readFromCache(zobristHashMove);
    }

}
