package net.chesstango.search.smart.alphabeta.filters;

import net.chesstango.board.Game;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.BinaryUtils;
import net.chesstango.search.smart.SearchContext;

import java.util.Map;

import net.chesstango.search.smart.transposition.TranspositionType;

import net.chesstango.search.smart.transposition.TranspositionEntry;

/**
 * @author Mauricio Coria
 */
public class QTranspositionTable implements AlphaBetaFilter {
    private AlphaBetaFilter next;
    private Map<Long, TranspositionEntry> maxMap;
    private Map<Long, TranspositionEntry> minMap;
    private Game game;

    @Override
    public void beforeSearch(Game game, int maxDepth) {
        this.game = game;
    }

    @Override
    public void afterSearch(SearchMoveResult result) {
    }

    @Override
    public void beforeSearchByDepth(SearchContext context) {
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
        if (game.getStatus().isInProgress()) {
            long hash = game.getChessPosition().getZobristHash();
            long bestMoveAndValue;

            TranspositionEntry entry = maxMap.get(hash);

            if (entry == null) {
                entry = new TranspositionEntry();

                maxMap.put(hash, entry);

                bestMoveAndValue = next.maximize(currentPly, alpha, beta);
            } else {
                if (entry.bestMoveAndValue != 0) {
                    // Es un valor exacto
                    if (entry.transpositionType == TranspositionType.EXACT) {
                        return entry.bestMoveAndValue;
                    } else if (entry.transpositionType == TranspositionType.LOWER_BOUND && beta <= entry.value) {
                        return entry.bestMoveAndValue;
                    } else if (entry.transpositionType == TranspositionType.UPPER_BOUND && entry.value <= alpha) {
                        return entry.bestMoveAndValue;
                    }
                }

                if (entry.qBestMoveAndValue != 0) {
                    // Es un valor exacto
                    if (entry.qTranspositionType == TranspositionType.EXACT) {
                        return entry.qBestMoveAndValue;
                    } else if (entry.qTranspositionType == TranspositionType.LOWER_BOUND && beta <= entry.qValue) {
                        return entry.qBestMoveAndValue;
                    } else if (entry.qTranspositionType == TranspositionType.UPPER_BOUND && entry.qValue <= alpha) {
                        return entry.qBestMoveAndValue;
                    }
                }

                bestMoveAndValue = next.maximize(currentPly, alpha, beta);
            }

            updateQEntry(entry, alpha, beta, bestMoveAndValue);

            return entry.qBestMoveAndValue;
        }

        return next.maximize(currentPly, alpha, beta);
    }

    @Override
    public long minimize(final int currentPly, final int alpha, final int beta) {
        if (game.getStatus().isInProgress()) {
            long hash = game.getChessPosition().getZobristHash();
            long bestMoveAndValue;

            TranspositionEntry entry = minMap.get(hash);

            if (entry == null) {
                entry = new TranspositionEntry();

                minMap.put(hash, entry);

                bestMoveAndValue = next.minimize(currentPly, alpha, beta);
            } else {
                if (entry.bestMoveAndValue != 0) {
                    // Es un valor exacto
                    if (entry.transpositionType == TranspositionType.EXACT) {
                        return entry.bestMoveAndValue;
                    } else if (entry.transpositionType == TranspositionType.LOWER_BOUND && beta <= entry.value) {
                        return entry.bestMoveAndValue;
                    } else if (entry.transpositionType == TranspositionType.UPPER_BOUND && entry.value <= alpha) {
                        return entry.bestMoveAndValue;
                    }
                }

                if (entry.qBestMoveAndValue != 0) {
                    // Es un valor exacto
                    if (entry.qTranspositionType == TranspositionType.EXACT) {
                        return entry.qBestMoveAndValue;
                    } else if (entry.qTranspositionType == TranspositionType.LOWER_BOUND && beta <= entry.qValue) {
                        return entry.qBestMoveAndValue;
                    } else if (entry.qTranspositionType == TranspositionType.UPPER_BOUND && entry.qValue <= alpha) {
                        return entry.qBestMoveAndValue;
                    }
                }

                bestMoveAndValue = next.minimize(currentPly, alpha, beta);
            }

            updateQEntry(entry, alpha, beta, bestMoveAndValue);

            return entry.qBestMoveAndValue;
        }

        return next.minimize(currentPly, alpha, beta);
    }

    public void setNext(AlphaBetaFilter next) {
        this.next = next;
    }

    protected void updateQEntry(TranspositionEntry entry, int alpha, int beta, long bestMoveAndValue) {
        int value = BinaryUtils.decodeValue(bestMoveAndValue);
        TranspositionType transpositionType;
        if (beta <= value) {
            transpositionType = TranspositionType.LOWER_BOUND;
        } else if (value <= alpha) {
            transpositionType = TranspositionType.UPPER_BOUND;
        } else {
            transpositionType = TranspositionType.EXACT;
        }

        entry.qBestMoveAndValue = bestMoveAndValue;
        entry.qValue = value;
        entry.qTranspositionType = transpositionType;
    }
}
