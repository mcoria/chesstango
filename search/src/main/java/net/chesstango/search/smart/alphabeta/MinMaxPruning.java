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

    private List<AlphaBetaFilter> filters;


    @Override
    public SearchMoveResult searchBestMove(Game game, SearchContext context) {
        notifyInit(context);
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
                    alphaBetaFilter.maximize(game, 1, GameEvaluator.INFINITE_NEGATIVE, bestValue) :
                    alphaBetaFilter.minimize(game, 1, bestValue, GameEvaluator.INFINITE_POSITIVE);

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
                .setVisitedNodesCounters(context.getVisitedNodesCounters())
                .setDistinctMovesPerLevel(context.getDistinctMovesPerLevel())
                .setEvaluationCollisions(bestMoves.size() - 1);
    }

    public void setMoveSorter(MoveSorter moveSorter) {
        this.moveSorter = moveSorter;
    }

    public void setAlphaBetaSearch(AlphaBetaFilter alphaBetaFilter) {
        this.alphaBetaFilter = alphaBetaFilter;
    }

    public void setFilters(List<AlphaBetaFilter> filters) {
        this.filters = filters;
    }
    private void notifyInit(SearchContext context) {
        filters.stream().forEach(filter -> filter.init(context));
    }

    @Override
    public void stopSearching() {
        keepProcessing = false;
        filters.stream().forEach(AlphaBetaFilter::stopSearching);
    }
}
