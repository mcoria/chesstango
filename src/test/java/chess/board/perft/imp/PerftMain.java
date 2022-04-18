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
		
		//parser.parseFEN(FENDecoder.INITIAL_FEN);
		parser.parseFEN("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1"); //KiwipeteTest
		
		Game board = builder.getResult();
		
		//Perft main = new PerftWithMap();
		Perft main = new PerftBrute();
		
		Instant start = Instant.now();
		PerftResult result = main.start(board, 5);
		Instant end = Instant.now();
		
		main.printResult(result);
		
		Duration timeElapsed = Duration.between(start, end);
		System.out.println("Time taken: "+ timeElapsed.toMillis() +" ms");
	}	
}
