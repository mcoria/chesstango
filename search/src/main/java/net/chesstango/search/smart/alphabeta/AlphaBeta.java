package net.chesstango.search.smart.alphabeta;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.StopSearchingException;
import net.chesstango.search.smart.BinaryUtils;
import net.chesstango.search.smart.SearchContext;
import net.chesstango.search.smart.SearchLifeCycle;
import net.chesstango.search.smart.SearchSmart;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFilter;

import java.util.List;

/**
 * @author Mauricio Coria
 */
public class AlphaBeta implements SearchSmart {

    private AlphaBetaFilter alphaBetaFilter;

    private List<SearchLifeCycle> searchActions;

    private Game game;

    @Override
    public SearchMoveResult search(SearchContext context) {
        final Color currentTurn = game.getChessPosition().getCurrentTurn();

        try {
            init(context);
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

            close(searchResult);
            return searchResult;

        } catch (StopSearchingException ex) {
            close(null);
            throw ex;
        }
    }

    @Override
    public void stopSearching() {
        synchronized (searchActions) {
            searchActions.stream().forEach(SearchLifeCycle::stopSearching);
        }
    }

    public void setAlphaBetaSearch(AlphaBetaFilter alphaBetaFilter) {
        this.alphaBetaFilter = alphaBetaFilter;
    }

    public void setSearchActions(List<SearchLifeCycle> searchActions) {
        this.searchActions = searchActions;
    }

    @Override
    public void initSearch(Game game, int maxDepth) {
        this.game = game;
        synchronized (searchActions) {
            searchActions.stream().forEach(filter -> filter.initSearch(game, maxDepth));
        }
    }

    @Override
    public void closeSearch(SearchMoveResult result) {
        synchronized (searchActions) {
            searchActions.stream().forEach(filter -> filter.closeSearch(result));
        }
    }


    public void init(SearchContext context) {
        synchronized (searchActions) {
            searchActions.stream().forEach(filter -> filter.init(context));
        }
    }


    public void close(SearchMoveResult result) {
        synchronized (searchActions) {
            searchActions.stream().forEach(filter -> filter.close(result));
        }
    }

    @Override
    public void reset() {
        synchronized (searchActions) {
            searchActions.stream().forEach(filter -> filter.reset());
        }
    }
}
