package main;

import java.util.Map;

import chess.Move;

public class Node {
	
	private String id;
	
	private int level;
	
	private Map<Move, Node> childs;
	
	private int childTotal;
	
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

	public int getChildTotal() {
		return childTotal;
	}

	public void setChildTotal(int childTotal) {
		this.childTotal = childTotal;
	}

}
