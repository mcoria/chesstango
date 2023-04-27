package net.chesstango.search.smart.alphabeta;

import net.chesstango.board.Game;
import net.chesstango.search.smart.SearchContext;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Mauricio Coria
 */
public class TranspositionTable implements AlphaBetaFilter {

    private AlphaBetaFilter next;

    private Map<Long, TableEntry> maxMap;

    private Map<Long, TableEntry> minMap;

    private  int maxPly;

    @Override
    public void init(Game game, SearchContext context) {
        maxPly = context.getMaxPly();
        maxMap = new HashMap<>();
        minMap = new HashMap<>();
    }

    @Override
    public int maximize(Game game, int currentPly, int alpha, int beta) {

        long hash = game.getChessPosition().getPositionHash();

        TableEntry entry = maxMap.get(hash);

        int searchDepth = maxPly - currentPly;

        if (entry == null || entry != null && searchDepth > entry.searchDepth ) {
            entry = new TableEntry();
            entry.evaluation = next.maximize(game, currentPly, alpha, beta);
            entry.searchDepth = searchDepth;
            maxMap.put(hash, entry);
        }

        return entry.evaluation;
    }

    @Override
    public int minimize(Game game, int currentPly, int alpha, int beta) {

        long hash = game.getChessPosition().getPositionHash();

        TableEntry entry = minMap.get(hash);

        int searchDepth = maxPly - currentPly;

        if (entry == null || entry != null && searchDepth > entry.searchDepth ) {
            entry = new TableEntry();
            entry.evaluation = next.minimize(game, currentPly, alpha, beta);
            entry.searchDepth = searchDepth;
            minMap.put(hash, entry);
        }

        return entry.evaluation;
    }

    @Override
    public void stopSearching() {
    }

    public void setNext(AlphaBetaFilter next) {
        this.next = next;
    }

    private static class TableEntry {
        int evaluation;
        int searchDepth;

    }
}
