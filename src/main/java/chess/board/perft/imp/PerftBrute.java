package chess.board.perft.imp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import chess.board.Game;
import chess.board.moves.Move;
import chess.board.perft.Perft;
import chess.board.perft.PerftResult;

/**
 * @author Mauricio Coria
 *
 */
public class PerftBrute implements Perft {
	
	private int maxLevel;


	@Override
	public PerftResult start(Game game, int maxLevel) {
		this.maxLevel = maxLevel;
		PerftResult perftResult = new PerftResult();
		int totalNodes = 0;

		Collection<Move> movimientosPosible = game.getPossibleMoves();

		for (Move move : movimientosPosible) {
			int nodeCount = 0;

			if(maxLevel > 1){
				game.executeMove(move);
				nodeCount = visitChilds(game, 2);
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

	private int visitChilds(Game game, int level) {
		int totalNodes = 0;
		
		Collection<Move> movimientosPosible = game.getPossibleMoves();
		

		if (level < this.maxLevel) {

			for (Move move : movimientosPosible) {
	
				game.executeMove(move);
	
				totalNodes += visitChilds(game, level + 1);

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
		
		Map<Move, Integer> childs = perftResult.getChilds();
		
		if(childs != null){
			List<Move> moves = new ArrayList<Move>(childs.keySet());
			Collections.reverse(moves);
			
			for (Move move : moves) {
	            System.out.println("Move = " + move.toString() + 
                        ", Total = " + childs.get(move)); 				
			}
		}
		//System.out.println("DefaultLegalMoveGenerator "  + DefaultLegalMoveGenerator.count);
		//System.out.println("NoCheckLegalMoveGenerator "  + NoCheckLegalMoveGenerator.count);		
	}	

}
