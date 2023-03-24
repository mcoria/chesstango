package net.chesstango.board.perft.imp;

import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MoveContainerReader;
import net.chesstango.board.perft.Perft;
import net.chesstango.board.perft.PerftResult;
import net.chesstango.board.representations.fen.FENEncoder;

import java.util.*;
import java.util.function.Function;

/**
 * @author Mauricio Coria
 *
 */
public class PerftWithMap<T> implements Perft  {
	
	private static final int[] capacities = new int[]{1, 20, 400, 7602, 101240, 1240671, 1240671, 1240671};

	private final Function<Game, T> fnGetGameId;
	
	private int maxLevel;
	
	private List<Map<T, Long>> nodeListMap;
	private int[] repetedNodes;

	public PerftWithMap(Function<Game, T> fnGetGameId) {
		this.fnGetGameId = fnGetGameId;
	}

	public PerftResult start(Game board, int maxLevel) {
		this.maxLevel = maxLevel;
		this.nodeListMap = new  ArrayList<Map<T, Long>>(maxLevel + 1);
		this.repetedNodes = new int[maxLevel + 1];
		
		for(int i = 0; i < maxLevel + 1; i++){
			Map<T, Long> nodeMap = new HashMap<T, Long>(capacities[i]);
			nodeListMap.add(nodeMap);
		}
		
		return visitLevel1(board);
		
	}
	
	private PerftResult visitLevel1(Game game) {
		PerftResult perftResult = new PerftResult();

		long totalNodes = 0;


		Iterable<Move> movimientosPosible = game.getPossibleMoves();
		
		
		if (maxLevel == 1) {
			for (Move move : movimientosPosible) {
				int nodeCount = 1;

				perftResult.add(move, 1);

				totalNodes += nodeCount;
			}
		} else {
			for (Move move : movimientosPosible) {
				long nodeCount = 0;

				game.executeMove(move);

				if (maxLevel > 1) {
					nodeCount = visitChilds(game, 2);
				} else {
					nodeCount = 1;
				}

				perftResult.add(move, nodeCount);

				totalNodes += nodeCount;

				game.undoMove();
			}
		}
		
		perftResult.setTotalNodes(totalNodes);

		
		return perftResult;
	}	

	private long visitChilds(Game game, int level) {
		long totalNodes = 0;

		MoveContainerReader movimientosPosible = game.getPossibleMoves();

		if (level < this.maxLevel) {
			Map<T, Long> nodeMap = nodeListMap.get(level);
			
			for (Move move : movimientosPosible) {
				Long nodeCount = null;
						
				game.executeMove(move);

				T id = fnGetGameId.apply(game);

				nodeCount = nodeMap.get(id);

				if (nodeCount == null) {

					nodeCount = visitChilds(game, level + 1);
					
					nodeMap.put(id, nodeCount);

				} else {
					repetedNodes[level]++;
				}

				totalNodes += nodeCount;

				game.undoMove();
			}
		} else {
			totalNodes = movimientosPosible.size();
		}

		return totalNodes;
	}
	
	
	
	
	public void printResult(PerftResult result) {
		System.out.println("Total Moves: " + result.getMovesCount());
		System.out.println("Total Nodes: " + result.getTotalNodes());
		
		Map<Move, Long> childs = result.getChilds();
		
		if(childs != null){
			List<Move> moves = new ArrayList<Move>(childs.keySet());
			Collections.reverse(moves);
			
			for (Move move : moves) {
	            System.out.println("Move = " + move.toString() + 
                        ", Total = " + childs.get(move)); 				
			}
		}
		
		for (int i = 0; i < repetedNodes.length; i++) {
			System.out.println("Level " + i + " nodes=" + nodeListMap.get(i).size() + " repeated=" + repetedNodes[i]);
		}
		
		//System.out.println("DefaultLegalMoveGenerator "  + DefaultLegalMoveGenerator.count);
		//System.out.println("NoCheckLegalMoveGenerator "  + NoCheckLegalMoveGenerator.count);
	}


	private static final FENEncoder coder = new FENEncoder();
	//TODO: este metodo se esta morfando una parte significativa de la ejecucion
	public static String getStringGameId(Game game) {
		game.getChessPosition().constructBoardRepresentation(coder);
		return coder.getFENWithoutClocks();
	}

	public static Long getZobristGameId(Game game) {
		return game.getChessPosition().getHash();
	}
}
