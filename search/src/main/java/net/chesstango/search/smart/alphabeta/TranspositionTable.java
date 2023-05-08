package net.chesstango.search.smart.alphabeta;

import net.chesstango.board.Game;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.BinaryUtils;
import net.chesstango.search.smart.SearchContext;

import java.util.Map;

import static net.chesstango.search.smart.SearchContext.TableEntry;

/**
 * @author Mauricio Coria
 */
public class TranspositionTable implements AlphaBetaFilter {

    private AlphaBetaFilter next;

    private Map<Long, TableEntry> maxMap;
    private Map<Long, TableEntry> minMap;

    private Game game;
    private int maxPly;

    @Override
    public void init(Game game, SearchContext context) {
        this.game = game;
        this.maxPly = context.getMaxPly();
        this.maxMap = context.getMaxMap();
        this.minMap = context.getMinMap();
    }

    @Override
    public void close(SearchMoveResult result) {

    }

    @Override
    public long maximize(final int currentPly, final int alpha, final int beta) {
        return process(currentPly, alpha, beta, true);
    }

    @Override
    public long minimize(final int currentPly, final int alpha, final int beta) {
        return process(currentPly, alpha, beta, false);
    }

    @Override
    public void stopSearching() {
    }

    public void setNext(AlphaBetaFilter next) {
        this.next = next;
    }

    private long process(int currentPly, int alpha, int beta, boolean maximize) {
        int searchDepth = maxPly - currentPly;

        if (searchDepth > 0 && game.getStatus().isInProgress()) {
            long hash = game.getChessPosition().getPositionHash();

            SearchContext.TableEntry entry = maximize ? maxMap.get(hash) : minMap.get(hash);

            if (entry == null) {
                long bestMoveAndValue = maximize ? next.maximize(currentPly, alpha, beta) : next.minimize(currentPly, alpha, beta);

                entry = updateEntry(new TableEntry(), currentPly, alpha, beta, bestMoveAndValue);

                if (maximize) {
                    maxMap.put(hash, entry);
                } else {
                    minMap.put(hash, entry);
                }
            } else {
                if (searchDepth > entry.searchDepth) {
                    long bestMoveAndValue = maximize ? next.maximize(currentPly, alpha, beta) : next.minimize(currentPly, alpha, beta);

                    entry = updateEntry(entry, currentPly, alpha, beta, bestMoveAndValue);

                } else {
                    // Es un valor exacto
                    if (entry.exact) {
                        entry = entry;
                    } else {
                        if (maximize) {
                            if (entry.value >= beta) {
                                entry = entry;
                            } else {
                                long bestMoveAndValue = maximize ? next.maximize(currentPly, alpha, beta) : next.minimize(currentPly, alpha, beta);

                                entry = updateEntry(entry, currentPly, alpha, beta, bestMoveAndValue);
                            }
                        } else {
                            if (entry.value <= alpha) {
                                entry = entry;
                            } else {
                                long bestMoveAndValue = maximize ? next.maximize(currentPly, alpha, beta) : next.minimize(currentPly, alpha, beta);

                                entry = updateEntry(entry, currentPly, alpha, beta, bestMoveAndValue);
                            }
                        }
                    }
                }
            }
            return entry.bestMoveAndValue;
        }

        return maximize ? next.maximize(currentPly, alpha, beta) : next.minimize(currentPly, alpha, beta);
    }

    private TableEntry updateEntry(TableEntry entry, int currentPly, int alpha, int beta, long bestMoveAndValue) {
        entry.bestMoveAndValue = bestMoveAndValue;
        entry.searchDepth = maxPly - currentPly;
        entry.alpha = alpha;
        entry.beta = beta;
        entry.value = BinaryUtils.decodeValue(bestMoveAndValue);
        entry.exact = entry.value > alpha && entry.value < beta;
        return entry;
    }
}
