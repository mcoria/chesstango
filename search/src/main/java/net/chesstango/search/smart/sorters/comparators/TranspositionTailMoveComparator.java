package net.chesstango.search.smart.sorters.comparators;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.transposition.TTable;
import net.chesstango.search.smart.transposition.TranspositionEntry;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * @author Mauricio Coria
 */
public class TranspositionTailMoveComparator implements MoveComparator, SearchByCycleListener {
    private final Function<SearchByCycleContext, TTable> fnGetMaxMap;
    private final Function<SearchByCycleContext, TTable> fnGetMinMap;
    private Game game;
    private TTable maxMap;
    private TTable minMap;

    private Map<Short, TranspositionEntry> moveToEntry;

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
    public void afterSearch() {
    }

    @Override
    public void beforeSort() {
        moveToEntry = new HashMap<>();
    }

    @Override
    public void afterSort() {

    }

    @Override
    public int compare(Move o1, Move o2) {
        final Color currentTurn = game.getChessPosition().getCurrentTurn();

        final TTable map = Color.WHITE.equals(currentTurn) ? minMap : maxMap;

        final TranspositionEntry moveEntry1 = getTranspositionEntry(o1, map);

        final TranspositionEntry moveEntry2 = getTranspositionEntry(o2, map);

        if (Objects.nonNull(moveEntry1) && Objects.nonNull(moveEntry2)) {
            int moveValue1 = TranspositionEntry.decodeValue(moveEntry1.movesAndValue);
            int moveValue2 = TranspositionEntry.decodeValue(moveEntry2.movesAndValue);
            return Color.WHITE.equals(currentTurn) ? Integer.compare(moveValue1, moveValue2) : Integer.compare(moveValue2, moveValue1);
        } else if (Objects.isNull(moveEntry1) && Objects.nonNull(moveEntry2)) {
            return -1;
        } else if (Objects.nonNull(moveEntry1) && Objects.isNull(moveEntry2)) {
            return 1;
        }

        return 0;
    }

    private TranspositionEntry getTranspositionEntry(Move move, TTable map) {
        final short key = move.binaryEncoding();
        return moveToEntry.computeIfAbsent(key, k -> {
            long zobristHashMove = game.getChessPosition().getZobristHash(move);
            return map.read(zobristHashMove);
        });
    }
}
