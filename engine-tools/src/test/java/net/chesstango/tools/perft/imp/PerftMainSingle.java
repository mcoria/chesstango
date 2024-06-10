package net.chesstango.tools.perft.imp;

import net.chesstango.board.Game;
import net.chesstango.board.builders.GameBuilder;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.tools.perft.Perft;
import net.chesstango.tools.perft.PerftResult;

import java.time.Duration;
import java.time.Instant;

/**
 * @author Mauricio Coria
 *
 * In case we want to excecute a very long test and measure performance
 */
public class PerftMainSingle {

	public static void main(String[] args) {
		//String fen = FENDecoder.INITIAL_FEN;
		String fen = "r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1";  //KiwipeteTest

		System.out.println(String.format("FEN =  %s", fen));

		GameBuilder builder = new GameBuilder();
		
		FENDecoder parser = new FENDecoder(builder);

		parser.parseFEN(fen);
		
		Game board = builder.getChessRepresentation();
		
		//Perft main = new PerftWithMap();
		Perft main = new PerftBrute();
		
		Instant start = Instant.now();
		PerftResult result = main.start(board, 8);
		Instant end = Instant.now();
		
		main.printResult(result);
		
		Duration timeElapsed = Duration.between(start, end);
		System.out.println("Time taken: "+ timeElapsed.toMillis() +" ms");
	}	
}
