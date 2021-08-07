package main;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import builder.ChessBuilderGame;
import chess.Game;
import movecalculators.DefaultLegalMoveCalculator;
import movecalculators.NoCheckLegalMoveCalculator;
import moveexecutors.Move;
import parsers.FENCoder;
import parsers.FENParser;

public class ChessMain {
	
	private static final int capacities[] = new int[]{1, 20, 400, 7602, 101240, 1240671, 0};
	
	private int maxLevel;
	
	private FENCoder coder = new FENCoder();
	
	private List<Map<String, Node>> nodeListMap;
	private int[] repetedNodes;
	
	public static void main(String[] args) {
		ChessBuilderGame builder = new ChessBuilderGame();
		
		FENParser parser = new FENParser(builder);
		
		parser.parseFEN(FENParser.INITIAL_FEN);
		
		Game board = builder.getGame();
		
		ChessMain main = new ChessMain();
		
		Node rootNode = main.start(board, 5);
		
		main.printNode(board, rootNode);
		
	}

	public Node start(Game board, int maxLevel) {
		this.maxLevel = maxLevel;
		this.nodeListMap = new  ArrayList<Map<String, Node>>(maxLevel + 1);
		this.repetedNodes = new int[maxLevel + 1];
		
		for(int i = 0; i < maxLevel + 1; i++){
			Map<String, Node> nodeMap = new HashMap<String, Node>(capacities[i]);
			nodeListMap.add(nodeMap);
		}
		
		
		String rootId = code(board);
		Node rootNode = new Node(rootId);
		
		Map<String, Node> nodeMap = nodeListMap.get(0);
		nodeMap.put(rootId, rootNode);
		
		visitChilds(board, rootNode, 1);
		
		return rootNode;
		
	}

	private void visitChilds(Game game, Node currentNode, int level) {
		int totalMoves = 0;

		Map<String, Node> nodeMap = nodeListMap.get(level);

		Collection<Move> movimientosPosible = game.getMovimientosPosibles();

		Map<Move, Node> childNodes = new HashMap<Move, Node>(movimientosPosible.size());

		for (Move move : movimientosPosible) {

			game.executeMove(move);

			String id = code(game);
			
			Node node = nodeMap.get(id);

			if (node == null) {
				node = new Node(id);
				nodeMap.put(id, node);

				if (level < this.maxLevel) {
					visitChilds(game, node, level + 1);
				} else {
					node.setChildNodesCounter(1);
				}

			} else {
				repetedNodes[level]++;
			}
			
			childNodes.put(move, node);
			totalMoves += node.getChildNodesCounter();
			
			game.undoMove();
		}

		currentNode.setChilds(childNodes);
		currentNode.setChildNodesCounter(totalMoves);
	}
	
	
	
	
	public void printNode(Game game, Node rootNode) {
		System.out.println("Total Moves: " + rootNode.getMoves());
		System.out.println("Total Nodes: " + rootNode.getChildNodesCounter());
		
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
			System.out.println("Level " + i + " nodes=" + nodeListMap.get(i).size() + " repeated=" + repetedNodes[i]);
		}
		
		System.out.println("DefaultLegalMoveCalculator "  + DefaultLegalMoveCalculator.count);
		System.out.println("NoCheckLegalMoveCalculator "  + NoCheckLegalMoveCalculator.count);
	}
	
	//TODO: este metodo se esta morfando una parte significativa de la ejecucion
	private String code(Game board) {
		board.getTablero().buildRepresentation(coder);
		return coder.getFEN();
	}

}
