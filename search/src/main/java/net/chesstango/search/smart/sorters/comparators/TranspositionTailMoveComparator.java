package net.chesstango.search.smart.sorters.comparators;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.transposition.TTable;
import net.chesstango.search.smart.transposition.TranspositionEntry;

import java.util.Map;
import java.util.function.Function;

/**
 * @author Mauricio Coria
 */
public class TranspositionTailMoveComparator implements MoveComparator, SearchByCycleListener {
    private final Function<SearchByCycleContext, TTable> fnGetMaxMap;
    private final Function<SearchByCycleContext, TTable> fnGetMinMap;

    @Getter
    @Setter
    private MoveComparator next;

    private Game game;
    private TTable maxMap;
    private TTable minMap;
    private Map<Short, Long> moveToZobrist;
    private Color currentTurn;
    private TTable currentMap;

    public TranspositionTailMoveComparator(Function<SearchByCycleContext, TTable> fnGetMaxMap, Function<SearchByCycleContext, TTable> fnGetMinMap) {
        this.fnGetMaxMap = fnGetMaxMap;
        this.fnGetMinMap = fnGetMinMap;
    }

    @Override
    public void beforeSearch(SearchByCycleContext context) {
        this.game = context.getGame();
        this.maxMap = fnGetMaxMap.apply(context);
        this.minMap = fnGetMinMap.apply(context);
    }

    @Override
    public void beforeSort(final int currentPly, Map<Short, Long> moveToZobrist) {
        this.moveToZobrist = moveToZobrist;
        this.currentTurn = game.getChessPosition().getCurrentTurn();
        this.currentMap = Color.WHITE.equals(currentTurn) ? minMap : maxMap;

        next.beforeSort(currentPly, moveToZobrist);
    }

    @Override
    public void afterSort(Map<Short, Long> moveToZobrist) {
        next.afterSort(moveToZobrist);
    }

    @Override
    public int compare(Move o1, Move o2) {
        int result = 0;

        final TranspositionEntry moveEntry1 = getTranspositionEntry(o1);
        final TranspositionEntry moveEntry2 = getTranspositionEntry(o2);

        if (moveEntry1 != null && moveEntry2 != null) {
            int moveValue1 = TranspositionEntry.decodeValue(moveEntry1.movesAndValue);
            int moveValue2 = TranspositionEntry.decodeValue(moveEntry2.movesAndValue);
            result = Color.WHITE.equals(currentTurn) ? Integer.compare(moveValue1, moveValue2) : Integer.compare(moveValue2, moveValue1);
        } else if (moveEntry1 != null) {
            return 1;
        } else if (moveEntry2 != null) {
            return -1;
        }

        return result == 0 ? next.compare(o1, o2) : result;
    }

    private TranspositionEntry getTranspositionEntry(Move move) {
        final short moveEncoded = move.binaryEncoding();

        final long zobristHashMove = moveToZobrist.computeIfAbsent(moveEncoded, k -> game.getChessPosition().getZobristHash(move));

        return currentMap.read(zobristHashMove);
    }
}
