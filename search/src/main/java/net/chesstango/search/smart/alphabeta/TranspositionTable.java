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

            if (entry == null) {
                entry = new TableEntry();

                entry.bestMoveAndValue = next.maximize(currentPly, alpha, beta);
                entry.searchDepth = searchDepth;

                maxMap.put(hash, entry);
            } else if (searchDepth > entry.searchDepth) { // Reutilizamos el objeto
                entry.bestMoveAndValue = next.maximize(currentPly, alpha, beta);
                entry.searchDepth = searchDepth;
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

            if (entry == null) {
                entry = new TableEntry();

                entry.bestMoveAndValue = next.minimize(currentPly, alpha, beta);
                entry.searchDepth = searchDepth;

                minMap.put(hash, entry);
            } else if (searchDepth > entry.searchDepth) { // Reutilizamos el objeto
                entry.bestMoveAndValue = next.minimize(currentPly, alpha, beta);
                entry.searchDepth = searchDepth;
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
