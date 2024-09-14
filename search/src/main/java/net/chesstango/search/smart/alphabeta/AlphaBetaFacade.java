package net.chesstango.search.smart.alphabeta;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.search.MoveEvaluation;
import net.chesstango.search.MoveEvaluationType;
import net.chesstango.search.SearchResult;
import net.chesstango.search.SearchResultByDepth;
import net.chesstango.search.smart.SearchAlgorithm;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByDepthContext;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFilter;
import net.chesstango.search.smart.features.transposition.TranspositionEntry;

/**
 * @author Mauricio Coria
 */
public class AlphaBetaFacade implements SearchAlgorithm {

    @Setter
    @Getter
    private AlphaBetaFilter alphaBetaFilter;

    private Game game;

    private MoveEvaluation bestMoveEvaluation;

    @Override
    public void search() {
        final Color currentTurn = game.getChessPosition().getCurrentTurn();

        long bestMoveAndValue = Color.WHITE.equals(currentTurn) ?
                alphaBetaFilter.maximize(0, Evaluator.INFINITE_NEGATIVE, Evaluator.INFINITE_POSITIVE) :
                alphaBetaFilter.minimize(0, Evaluator.INFINITE_NEGATIVE, Evaluator.INFINITE_POSITIVE);

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


        this.bestMoveEvaluation = new MoveEvaluation(bestMove, bestValue, MoveEvaluationType.EXACT);
    }

    @Override
    public void beforeSearch(SearchByCycleContext context) {
        this.game = context.getGame();
        this.bestMoveEvaluation = null;
    }

    @Override
    public void afterSearch(SearchResult result) {
        result.setBestMoveEvaluation(bestMoveEvaluation);
    }

    @Override
    public void beforeSearchByDepth(SearchByDepthContext context) {
        this.bestMoveEvaluation = null;
    }

    @Override
    public void afterSearchByDepth(SearchResultByDepth result) {
        result.setBestMoveEvaluation(bestMoveEvaluation);

        /**
         * Aca hay un issue; si PV.depth > currentSearchDepth quiere decir que es un mate encontrado más alla del horizonte
         */
        result.setSearchNextDepth(
                Evaluator.WHITE_WON != bestMoveEvaluation.evaluation() &&
                Evaluator.BLACK_WON != bestMoveEvaluation.evaluation()
        );
    }
}
