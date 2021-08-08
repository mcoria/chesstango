package main;

import java.util.HashMap;
import java.util.Map;

import moveexecutors.Move;

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

	public Map<Move, Integer> getMoves(){
		return moves;
	}
}
