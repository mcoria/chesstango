package net.chesstango.search.smart.alphabeta.filters.once;

import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.search.MoveEvaluation;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.StopSearchingException;
import net.chesstango.search.smart.SearchByDepthListener;
import net.chesstango.search.smart.SearchContext;
import net.chesstango.search.smart.SearchCycleListener;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFilter;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFunction;
import net.chesstango.search.smart.transposition.TranspositionEntry;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * @author Mauricio Coria
 */
public class StopProcessingCatch implements AlphaBetaFilter, SearchCycleListener, SearchByDepthListener {

    @Setter
    private AlphaBetaFilter next;

    @Setter
    private Game game;


    private Move lastBestMove;

    private Integer lastBestValue;


    @Override
    public void beforeSearch(Game game) {
        this.game = game;
        this.lastBestMove = null;
        this.lastBestValue = null;
    }

    @Override
    public void afterSearch(SearchMoveResult result) {
    }

    @Override
    public void beforeSearchByDepth(SearchContext context) {
        lastBestValue = context.getLastBestEvaluation();
        lastBestMove = context.getLastBestMove();
    }

    @Override
    public void afterSearchByDepth(SearchMoveResult result) {
    }

    @Override
    public long maximize(int currentPly, int alpha, int beta) {
        return process(currentPly, alpha, beta, next::maximize, false);
    }

    @Override
    public long minimize(int currentPly, int alpha, int beta) {
        return process(currentPly, alpha, beta, next::minimize, true);
    }

    private long process(int currentPly, int alpha, int beta, AlphaBetaFunction fn, boolean naturalOrderSort) {
        final long startHash = game.getChessPosition().getZobristHash();

        try {
            return fn.search(currentPly, alpha, beta);
        } catch (StopSearchingException re) {
            undoMoves(startHash);
        }

        Move bestMove;
        Integer bestValue;

        /*
        if (!currentMoveEvaluations.isEmpty()) {
            sortMoveEvaluations(currentMoveEvaluations, naturalOrderSort);
            MoveEvaluation moveEvaluation = currentMoveEvaluations.get(0);
            bestMove = moveEvaluation.move();
            bestValue = moveEvaluation.evaluation();
        } else*/

        if (Objects.nonNull(lastBestMove)) {
            bestMove = lastBestMove;
            bestValue = lastBestValue;
        } else {
            throw new RuntimeException("Stopped too early");
        }

        return TranspositionEntry.encode(bestMove, bestValue);
    }

    private void undoMoves(long startHash) {
        long currentHash = game.getChessPosition().getZobristHash();
        while (currentHash != startHash) {
            game.undoMove();
            currentHash = game.getChessPosition().getZobristHash();
        }
    }

    private void sortMoveEvaluations(List<MoveEvaluation> moveEvaluations, boolean naturalOrder) {
        if (naturalOrder) {
            moveEvaluations.sort(Comparator.comparing(MoveEvaluation::evaluation));
        } else {
            moveEvaluations.sort(Comparator.comparing(MoveEvaluation::evaluation).reversed());
        }
    }
}
