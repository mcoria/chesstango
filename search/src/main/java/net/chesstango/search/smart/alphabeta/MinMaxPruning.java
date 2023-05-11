package net.chesstango.search.smart.alphabeta;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.AbstractSmart;
import net.chesstango.search.smart.BinaryUtils;
import net.chesstango.search.smart.SearchListener;
import net.chesstango.search.smart.SearchContext;
import net.chesstango.search.smart.alphabeta.filters.AlphaBeta;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFilter;
import net.chesstango.search.smart.alphabeta.filters.Quiescence;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class MinMaxPruning implements AbstractSmart {

    private AlphaBetaFilter alphaBetaFilter;

    private List<SearchListener> searchActions;

    @Override
    public SearchMoveResult searchBestMove(Game game, SearchContext context) {
        final Color currentTurn = game.getChessPosition().getCurrentTurn();

        initListeners(game, context);

        long bestMoveAndValue = Color.WHITE.equals(currentTurn) ?
                alphaBetaFilter.maximize(0, GameEvaluator.WHITE_LOST, GameEvaluator.BLACK_LOST) :
                alphaBetaFilter.minimize(0, GameEvaluator.WHITE_LOST, GameEvaluator.BLACK_LOST);

        int bestValue = BinaryUtils.decodeValue(bestMoveAndValue);
        short bestMoveEncoded = BinaryUtils.decodeMove(bestMoveAndValue);

        Move bestMove = null;
        for (Move move : game.getPossibleMoves()) {
            if (move.binaryEncoding() == bestMoveEncoded) {
                bestMove = move;
                break;
            }
        }
        if (bestMove == null) {
            throw new RuntimeException("BestMove not found");
        }

        SearchMoveResult searchResult = new SearchMoveResult(context.getMaxPly(), bestValue, bestMove, null);

        closeListeners(searchResult);

        return searchResult;
    }

    @Override
    public void stopSearching() {
        searchActions.stream().forEach(filterActions -> {
            if (filterActions instanceof AlphaBeta) {
                ((AlphaBeta) filterActions).stopSearching();
            }
            if (filterActions instanceof Quiescence) {
                ((Quiescence) filterActions).stopSearching();
            }
        });
    }

    public void setAlphaBetaSearch(AlphaBetaFilter alphaBetaFilter) {
        this.alphaBetaFilter = alphaBetaFilter;
    }

    public void setSearchActions(List<SearchListener> searchActions) {
        this.searchActions = searchActions;
    }


    private void initListeners(Game game, SearchContext context) {
        searchActions.stream().forEach(filter -> filter.init(game, context));
    }

    private void closeListeners(SearchMoveResult searchResult) {
        searchActions.stream().forEach(filter -> filter.close(searchResult));
    }
}
