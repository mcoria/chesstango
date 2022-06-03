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
    private static final int DEFAULT_MAXLEVEL = 5;
    private final int maxLevel;
    private final GameEvaluator evaluator = new GameEvaluator();
    private final List<Move> moveStacks[];
    private Game game = null;


    public MinMaxPrunning() {
        this(DEFAULT_MAXLEVEL);
    }

    public MinMaxPrunning(int level) {
        this.maxLevel = level;
        this.moveStacks = new List[level];
        for (int i = 0; i < level; i++) {
            moveStacks[i] = new ArrayList<>();
        }
    }

    @Override
    public Move findBestMove(final Game game) {
        this.game = game;
        this.keepProcessing = true;

        final List<List<Move>> possiblePaths = new ArrayList<List<Move>>();
        final boolean minOrMax = Color.WHITE.equals(game.getChessPositionReader().getCurrentTurn()) ? false : true;

        List<Move> currentPath = this.moveStacks[maxLevel - 1];

        int bestValue = minOrMax ? GameEvaluator.INFINITE_POSITIVE : GameEvaluator.INFINITE_NEGATIVE ;
        boolean search = true;
        Iterator<Move> possibleMovesIterator = game.getPossibleMoves().iterator();
        while (possibleMovesIterator.hasNext() && search && keepProcessing) {
            Move move = possibleMovesIterator.next();
            game.executeMove(move);

            int currentValue = minOrMax ? maximize(maxLevel - 1, GameEvaluator.INFINITE_NEGATIVE, bestValue) :
                                            minimize(maxLevel - 1, bestValue, GameEvaluator.INFINITE_POSITIVE);

            if (minOrMax && currentValue < bestValue || !minOrMax && currentValue > bestValue) {
                bestValue = currentValue;
                if (minOrMax && bestValue == GameEvaluator.BLACK_WON||             //Black wins
                        !minOrMax && bestValue == GameEvaluator.WHITE_WON) {        //White wins
                    search = false;
                }


                possiblePaths.clear();
            }

            if (currentValue == bestValue) {
                currentPath.add(move);
                if(maxLevel > 1){
                    currentPath.addAll(this.moveStacks[maxLevel - 2]);
                }
                possiblePaths.add(currentPath);

                currentPath = new ArrayList<>();
            }

            game.undoMove();
        }
        evaluation = bestValue;

        printpossiblePaths(possiblePaths);

        Collection possibleMoves = new HashSet<Move>();
        possiblePaths.stream().forEach( path -> possibleMoves.add(path.get(0)) );

        return selectMove(possibleMoves);
    }

    private int minimize(final int currentLevel, final int alpha, final int beta) {
        MoveContainerReader possibleMoves = game.getPossibleMoves();
        if (currentLevel == 0 || possibleMoves.size() == 0) {
            if(currentLevel > 0) {
                moveStacks[currentLevel - 1].clear();
            }
            return evaluator.evaluate(game);
        } else {
            final List<Move> currentPath = moveStacks[currentLevel - 1];
            currentPath.clear();
            boolean search = true;
            int minValue = GameEvaluator.INFINITE_POSITIVE;
            Iterator<Move> possibleMovesIterator = possibleMoves.iterator();
            while (possibleMovesIterator.hasNext() && search && keepProcessing) {
                Move move = possibleMovesIterator.next();
                game.executeMove(move);

                int currentValue = maximize(currentLevel - 1, alpha, Math.min(minValue, beta));

                if(currentValue < minValue) {
                    minValue = currentValue;
                    if (alpha >= minValue) {
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
            return minValue;
        }
    }

    private Integer maximize(final int currentLevel, final int alpha, final int beta) {
        MoveContainerReader possibleMoves = game.getPossibleMoves();
        if (currentLevel == 0 || possibleMoves.size() == 0) {
            if(currentLevel > 0) {
                moveStacks[currentLevel - 1].clear();;
            }
            return evaluator.evaluate(game);
        } else {
            final List<Move> currentPath = moveStacks[currentLevel - 1];
            currentPath.clear();
            boolean search = true;
            int maxValue = GameEvaluator.INFINITE_NEGATIVE;
            Iterator<Move> possibleMovesIterator = possibleMoves.iterator();
            while (possibleMovesIterator.hasNext() && search && keepProcessing) {
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

    private void printpossiblePaths(List<List<Move>> possiblePaths) {
        int pathNumber = 1;
        System.out.println("Evaluation = " + this.evaluation);
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

}
