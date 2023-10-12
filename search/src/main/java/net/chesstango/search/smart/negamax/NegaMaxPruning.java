package net.chesstango.search.smart.negamax;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.MoveSelector;
import net.chesstango.search.smart.SearchContext;
import net.chesstango.search.smart.SearchSmart;
import net.chesstango.search.smart.sorters.MoveSorter;
import net.chesstango.search.smart.statistics.NodeStatistics;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class NegaMaxPruning implements SearchSmart {
    private volatile boolean keepProcessing;
    private Game game;
    private MoveSorter moveSorter;
    private final NegaQuiescence negaQuiescence;
    private int[] visitedNodesCounter;

    public NegaMaxPruning(NegaQuiescence negaQuiescence) {
        this.negaQuiescence = negaQuiescence;
    }

    @Override
    public SearchMoveResult search(SearchContext context) {
        this.keepProcessing = true;
        this.visitedNodesCounter = new int[context.getMaxPly()];

        this.moveSorter.beforeSearchByDepth(context);

        final boolean minOrMax = Color.WHITE.equals(game.getChessPosition().getCurrentTurn()) ? false : true;
        final List<Move> bestMoves = new ArrayList<Move>();
        final Color currentTurn = game.getChessPosition().getCurrentTurn();

        int bestValue = GameEvaluator.INFINITE_NEGATIVE;
        boolean search = true;

        List<Move> sortedMoves = moveSorter.getSortedMoves();
        Iterator<Move> moveIterator = sortedMoves.iterator();
        while (moveIterator.hasNext() && search && keepProcessing) {
            Move move = moveIterator.next();

            game.executeMove(move);

            int currentValue = -negaMax(game, context.getMaxPly() - 1, GameEvaluator.INFINITE_NEGATIVE, -bestValue);

            if (currentValue > bestValue) {
                bestValue = currentValue;
                bestMoves.clear();
                bestMoves.add(move);

                // Stop searching if we have found checkmate
                if (bestValue == GameEvaluator.WHITE_WON) {
                    search = false;
                }

            } else if (currentValue == bestValue) {
                bestMoves.add(move);
            }

            game.undoMove();
        }


        Move bestMove = MoveSelector.selectMove(currentTurn, bestMoves);

        return new SearchMoveResult(context.getMaxPly(), minOrMax ? -bestValue : bestValue, bestMove, null)
                .setRegularNodeStatistics(new NodeStatistics(new int[30], visitedNodesCounter))
                .setBestMoves(bestMoves);
    }

    @Override
    public void stopSearching() {
        this.keepProcessing = false;
    }

    protected int negaMax(Game game, final int currentPly, final int alpha, final int beta) {
        visitedNodesCounter[visitedNodesCounter.length - currentPly - 1]++;
        if (currentPly == 0 || !game.getStatus().isInProgress()) {
            return negaQuiescence.quiescenceMax(game, alpha, beta);
        } else {
            boolean search = true;
            int maxValue = GameEvaluator.INFINITE_NEGATIVE;

            List<Move> sortedMoves = moveSorter.getSortedMoves();
            Iterator<Move> moveIterator = sortedMoves.iterator();
            while (moveIterator.hasNext() && search && keepProcessing) {
                Move move = moveIterator.next();

                game = game.executeMove(move);

                int currentValue = -negaMax(game, currentPly - 1, -beta, -Math.max(maxValue, alpha));

                if (currentValue > maxValue) {
                    maxValue = currentValue;
                    if (maxValue >= beta) {
                        search = false;
                    }
                }

                game = game.undoMove();
            }
            return maxValue;
        }
    }

    public void setVisitedNodesCounter(int[] visitedNodesCounter) {
        this.visitedNodesCounter = visitedNodesCounter;
    }

    public void setMoveSorter(MoveSorter moveSorter) {
        this.moveSorter = moveSorter;
    }

    @Override
    public void beforeSearch(Game game) {
        this.game = game;
        moveSorter.beforeSearch(game);
    }

    @Override
    public void beforeSearchByDepth(SearchContext context) {

    }

    @Override
    public void afterSearchByDepth(SearchMoveResult result) {

    }

    @Override
    public void afterSearch(SearchMoveResult result) {

    }

    @Override
    public void reset() {

    }
}
