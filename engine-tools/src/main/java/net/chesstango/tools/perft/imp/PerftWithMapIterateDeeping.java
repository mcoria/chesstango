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
public class PerftWithMapIterateDeeping<T> implements Perft {
    private final Function<Game, T> fnGetGameId;
    protected int maxLevel;
    protected int depth;

    protected Map<T, Long[]> transpositionTable;

    public PerftWithMapIterateDeeping(Function<Game, T> fnGetGameId) {
        this.fnGetGameId = fnGetGameId;
        this.transpositionTable = new HashMap<>();
    }

    public PerftResult start(Game game, int depth) {
        this.depth = depth;

        PerftResult result = null;
        for (int i = 1; i <= depth; i++) {
            try {
                result = visitLevel1(game, i);
            } catch (RuntimeException e) {
                System.err.printf("Error with game board: %s\n", game);
                throw e;
            }
        }
        return result;

        //return visitLevel1(board, depth);
    }

    protected PerftResult visitLevel1(Game game, int maxLevel) {
        PerftResult perftResult = new PerftResult();
        this.maxLevel = maxLevel;
        long totalNodes = 0;

        T id = fnGetGameId.apply(game);
        Long nodeCounts[] = transpositionTable.computeIfAbsent(id, k -> new Long[depth]);

        Iterable<Move> movimientosPosible = game.getPossibleMoves();
        for (Move move : movimientosPosible) {
            long nodeCount = 0;

            if (maxLevel > 1) {
                game.executeMove(move);

                T childId = fnGetGameId.apply(game);
                Long childNodeCounts[] = transpositionTable.computeIfAbsent(childId, k -> new Long[depth]);

                nodeCount = visitChild(game, 2, childNodeCounts);

                game.undoMove();
            } else {
                nodeCount = 1;
            }

            perftResult.add(move, nodeCount);

            totalNodes += nodeCount;

        }
        perftResult.setTotalNodes(totalNodes);

        nodeCounts[maxLevel - 1] = totalNodes;

        return perftResult;
    }

    protected long visitChild(Game game, int level, Long[] nodeCounts) {
        long totalNodes = 0;

        MoveContainerReader movimientosPosible = game.getPossibleMoves();

        if (level < this.maxLevel) {
            for (Move move : movimientosPosible) {

                game.executeMove(move);

                T idChild = fnGetGameId.apply(game);

                Long childNodeCounts[] = transpositionTable.computeIfAbsent(idChild, k -> new Long[depth]);

                if (childNodeCounts[maxLevel - level - 1] == null) {
                    visitChild(game, level + 1, childNodeCounts);
                }

                totalNodes += childNodeCounts[maxLevel - level - 1];

                game.undoMove();
            }
        } else {
            totalNodes = movimientosPosible.size();
        }

        nodeCounts[maxLevel - level] = totalNodes;

        return totalNodes;
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

		/*
		for (int i = 0; i < repeatedNodes.length; i++) {
			System.out.println("Level " + i + " nodes=" + nodeListMap.get(i).size() + " repeated=" + repeatedNodes[i]);
		}*/

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
