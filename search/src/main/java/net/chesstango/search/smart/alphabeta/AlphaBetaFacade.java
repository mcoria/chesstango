package net.chesstango.search.smart.alphabeta;

import lombok.Setter;
import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.SearchContext;
import net.chesstango.search.smart.SearchCycleListener;
import net.chesstango.search.smart.SearchSmart;
import net.chesstango.search.smart.SmartListenerMediator;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFilter;
import net.chesstango.search.smart.transposition.TranspositionEntry;

/**
 * @author Mauricio Coria
 */
public class AlphaBetaFacade implements SearchSmart, SearchCycleListener {

    @Setter
    private AlphaBetaFilter alphaBetaFilter;

    @Setter
    private SmartListenerMediator smartListenerMediator;

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
        smartListenerMediator.triggerStopSearching();
    }

    @Override
    public void beforeSearch(Game game) {
        this.game = game;
    }

    @Override
    public void afterSearch(SearchMoveResult result) {
    }

    @Override
    public void reset() {
        smartListenerMediator.triggerReset();
    }

    @Override
    public void beforeSearchByDepth(SearchContext context) {
        smartListenerMediator.triggerBeforeSearchByDepth(context);
    }

    @Override
    public void afterSearchByDepth(SearchMoveResult result) {
        smartListenerMediator.triggerAfterSearchByDepth(result);
    }
}
