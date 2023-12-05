package net.chesstango.search.smart.alphabeta.filters;

import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.SearchContext;
import net.chesstango.search.smart.transposition.TTable;
import net.chesstango.search.smart.transposition.TranspositionBound;
import net.chesstango.search.smart.transposition.TranspositionEntry;

/**
 * @author Mauricio Coria
 */
public class TranspositionTableQ implements AlphaBetaFilter {
    @Setter
    private AlphaBetaFilter next;
    private TTable maxMap;
    private TTable minMap;
    private Game game;

    @Override
    public void beforeSearch(Game game) {
        this.game = game;
    }

    @Override
    public void afterSearch(SearchMoveResult result) {
    }

    @Override
    public void beforeSearchByDepth(SearchContext context) {
        this.maxMap = context.getQMaxMap();
        this.minMap = context.getQMinMap();
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
        long hash = game.getChessPosition().getZobristHash();

        TranspositionEntry entry = maxMap.getForWrite(hash);

        if (entry.isStored(hash)) {
            int value = TranspositionEntry.decodeValue(entry.movesAndValue);
            // Es un valor exacto
            if (entry.transpositionBound == TranspositionBound.EXACT) {
                return entry.movesAndValue;
            } else if (entry.transpositionBound == TranspositionBound.LOWER_BOUND && beta <= value) {
                return entry.movesAndValue;
            } else if (entry.transpositionBound == TranspositionBound.UPPER_BOUND && value <= alpha) {
                return entry.movesAndValue;
            }
        }

        long bestMoveAndValue = next.maximize(currentPly, alpha, beta);

        updateQEntry(entry, hash, alpha, beta, bestMoveAndValue);

        return entry.movesAndValue;
    }

    @Override
    public long minimize(final int currentPly, final int alpha, final int beta) {
        long hash = game.getChessPosition().getZobristHash();

        TranspositionEntry entry = minMap.getForWrite(hash);

        if (entry.isStored(hash)) {
            int value = TranspositionEntry.decodeValue(entry.movesAndValue);
            // Es un valor exacto
            if (entry.transpositionBound == TranspositionBound.EXACT) {
                return entry.movesAndValue;
            } else if (entry.transpositionBound == TranspositionBound.LOWER_BOUND && beta <= value) {
                return entry.movesAndValue;
            } else if (entry.transpositionBound == TranspositionBound.UPPER_BOUND && value <= alpha) {
                return entry.movesAndValue;
            }
        }

        long bestMoveAndValue = next.minimize(currentPly, alpha, beta);

        updateQEntry(entry, hash, alpha, beta, bestMoveAndValue);

        return entry.movesAndValue;
    }

    protected void updateQEntry(TranspositionEntry entry, long hash, int alpha, int beta, long moveAndValue) {
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
        entry.transpositionBound = transpositionBound;
        entry.movesAndValue = moveAndValue;
    }
}
