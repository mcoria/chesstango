package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import chess.Board;
import chess.Move;
import parsers.FENCoder;
import parsers.FENParser;

public class ChessMain {

	public static void main(String[] args) {
		Board board = FENParser.parseFEN(FENParser.INITIAL_FEN);
		
		ChessMain main = new ChessMain();
		
		Node rootNode = main.start(board, 5);
		
		main.printNode(board, rootNode);
		
	}

	public void printNode(Board board, Node rootNode) {
		System.out.println("Total Nodes: " + rootNode.getChildNodesCounter());
		System.out.println("Total Moves: " + rootNode.getMoves());
		
		Map<Move, Node> childs = rootNode.getChilds();
		if(childs != null){
			List<Move> moves = new ArrayList<Move>(childs.keySet());
			Collections.sort(moves);
			Collections.reverse(moves);
			
			for (Move move : moves) {
	            System.out.println("Move = " + move.toString() + 
                        ", Total = " + childs.get(move).getChildNodesCounter()); 				
			}
		}
		
		System.out.println(FENCoder.codeFEN(board));
	}

	private int maxLevel;
	private FENCoder coder = new FENCoder();
	
	private List<Map<String, Node>> nodeListMap; 

	Node start(Board board, int maxLevel) {
		this.maxLevel = maxLevel;
		this.nodeListMap = new  ArrayList<Map<String, Node>>(maxLevel);
		
		String rootId = coder.code(board);
		Node rootNode = new Node(rootId, 0);
		visitChilds(board, 1, rootNode);
		
		return rootNode;
		
	}

	private void visitChilds(Board board, int currentLevel, Node currentNode) {
		int totalMoves = 0;
		Map<String, Node> nodeMap = getNodeMap(currentLevel);
		Map<Move, Node> childNodes = new HashMap<Move, Node>();
		for (Move move : board.getMovimientosPosibles()) {
			board.executeMove(move);
			
			String id = coder.code(board);
			Node node = nodeMap.get(id);
			if(node==null){
				node = new Node(id, currentLevel + 1);
				if (currentLevel < this.maxLevel) {
					visitChilds(board, currentLevel + 1, node);
				} else if (currentLevel == this.maxLevel) {
					node.setChildNodesCounter(1);
				} else {
					throw new RuntimeException("Error");
				}
				nodeMap.put(id, node);
			}
			
			childNodes.put(move, node);
			totalMoves += node.getChildNodesCounter();

			board.undoMove();
		}
			
		currentNode.setChilds(childNodes);
		currentNode.setChildNodesCounter(totalMoves);
	}

	private Map<String, Node> getNodeMap(int currentLevel) {
		Map<String, Node> nodeMap = null;
		if(nodeListMap.size() <  currentLevel){
			nodeMap = new HashMap<String, Node>();
			nodeListMap.add(nodeMap);
		}
		return nodeListMap.get(currentLevel - 1);
	}

}
