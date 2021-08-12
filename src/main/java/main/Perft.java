package main;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import builder.ChessBuilderGame;
import chess.Game;
import movecalculators.DefaultLegalMoveCalculator;
import movecalculators.NoCheckLegalMoveCalculator;
import moveexecutors.Move;
import parsers.FENParser;

public class Perft {
	
	private int maxLevel;
	
	public static void main(String[] args) {
		ChessBuilderGame builder = new ChessBuilderGame();
		
		FENParser parser = new FENParser(builder);
		
		parser.parseFEN(FENParser.INITIAL_FEN);
		
		Game board = builder.getGame();
		
		Perft main = new Perft();
		
		Instant start = Instant.now();
		PerftResult perftResult = main.start(board, 7);
		Instant end = Instant.now();
		
		main.printResult(perftResult);

		
		Duration timeElapsed = Duration.between(start, end);
		System.out.println("Time taken: "+ timeElapsed.toMillis() +" milliseconds");
	}

	public PerftResult start(Game game, int maxLevel) {
		this.maxLevel = maxLevel;
		PerftResult perftResult = new PerftResult();
		int totalNodes = 0;

		Collection<Move> movimientosPosible = game.getMovimientosPosibles();

		for (Move move : movimientosPosible) {
			int nodeCount = 0;
			
			game.executeMove(move);

			if(maxLevel > 1){
				nodeCount = visitChilds(game, 2);
			} else {
				nodeCount = 1;
			}

			perftResult.add(move, nodeCount);
			
			totalNodes += nodeCount;
			
			game.undoMove();
		}
		
		perftResult.setTotalNodes(totalNodes);
		
		return perftResult;
		
	}

	private int visitChilds(Game game, int level) {
		int totalNodes = 0;
		
		Collection<Move> movimientosPosible = game.getMovimientosPosibles();
		

		if (level < this.maxLevel) {

			for (Move move : movimientosPosible) {
	
				game.executeMove(move);
	
				totalNodes += visitChilds(game, level + 1);

				game.undoMove();
			}
		} else {
			totalNodes = movimientosPosible.size();
		}


		return totalNodes;
	}
	
	
	
	
	public void printResult(PerftResult result) {
		System.out.println("Total Moves: " + result.getMovesCount());
		System.out.println("Total Nodes: " + result.getTotalNodes());
		
		Map<Move, Integer> childs = result.getChilds();
		
		if(childs != null){
			List<Move> moves = new ArrayList<Move>(childs.keySet());
			Collections.reverse(moves);
			
			for (Move move : moves) {
	            System.out.println("Move = " + move.toString() + 
                        ", Total = " + childs.get(move)); 				
			}
		}
		
		System.out.println("DefaultLegalMoveCalculator "  + DefaultLegalMoveCalculator.count);
		System.out.println("NoCheckLegalMoveCalculator "  + NoCheckLegalMoveCalculator.count);
	}
	

}
