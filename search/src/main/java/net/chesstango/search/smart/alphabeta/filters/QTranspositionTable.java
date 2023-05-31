package net.chesstango.search.smart.alphabeta.filters;

import net.chesstango.board.Game;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.BinaryUtils;
import net.chesstango.search.smart.SearchContext;

import java.util.Map;

import static net.chesstango.search.smart.Transposition.Type;

import net.chesstango.search.smart.Transposition;

/**
 * @author Mauricio Coria
 */
public class QTranspositionTable implements AlphaBetaFilter {
    private AlphaBetaFilter next;
    private Map<Long, Transposition> maxMap;
    private Map<Long, Transposition> minMap;
    private Game game;

    @Override
    public void initSearch(Game game, int maxDepth) {
        this.game = game;
    }

    @Override
    public void closeSearch(SearchMoveResult result) {
    }

    @Override
    public void init(SearchContext context) {
        this.maxMap = context.getMaxMap();
        this.minMap = context.getMinMap();
    }

    @Override
    public void close(SearchMoveResult result) {
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
            long hash = game.getChessPosition().getPositionHash();
            long bestMoveAndValue;

            Transposition entry = maxMap.get(hash);

            if (entry == null) {
                entry = new Transposition();

                maxMap.put(hash, entry);

                bestMoveAndValue = next.maximize(currentPly, alpha, beta);
            } else {
                if (entry.bestMoveAndValue != 0) {
                    // Es un valor exacto
                    if (entry.type == Type.EXACT) {
                        return entry.bestMoveAndValue;
                    } else if (entry.type == Type.LOWER_BOUND && beta <= entry.value) {
                        return entry.bestMoveAndValue;
                    } else if (entry.type == Type.UPPER_BOUND && entry.value <= alpha) {
                        return entry.bestMoveAndValue;
                    }
                }

                if (entry.qBestMoveAndValue != 0) {
                    // Es un valor exacto
                    if (entry.qType == Type.EXACT) {
                        return entry.qBestMoveAndValue;
                    } else if (entry.qType == Type.LOWER_BOUND && beta <= entry.qValue) {
                        return entry.qBestMoveAndValue;
                    } else if (entry.qType == Type.UPPER_BOUND && entry.qValue <= alpha) {
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
            long hash = game.getChessPosition().getPositionHash();
            long bestMoveAndValue;

            Transposition entry = minMap.get(hash);

            if (entry == null) {
                entry = new Transposition();

                minMap.put(hash, entry);

                bestMoveAndValue = next.minimize(currentPly, alpha, beta);
            } else {
                if (entry.bestMoveAndValue != 0) {
                    // Es un valor exacto
                    if (entry.type == Type.EXACT) {
                        return entry.bestMoveAndValue;
                    } else if (entry.type == Type.LOWER_BOUND && beta <= entry.value) {
                        return entry.bestMoveAndValue;
                    } else if (entry.type == Type.UPPER_BOUND && entry.value <= alpha) {
                        return entry.bestMoveAndValue;
                    }
                }

                if (entry.qBestMoveAndValue != 0) {
                    // Es un valor exacto
                    if (entry.qType == Type.EXACT) {
                        return entry.qBestMoveAndValue;
                    } else if (entry.qType == Type.LOWER_BOUND && beta <= entry.qValue) {
                        return entry.qBestMoveAndValue;
                    } else if (entry.qType == Type.UPPER_BOUND && entry.qValue <= alpha) {
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

    protected void updateQEntry(Transposition entry, int alpha, int beta, long bestMoveAndValue) {
        int value = BinaryUtils.decodeValue(bestMoveAndValue);
        Type type;
        if (beta <= value) {
            type = Type.LOWER_BOUND;
        } else if (value <= alpha) {
            type = Type.UPPER_BOUND;
        } else {
            type = Type.EXACT;
        }

        entry.qBestMoveAndValue = bestMoveAndValue;
        entry.qValue = value;
        entry.qType = type;
    }
}
