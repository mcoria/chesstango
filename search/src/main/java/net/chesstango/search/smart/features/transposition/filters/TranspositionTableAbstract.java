package net.chesstango.search.smart.features.transposition.filters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.search.smart.SearchByDepthContext;
import net.chesstango.search.smart.SearchByDepthListener;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFilter;
import net.chesstango.search.smart.features.transposition.TTable;
import net.chesstango.search.smart.features.transposition.TranspositionBound;
import net.chesstango.search.smart.features.transposition.TranspositionEntry;

/**
 * @author Mauricio Coria
 */
@Setter
@Getter
public abstract class TranspositionTableAbstract implements AlphaBetaFilter {


    private AlphaBetaFilter next;
    protected TTable maxMap;
    protected TTable minMap;
    private Game game;

    @Setter
    private int maxPly;

    protected abstract boolean isTranspositionEntryValid(TranspositionEntry entry, long hash, int searchDepth);

    @Override
    public long maximize(final int currentPly, final int alpha, final int beta) {
        int searchDepth = Math.abs(maxPly - currentPly);

        long hash = game.getPosition().getZobristHash();

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

        long hash = game.getPosition().getZobristHash();

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
