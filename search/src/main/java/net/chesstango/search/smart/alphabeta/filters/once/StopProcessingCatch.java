package net.chesstango.search.smart.alphabeta.filters.once;

import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.StopSearchingException;
import net.chesstango.search.smart.SearchContext;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFilter;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFunction;
import net.chesstango.search.smart.transposition.TranspositionEntry;

import java.util.Objects;

/**
 * @author Mauricio Coria
 */
public class StopProcessingCatch implements AlphaBetaFilter {

    @Setter
    private AlphaBetaFilter next;

    @Setter
    private Game game;

    @Setter
    private AlphaBetaFirst alphaBetaFirst;


    private Move lastBestMove;

    private Integer lastBestValue;


    @Override
    public void beforeSearch(Game game, int maxDepth) {
        this.game = game;
        this.lastBestMove = null;
        this.lastBestValue = null;
    }

    @Override
    public void afterSearch(SearchMoveResult result) {
    }

    @Override
    public void beforeSearchByDepth(SearchContext context) {
        lastBestValue = context.getLastBestValue();
        lastBestMove = context.getLastBestMove();
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
        return process(currentPly, alpha, beta, next::maximize);
    }

    @Override
    public long minimize(int currentPly, int alpha, int beta) {
        return process(currentPly, alpha, beta, next::minimize);
    }

    private long process(int currentPly, int alpha, int beta, AlphaBetaFunction fn) {
        final long startHash = game.getChessPosition().getZobristHash();

        try {
            return fn.search(currentPly, alpha, beta);
        } catch (StopSearchingException re) {
            undoMoves(startHash);
        }

        Move bestMove;
        Integer bestValue;

        if (Objects.nonNull(alphaBetaFirst.getBestMove())) {
            bestMove = alphaBetaFirst.getBestMove();
            bestValue = alphaBetaFirst.getBestValue();
        } else if (Objects.nonNull(lastBestMove)) {
            bestMove = lastBestMove;
            bestValue = lastBestValue;
        } else {
            throw new RuntimeException("Stopped too early");
        }

        return TranspositionEntry.encode(bestMove, null, bestValue);
    }

    private void undoMoves(long startHash) {
        long currentHash = game.getChessPosition().getZobristHash();
        while (currentHash != startHash) {
            game.undoMove();
            currentHash = game.getChessPosition().getZobristHash();
        }
    }
}
