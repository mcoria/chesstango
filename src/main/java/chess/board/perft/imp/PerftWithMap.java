package chess.board.perft.imp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import chess.board.Game;
import chess.board.fen.FENEncoder;
import chess.board.moves.Move;
import chess.board.moves.containers.MoveContainerReader;
import chess.board.perft.Perft;
import chess.board.perft.PerftResult;

/**
 * @author Mauricio Coria
 *
 */
public class PerftWithMap implements Perft  {
	
	private static final int[] capacities = new int[]{1, 20, 400, 7602, 101240, 1240671, 1240671, 1240671};
	
	private int maxLevel;
	
	private final FENEncoder coder = new FENEncoder();
	
	private List<Map<String, Integer>> nodeListMap;
	private int[] repetedNodes;

	public PerftResult start(Game board, int maxLevel) {
		this.maxLevel = maxLevel;
		this.nodeListMap = new  ArrayList<Map<String, Integer>>(maxLevel + 1);
		this.repetedNodes = new int[maxLevel + 1];
		
		for(int i = 0; i < maxLevel + 1; i++){
			Map<String, Integer> nodeMap = new HashMap<String, Integer>(capacities[i]);
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
				int nodeCount = 0;

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

	private int visitChilds(Game game, int level) {
		int totalNodes = 0;

		MoveContainerReader movimientosPosible = game.getPossibleMoves();

		if (level < this.maxLevel) {
			Map<String, Integer> nodeMap = nodeListMap.get(level);
			
			for (Move move : movimientosPosible) {
				Integer nodeCount = null;
						
				game.executeMove(move);

				String id = getGameId(game);

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
	
	//TODO: este metodo se esta morfando una parte significativa de la ejecucion
	private String getGameId(Game board) {
		board.getChessPositionReader().constructBoardRepresentation(coder);
		return coder.getResult();
	}

}
