package net.chesstango.search.smart.alphabeta;

import net.chesstango.board.Game;
import net.chesstango.search.SearchMoveResult;
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

    private  int maxPly;
    private Game game;

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

            if (entry == null || entry != null && searchDepth > entry.searchDepth) {
                long bestMoveAndValue = maximize ? next.maximize(currentPly, alpha, beta) : next.minimize(currentPly, alpha, beta);

                int currentValue = (int) (0b00000000_00000000_00000000_00000000_00000000_11111111_11111111_11111111_11111111L & bestMoveAndValue);

                if(currentValue >  alpha && currentValue < beta) {
                    entry = new TableEntry();
                    entry.bestMoveAndValue = bestMoveAndValue;
                    entry.searchDepth = searchDepth;
                    entry.alpha = alpha;
                    entry.beta = beta;

                    if(maximize) {
                        maxMap.put(hash, entry);
                    } else {
                        minMap.put(hash, entry);
                    }
                }

                return bestMoveAndValue;
            }

            return entry.bestMoveAndValue;
        }

        return maximize ? next.maximize(currentPly, alpha, beta) : next.minimize(currentPly, alpha, beta);
    }
}
