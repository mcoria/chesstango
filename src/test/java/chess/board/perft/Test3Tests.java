package chess.board.perft;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import chess.board.Game;
import chess.board.Square;
import chess.board.builder.imp.GameBuilder;
import chess.board.debug.builder.DebugChessFactory;
import chess.board.fen.FENDecoder;


//Kiwipete
/**
 * @author Mauricio Coria
 *
 */
public class Test3Tests {

	private Perft main;
	
	private Game board;
	
	@Before
	public void setUp() throws Exception {
		main = new Perft();
		board =  getGame("4k2r/6K1/8/8/8/8/8/8 w k - 0 1");
	}

	@Test
	public void test_divide2() {		
		PerftResult result= main.start(board, 2);
		
		assertEquals(14, result.getChildNode(Square.g7, Square.g6));
		assertEquals(13, result.getChildNode(Square.g7, Square.f6));
		assertEquals(5, result.getChildNode(Square.g7, Square.h8));
		
		assertEquals(3, result.getMovesCount());
		assertEquals(32, result.getTotalNodes());
	}
	
	
	@Test
	public void test_g7g6() {
		board.executeMove(Square.g7, Square.g6);
		
		PerftResult result= main.start(board, 1);
		
		assertEquals(1, result.getChildNode(Square.e8, Square.g8));
		assertEquals(1, result.getChildNode(Square.e8, Square.f8));
		assertEquals(1, result.getChildNode(Square.e8, Square.d8));
		assertEquals(1, result.getChildNode(Square.e8, Square.e7));
		assertEquals(1, result.getChildNode(Square.e8, Square.d7));
		assertEquals(1, result.getChildNode(Square.h8, Square.g8));
		assertEquals(1, result.getChildNode(Square.h8, Square.f8));
		assertEquals(1, result.getChildNode(Square.h8, Square.h7));
		assertEquals(1, result.getChildNode(Square.h8, Square.h6));
		assertEquals(1, result.getChildNode(Square.h8, Square.h5));
		assertEquals(1, result.getChildNode(Square.h8, Square.h4));
		assertEquals(1, result.getChildNode(Square.h8, Square.h3));
		assertEquals(1, result.getChildNode(Square.h8, Square.h2));
		assertEquals(1, result.getChildNode(Square.h8, Square.h1));


		assertEquals(14, result.getMovesCount());
		assertEquals(14, result.getTotalNodes());
	}	
	
	private Game getGame(String string) {		
		GameBuilder builder = new GameBuilder(new DebugChessFactory());
		//ChessBuilderGame builder = new ChessBuilderGame();

		FENDecoder parser = new FENDecoder(builder);
		
		parser.parseFEN(string);
		
		return builder.getResult();
	}	
}
