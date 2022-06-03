/**
 *
 */
package chess.ai.imp.smart;

import java.util.*;

import chess.board.Color;
import chess.board.Game;
import chess.board.moves.Move;
import chess.board.moves.containers.MoveContainerReader;
import chess.board.position.imp.PositionState;

/**
 * @author Mauricio Coria
 */
public class MinMaxPrunning extends AbstractSmart {
    private static final int DEFAULT_MAXLEVEL = 5;
    private final int maxLevel;
    private final int minPossibleValue;
    private final int maxPossibleValue;
    private final GameEvaluator evaluator = new GameEvaluator();
    private final List<Move> moveStack[];
    private Game game = null;
    private boolean keepProcessing;


    public MinMaxPrunning() {
        this(DEFAULT_MAXLEVEL);
    }

    public MinMaxPrunning(int level) {
        this.maxLevel = level;
        this.minPossibleValue = Integer.MIN_VALUE + level + 1;
        this.maxPossibleValue = Integer.MAX_VALUE - level - 1;
        this.moveStack = new List[level];
        for (int i = 0; i < level; i++) {
            moveStack[i] = new ArrayList<>();
        }
    }

    @Override
    public Move findBestMove(final Game game) {
        this.game = game;
        this.keepProcessing = true;

        final List<Move> possibleMoves = new ArrayList<Move>();
        final List<List<Move>> possiblePaths = new ArrayList<List<Move>>();
        final boolean minOrMax = Color.WHITE.equals(game.getChessPositionReader().getCurrentTurn()) ? false : true;

        int bestValue = minOrMax ? Integer.MAX_VALUE : Integer.MIN_VALUE;
        boolean search = true;
        List<Move> currentStack = moveStack[maxLevel - 1];
        currentStack.clear();
        Iterator<Move> possibleMovesIterator = game.getPossibleMoves().iterator();
        while (possibleMovesIterator.hasNext() && search && keepProcessing) {
            Move move = possibleMovesIterator.next();
            game.executeMove(move);

            int currentValue = minOrMax ? maximize(maxLevel - 1, Integer.MIN_VALUE, bestValue) :
                    minimize(maxLevel - 1, bestValue, Integer.MAX_VALUE);

            if (minOrMax && currentValue < bestValue || !minOrMax && currentValue > bestValue) {
                bestValue = currentValue;
                possibleMoves.clear();
                possiblePaths.clear();
                if (minOrMax && bestValue < minPossibleValue || !minOrMax && bestValue > maxPossibleValue) {
                    search = false;
                }
            }

            if (currentValue == bestValue) {
                possibleMoves.add(move);
                currentStack.add(move);
                if(maxLevel > 1){
                    currentStack.addAll(moveStack[maxLevel - 2]);
                }
                possiblePaths.add(currentStack);
                currentStack = new ArrayList<>();
            }

            game.undoMove();
        }
        printpossiblePaths(possiblePaths);
        return selectMove(possibleMoves);
    }

    private void printpossiblePaths(List<List<Move>> possiblePaths) {
        int pathNumber = 1;
        for (List<Move> path:
                possiblePaths) {
            System.out.println("Path " + pathNumber);
            for (Move move:
                    path) {
                System.out.println(move);

            }
            System.out.println("=======================");
            pathNumber++;
        }
    }

    private int minimize(final int currentLevel, final int alpha, final int beta) {
        MoveContainerReader possibleMoves = game.getPossibleMoves();
        if (currentLevel == 0 || possibleMoves.size() == 0) {
            return evaluator.evaluate(game);
        } else {
            final List<Move> currentStack = moveStack[currentLevel - 1];
            currentStack.clear();
            boolean search = true;
            int minValue = Integer.MAX_VALUE;
            Iterator<Move> possibleMovesIterator = possibleMoves.iterator();
            while (possibleMovesIterator.hasNext() && search && keepProcessing) {
                Move move = possibleMovesIterator.next();
                game.executeMove(move);

                int currentValue = maximize(currentLevel - 1, alpha, Math.min(minValue, beta));

                if(currentValue < minValue) {
                    minValue = currentValue;
                    currentStack.clear();
                    currentStack.add(move);
                    if(currentLevel > 1){
                        currentStack.addAll(moveStack[currentLevel - 2]);
                    }
                    if (alpha >= minValue || minValue < minPossibleValue) {
                        search = false;
                    }
                }

                game.undoMove();
            }
            return minValue;
        }
    }

    private Integer maximize(final int currentLevel, final int alpha, final int beta) {
        MoveContainerReader possibleMoves = game.getPossibleMoves();
        if (currentLevel == 0 || possibleMoves.size() == 0) {
            return evaluator.evaluate(game);
        } else {
            final List<Move> currentStack = moveStack[currentLevel - 1];
            currentStack.clear();
            boolean search = true;
            int maxValue = Integer.MIN_VALUE;
            Iterator<Move> possibleMovesIterator = possibleMoves.iterator();
            while (possibleMovesIterator.hasNext() && search && keepProcessing) {
                Move move = possibleMovesIterator.next();
                game.executeMove(move);

                int currentValue = minimize(currentLevel - 1, Math.max(maxValue, alpha), beta);

                if(currentValue > maxValue) {
                    maxValue = currentValue;
                    currentStack.clear();
                    currentStack.add(move);
                    if(currentLevel > 1){
                        currentStack.addAll(moveStack[currentLevel - 2]);
                    }
                    if (maxValue >= beta || maxValue > maxPossibleValue) {
                        search = false;
                    }
                }

                game.undoMove();
            }
            return maxValue;
        }
    }

    @Override
    public void stopProcessing() {
        keepProcessing = false;
    }

}
