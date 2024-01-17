package net.chesstango.search.smart.alphabeta;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.MoveEvaluation;
import net.chesstango.search.MoveEvaluationType;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.*;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFilter;
import net.chesstango.search.smart.transposition.TranspositionEntry;

/**
 * @author Mauricio Coria
 */
public class AlphaBetaFacade implements SmartAlgorithm, SearchByCycleListener, SearchByDepthListener {

    @Setter
    @Getter
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

        return new MoveEvaluation(bestMove, bestValue, MoveEvaluationType.EXACT);
    }

    @Override
    public void beforeSearch(SearchByCycleContext context) {
        this.game = context.getGame();
    }

    @Override
    public void afterSearch() {
    }

    @Override
    public void beforeSearchByDepth(SearchByDepthContext context) {
    }

    @Override
    public void afterSearchByDepth(SearchMoveResult result) {

    }
}
