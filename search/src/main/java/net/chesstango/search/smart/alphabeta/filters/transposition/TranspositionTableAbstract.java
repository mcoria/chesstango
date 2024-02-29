package net.chesstango.search.smart.alphabeta.filters.transposition;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.SearchByDepthContext;
import net.chesstango.search.smart.SearchByDepthListener;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFilter;
import net.chesstango.search.smart.transposition.TTable;
import net.chesstango.search.smart.transposition.TranspositionBound;
import net.chesstango.search.smart.transposition.TranspositionEntry;

/**
 * @author Mauricio Coria
 */
public abstract class TranspositionTableAbstract implements AlphaBetaFilter, SearchByCycleListener, SearchByDepthListener {

    @Setter
    @Getter
    private AlphaBetaFilter next;

    protected TTable maxMap;
    protected TTable minMap;

    protected Game game;

    protected int maxPly;

    @Override
    public void beforeSearch(SearchByCycleContext context) {
        this.game = context.getGame();
    }

    @Override
    public void beforeSearchByDepth(SearchByDepthContext context) {
        this.maxPly = context.getMaxPly();
    }

    protected abstract boolean isTranspositionEntryValid(TranspositionEntry entry, long hash, int searchDepth);

    @Override
    public long maximize(final int currentPly, final int alpha, final int beta) {
        int searchDepth = Math.abs(maxPly - currentPly);

        long hash = game.getChessPosition().getZobristHash();

        TranspositionEntry entry = maxMap.read(hash);

        if (entry != null && isTranspositionEntryValid(entry, hash, searchDepth)) {
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

        /**
         * Aca deberiamos llamar a la estrategia para deterimanr si reemplazamos o no
         */

        writeTransposition(maxMap, hash, searchDepth, alpha, beta, moveAndValue);

        return moveAndValue;
    }

    @Override
    public long minimize(final int currentPly, final int alpha, final int beta) {
        int searchDepth = Math.abs(maxPly - currentPly);

        long hash = game.getChessPosition().getZobristHash();

        TranspositionEntry entry = minMap.read(hash);

        if (entry != null && isTranspositionEntryValid(entry, hash, searchDepth)) {
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

        /**
         * Aca deberiamos llamar a la estrategia para deterimanr si reemplazamos o no
         */

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
