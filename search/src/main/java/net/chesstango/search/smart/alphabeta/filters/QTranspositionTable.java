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
    private Map<Long, TableEntry> maxMap;
    private Map<Long, TableEntry> minMap;
    private Game game;

    @Override
    public void init(SearchContext context) {
        this.game = context.getGame();
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

    public void setNext(AlphaBetaFilter next) {
        this.next = next;
    }


    private long process(int currentPly, int alpha, int beta, boolean maximize) {
        if (game.getStatus().isInProgress()) {
            long hash = game.getChessPosition().getPositionHash();

            SearchContext.TableEntry entry = maximize ? maxMap.get(hash) : minMap.get(hash);

            if (entry == null) {
                long bestMoveAndValue = maximize ? next.maximize(currentPly, alpha, beta) : next.minimize(currentPly, alpha, beta);

                entry = updateQEntry(new TableEntry(), alpha, beta, bestMoveAndValue);

                if (maximize) {
                    maxMap.put(hash, entry);
                } else {
                    minMap.put(hash, entry);
                }
            } else {
                if(entry.bestMoveAndValue != 0){
                    if (entry.exact) {
                        return entry.bestMoveAndValue;
                    } else {
                        if (entry.value < alpha || beta < entry.value) {
                            return entry.bestMoveAndValue;
                        }
                    }
                }

                if(entry.qBestMoveAndValue != 0){
                    // Es un valor exacto
                    if (entry.qExact) {
                        entry = entry;
                    } else {
                        if (entry.qValue < alpha || beta < entry.qValue) {
                            entry = entry;
                        } else {
                            long bestMoveAndValue = maximize ? next.maximize(currentPly, alpha, beta) : next.minimize(currentPly, alpha, beta);

                            entry = updateQEntry(entry, alpha, beta, bestMoveAndValue);
                        }
                    }
                } else {
                    long bestMoveAndValue = maximize ? next.maximize(currentPly, alpha, beta) : next.minimize(currentPly, alpha, beta);

                    entry = updateQEntry(entry, alpha, beta, bestMoveAndValue);
                }
            }
            return entry.qBestMoveAndValue;
        }

        return maximize ? next.maximize(currentPly, alpha, beta) : next.minimize(currentPly, alpha, beta);
    }

    private TableEntry updateQEntry(TableEntry entry, int alpha, int beta, long bestMoveAndValue) {
        entry.qBestMoveAndValue = bestMoveAndValue;
        entry.qAlpha = alpha;
        entry.qBeta = beta;
        entry.qValue = BinaryUtils.decodeValue(bestMoveAndValue);
        entry.qExact = alpha < entry.value  && entry.value < beta;
        return entry;
    }
}
