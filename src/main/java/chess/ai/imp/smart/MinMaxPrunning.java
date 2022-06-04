/**
 *
 */
package chess.ai.imp.smart;

import java.util.*;

import chess.board.Color;
import chess.board.Game;
import chess.board.moves.Move;
import chess.board.moves.containers.MoveContainerReader;

/**
 * @author Mauricio Coria
 */
public class MinMaxPrunning extends AbstractSmart {
    private static final int DEFAULT_MAX_PLIES = 6;
    private final int plies;
    private final GameEvaluator evaluator = new GameEvaluator();
    private final List<Move> moveStacks[];
    private Game game = null;

    public MinMaxPrunning() {
        this(DEFAULT_MAX_PLIES);
    }

    public MinMaxPrunning(int level) {
        this.plies = level;
        this.moveStacks = new List[level];
        for (int i = 0; i < level; i++) {
            moveStacks[i] = new ArrayList<>();
        }
    }

    @Override
    public Move findBestMove(final Game game) {
        this.game = game;
        this.keepProcessing = true;

        final boolean minOrMax = Color.WHITE.equals(game.getChessPositionReader().getCurrentTurn()) ? false : true;

        List<Move> currentPath = this.moveStacks[plies - 1];

        int bestValue = minOrMax ? GameEvaluator.INFINITE_POSITIVE : GameEvaluator.INFINITE_NEGATIVE ;
        boolean search = true;
        Iterator<Move> possibleMovesIterator = game.getPossibleMoves().iterator();
        while (possibleMovesIterator.hasNext() && search && keepProcessing) {
            Move move = possibleMovesIterator.next();
            game.executeMove(move);

            int currentValue = minOrMax ? maximize(plies - 1, GameEvaluator.INFINITE_NEGATIVE, bestValue) :
                                            minimize(plies - 1, bestValue, GameEvaluator.INFINITE_POSITIVE);

            if (minOrMax && currentValue < bestValue || !minOrMax && currentValue > bestValue) {
                bestValue = currentValue;
                if (minOrMax && bestValue == GameEvaluator.BLACK_WON||             //Black wins
                        !minOrMax && bestValue == GameEvaluator.WHITE_WON) {        //White wins
                    search = false;
                }

                currentPath.clear();
                currentPath.add(move);
                if(plies > 1){
                    currentPath.addAll(this.moveStacks[plies - 2]);
                }
            }

            game.undoMove();
        }
        evaluation = bestValue;

        //printPath(currentPath);

        return currentPath.get(0);
    }

    private int minimize(final int currentPly, final int alpha, final int beta) {
        if (currentPly == 0 || game.getPossibleMoves().size() == 0) {
            if(currentPly > 0) {
                moveStacks[currentPly - 1].clear();
            }
            return evaluator.evaluate(game);
        } else {
            final List<Move> currentPath = moveStacks[currentPly - 1];
            currentPath.clear();
            boolean search = true;
            int minValue = GameEvaluator.INFINITE_POSITIVE;

            for (Iterator<Move> possibleMovesIterator = game.getPossibleMoves().iterator();
                 search && keepProcessing && possibleMovesIterator.hasNext(); ) {
                Move move = possibleMovesIterator.next();

                game.executeMove(move);

                int currentValue = maximize(currentPly - 1, alpha, Math.min(minValue, beta));

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

                game.undoMove();
            }
            return minValue;
        }
    }

    private Integer maximize(final int currentLevel, final int alpha, final int beta) {
        if (currentLevel == 0 || game.getPossibleMoves().size() == 0) {
            if(currentLevel > 0) {
                moveStacks[currentLevel - 1].clear();;
            }
            return evaluator.evaluate(game);
        } else {
            final List<Move> currentPath = moveStacks[currentLevel - 1];
            boolean search = true;
            int maxValue = GameEvaluator.INFINITE_NEGATIVE;

            for (Iterator<Move> possibleMovesIterator = game.getPossibleMoves().iterator();
                 search && keepProcessing && possibleMovesIterator.hasNext(); ) {
                Move move = possibleMovesIterator.next();

                game.executeMove(move);

                int currentValue = minimize(currentLevel - 1, Math.max(maxValue, alpha), beta);

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

                game.undoMove();
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
