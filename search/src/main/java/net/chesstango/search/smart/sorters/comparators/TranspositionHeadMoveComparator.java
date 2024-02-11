package net.chesstango.search.smart.sorters.comparators;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.transposition.TTable;
import net.chesstango.search.smart.transposition.TranspositionEntry;

import java.util.Objects;
import java.util.function.Function;

/**
 * @author Mauricio Coria
 */
public class TranspositionHeadMoveComparator implements MoveComparator, SearchByCycleListener {
    private final Function<SearchByCycleContext, TTable> fnGetMaxMap;
    private final Function<SearchByCycleContext, TTable> fnGetMinMap;
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
    public void afterSearch() {
    }

    @Override
    public void beforeSort() {
        final Color currentTurn = game.getChessPosition().getCurrentTurn();

        long hash = game.getChessPosition().getZobristHash();

        TranspositionEntry entry = Color.WHITE.equals(currentTurn) ?
                maxMap.read(hash) : minMap.read(hash);

        if (Objects.nonNull(entry)) {
            bestMoveEncoded = TranspositionEntry.decodeBestMove(entry.movesAndValue);
        } else {
            bestMoveEncoded = 0;
        }
    }

    @Override
    public void afterSort() {
    }


    @Override
    public int compare(Move o1, Move o2) {
        if (o1.binaryEncoding() == bestMoveEncoded) {
            return 1;
        } else if (o2.binaryEncoding() == bestMoveEncoded) {
            return -1;
        }

        return 0;
    }
}
