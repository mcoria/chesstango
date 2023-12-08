package net.chesstango.search.smart.alphabeta;

import lombok.Setter;
import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.*;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFilter;
import net.chesstango.search.smart.transposition.TranspositionEntry;

import java.util.List;

/**
 * @author Mauricio Coria
 */
public class AlphaBetaFacade implements SearchSmart {

    @Setter
    private AlphaBetaFilter alphaBetaFilter;


    private List<StopSearchListener> stopSearchListeners;

    private List<ResetListener> resetListeners;

    private List<SearchByDepthListener> searchByDepthListeners;

    private List<SearchCycleListener> searchCycleListeners;


    private Game game;

    @Override
    public SearchMoveResult search(SearchContext context) {
        final Color currentTurn = game.getChessPosition().getCurrentTurn();

        long bestMoveAndValue = Color.WHITE.equals(currentTurn) ?
                alphaBetaFilter.maximize(0, GameEvaluator.INFINITE_NEGATIVE, GameEvaluator.INFINITE_POSITIVE) :
                alphaBetaFilter.minimize(0, GameEvaluator.INFINITE_NEGATIVE, GameEvaluator.INFINITE_POSITIVE);

        int bestValue = TranspositionEntry.decodeValue(bestMoveAndValue);
        short bestMoveEncoded = TranspositionEntry.decodeBestMove(bestMoveAndValue);

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

        return new SearchMoveResult(context.getMaxPly(), bestValue, bestMove, null);
    }

    @Override
    public void stopSearching() {
        stopSearchListeners.forEach(StopSearchListener::stopSearching);
    }

    @Override
    public void beforeSearch(Game game) {
        this.game = game;
        searchCycleListeners.forEach(filter -> filter.beforeSearch(game));
    }

    @Override
    public void afterSearch(SearchMoveResult result) {
        searchCycleListeners.forEach(filter -> filter.afterSearch(result));
    }

    @Override
    public void reset() {
        resetListeners.forEach(ResetListener::reset);
    }

    @Override
    public void beforeSearchByDepth(SearchContext context) {
        searchByDepthListeners.forEach(filter -> filter.beforeSearchByDepth(context));
    }

    @Override
    public void afterSearchByDepth(SearchMoveResult result) {
        searchByDepthListeners.forEach(filter -> filter.afterSearchByDepth(result));
    }

    public void setSearchActions(List<SmartListener> searchActions) {
        stopSearchListeners = searchActions.stream()
                .filter(StopSearchListener.class::isInstance)
                .map(StopSearchListener.class::cast)
                .toList();


        resetListeners = searchActions.stream()
                .filter(ResetListener.class::isInstance)
                .map(ResetListener.class::cast)
                .toList();

        searchByDepthListeners = searchActions.stream()
                .filter(SearchByDepthListener.class::isInstance)
                .map(SearchByDepthListener.class::cast)
                .toList();

        searchCycleListeners = searchActions.stream()
                .filter(SearchCycleListener.class::isInstance)
                .map(SearchCycleListener.class::cast)
                .toList();
    }
}
