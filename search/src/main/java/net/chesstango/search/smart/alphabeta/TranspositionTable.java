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
    private  int maxPly;

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

        if(searchDepth > 0 && game.getStatus().isInProgress()) {
            long hash = game.getChessPosition().getPositionHash();

            SearchContext.TableEntry entry = maximize ? maxMap.get(hash) : minMap.get(hash);

            if (entry == null) {
                entry = searchAndUpdate(new TableEntry(), currentPly, alpha, beta, maximize);

                if(maximize) {
                    maxMap.put(hash, entry);
                } else {
                    minMap.put(hash, entry);
                }
            } else {
                if(searchDepth > entry.searchDepth){
                    entry = searchAndUpdate(entry, currentPly, alpha, beta, maximize);

                } else {
                    // Es un valor exacto
                    if (entry.exact) {
                        entry = entry;
                    } else {
                        if(maximize){
                            if(entry.value >= beta){
                                entry = entry;
                            } else {
                                entry = searchAndUpdate(entry, currentPly, alpha, beta, true);
                            }
                        } else {
                            if(entry.value <= alpha){
                                entry = entry;
                            } else {
                                entry = searchAndUpdate(entry, currentPly, alpha, beta, false);
                            }
                        }
                    }
                }
            }

            return entry.bestMoveAndValue;
        }

        return maximize ? next.maximize(currentPly, alpha, beta) : next.minimize(currentPly, alpha, beta);
    }

    private TableEntry searchAndUpdate(TableEntry entry, int currentPly, int alpha, int beta, boolean maximize) {
        long bestMoveAndValue = maximize ? next.maximize(currentPly, alpha, beta) : next.minimize(currentPly, alpha, beta);
        entry.bestMoveAndValue = bestMoveAndValue;
        entry.searchDepth = maxPly - currentPly;
        entry.alpha = alpha;
        entry.beta = beta;
        entry.value = BinaryUtils.decodeValue(bestMoveAndValue);
        entry.exact =  entry.value > alpha && entry.value < beta;
        return entry;
    }
}
