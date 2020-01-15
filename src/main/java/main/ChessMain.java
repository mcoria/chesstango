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
		Board board = FENParser.parseFEN(FENParser.INITIAL_FEN);
		
		board.executeMove(Square.a2, Square.a4);
		board.executeMove(Square.a7, Square.a6);
		board.executeMove(Square.a4, Square.a5);
		board.executeMove(Square.b7, Square.b5);
		
		ChessMain main = new ChessMain();
		
		Node rootNode = main.start(board, 1);
		
		main.printNode(board, rootNode);
	}

	private void printNode(Board board, Node rootNode) {
		System.out.println("Total Nodes: " + rootNode.getChildNodesCounter());
		System.out.println("Total Moves: " + board.getMovimientosPosibles().size());
		
		Map<Move, Node> childs = rootNode.getChilds();
		if(childs != null){
			List<Move> moves = new ArrayList<Move>(childs.keySet());
			Collections.sort(moves);
			Collections.reverse(moves);
			
			for (Move move : moves) {
	            System.out.println("Move = " + move.toString() + 
                        ", Total = " + childs.get(move).getChildNodesCounter()); 				
			}
		} else {
			List<Move> moves = new ArrayList<Move>(board.getMovimientosPosibles());
			Collections.sort(moves);
			Collections.reverse(moves);
			
			for (Move move : moves) {
	            System.out.println("Move = " + move.toString() + 
                        ", Total = 1"); 				
			}			
		}
	}

	private int maxLevel;
	private FENCoder coder = new FENCoder();

	Node start(Board board, int maxLevel) {
		this.maxLevel = maxLevel;
		
		String rootId = coder.code(board);
		Node rootNode = new Node(rootId, 1);
		visitChilds(board, 1, rootNode);
		
		return rootNode;
		
	}

	private void visitChilds(Board board, int currentLevel, Node currentNode) {
		int totalMoves = 0;
		Set<Move> posibles = board.getMovimientosPosibles();
		if(currentLevel < this.maxLevel){
			Map<Move, Node> childNodes = new HashMap<Move, Node>();
			for (Move move : posibles) {
				board.executeMove(move);
				
				String id = coder.code(board);
				Node node = new Node(id, 0);
				visitChilds(board, currentLevel + 1, node);
				totalMoves += node.getChildNodesCounter();
				
				board.undoMove();
				
				childNodes.put(move, node);
			}
			currentNode.setChilds(childNodes);
		} else {
			totalMoves = posibles.size();
		}
		currentNode.setChildNodesCounter(totalMoves);
	}

}
