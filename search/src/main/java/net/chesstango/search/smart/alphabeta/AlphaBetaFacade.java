package net.chesstango.search.smart.alphabeta;

import lombok.Setter;
import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.MoveEvaluation;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.*;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFilter;
import net.chesstango.search.smart.transposition.TranspositionEntry;

/**
 * @author Mauricio Coria
 */
public class AlphaBetaFacade implements SmartAlgorithm, SearchByCycleListener, SearchByDepthListener {

    @Setter
    private AlphaBetaFilter alphaBetaFilter;

    private Game game;


    @Override
    public MoveEvaluation search() {
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

        //return new SearchMoveResult(maxPly, bestValue, bestMove, null);
        return new MoveEvaluation(bestMove, bestValue);
    }

    @Override
    public void beforeSearch(Game game) {
        this.game = game;
    }

    @Override
    public void afterSearch(SearchMoveResult result) {
    }

    @Override
    public void beforeSearchByDepth(SearchContext context) {
    }

    @Override
    public void afterSearchByDepth(SearchMoveResult result) {

    }
}
