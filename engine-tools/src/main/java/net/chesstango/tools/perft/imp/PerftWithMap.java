package net.chesstango.tools.perft.imp;

import net.chesstango.board.Game;
import net.chesstango.board.builders.ChessPositionBuilder;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.containers.MoveContainerReader;
import net.chesstango.board.representations.fen.FENEncoderWithoutClocks;
import net.chesstango.tools.perft.Perft;
import net.chesstango.tools.perft.PerftResult;

import java.util.*;
import java.util.function.Function;

/**
 * @author Mauricio Coria
 */
public class PerftWithMap<T> implements Perft {

    private static final int[] capacities = new int[]{1, 20, 400, 7602, 101240, 1240671, 1240671, 1240671};

    private final Function<Game, T> fnGetGameId;

    private int maxLevel;

    private List<Map<T, Long>> nodeListMap;
    private int[] repeatedNodes;

    public PerftWithMap(Function<Game, T> fnGetGameId) {
        this.fnGetGameId = fnGetGameId;
    }

    public PerftResult start(Game board, int maxLevel) {
        this.maxLevel = maxLevel;
        this.nodeListMap = new ArrayList<Map<T, Long>>(maxLevel + 1);
        this.repeatedNodes = new int[maxLevel + 1];

        for (int i = 0; i < maxLevel + 1; i++) {
            Map<T, Long> nodeMap = new HashMap<T, Long>(capacities[i]);
            nodeListMap.add(nodeMap);
        }

        return visitLevel1(board);

    }

    private PerftResult visitLevel1(Game game) {
        PerftResult perftResult = new PerftResult();
        long totalNodes = 0;

        Iterable<Move> movimientosPosible = game.getPossibleMoves();

        for (Move move : movimientosPosible) {
            long nodeCount = 0;

            if (maxLevel > 1) {
                game.executeMove(move);
                nodeCount = visitChild(game, 2);
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

    private long visitChild(Game game, int level) {
        long totalNodes = 0;

        MoveContainerReader movimientosPosible = game.getPossibleMoves();

        if (level < this.maxLevel) {

            for (Move move : movimientosPosible) {

                game.executeMove(move);

                totalNodes += searchNode(game, level);

                game.undoMove();
            }
        } else {
            totalNodes = movimientosPosible.size();
        }

        return totalNodes;
    }

    private Long searchNode(Game game, int level) {
        Map<T, Long> nodeMap = nodeListMap.get(level);

        T id = fnGetGameId.apply(game);

        Long nodeCount = nodeMap.get(id);

        if (nodeCount == null) {

            nodeCount = visitChild(game, level + 1);

            nodeMap.put(id, nodeCount);

        } else {
            repeatedNodes[level]++;
        }

        return nodeCount;
    }


    public void printResult(PerftResult result) {
        System.out.println("Total Moves: " + result.getMovesCount());
        System.out.println("Total Nodes: " + result.getTotalNodes());

        Map<Move, Long> childs = result.getChilds();

        if (childs != null) {
            List<Move> moves = new ArrayList<Move>(childs.keySet());
            Collections.reverse(moves);

            for (Move move : moves) {
                System.out.println("Move = " + move.toString() +
                        ", Total = " + childs.get(move));
            }
        }

        for (int i = 0; i < repeatedNodes.length; i++) {
            System.out.println("Level " + i + " nodes=" + nodeListMap.get(i).size() + " repeated=" + repeatedNodes[i]);
        }

        //System.out.println("DefaultLegalMoveGenerator "  + DefaultLegalMoveGenerator.count);
        //System.out.println("NoCheckLegalMoveGenerator "  + NoCheckLegalMoveGenerator.count);
    }


    private static final ChessPositionBuilder<String> coder = new FENEncoderWithoutClocks();

    //TODO: este metodo se esta morfando una parte significativa de la ejecucion
    public static String getStringGameId(Game game) {
        game.getChessPosition().constructChessPositionRepresentation(coder);
        return coder.getChessRepresentation();
    }

    public static Long getZobristGameId(Game game) {
        return game.getChessPosition().getZobristHash();
    }
}
