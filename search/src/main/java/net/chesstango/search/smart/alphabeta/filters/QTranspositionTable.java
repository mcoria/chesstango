package net.chesstango.search.smart.alphabeta.filters;

import net.chesstango.board.Game;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.BinaryUtils;
import net.chesstango.search.smart.SearchContext;

import java.util.Map;

import static net.chesstango.search.smart.SearchContext.EntryType;
import static net.chesstango.search.smart.SearchContext.TableEntry;

/**
 * @author Mauricio Coria
 */
public class QTranspositionTable implements AlphaBetaFilter {
    private AlphaBetaFilter next;
    private Map<Long, TableEntry> maxMap;
    private Map<Long, TableEntry> minMap;
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

            SearchContext.TableEntry entry = maxMap.get(hash);

            if (entry == null) {
                entry = new TableEntry();

                maxMap.put(hash, entry);

                bestMoveAndValue = next.maximize(currentPly, alpha, beta);
            } else {
                if (entry.bestMoveAndValue != 0) {
                    // Es un valor exacto
                    if (entry.type == SearchContext.EntryType.EXACT) {
                        return entry.bestMoveAndValue;
                    } else if (entry.type == SearchContext.EntryType.LOWER_BOUND && beta <= entry.value) {
                        return entry.bestMoveAndValue;
                    } else if (entry.type == SearchContext.EntryType.UPPER_BOUND && entry.value <= alpha) {
                        return entry.bestMoveAndValue;
                    }
                }

                if (entry.qBestMoveAndValue != 0) {
                    // Es un valor exacto
                    if (entry.qType == SearchContext.EntryType.EXACT) {
                        return entry.qBestMoveAndValue;
                    } else if (entry.qType == SearchContext.EntryType.LOWER_BOUND && beta <= entry.qValue) {
                        return entry.qBestMoveAndValue;
                    } else if (entry.qType == SearchContext.EntryType.UPPER_BOUND && entry.qValue <= alpha) {
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

            SearchContext.TableEntry entry = minMap.get(hash);

            if (entry == null) {
                entry = new TableEntry();

                minMap.put(hash, entry);

                bestMoveAndValue = next.minimize(currentPly, alpha, beta);
            } else {
                if (entry.bestMoveAndValue != 0) {
                    // Es un valor exacto
                    if (entry.type == SearchContext.EntryType.EXACT) {
                        return entry.bestMoveAndValue;
                    } else if (entry.type == SearchContext.EntryType.LOWER_BOUND && beta <= entry.value) {
                        return entry.bestMoveAndValue;
                    } else if (entry.type == SearchContext.EntryType.UPPER_BOUND && entry.value <= alpha) {
                        return entry.bestMoveAndValue;
                    }
                }

                if (entry.qBestMoveAndValue != 0) {
                    // Es un valor exacto
                    if (entry.qType == SearchContext.EntryType.EXACT) {
                        return entry.qBestMoveAndValue;
                    } else if (entry.qType == SearchContext.EntryType.LOWER_BOUND && beta <= entry.qValue) {
                        return entry.qBestMoveAndValue;
                    } else if (entry.qType == SearchContext.EntryType.UPPER_BOUND && entry.qValue <= alpha) {
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

    protected void updateQEntry(TableEntry entry, int alpha, int beta, long bestMoveAndValue) {
        int value = BinaryUtils.decodeValue(bestMoveAndValue);
        EntryType type;
        if (beta <= value) {
            type = EntryType.LOWER_BOUND;
        } else if (value <= alpha) {
            type = EntryType.UPPER_BOUND;
        } else {
            type = EntryType.EXACT;
        }

        entry.qBestMoveAndValue = bestMoveAndValue;
        entry.qValue = value;
        entry.qType = type;
    }
}
