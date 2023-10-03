package net.chesstango.search.smart.alphabeta;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.SearchContext;
import net.chesstango.search.smart.SearchLifeCycle;
import net.chesstango.search.smart.SearchSmart;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFilter;
import net.chesstango.search.smart.transposition.TranspositionEntry;

import java.util.List;

/**
 * @author Mauricio Coria
 */
public class AlphaBetaFacade implements SearchSmart {

    private AlphaBetaFilter alphaBetaFilter;

    private List<SearchLifeCycle> searchActions;

    private Game game;

    @Override
    public SearchMoveResult search(SearchContext context) {
        final Color currentTurn = game.getChessPosition().getCurrentTurn();

        long bestMoveAndValue = Color.WHITE.equals(currentTurn) ?
                alphaBetaFilter.maximize(0, GameEvaluator.WHITE_LOST, GameEvaluator.BLACK_LOST) :
                alphaBetaFilter.minimize(0, GameEvaluator.WHITE_LOST, GameEvaluator.BLACK_LOST);

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
        synchronized (searchActions) {
            searchActions.stream().forEach(SearchLifeCycle::stopSearching);
        }
    }

    @Override
    public void beforeSearch(Game game, int maxDepth) {
        this.game = game;
        synchronized (searchActions) {
            searchActions.stream().forEach(filter -> filter.beforeSearch(game, maxDepth));
        }
    }

    @Override
    public void afterSearch(SearchMoveResult result) {
        synchronized (searchActions) {
            searchActions.stream().forEach(filter -> filter.afterSearch(result));
        }
    }

    @Override
    public void reset() {
        synchronized (searchActions) {
            searchActions.stream().forEach(filter -> filter.reset());
        }
    }

    @Override
    public void beforeSearchByDepth(SearchContext context) {
        synchronized (searchActions) {
            searchActions.stream().forEach(filter -> filter.beforeSearchByDepth(context));
        }
    }

    @Override
    public void afterSearchByDepth(SearchMoveResult result) {
        synchronized (searchActions) {
            searchActions.stream().forEach(filter -> filter.afterSearchByDepth(result));
        }
    }

    public void setAlphaBetaSearch(AlphaBetaFilter alphaBetaFilter) {
        this.alphaBetaFilter = alphaBetaFilter;
    }

    public void setSearchActions(List<SearchLifeCycle> searchActions) {
        this.searchActions = searchActions;
    }

}
