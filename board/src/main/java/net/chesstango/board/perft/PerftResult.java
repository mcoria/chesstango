package net.chesstango.board.perft;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;

/**
 * @author Mauricio Coria
 *
 */
public class PerftResult {
	
	private long totalNodes;
	
	private final Map<Move, Long> moves = new HashMap<Move, Long>();

	public void add(Move move, long nodeCount) {
		moves.put(move, nodeCount);
	}

	public long getTotalNodes() {
		return totalNodes;
	}

	public void setTotalNodes(long totalNodes) {
		this.totalNodes = totalNodes;
	}

	public long getMovesCount() {
		return moves.size();
	}

	public Map<Move, Long> getChilds(){
		return moves;
	}

	public long getChildNode(Square from, Square to) {
		for(Entry<Move, Long> entry: moves.entrySet()){
			Move move = entry.getKey();
			if(move.getFrom().getKey().equals(from) && move.getTo().getKey().equals(to)){
				return entry.getValue();
			}
		}
		throw new RuntimeException("Move not found");
	}
	
	public boolean moveExists(Square from, Square to) {
		for(Entry<Move, Long> entry: moves.entrySet()){
			Move move = entry.getKey();
			if(move.getFrom().getKey().equals(from) && move.getTo().getKey().equals(to)){
				return true;
			}
		}
		return false;
	}
}
