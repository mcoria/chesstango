package net.chesstango.search.smart.alphabeta;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.search.RootChildEvaluation;
import net.chesstango.search.Bound;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.SearchAlgorithm;

/**
 * @author Mauricio Coria
 */
public class AlphaBetaFacade implements SearchAlgorithm {

    @Setter
    @Getter
    private AlphaBetaFilter alphaBetaFilter;

    @Setter
    private Game game;

    @Getter
    private RootChildEvaluation bestMoveEvaluation;

    @Override
    public void search() {
        final Color currentTurn = game.getPosition().getCurrentTurn();

        final long bestMoveAndValue = Color.WHITE.equals(currentTurn) ?
                alphaBetaFilter.maximize(0, Evaluator.INFINITE_NEGATIVE, Evaluator.INFINITE_POSITIVE) :
                alphaBetaFilter.minimize(0, Evaluator.INFINITE_NEGATIVE, Evaluator.INFINITE_POSITIVE);

        final int bestValue = AlphaBetaHelper.decodeValue(bestMoveAndValue);
        final short bestMoveEncoded = AlphaBetaHelper.decodeMove(bestMoveAndValue);

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


        this.bestMoveEvaluation = new RootChildEvaluation(bestMove, bestValue, Bound.EXACT);
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
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
