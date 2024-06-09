package net.chesstango.tools.perft.imp;

import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.containers.MoveContainerReader;
import net.chesstango.tools.perft.Perft;
import net.chesstango.tools.perft.PerftResult;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Mauricio Coria
 */
public class PerftBrute implements Perft {

    private int maxLevel;


    @Override
    public PerftResult start(Game game, int maxLevel) {
        this.maxLevel = maxLevel;
        PerftResult perftResult = new PerftResult();
        long totalNodes = 0;

        Iterable<Move> possibleMoves = game.getPossibleMoves();

        long zobristHashBefore = game.getChessPosition().getZobristHash();
        for (Move move : possibleMoves) {
            long nodeCount = 0;

            if (maxLevel > 1) {

                long zobristHashMove = game.getChessPosition().getZobristHash(move);
                game.executeMove(move);
                if (zobristHashMove != game.getChessPosition().getZobristHash()) {
                    throw new RuntimeException("Invalid game.getChessPosition().getZobristHash(move);");
                }
                nodeCount = visitChild(game, 2);


                game.undoMove();
                long zobristHashAfter = game.getChessPosition().getZobristHash();
                if (zobristHashBefore != zobristHashAfter) {
                    throw new RuntimeException("hashBefore != hashAfter");
                }
            } else {
                nodeCount = 1;
            }

            perftResult.add(move, nodeCount);

            totalNodes += nodeCount;

        }

        perftResult.setTotalNodes(totalNodes);

        return perftResult;
    }

    private long visitChild(Game game, int level) {
        long totalNodes = 0;

        MoveContainerReader movimientosPosible = game.getPossibleMoves();

        if (level < this.maxLevel) {

            long zobristHashBefore = game.getChessPosition().getZobristHash();

            for (Move move : movimientosPosible) {

                long zobristHashMove = game.getChessPosition().getZobristHash(move);
                game.executeMove(move);
                if (zobristHashMove != game.getChessPosition().getZobristHash()) {
                    throw new RuntimeException("Invalid game.getChessPosition().getZobristHash(move);");
                }

                totalNodes += visitChild(game, level + 1);

                game.undoMove();
                long zobristHashAfter = game.getChessPosition().getZobristHash();
                if (zobristHashBefore != zobristHashAfter) {
                    throw new RuntimeException("hashBefore != hashAfter");
                }

            }
        } else {
            totalNodes = movimientosPosible.size();
        }

        return totalNodes;
    }


    public void printResult(PerftResult perftResult) {
        System.out.println("Total Moves: " + perftResult.getMovesCount());
        System.out.println("Total Nodes: " + perftResult.getTotalNodes());

        Map<Move, Long> childs = perftResult.getChilds();

        if (childs != null) {
            List<Move> moves = new ArrayList<Move>(childs.keySet());
            Collections.reverse(moves);

            for (Move move : moves) {
                System.out.println("Move = " + move.toString() +
                        ", Total = " + childs.get(move));
            }
        }
        //System.out.println("DefaultLegalMoveGenerator "  + DefaultLegalMoveGenerator.count);
        //System.out.println("NoCheckLegalMoveGenerator "  + NoCheckLegalMoveGenerator.count);
        //System.out.println("MAX_MOVECOUNTER_SIZE = "  + NoCheckLegalMoveGenerator.MAX_MOVECOUNTER_SIZE);
    }

}
