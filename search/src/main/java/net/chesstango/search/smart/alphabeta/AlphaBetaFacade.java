package net.chesstango.search.smart.alphabeta;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.search.MoveEvaluation;
import net.chesstango.search.MoveEvaluationType;
import net.chesstango.search.SearchResultByDepth;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.SearchAlgorithm;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFilter;
import net.chesstango.search.smart.features.transposition.TranspositionEntry;

/**
 * @author Mauricio Coria
 */
public class AlphaBetaFacade implements SearchAlgorithm {

    @Setter
    @Getter
    private AlphaBetaFilter alphaBetaFilter;

    @Setter
    private Game game;

    private MoveEvaluation bestMoveEvaluation;

    @Override
    public void search() {
        final Color currentTurn = game.getPosition().getCurrentTurn();

        final long bestMoveAndValue = Color.WHITE.equals(currentTurn) ?
                alphaBetaFilter.maximize(0, Evaluator.INFINITE_NEGATIVE, Evaluator.INFINITE_POSITIVE) :
                alphaBetaFilter.minimize(0, Evaluator.INFINITE_NEGATIVE, Evaluator.INFINITE_POSITIVE);

        final int bestValue = TranspositionEntry.decodeValue(bestMoveAndValue);
        final short bestMoveEncoded = TranspositionEntry.decodeBestMove(bestMoveAndValue);

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
    public void beforeSearch() {
        this.bestMoveEvaluation = null;
    }

    @Override
    public void beforeSearchByDepth() {
        this.bestMoveEvaluation = null;
    }

    @Override
    public void afterSearchByDepth(SearchResultByDepth result) {
        result.setBestMoveEvaluation(bestMoveEvaluation);

        /**
         * Aca hay un issue; si PV.depth > currentSearchDepth quiere decir que es un mate encontrado más alla del horizonte
         * Deberiamos continuar buscando hasta que se encuentre un mate antes del horizonte
         */
        result.setContinueDeepening(
                Evaluator.WHITE_WON != bestMoveEvaluation.evaluation() &&
                        Evaluator.BLACK_WON != bestMoveEvaluation.evaluation()
        );
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
