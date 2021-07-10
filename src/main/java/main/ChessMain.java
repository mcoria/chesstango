package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import builder.ChessBuilderConcrete;
import chess.Game;
import chess.Move;
import parsers.FENCoder;
import parsers.FENParser;

public class ChessMain {
	
	private int maxLevel;
	
	private FENCoder coder = new FENCoder();
	
	private List<Map<String, Node>> nodeListMap;
	private int[] repetedNodes;
	
	public static void main(String[] args) {
		ChessBuilderConcrete builder = new ChessBuilderConcrete();
		
		FENParser parser = new FENParser(new ChessBuilderConcrete());
		
		parser.parseFEN(FENParser.INITIAL_FEN);
		
		Game board = builder.buildGame();
		
		ChessMain main = new ChessMain();
		
		Node rootNode = main.start(board, 5);
		
		main.printNode(board, rootNode);
		
	}

	public Node start(Game board, int maxLevel) {
		this.maxLevel = maxLevel;
		this.nodeListMap = new  ArrayList<Map<String, Node>>(maxLevel);
		this.repetedNodes = new int[maxLevel];
		
		String rootId = code(board);
		Node rootNode = new Node(rootId, 0);
		visitChilds(board, rootNode);
		
		return rootNode;
		
	}

	private void visitChilds(Game board, Node currentNode) {
		int totalMoves = 0;
		
		int currentLevel = currentNode.getLevel() + 1;
		
		Map<String, Node> nodeMap = getNodeMap(currentLevel);
		
		Map<Move, Node> childNodes = new HashMap<Move, Node>();
		
		for (Move move : board.getMovimientosPosibles()) {
			board.executeMove(move);

			String id = code(board);
			Node node = nodeMap.get(id);
			if (node == null) {
				node = new Node(id, currentLevel);
				if (currentLevel < this.maxLevel) {
					visitChilds(board, node);
				} else if (currentLevel == this.maxLevel) {
					node.setChildNodesCounter(1);
				} else {
					throw new RuntimeException("Error");
				}
				nodeMap.put(id, node);
			}else {
				repetedNodes[currentLevel - 1]++;
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
	
	public void printNode(Game board, Node rootNode) {
		System.out.println("Total Nodes: " + rootNode.getChildNodesCounter());
		System.out.println("Total Moves: " + rootNode.getMoves());
		
		Map<Move, Node> childs = rootNode.getChilds();
		if(childs != null){
			List<Move> moves = new ArrayList<Move>(childs.keySet());
			Collections.reverse(moves);
			
			for (Move move : moves) {
	            System.out.println("Move = " + move.toString() + 
                        ", Total = " + childs.get(move).getChildNodesCounter()); 				
			}
		}
		
		for (int i = 0; i < repetedNodes.length; i++) {
			System.out.println("Level " + i + " Nodes=" + nodeListMap.get(i).size() + " repeated=" + repetedNodes[i]);
		}
	}
	
	private String code(Game board) {
		board.getTablero().buildRepresentation(coder);
		return coder.getFEN();
	}

}
