package net.chesstango.search.smart.negamax;

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
import net.chesstango.search.smart.*;
import net.chesstango.search.smart.sorters.MoveSorter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class NegaMaxPruning implements SearchAlgorithm {
    private final NegaQuiescence negaQuiescence;

    @Setter
    private Game game;

    @Setter
    @Getter
    private MoveSorter moveSorter;

    @Setter
    private int[] visitedNodesCounter;

    private int maxPly;

    private MoveEvaluation bestMoveEvaluation;

    public NegaMaxPruning(NegaQuiescence negaQuiescence) {
        this.negaQuiescence = negaQuiescence;
    }

    @Override
    public void search() {
        this.visitedNodesCounter = new int[30];

        final boolean minOrMax = !Color.WHITE.equals(game.getPosition().getCurrentTurn());
        final List<Move> bestMoves = new ArrayList<>();
        final Color currentTurn = game.getPosition().getCurrentTurn();

        int bestValue = Evaluator.INFINITE_NEGATIVE;
        boolean search = true;

        Iterable<Move> sortedMoves = moveSorter.getOrderedMoves(0);
        Iterator<Move> moveIterator = sortedMoves.iterator();
        while (moveIterator.hasNext() && search) {
            Move move = moveIterator.next();

            move.executeMove();

            int currentValue = -negaMax(game, maxPly - 1, Evaluator.INFINITE_NEGATIVE, -bestValue);

            if (currentValue > bestValue) {
                bestValue = currentValue;
                bestMoves.clear();
                bestMoves.add(move);

                // Stop searching if we have found checkmate
                if (bestValue == Evaluator.WHITE_WON) {
                    search = false;
                }

            } else if (currentValue == bestValue) {
                bestMoves.add(move);
            }

            move.undoMove();
        }

        Move bestMove = MoveSelector.selectMove(currentTurn, bestMoves);

        bestMoveEvaluation =  new MoveEvaluation(bestMove, minOrMax ? -bestValue : bestValue, MoveEvaluationType.EXACT);
    }

    protected int negaMax(Game game, final int currentPly, final int alpha, final int beta) {
        visitedNodesCounter[visitedNodesCounter.length - currentPly - 1]++;
        if (currentPly == 0 || !game.getStatus().isInProgress()) {
            return negaQuiescence.quiescenceMax(game, alpha, beta);
        } else {
            boolean search = true;
            int maxValue = Evaluator.INFINITE_NEGATIVE;

            Iterable<Move> sortedMoves = moveSorter.getOrderedMoves(currentPly);
            Iterator<Move> moveIterator = sortedMoves.iterator();
            while (moveIterator.hasNext() && search) {
                Move move = moveIterator.next();

                move.executeMove();

                int currentValue = -negaMax(game, currentPly - 1, -beta, -Math.max(maxValue, alpha));

                if (currentValue > maxValue) {
                    maxValue = currentValue;
                    if (maxValue >= beta) {
                        search = false;
                    }
                }

                move.undoMove();
            }
            return maxValue;
        }
    }

    @Override
    public void beforeSearch() {
        if (moveSorter instanceof SearchByCycleListener moveSorterListener) {
            moveSorterListener.beforeSearch();
        }
        this.negaQuiescence.setupGameEvaluator(game);
    }

    @Override
    public void beforeSearchByDepth(SearchByDepthContext context) {
        this.maxPly = context.getMaxPly();
        this.bestMoveEvaluation = null;
    }

    @Override
    public void afterSearchByDepth(SearchResultByDepth result) {
        result.setBestMoveEvaluation(bestMoveEvaluation);

        /**
         * Aca hay un issue; si PV.depth > currentSearchDepth quiere decir que es un mate encontrado más alla del horizonte
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
