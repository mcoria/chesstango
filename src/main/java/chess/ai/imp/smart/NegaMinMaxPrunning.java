/**
 *
 */
package chess.ai.imp.smart;

import chess.board.Color;
import chess.board.Game;
import chess.board.moves.Move;
import chess.board.moves.containers.MoveContainerReader;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Mauricio Coria
 */
public class NegaMinMaxPrunning extends AbstractSmart {
    private static final int DEFAULT_MAXLEVEL = 7;
    private final int maxLevel;
    private final GameEvaluator evaluator = new GameEvaluator();
    private Game game = null;
    private boolean keepProcessing;

    private final List<Move> moveStack[];

    public NegaMinMaxPrunning() {
        this(DEFAULT_MAXLEVEL);
    }

    public NegaMinMaxPrunning(int level) {
        this.maxLevel = level;
        this.moveStack = new List[level];
        for (int i = 0; i < level; i++) {
            moveStack[i] = new ArrayList<>();
        }
    }

    @Override
    public Move findBestMove(final Game game) {
        this.game = game;
        this.keepProcessing = true;

        final List<List<Move>> possiblePaths = new ArrayList<List<Move>>();
        final List<Move> currentPath = moveStack[maxLevel - 1];


        int alpha =  GameEvaluator.INFINITE_NEGATIVE;
        final int beta = GameEvaluator.INFINITE_POSITIVE;

        boolean search = true;
        Iterator<Move> possibleMovesIterator = game.getPossibleMoves().iterator();
        while (possibleMovesIterator.hasNext() && search && keepProcessing) {
            Move move = possibleMovesIterator.next();
            game.executeMove(move);

            int currentValue = - negaMinMax(maxLevel - 1, -beta, -alpha);

            if (currentValue > alpha) {
                alpha = currentValue;
                if (alpha == GameEvaluator.INFINITE_POSITIVE) {
                    search = false;
                }

                possiblePaths.clear();
                currentPath.clear();
                currentPath.add(move);
                if(maxLevel > 1){
                    currentPath.addAll(moveStack[maxLevel - 2]);
                }

                possiblePaths.add( currentPath.stream().collect(Collectors.toList()) );


            } else if (currentValue == alpha) {
                currentPath.clear();
                currentPath.add(move);
                if(maxLevel > 1){
                    currentPath.addAll(moveStack[maxLevel - 2]);
                }
                possiblePaths.add( currentPath.stream().collect(Collectors.toList()) );
            }

            game.undoMove();
        }


        List<Move> possibleMoves = new ArrayList<>();
        for (List<Move> path: possiblePaths){
            possibleMoves.add(path.get(0));
        }

        System.out.println("Score = " + alpha);
        printPossiblePaths(possiblePaths);

        return selectMove(possibleMoves);
    }

    private Integer negaMinMax(final int currentLevel, int alpha, final int beta) {
        final MoveContainerReader possibleMoves = game.getPossibleMoves();
        if (currentLevel == 0 || possibleMoves.size() == 0) {
            if(currentLevel > 0){
                moveStack[currentLevel - 1].clear();
            }
            return evaluator.evaluate(game);
        } else {
            final List<Move> currentPath = moveStack[currentLevel - 1];
            boolean search = true;
            int maxValue = GameEvaluator.INFINITE_NEGATIVE;
            Iterator<Move> possibleMovesIterator = possibleMoves.iterator();
            while (possibleMovesIterator.hasNext() && search && keepProcessing) {
                Move move = possibleMovesIterator.next();
                game.executeMove(move);

                int currentValue = - negaMinMax(currentLevel - 1, -beta, -alpha);

                if(currentValue > maxValue){
                    maxValue = currentValue;
                    if(maxValue > alpha) {
                        alpha = maxValue;
                        if (alpha >= beta) {
                            search = false;
                        }
                    }

                    currentPath.clear();
                    currentPath.add(move);
                    if(currentLevel > 1){
                        currentPath.addAll(moveStack[currentLevel - 2]);
                    }
                } else if (currentValue == maxValue) {
                    currentPath.clear();
                    currentPath.add(move);
                    if(currentLevel > 1){
                        currentPath.addAll(moveStack[currentLevel - 2]);
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

    private void printPossiblePaths(List<List<Move>> possiblePaths) {
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
}
