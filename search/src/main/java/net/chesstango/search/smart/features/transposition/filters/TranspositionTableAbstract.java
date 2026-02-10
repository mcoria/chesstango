package net.chesstango.search.smart.features.transposition.filters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFilter;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaHelper;
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
            // Es un valor exacto
            if (entry.transpositionBound == TranspositionBound.EXACT) {
                return AlphaBetaHelper.encode(entry.move, entry.value);
            } else if (entry.transpositionBound == TranspositionBound.LOWER_BOUND && beta <= entry.value) {
                return AlphaBetaHelper.encode(entry.move, entry.value);
            } else if (entry.transpositionBound == TranspositionBound.UPPER_BOUND && entry.value <= alpha) {
                return AlphaBetaHelper.encode(entry.move, entry.value);
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
            // Es un valor exacto
            if (entry.transpositionBound == TranspositionBound.EXACT) {
                return AlphaBetaHelper.encode(entry.move, entry.value);
            } else if (entry.transpositionBound == TranspositionBound.LOWER_BOUND && beta <= entry.value) {
                return AlphaBetaHelper.encode(entry.move, entry.value);
            } else if (entry.transpositionBound == TranspositionBound.UPPER_BOUND && entry.value <= alpha) {
                return AlphaBetaHelper.encode(entry.move, entry.value);
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
        int value = AlphaBetaHelper.decodeValue(moveAndValue);

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
