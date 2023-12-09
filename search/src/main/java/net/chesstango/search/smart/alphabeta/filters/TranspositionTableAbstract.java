package net.chesstango.search.smart.alphabeta.filters;

import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.transposition.TTable;
import net.chesstango.search.smart.transposition.TranspositionBound;
import net.chesstango.search.smart.transposition.TranspositionEntry;

/**
 * @author Mauricio Coria
 */
public abstract class TranspositionTableAbstract implements AlphaBetaFilter, SearchByCycleListener {

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
    public void afterSearch(SearchMoveResult result) {
    }

    protected abstract boolean isTranspositionEntryValid(TranspositionEntry entry, long hash, int searchDepth);

    @Override
    public long maximize(final int currentPly, final int alpha, final int beta) {
        int searchDepth = maxPly > currentPly ? maxPly - currentPly : currentPly - maxPly;

        long hash = game.getChessPosition().getZobristHash();

        TranspositionEntry entry = maxMap.getForWrite(hash);

        if (isTranspositionEntryValid(entry, hash, searchDepth)) {
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

        long moveAndValue = next.maximize(currentPly, alpha, beta);

        updateEntry(entry, hash, searchDepth, alpha, beta, moveAndValue);

        return entry.movesAndValue;
    }

    @Override
    public long minimize(final int currentPly, final int alpha, final int beta) {
        int searchDepth = maxPly > currentPly ? maxPly - currentPly : currentPly - maxPly;

        long hash = game.getChessPosition().getZobristHash();

        TranspositionEntry entry = minMap.getForWrite(hash);

        if (isTranspositionEntryValid(entry, hash, searchDepth)) {
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

        long moveAndValue = next.minimize(currentPly, alpha, beta);

        updateEntry(entry, hash, searchDepth, alpha, beta, moveAndValue);

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
