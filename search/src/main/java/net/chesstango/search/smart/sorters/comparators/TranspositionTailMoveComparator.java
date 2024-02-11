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
import java.util.Optional;
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
    private Map<Short, Optional<TranspositionEntry>> moveToEntry;
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
    public void afterSearch() {
    }

    @Override
    public void beforeSort() {
        moveToEntry = new HashMap<>();
        currentTurn = game.getChessPosition().getCurrentTurn();
        currentMap = Color.WHITE.equals(currentTurn) ? minMap : maxMap;
    }

    @Override
    public void afterSort() {

    }

    @Override
    public int compare(Move o1, Move o2) {
        final Optional<TranspositionEntry> moveEntry1 = getTranspositionEntry(o1);
        final Optional<TranspositionEntry> moveEntry2 = getTranspositionEntry(o2);

        if (moveEntry1.isPresent() && moveEntry2.isPresent()) {
            int moveValue1 = TranspositionEntry.decodeValue(moveEntry1.get().movesAndValue);
            int moveValue2 = TranspositionEntry.decodeValue(moveEntry2.get().movesAndValue);
            return Color.WHITE.equals(currentTurn) ? Integer.compare(moveValue1, moveValue2) : Integer.compare(moveValue2, moveValue1);
        } else if (moveEntry1.isPresent()) {
            return 1;
        } else if (moveEntry2.isPresent()) {
            return -1;
        }

        return 0;
    }

    private Optional<TranspositionEntry> getTranspositionEntry(Move move) {
        final short key = move.binaryEncoding();
        return moveToEntry.computeIfAbsent(key, k -> {
            long zobristHashMove = game.getChessPosition().getZobristHash(move);
            TranspositionEntry entry = currentMap.read(zobristHashMove);
            return Objects.nonNull(entry) ? Optional.of(entry) : Optional.empty();
        });
    }
}
