package chess.main;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import chess.Game;
import chess.Square;
import chess.builder.ChessPositionBuilderImp;
import chess.debug.builder.DebugChessFactory;
import chess.parsers.FENParser;


//Kiwipete
/**
 * @author Mauricio Coria
 *
 */
public class Test3Tests {

	private ChessMain main;
	
	private Game board;
	
	@Before
	public void setUp() throws Exception {
		main = new ChessMain();
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
		ChessPositionBuilderImp builder = new ChessPositionBuilderImp(new DebugChessFactory());
		//ChessBuilderGame builder = new ChessBuilderGame();

		FENParser parser = new FENParser(builder);
		
		parser.parseFEN(string);
		
		return builder.getGame();
	}	
}
