package net.chesstango.search.smart.alphabeta;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.AbstractSmart;
import net.chesstango.search.smart.MoveSelector;
import net.chesstango.search.smart.MoveSorter;
import net.chesstango.search.smart.SearchContext;

import java.util.*;

/**
 * @author Mauricio Coria
 */
public class MinMaxPruning extends AbstractSmart {
    private MoveSorter moveSorter;

    private AlphaBetaFilter alphaBetaFilter;

    @Override
    public SearchMoveResult searchBestMove(Game game) {
        return searchBestMove(game, 10);
    }

    @Override
    public SearchMoveResult searchBestMove(Game game, final int depth) {
        SearchContext context = new SearchContext(depth);

        int[] visitedNodesCounter = new int[30];
        List<Set<Move>> distinctMoves = new ArrayList<>(visitedNodesCounter.length);
        for (int i = 0; i < 30; i++) {
            distinctMoves.add(new HashSet<>());
        }

        context.setVisitedNodesCounter(visitedNodesCounter);
        context.setDistinctMoves(distinctMoves);

        return searchBestMove(game, context);
    }

    @Override
    public SearchMoveResult searchBestMove(Game game, SearchContext context) {
        this.keepProcessing = true;
        final boolean minOrMax = Color.WHITE.equals(game.getChessPosition().getCurrentTurn()) ? false : true;
        final List<Move> bestMoves = new ArrayList<Move>();

        int bestValue = minOrMax ? GameEvaluator.INFINITE_POSITIVE : GameEvaluator.INFINITE_NEGATIVE;
        boolean search = true;

        Queue<Move> sortedMoves = moveSorter.sortMoves(game.getPossibleMoves());
        while (!sortedMoves.isEmpty() && search && keepProcessing) {
            Move move = sortedMoves.poll();

            game = game.executeMove(move);

            int currentValue = minOrMax ?
                    alphaBetaFilter.maximize(game, 1, GameEvaluator.INFINITE_NEGATIVE, bestValue, context) :
                    alphaBetaFilter.minimize(game, 1, bestValue, GameEvaluator.INFINITE_POSITIVE, context);

            if (minOrMax && currentValue < bestValue || !minOrMax && currentValue > bestValue) {
                bestValue = currentValue;
                bestMoves.clear();
                bestMoves.add(move);
                if (minOrMax && bestValue == GameEvaluator.BLACK_WON ||             //Black wins
                        !minOrMax && bestValue == GameEvaluator.WHITE_WON) {        //White wins
                    search = false;
                }

            } else if (currentValue == bestValue) {
                bestMoves.add(move);
            }

            game = game.undoMove();
        }

        if (bestMoves.size() == 0 &&
                (minOrMax && bestValue == GameEvaluator.WHITE_WON || !minOrMax && bestValue == GameEvaluator.BLACK_WON)) {
            game.getPossibleMoves().forEach(bestMoves::add);
        }


        return new SearchMoveResult(context.getMaxPly(), bestValue, new MoveSelector().selectMove(game.getChessPosition().getCurrentTurn(), bestMoves), null)
                .setVisitedNodesCounter(context.getVisitedNodesCounter())
                .setDistinctMoves(context.getDistinctMoves());
    }

    public void setMoveSorter(MoveSorter moveSorter) {
        this.moveSorter = moveSorter;
    }

    public void setAlphaBetaSearch(AlphaBetaFilter alphaBetaFilter) {
        this.alphaBetaFilter = alphaBetaFilter;
    }

}
