package net.chesstango.search.smart.alphabeta.filters.once;

import net.chesstango.board.Game;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.StopSearchingException;
import net.chesstango.search.smart.SearchContext;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFilter;

/**
 * @author Mauricio Coria
 */
public class StopProcessingCatch implements AlphaBetaFilter {

    private AlphaBetaFilter next;

    private Game game;
    private int processingCounter;

    @Override
    public void beforeSearch(Game game, int maxDepth) {
        this.game = game;
    }

    @Override
    public void afterSearch(SearchMoveResult result) {

    }

    @Override
    public void beforeSearchByDepth(SearchContext context) {
        this.processingCounter = 0;
    }

    @Override
    public void afterSearchByDepth(SearchMoveResult result) {

    }

    @Override
    public void stopSearching() {

    }

    @Override
    public void reset() {

    }

    @Override
    public long maximize(int currentPly, int alpha, int beta) {
        return process(currentPly, alpha, beta, true);
    }

    @Override
    public long minimize(int currentPly, int alpha, int beta) {
        return process(currentPly, alpha, beta, false);
    }

    public void setNext(AlphaBetaFilter next) {
        this.next = next;
    }

    private long process(int currentPly, int alpha, int beta, boolean maximize) {
        synchronized (this) {
            processingCounter++;
        }
        if (processingCounter > 1) {
            throw new RuntimeException("Filter already processing");
        }
        final long startHash = game.getChessPosition().getZobristHash();
        try {
            if (maximize) {
                return next.maximize(currentPly, alpha, beta);
            } else {
                return next.minimize(currentPly, alpha, beta);
            }
        } catch (StopSearchingException re) {
            long currentHash = game.getChessPosition().getZobristHash();
            while (currentHash != startHash) {
                game.undoMove();
                currentHash = game.getChessPosition().getZobristHash();
            }
            throw re;
        }
    }
}
