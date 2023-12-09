package net.chesstango.search.smart.negamax;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.*;
import net.chesstango.search.smart.sorters.MoveSorter;
import net.chesstango.search.smart.statistics.NodeStatistics;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class NegaMaxPruning implements SmartAlgorithm, SearchCycleListener, SearchByDepthListener, StopSearchListener {
    private volatile boolean keepProcessing;
    private final NegaQuiescence negaQuiescence;
    private Game game;
    private MoveSorter moveSorter;
    private int[] visitedNodesCounter;
    private int maxPly;


    public NegaMaxPruning(NegaQuiescence negaQuiescence) {
        this.negaQuiescence = negaQuiescence;
    }

    @Override
    public SearchMoveResult search() {
        this.keepProcessing = true;
        this.visitedNodesCounter = new int[30];

        //this.moveSorter.beforeSearchByDepth(context);

        final boolean minOrMax = !Color.WHITE.equals(game.getChessPosition().getCurrentTurn());
        final List<Move> bestMoves = new ArrayList<Move>();
        final Color currentTurn = game.getChessPosition().getCurrentTurn();

        int bestValue = GameEvaluator.INFINITE_NEGATIVE;
        boolean search = true;

        List<Move> sortedMoves = moveSorter.getSortedMoves();
        Iterator<Move> moveIterator = sortedMoves.iterator();
        while (moveIterator.hasNext() && search && keepProcessing) {
            Move move = moveIterator.next();

            game.executeMove(move);

            int currentValue = -negaMax(game, maxPly - 1, GameEvaluator.INFINITE_NEGATIVE, -bestValue);

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

        return new SearchMoveResult(maxPly, minOrMax ? -bestValue : bestValue, bestMove, null)
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

    @Override
    public void beforeSearch(Game game) {
        this.game = game;
        this.moveSorter.beforeSearch(game);
        this.negaQuiescence.setupGameEvaluator(game);
    }

    @Override
    public void afterSearch(SearchMoveResult result) {
    }

    @Override
    public void beforeSearchByDepth(SearchContext context) {
        this.maxPly = context.getMaxPly();
    }

    @Override
    public void afterSearchByDepth(SearchMoveResult result) {

    }

    public void setVisitedNodesCounter(int[] visitedNodesCounter) {
        this.visitedNodesCounter = visitedNodesCounter;
    }

    public void setMoveSorter(MoveSorter moveSorter) {
        this.moveSorter = moveSorter;
    }
}
