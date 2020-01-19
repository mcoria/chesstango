package main;

import java.util.Map;

import chess.Move;
import chess.Square;

public class Node {
	
	private String id;
	
	private int level;
	
	private Map<Move, Node> childs;
	
	private int childNodesCounter;
	
	public Node(String id, int level) {
		this.id = id;
		this.level = level;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Map<Move, Node> getChilds() {
		return childs;
	}

	public void setChilds(Map<Move, Node> childs) {
		this.childs = childs;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getChildNodesCounter() {
		return childNodesCounter;
	}

	public int getMoves() {
		return childs.size();
	}
	
	public void setChildNodesCounter(int childTotal) {
		this.childNodesCounter = childTotal;
	}

	public Node getChildNode(Square from, Square to) {
		Node node = null;
		for (Map.Entry<Move, Node> entry : childs.entrySet()) {
			Move move = entry.getKey();
			if(move.getFrom().getKey().equals(from) && move.getTo().getKey().equals(to)){
				node = entry.getValue();
				childs.remove(move);
				break;
			}
		}
		return node;
	}

}
