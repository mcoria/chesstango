package net.chesstango.search.smart.alphabeta.filters;

import net.chesstango.board.Game;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.BinaryUtils;
import net.chesstango.search.smart.SearchContext;
import net.chesstango.search.smart.transposition.TTable;
import net.chesstango.search.smart.transposition.TranspositionEntry;
import net.chesstango.search.smart.transposition.TranspositionType;

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
            long bestMoveAndValue;

            TranspositionEntry entry = maxMap.get(hash);

            if (entry == null) {
                entry = maxMap.allocate(hash);
            } else if (searchDepth <= entry.searchDepth) {
                // Es un valor exacto
                if (entry.transpositionType == TranspositionType.EXACT) {
                    return entry.bestMoveAndValue;
                } else if (entry.transpositionType == TranspositionType.LOWER_BOUND && beta <= entry.value) {
                    return entry.bestMoveAndValue;
                } else if (entry.transpositionType == TranspositionType.UPPER_BOUND && entry.value <= alpha) {
                    return entry.bestMoveAndValue;
                }
            }

            bestMoveAndValue = next.maximize(currentPly, alpha, beta);

            updateEntry(entry, hash, searchDepth, alpha, beta, bestMoveAndValue);

            return entry.bestMoveAndValue;
        }

        return next.maximize(currentPly, alpha, beta);
    }

    @Override
    public long minimize(final int currentPly, final int alpha, final int beta) {
        int searchDepth = maxPly - currentPly;

        if (searchDepth > 0 && game.getStatus().isInProgress()) {
            long hash = game.getChessPosition().getZobristHash();
            long bestMoveAndValue;

            TranspositionEntry entry = minMap.get(hash);

            if (entry == null) {
                entry = minMap.allocate(hash);
            } else if (searchDepth <= entry.searchDepth) {
                // Es un valor exacto
                if (entry.transpositionType == TranspositionType.EXACT) {
                    return entry.bestMoveAndValue;
                } else if (entry.transpositionType == TranspositionType.LOWER_BOUND && beta <= entry.value) {
                    return entry.bestMoveAndValue;
                } else if (entry.transpositionType == TranspositionType.UPPER_BOUND && entry.value <= alpha) {
                    return entry.bestMoveAndValue;
                }
            }

            bestMoveAndValue = next.minimize(currentPly, alpha, beta);

            updateEntry(entry, hash, searchDepth, alpha, beta, bestMoveAndValue);

            return entry.bestMoveAndValue;
        }

        return next.minimize(currentPly, alpha, beta);
    }

    public void setNext(AlphaBetaFilter next) {
        this.next = next;
    }

    protected void updateEntry(TranspositionEntry entry, long hash, int searchDepth, int alpha, int beta, long bestMoveAndValue) {
        int value = BinaryUtils.decodeValue(bestMoveAndValue);
        TranspositionType transpositionType;
        if (beta <= value) {
            transpositionType = TranspositionType.LOWER_BOUND;
        } else if (value <= alpha) {
            transpositionType = TranspositionType.UPPER_BOUND;
        } else {
            transpositionType = TranspositionType.EXACT;
        }

        entry.hash = hash;
        entry.searchDepth = searchDepth;
        entry.bestMoveAndValue = bestMoveAndValue;
        entry.value = value;
        entry.transpositionType = transpositionType;
    }
}
