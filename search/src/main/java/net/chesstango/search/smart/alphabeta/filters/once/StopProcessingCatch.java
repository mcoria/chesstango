package net.chesstango.search.smart.alphabeta.filters.once;

import net.chesstango.board.Game;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.SearchContext;
import net.chesstango.search.smart.StopProcessingException;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFilter;

/**
 * @author Mauricio Coria
 */
public class StopProcessingCatch implements AlphaBetaFilter {

    private AlphaBetaFilter next;

    private Game game;
    private int processingCounter;

    @Override
    public void init(Game game, SearchContext context) {
        this.game = game;
        this.processingCounter = 0;
    }

    @Override
    public void close(SearchMoveResult result) {

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
        final long startHash = game.getChessPosition().getPositionHash();
        try {
            if (maximize) {
                return next.maximize(currentPly, alpha, beta);
            } else {
                return next.minimize(currentPly, alpha, beta);
            }
        } catch (StopProcessingException re) {
            long currentHash = game.getChessPosition().getPositionHash();
            while (currentHash != startHash) {
                game.undoMove();
                currentHash = game.getChessPosition().getPositionHash();
            }
            throw re;
        }
    }
}
