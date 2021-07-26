package main;

import java.util.Map;

import chess.Move;
import chess.Square;

public class Node {
	
	private String id;
	
	private Map<Move, Node> childs;
	
	private int childNodesCounter;
	
	public Node(String id) {
		this.id = id;
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

	public String divide() {
		String result = "";
		for (Map.Entry<Move, Node> entry : childs.entrySet()) {
			Move move = entry.getKey();
			Node node = entry.getValue();
			result += move.getFrom().getKey().toString() + move.getTo().getKey().toString() + " " + node.getChildNodesCounter() + "\n";
		}
		return result;
	}
	
	
	@Override
	public int hashCode() {
		return id.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Node){
			Node theOther = (Node) obj;
			return id.equals(theOther.id);
		}
		return false;
	}

}
