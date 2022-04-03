/**
 * 
 */
package chess.board.perft.imp;

import java.time.Duration;
import java.time.Instant;

import chess.board.Game;
import chess.board.builder.imp.GameBuilder;
import chess.board.fen.FENDecoder;
import chess.board.perft.Perft;
import chess.board.perft.PerftResult;
import chess.board.perft.imp.PerftBrute;

/**
 * @author Mauricio Coria
 *
 */
public class PerftMain {

	public static void main(String[] args) {
		GameBuilder builder = new GameBuilder();
		
		FENDecoder parser = new FENDecoder(builder);
		
		parser.parseFEN(FENDecoder.INITIAL_FEN);
		
		Game board = builder.getResult();
		
		//Perft main = new PerftWithMap();
		Perft main = new PerftBrute();
		
		Instant start = Instant.now();
		PerftResult result = main.start(board, 6);
		Instant end = Instant.now();
		
		main.printResult(result);
		
		Duration timeElapsed = Duration.between(start, end);
		System.out.println("Time taken: "+ timeElapsed.toMillis() +" ms");
	}	
}
