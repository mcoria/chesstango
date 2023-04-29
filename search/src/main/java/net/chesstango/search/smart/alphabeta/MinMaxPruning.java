package net.chesstango.search.smart.alphabeta;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.AbstractSmart;
import net.chesstango.search.smart.MoveSorter;
import net.chesstango.search.smart.SearchContext;

import java.util.ArrayList;
import java.util.List;

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

        final Color currentTurn = game.getChessPosition().getCurrentTurn();
        final List<Move> bestMoves = new ArrayList<Move>();

        initFilters(game, context);


        long bestMoveAndValue = Color.WHITE.equals(currentTurn) ?
                alphaBetaFilter.maximize(game, 0, GameEvaluator.BLACK_WON, GameEvaluator.WHITE_WON) :
                alphaBetaFilter.minimize(game, 0, GameEvaluator.BLACK_WON, GameEvaluator.WHITE_WON);

        int bestValue = (int) (0b00000000_00000000_00000000_00000000_00000000_11111111_11111111_11111111_11111111L & bestMoveAndValue);
        short bestMoveEncoded = (short) (bestMoveAndValue >> 32);

        Move bestMove = null;
        for (Move move : game.getPossibleMoves()) {
            if (move.binaryEncoding() == bestMoveEncoded) {
                bestMove = move;
            }
        }

        if (bestMove == null) {
            throw new RuntimeException("BestMove not found");
        }


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
