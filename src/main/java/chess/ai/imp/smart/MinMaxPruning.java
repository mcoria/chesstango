/**
 *
 */
package chess.ai.imp.smart;

import chess.ai.imp.smart.evaluation.GameEvaluator;
import chess.board.Color;
import chess.board.Game;
import chess.board.moves.Move;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

/**
 * @author Mauricio Coria
 */
public class MinMaxPruning extends AbstractSmart {
    private static final int DEFAULT_MAX_PLIES = 6;
    private final GameEvaluator evaluator;
    private final MoveSorter moveSorter;

    private final int plies;
    private final List<Move> moveStacks[];

    public MinMaxPruning() {
        this(DEFAULT_MAX_PLIES);
    }

    public MinMaxPruning(int level) {
        this(level, new GameEvaluator(), new MoveSorter());
    }

    public MinMaxPruning(int level, GameEvaluator evaluator, MoveSorter moveSorter) {
        this.plies = level;
        this.moveStacks = new List[level];
        for (int i = 0; i < level; i++) {
            moveStacks[i] = new ArrayList<>();
        }
        this.evaluator = evaluator;
        this.moveSorter = moveSorter;
    }

    @Override
    public Move searchBestMove(Game game) {
        this.keepProcessing = true;

        final boolean minOrMax = Color.WHITE.equals(game.getChessPosition().getCurrentTurn()) ? false : true;

        List<Move> currentPath = this.moveStacks[plies - 1];

        int bestValue = minOrMax ? GameEvaluator.INFINITE_POSITIVE : GameEvaluator.INFINITE_NEGATIVE;
        Move bestMove = null;
        boolean search = true;

        Queue<Move> sortedMoves = moveSorter.sortMoves(game.getPossibleMoves());
        while (!sortedMoves.isEmpty() && search && keepProcessing) {
            Move move = sortedMoves.poll();

            game = game.executeMove(move);

            int currentValue = minOrMax ? maximize(game,plies - 1, GameEvaluator.INFINITE_NEGATIVE, bestValue) :
                                            minimize(game,plies - 1, bestValue, GameEvaluator.INFINITE_POSITIVE);

            if (minOrMax && currentValue < bestValue || !minOrMax && currentValue > bestValue) {
                bestValue = currentValue;
                bestMove = move;
                if (minOrMax && bestValue == GameEvaluator.BLACK_WON||             //Black wins
                        !minOrMax && bestValue == GameEvaluator.WHITE_WON) {        //White wins
                    search = false;
                }

                currentPath.clear();
                currentPath.add(bestMove);
                if(plies > 1){
                    currentPath.addAll(this.moveStacks[plies - 2]);
                }
            }

            game = game.undoMove();
        }
        evaluation = bestValue;


        if (minOrMax && evaluation == GameEvaluator.INFINITE_POSITIVE  || !minOrMax && evaluation == GameEvaluator.INFINITE_NEGATIVE) {
            assert(currentPath.size() == 0);
            currentPath.clear();
            // Necesitamos seleccionar algun movimiento
            Iterator<Move> possibleMovesIterator = game.getPossibleMoves().iterator();
            while (possibleMovesIterator.hasNext()) {
                Move move = possibleMovesIterator.next();
                bestMove = move;
                currentPath.add(bestMove);
                break;
            }
            //TODO: deberiamos selecionar algun path
            //if(plies > 1){
            //    currentPath.addAll(this.moveStacks[plies - 2]);
            //}
        }
        //printPath(currentPath);

        return bestMove;
    }

    protected int minimize(Game game, final int currentPly, final int alpha, final int beta) {
        if (currentPly == 0 || !game.getStatus().isInProgress()) {
            if(currentPly > 0) {
                moveStacks[currentPly - 1].clear();
            }
            return evaluator.evaluate(game);
        } else {
            final List<Move> currentPath = moveStacks[currentPly - 1];
            currentPath.clear();
            boolean search = true;
            int minValue = GameEvaluator.INFINITE_POSITIVE;

            for (Queue<Move> sortedMoves = moveSorter.sortMoves(game.getPossibleMoves());
                 search && keepProcessing && !sortedMoves.isEmpty(); ) {
                Move move = sortedMoves.poll();

                game = game.executeMove(move);

                int currentValue = maximize(game, currentPly - 1, alpha, Math.min(minValue, beta));

                if(currentValue < minValue) {
                    minValue = currentValue;
                    if (alpha >= minValue) {
                        search = false;
                    }

                    currentPath.clear();
                    currentPath.add(move);
                    if(currentPly > 1){
                        currentPath.addAll(moveStacks[currentPly - 2]);
                    }
                }

                game = game.undoMove();
            }
            return minValue;
        }
    }

    protected int maximize(Game game, final int currentLevel, final int alpha, final int beta) {
        if (currentLevel == 0 || !game.getStatus().isInProgress()) {
            if(currentLevel > 0) {
                moveStacks[currentLevel - 1].clear();;
            }
            return evaluator.evaluate(game);
        } else {
            final List<Move> currentPath = moveStacks[currentLevel - 1];
            boolean search = true;
            int maxValue = GameEvaluator.INFINITE_NEGATIVE;

            for (Queue<Move> sortedMoves = moveSorter.sortMoves(game.getPossibleMoves());
                 search && keepProcessing && !sortedMoves.isEmpty(); ) {
                Move move = sortedMoves.poll();

                game = game.executeMove(move);

                int currentValue = minimize(game,currentLevel - 1, Math.max(maxValue, alpha), beta);

                if(currentValue > maxValue) {
                    maxValue = currentValue;
                    if (maxValue >= beta) {
                        search = false;
                    }

                    currentPath.clear();
                    currentPath.add(move);
                    if(currentLevel > 1){
                        currentPath.addAll(moveStacks[currentLevel - 2]);
                    }
                }

                game = game.undoMove();
            }
            return maxValue;
        }
    }

    private void printPath(List<Move> path) {
        System.out.println("Evaluation = " + this.evaluation);
        for (Move move: path) {
            System.out.println(move);

        }
        System.out.println("=======================");
    }

}
