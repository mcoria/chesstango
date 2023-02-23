package net.chesstango.board.perft.imp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MoveContainerReader;
import net.chesstango.board.perft.Perft;
import net.chesstango.board.perft.PerftResult;

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

        Iterable<Move> movimientosPosible = game.getPossibleMoves();

        for (Move move : movimientosPosible) {
            long nodeCount = 0;

            if (maxLevel > 1) {
                game.executeMove(move);
                nodeCount = visitChildren(game, 2);
                game.undoMove();
            } else {
                nodeCount = 1;
            }

            perftResult.add(move, nodeCount);

            totalNodes += nodeCount;

        }

        perftResult.setTotalNodes(totalNodes);

        return perftResult;

    }

    private long visitChildren(Game game, int level) {
        long totalNodes = 0;

        MoveContainerReader movimientosPosible = game.getPossibleMoves();

        if (level < this.maxLevel) {

            for (Move move : movimientosPosible) {

                game.executeMove(move);

                totalNodes += visitChildren(game, level + 1);

                game.undoMove();
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
