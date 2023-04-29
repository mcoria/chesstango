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

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * @author Mauricio Coria
 */
public class MinMaxPruning extends AbstractSmart {
    private MoveSorter moveSorter;

    private AlphaBetaFilter alphaBetaFilter;

    private List<AlphaBetaFilter> filters;


    @Override
    public SearchMoveResult searchBestMove(Game game, SearchContext context) {
        this.keepProcessing = true;

        final Color currentTurn =  game.getChessPosition().getCurrentTurn();
        final boolean minOrMax = Color.WHITE.equals(currentTurn) ? false : true;
        final List<Move> bestMoves = new ArrayList<Move>();

        initFilters(game, context);

        int bestValue = minOrMax ? GameEvaluator.INFINITE_POSITIVE : GameEvaluator.INFINITE_NEGATIVE;
        boolean search = true;

        Queue<Move> sortedMoves = moveSorter.sortMoves(game.getPossibleMoves());
        while (!sortedMoves.isEmpty() && search && keepProcessing) {
            Move move = sortedMoves.poll();

            game = game.executeMove(move);

            long bestMoveAndValue = minOrMax ?
                    alphaBetaFilter.maximize(game, 1, GameEvaluator.BLACK_WON, bestValue) :
                    alphaBetaFilter.minimize(game, 1, bestValue, GameEvaluator.WHITE_WON);

            int currentValue = (int) bestMoveAndValue;

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

        Move bestMove = bestMoves.get(0);

        return new SearchMoveResult(context.getMaxPly(), bestValue, bestMove, null)
                .setVisitedNodesCounters(context.getVisitedNodesCounters())
                .setVisitedNodesQuiescenceCounter(context.getVisitedNodesQuiescenceCounter())
                .setDistinctMovesPerLevel(context.getDistinctMovesPerLevel())
                .setEvaluationCollisions(bestMoves.size() - 1)
                .setExpectedNodesCounters(context.getExpectedNodesCounters())
                .setBestMoveOptions(bestMoves);
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

    @Override
    public void stopSearching() {
        keepProcessing = false;
        filters.stream().forEach(AlphaBetaFilter::stopSearching);
    }

    private void initFilters(Game game, SearchContext context) {
        filters.stream().forEach(filter -> filter.init(game, context));
    }
}
