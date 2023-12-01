package net.chesstango.search.smart.alphabeta.filters.once;

import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.SearchContext;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFilter;
import net.chesstango.search.smart.transposition.TTable;
import net.chesstango.search.smart.transposition.TranspositionBound;
import net.chesstango.search.smart.transposition.TranspositionEntry;

/**
 * @author Mauricio Coria
 */
public class TranspositionTableFirst implements AlphaBetaFilter {
    @Setter
    private AlphaBetaFilter next;
    private TTable maxMap;
    private TTable minMap;

    private Game game;
    private int maxPly;

    @Override
    public void beforeSearch(Game game) {
        this.game = game;
    }

    @Override
    public void afterSearch(SearchMoveResult result) {

    }

    @Override
    public void beforeSearchByDepth(SearchContext context) {
        this.maxPly = context.getMaxPly();
        this.maxMap = context.getMaxMap();
        this.minMap = context.getMinMap();
    }

    @Override
    public void afterSearchByDepth(SearchMoveResult result) {
    }

    @Override
    public void stopSearching() {

    }

    @Override
    public void reset() {

    }

    @Override
    public long maximize(final int currentPly, final int alpha, final int beta) {
        int searchDepth = maxPly - currentPly;

        long hash = game.getChessPosition().getZobristHash();

        TranspositionEntry entry = maxMap.get(hash);

        long moveAndValue = next.maximize(currentPly, alpha, beta);

        updateEntry(entry, hash, searchDepth, alpha, beta, moveAndValue);

        return entry.movesAndValue;
    }

    @Override
    public long minimize(final int currentPly, final int alpha, final int beta) {
        int searchDepth = maxPly - currentPly;

        long hash = game.getChessPosition().getZobristHash();

        TranspositionEntry entry = minMap.get(hash);

        long moveAndValue = next.minimize(currentPly, alpha, beta);

        updateEntry(entry, hash, searchDepth, alpha, beta, moveAndValue);

        return entry.movesAndValue;
    }

    protected void updateEntry(TranspositionEntry entry, long hash, int searchDepth, int alpha, int beta, long moveAndValue) {
        int value = TranspositionEntry.decodeValue(moveAndValue);

        TranspositionBound transpositionBound;
        if (beta <= value) {
            transpositionBound = TranspositionBound.LOWER_BOUND;
        } else if (value <= alpha) {
            transpositionBound = TranspositionBound.UPPER_BOUND;
        } else {
            transpositionBound = TranspositionBound.EXACT;
        }

        entry.hash = hash;
        entry.searchDepth = searchDepth;
        entry.transpositionBound = transpositionBound;
        entry.movesAndValue = moveAndValue;
    }
}
