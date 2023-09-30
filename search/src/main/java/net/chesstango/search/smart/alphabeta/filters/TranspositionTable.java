package net.chesstango.search.smart.alphabeta.filters;

import net.chesstango.board.Game;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.SearchContext;
import net.chesstango.search.smart.transposition.TTable;
import net.chesstango.search.smart.transposition.TranspositionEntry;
import net.chesstango.search.smart.transposition.TranspositionBound;

/**
 * @author Mauricio Coria
 */
public class TranspositionTable implements AlphaBetaFilter {
    private AlphaBetaFilter next;
    private TTable maxMap;
    private TTable minMap;

    private Game game;
    private int maxPly;

    @Override
    public void beforeSearch(Game game, int maxDepth) {
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

        if (searchDepth > 0 && game.getStatus().isInProgress()) {
            long hash = game.getChessPosition().getZobristHash();
            TranspositionEntry entry = maxMap.get(hash);

            if (entry == null) {
                entry = maxMap.allocate(hash);
            } else if (searchDepth <= entry.searchDepth) {
                int value = TranspositionEntry.decodeValue(entry.boundMoveValue);
                TranspositionBound transpositionBound = TranspositionEntry.decodeBound(entry.boundMoveValue);
                // Es un valor exacto
                if (transpositionBound == TranspositionBound.EXACT) {
                    return entry.boundMoveValue;
                } else if (transpositionBound == TranspositionBound.LOWER_BOUND && beta <= value) {
                    return entry.boundMoveValue;
                } else if (transpositionBound == TranspositionBound.UPPER_BOUND && value <= alpha) {
                    return entry.boundMoveValue;
                }
            }

            long bestMoveAndValue = next.maximize(currentPly, alpha, beta);

            updateEntry(entry, hash, searchDepth, alpha, beta, bestMoveAndValue);

            return entry.boundMoveValue;
        }

        return next.maximize(currentPly, alpha, beta);
    }

    @Override
    public long minimize(final int currentPly, final int alpha, final int beta) {
        int searchDepth = maxPly - currentPly;

        if (searchDepth > 0 && game.getStatus().isInProgress()) {
            long hash = game.getChessPosition().getZobristHash();

            TranspositionEntry entry = minMap.get(hash);

            if (entry == null) {
                entry = minMap.allocate(hash);
            } else if (searchDepth <= entry.searchDepth) {
                int value = TranspositionEntry.decodeValue(entry.boundMoveValue);
                TranspositionBound transpositionBound = TranspositionEntry.decodeBound(entry.boundMoveValue);
                // Es un valor exacto
                if (transpositionBound == TranspositionBound.EXACT) {
                    return entry.boundMoveValue;
                } else if (transpositionBound == TranspositionBound.LOWER_BOUND && beta <= value) {
                    return entry.boundMoveValue;
                } else if (transpositionBound == TranspositionBound.UPPER_BOUND && value <= alpha) {
                    return entry.boundMoveValue;
                }
            }

            long bestMoveAndValue = next.minimize(currentPly, alpha, beta);

            updateEntry(entry, hash, searchDepth, alpha, beta, bestMoveAndValue);

            return entry.boundMoveValue;
        }

        return next.minimize(currentPly, alpha, beta);
    }

    public void setNext(AlphaBetaFilter next) {
        this.next = next;
    }

    protected void updateEntry(TranspositionEntry entry, long hash, int searchDepth, int alpha, int beta, long bestMoveAndValue) {
        int value = TranspositionEntry.decodeValue(bestMoveAndValue);
        short move = TranspositionEntry.decodeMove(bestMoveAndValue);

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
        entry.boundMoveValue = TranspositionEntry.encode(transpositionBound, move, value);
    }
}
