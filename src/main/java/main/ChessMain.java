package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import chess.Board;
import chess.Move;
import chess.Square;
import parsers.FENCoder;
import parsers.FENParser;

public class ChessMain {

	public static void main(String[] args) {
		//Board board = FENParser.parseFEN(FENParser.INITIAL_FEN);
		Board board = FENParser.parseFEN("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq -");
		
		board.executeMove(Square.d5, Square.d6);
		board.executeMove(Square.h3, Square.g2);
		
		ChessMain main = new ChessMain();
		
		Node rootNode = main.start(board, 1);
		
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
	
	private Map<String, Node> nodeMap = new HashMap<String, Node>();

	Node start(Board board, int maxLevel) {
		this.maxLevel = maxLevel;
		
		String rootId = coder.code(board);
		Node rootNode = new Node(rootId, 1);
		visitChilds(board, 1, rootNode);
		
		return rootNode;
		
	}

	private void visitChilds(Board board, int currentLevel, Node currentNode) {
		int totalMoves = 0;
		Map<Move, Node> childNodes = new HashMap<Move, Node>();
		Set<Move> posibles = board.getMovimientosPosibles();
		for (Move move : posibles) {
			String id = coder.code(board);
			Node node = nodeMap.get(id);
			if (node == null) {
				node = new Node(id, currentLevel);
				if (currentLevel < this.maxLevel) {
					board.executeMove(move);

					visitChilds(board, currentLevel + 1, node);

					board.undoMove();
				} else if (currentLevel == this.maxLevel) {
					node.setChildNodesCounter(1);
				} else {
					throw new RuntimeException("Error");
				}
			}
			childNodes.put(move, node);
			totalMoves += node.getChildNodesCounter();
		}
			
		currentNode.setChilds(childNodes);
		currentNode.setChildNodesCounter(totalMoves);
	}

}
