package net.chesstango.search.smart.sorters.comparators;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.transposition.TTable;
import net.chesstango.search.smart.transposition.TranspositionEntry;

import java.util.Comparator;
import java.util.Objects;
import java.util.function.Function;

/**
 * @author Mauricio Coria
 */
public class TranspositionTailMoveComparator implements Comparator<Move>, SearchByCycleListener {
    private final Function<SearchByCycleContext, TTable> fnGetMaxMap;
    private final Function<SearchByCycleContext, TTable> fnGetMinMap;
    private Game game;
    private TTable maxMap;
    private TTable minMap;

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
    public int compare(Move o1, Move o2) {
        final Color currentTurn = game.getChessPosition().getCurrentTurn();

        final TTable map = Color.WHITE.equals(currentTurn) ? minMap : maxMap;

        final long zobristHashMove1 = game.getChessPosition().getZobristHash(o1);

        final long zobristHashMove2 = game.getChessPosition().getZobristHash(o2);

        final TranspositionEntry moveEntry1 = map.read(zobristHashMove1);

        final TranspositionEntry moveEntry2 = map.read(zobristHashMove2);


        if (Objects.nonNull(moveEntry1) && Objects.nonNull(moveEntry2)) {
            int moveValue1 = TranspositionEntry.decodeValue(moveEntry1.movesAndValue);
            int moveValue2 = TranspositionEntry.decodeValue(moveEntry2.movesAndValue);
            if (moveValue1 != moveValue2) {
                return Color.WHITE.equals(currentTurn) ? Integer.compare(moveValue1, moveValue2) : Integer.compare(moveValue2, moveValue1);
            }
        } else if (Objects.isNull(moveEntry1) && Objects.nonNull(moveEntry2)) {
            return -1;
        } else if (Objects.nonNull(moveEntry1) && Objects.isNull(moveEntry2)) {
            return 1;
        }

        return 0;
    }
}
