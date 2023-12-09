package net.chesstango.search.smart.alphabeta.filters.once;

import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.SearchByDepthListener;
import net.chesstango.search.smart.SearchContext;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFilter;
import net.chesstango.search.smart.transposition.TTable;
import net.chesstango.search.smart.transposition.TranspositionBound;
import net.chesstango.search.smart.transposition.TranspositionEntry;

/**
 * @author Mauricio Coria
 */
public class TranspositionTableRoot implements AlphaBetaFilter, SearchByCycleListener, SearchByDepthListener {

    @Setter
    private AlphaBetaFilter next;
    protected TTable maxMap;
    protected TTable minMap;
    protected Game game;
    protected int maxPly;

    @Override
    public void beforeSearch(Game game) {
        this.game = game;
    }

    @Override
    public void beforeSearchByDepth(SearchContext context) {
        this.maxPly = context.getMaxPly();
        this.maxMap = context.getMaxMap();
        this.minMap = context.getMinMap();
    }

    @Override
    public void afterSearch(SearchMoveResult result) {
    }

    @Override
    public void afterSearchByDepth(SearchMoveResult result) {
    }


    @Override
    public long maximize(final int currentPly, final int alpha, final int beta) {
        long moveAndValue = next.maximize(currentPly, alpha, beta);

        long hash = game.getChessPosition().getZobristHash();

        TranspositionEntry entry = maxMap.getForWrite(hash);

        updateEntry(entry, hash, maxPly, alpha, beta, moveAndValue);

        return entry.movesAndValue;
    }

    @Override
    public long minimize(final int currentPly, final int alpha, final int beta) {
        long moveAndValue = next.minimize(currentPly, alpha, beta);

        long hash = game.getChessPosition().getZobristHash();

        TranspositionEntry entry = minMap.getForWrite(hash);

        updateEntry(entry, hash, maxPly, alpha, beta, moveAndValue);

        return entry.movesAndValue;
    }

    protected void updateEntry(TranspositionEntry entry, long hash, int depth, int alpha, int beta, long moveAndValue) {
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
        entry.searchDepth = depth;
        entry.transpositionBound = transpositionBound;
        entry.movesAndValue = moveAndValue;
    }
}
