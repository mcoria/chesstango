package net.chesstango.search.smart.alphabeta.filters;

import net.chesstango.board.Game;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.BinaryUtils;
import net.chesstango.search.smart.SearchContext;

import java.util.Map;

import static net.chesstango.search.smart.SearchContext.TableEntry;

/**
 * @author Mauricio Coria
 */
public class QTranspositionTable implements AlphaBetaFilter {
    private AlphaBetaFilter next;
    private Map<Long, TableEntry> qMaxMap;
    private Map<Long, TableEntry> qMinMap;
    private Game game;

    @Override
    public void init(SearchContext context) {
        this.game = context.getGame();
        this.qMaxMap = context.getQMaxMap();
        this.qMinMap = context.getQMinMap();
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

    public void setNext(AlphaBetaFilter next) {
        this.next = next;
    }


    private long process(int currentPly, int alpha, int beta, boolean maximize) {
        if (game.getStatus().isInProgress()) {
            long hash = game.getChessPosition().getPositionHash();

            SearchContext.TableEntry entry = maximize ? qMaxMap.get(hash) : qMinMap.get(hash);

            if (entry == null) {
                long bestMoveAndValue = maximize ? next.maximize(currentPly, alpha, beta) : next.minimize(currentPly, alpha, beta);

                entry = updateEntry(new TableEntry(), currentPly, alpha, beta, bestMoveAndValue);

                if (maximize) {
                    qMaxMap.put(hash, entry);
                } else {
                    qMinMap.put(hash, entry);
                }
            } else {
                // Es un valor exacto
                if (entry.exact) {
                    entry = entry;
                } else {
                    if (entry.value <= alpha || entry.value >= beta) {
                        entry = entry;
                    } else {
                        long bestMoveAndValue = maximize ? next.maximize(currentPly, alpha, beta) : next.minimize(currentPly, alpha, beta);

                        entry = updateEntry(entry, currentPly, alpha, beta, bestMoveAndValue);
                    }
                }
            }

            return entry.bestMoveAndValue;
        }

        return maximize ? next.maximize(currentPly, alpha, beta) : next.minimize(currentPly, alpha, beta);
    }

    private TableEntry updateEntry(TableEntry entry, int currentPly, int alpha, int beta, long bestMoveAndValue) {
        entry.bestMoveAndValue = bestMoveAndValue;
        entry.searchDepth = currentPly;
        entry.alpha = alpha;
        entry.beta = beta;
        entry.value = BinaryUtils.decodeValue(bestMoveAndValue);
        entry.exact = entry.value > alpha && entry.value < beta;
        return entry;
    }
}
