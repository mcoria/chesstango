package net.chesstango.search.smart.alphabeta.filters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.*;
import net.chesstango.search.smart.transposition.TTable;
import net.chesstango.search.smart.transposition.TranspositionBound;
import net.chesstango.search.smart.transposition.TranspositionEntry;

import java.util.List;

/**
 * @author Mauricio Coria
 */
public abstract class TranspositionTableAbstract implements AlphaBetaFilter, SearchByCycleListener, SearchByDepthListener, SearchPvListener {

    @Setter
    @Getter
    private AlphaBetaFilter next;

    protected TTable maxMap;
    protected TTable minMap;

    protected Game game;

    protected int maxPly;
    private boolean trackPV;

    @Override
    public void beforeSearch(SearchByCycleContext context) {
        this.game = context.getGame();
    }

    @Override
    public void afterSearch() {
    }

    @Override
    public void beforeSearchByDepth(SearchByDepthContext context) {
        this.maxPly = context.getMaxPly();
        this.trackPV = false;
    }

    @Override
    public void afterSearchByDepth(SearchMoveResult result) {
    }

    @Override
    public void beforePVSearch(int bestValue) {
        trackPV = true;
    }

    @Override
    public void afterPVSearch(List<Move> principalVariation) {
        trackPV = false;
    }

    protected abstract boolean isTranspositionEntryValid(TranspositionEntry entry, long hash, int searchDepth);

    protected abstract int getDepth(int currentPly);

    @Override
    public long maximize(final int currentPly, final int alpha, final int beta) {
        if (trackPV) {
            return next.maximize(currentPly, alpha, beta);
        }

        int searchDepth = getDepth(currentPly);

        long hash = game.getChessPosition().getZobristHash();

        TranspositionEntry entry = maxMap.read(hash);

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

        writeTransposition(maxMap, hash, searchDepth, alpha, beta, moveAndValue);

        return moveAndValue;
    }

    @Override
    public long minimize(final int currentPly, final int alpha, final int beta) {
        if (trackPV) {
            return next.minimize(currentPly, alpha, beta);
        }

        int searchDepth = getDepth(currentPly);

        long hash = game.getChessPosition().getZobristHash();

        TranspositionEntry entry = minMap.read(hash);

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

        writeTransposition(minMap, hash, searchDepth, alpha, beta, moveAndValue);

        return moveAndValue;
    }

    protected void writeTransposition(TTable table, long hash, int depth, int alpha, int beta, long moveAndValue) {
        int value = TranspositionEntry.decodeValue(moveAndValue);

        TranspositionBound transpositionBound;
        if (beta <= value) {
            transpositionBound = TranspositionBound.LOWER_BOUND;
        } else if (value <= alpha) {
            transpositionBound = TranspositionBound.UPPER_BOUND;
        } else {
            transpositionBound = TranspositionBound.EXACT;
        }

        table.write(hash, depth, moveAndValue, transpositionBound);
    }
}
