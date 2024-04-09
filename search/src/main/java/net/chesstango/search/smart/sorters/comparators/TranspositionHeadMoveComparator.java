package net.chesstango.search.smart.sorters.comparators;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.containers.MoveToHashMap;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.features.transposition.TTable;
import net.chesstango.search.smart.features.transposition.TranspositionEntry;

import java.util.Objects;
import java.util.function.Function;

/**
 * @author Mauricio Coria
 */
public class TranspositionHeadMoveComparator implements MoveComparator, SearchByCycleListener {

    private final Function<SearchByCycleContext, TTable> fnGetMaxMap;
    private final Function<SearchByCycleContext, TTable> fnGetMinMap;

    @Getter
    @Setter
    private MoveComparator next;
    private Game game;
    private TTable maxMap;
    private TTable minMap;
    private short bestMoveEncoded;

    public TranspositionHeadMoveComparator(Function<SearchByCycleContext, TTable> fnGetMaxMap, Function<SearchByCycleContext, TTable> fnGetMinMap) {
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
    public void beforeSort(final int currentPly, MoveToHashMap moveToZobrist) {
        final Color currentTurn = game.getChessPosition().getCurrentTurn();

        long hash = game.getChessPosition().getZobristHash();

        TranspositionEntry entry = Color.WHITE.equals(currentTurn) ?
                maxMap.read(hash) : minMap.read(hash);

        if (Objects.nonNull(entry)) {
            bestMoveEncoded = TranspositionEntry.decodeBestMove(entry.movesAndValue);
        } else {
            bestMoveEncoded = 0;
        }

        next.beforeSort(currentPly, moveToZobrist);
    }

    @Override
    public void afterSort(MoveToHashMap moveToZobrist) {
        next.afterSort(moveToZobrist);
    }


    @Override
    public int compare(Move o1, Move o2) {
        if (bestMoveEncoded != 0) {
            if (o1.binaryEncoding() == bestMoveEncoded) {
                return 1;
            } else if (o2.binaryEncoding() == bestMoveEncoded) {
                return -1;
            }
        }

        return next.compare(o1, o2);
    }
}
