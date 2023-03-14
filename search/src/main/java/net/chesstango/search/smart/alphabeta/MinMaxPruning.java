package net.chesstango.search.smart.alphabeta;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.AbstractSmart;
import net.chesstango.search.smart.MoveSelector;
import net.chesstango.search.smart.MoveSorter;

import java.util.*;

/**
 * @author Mauricio Coria
 */
public class MinMaxPruning extends AbstractSmart {
    private MoveSorter moveSorter;

    private AlphaBetaSearch alphaBetaSearch;

    @Override
    public SearchMoveResult searchBestMove(Game game) {
        return searchBestMove(game, 10);
    }

    @Override
    public SearchMoveResult searchBestMove(Game game, final int depth) {
        this.keepProcessing = true;

        SearchContext context = new SearchContext(depth);

        int[] visitedNodesCounter = new int[30];
        List<Set<Move>> distinctMoves = new ArrayList<>(visitedNodesCounter.length);
        for (int i = 0; i < 30; i++) {
            distinctMoves.add(new HashSet<>());
        }

        context.setVisitedNodesCounter(visitedNodesCounter);
        context.setDistinctMoves(distinctMoves);


        final boolean minOrMax = Color.WHITE.equals(game.getChessPosition().getCurrentTurn()) ? false : true;
        final List<Move> bestMoves = new ArrayList<Move>();

        int bestValue = minOrMax ? GameEvaluator.INFINITE_POSITIVE : GameEvaluator.INFINITE_NEGATIVE;
        boolean search = true;

        Queue<Move> sortedMoves = moveSorter.sortMoves(game.getPossibleMoves());
        while (!sortedMoves.isEmpty() && search && keepProcessing) {
            Move move = sortedMoves.poll();

            game = game.executeMove(move);

            int currentValue = minOrMax ?
                    alphaBetaSearch.maximize(game, 1, GameEvaluator.INFINITE_NEGATIVE, bestValue, context) :
                    alphaBetaSearch.minimize(game, 1, bestValue, GameEvaluator.INFINITE_POSITIVE, context);

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

        SearchMoveResult searchMoveResult = new SearchMoveResult(depth, bestValue, bestMoves.size() - 1, new MoveSelector().selectMove(game.getChessPosition().getCurrentTurn(), bestMoves), null);
        searchMoveResult.setVisitedNodesCounter(context.getVisitedNodesCounter());
        searchMoveResult.setDistinctMoves(context.getDistinctMoves());

        return searchMoveResult;
    }

    public void setMoveSorter(MoveSorter moveSorter) {
        this.moveSorter = moveSorter;
    }

    public void setAlphaBetaSearch(AlphaBetaSearch alphaBetaSearch) {
        this.alphaBetaSearch = alphaBetaSearch;
    }

}
