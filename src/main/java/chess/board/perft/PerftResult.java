package chess.board.perft;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import chess.board.Square;
import chess.board.moves.Move;

/**
 * @author Mauricio Coria
 *
 */
public class PerftResult {
	
	private int totalNodes;
	
	private Map<Move, Integer> moves = new HashMap<Move, Integer>();

	public void add(Move move, int nodeCount) {
		moves.put(move, nodeCount);
	}

	public int getTotalNodes() {
		return totalNodes;
	}

	public void setTotalNodes(int totalNodes) {
		this.totalNodes = totalNodes;
	}

	public int getMovesCount() {
		return moves.size();
	}

	public Map<Move, Integer> getChilds(){
		return moves;
	}

	public int getChildNode(Square from, Square to) {
		for(Entry<Move, Integer> entry: moves.entrySet()){
			Move move = entry.getKey();
			if(move.getFrom().getKey().equals(from) && move.getTo().getKey().equals(to)){
				return entry.getValue();
			}
		}
		throw new RuntimeException("Move not found");
	}
	
	public boolean moveExists(Square from, Square to) {
		for(Entry<Move, Integer> entry: moves.entrySet()){
			Move move = entry.getKey();
			if(move.getFrom().getKey().equals(from) && move.getTo().getKey().equals(to)){
				return true;
			}
		}
		return false;
	}
}