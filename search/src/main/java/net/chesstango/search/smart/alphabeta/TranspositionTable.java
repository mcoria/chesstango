package net.chesstango.search.smart.alphabeta;

import net.chesstango.board.Game;
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
    public long maximize(final int currentPly, final int alpha, final int beta) {
        int searchDepth = maxPly - currentPly;

        if(searchDepth > 0 && game.getStatus().isInProgress()) {
            long hash = game.getChessPosition().getPositionHash();

            SearchContext.TableEntry entry = maxMap.get(hash);

            if (entry == null || entry != null && searchDepth > entry.searchDepth) {
                long bestMoveAndValue = next.maximize(currentPly, alpha, beta);

                int currentValue = (int) (0b00000000_00000000_00000000_00000000_00000000_11111111_11111111_11111111_11111111L & bestMoveAndValue);

                if(currentValue >  alpha && currentValue < beta) {
                    entry = new TableEntry();
                    entry.bestMoveAndValue = bestMoveAndValue;
                    entry.searchDepth = searchDepth;
                    entry.alpha = alpha;
                    entry.beta = beta;

                    minMap.put(hash, entry);
                }

                return bestMoveAndValue;
            }

            return entry.bestMoveAndValue;
        }

        return next.maximize(currentPly, alpha, beta);
    }

    @Override
    public long minimize(final int currentPly, final int alpha, final int beta) {

        int searchDepth = maxPly - currentPly;

        if(searchDepth > 0 && game.getStatus().isInProgress()) {
            long hash = game.getChessPosition().getPositionHash();

            TableEntry entry = minMap.get(hash);

            if (entry == null || entry != null && searchDepth > entry.searchDepth) {
                long bestMoveAndValue = next.minimize(currentPly, alpha, beta);

                int currentValue = (int) (0b00000000_00000000_00000000_00000000_00000000_11111111_11111111_11111111_11111111L & bestMoveAndValue);

                if(currentValue >  alpha && currentValue < beta) {
                    entry = new TableEntry();
                    entry.bestMoveAndValue = bestMoveAndValue;
                    entry.searchDepth = searchDepth;
                    entry.alpha = alpha;
                    entry.beta = beta;

                    minMap.put(hash, entry);
                }

                return bestMoveAndValue;
            }

            return entry.bestMoveAndValue;
        }

        return next.minimize(currentPly, alpha, beta);
    }

    @Override
    public void stopSearching() {
    }

    public void setNext(AlphaBetaFilter next) {
        this.next = next;
    }
}
