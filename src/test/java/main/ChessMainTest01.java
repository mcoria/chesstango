package main;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import chess.Board;
import chess.Square;
import parsers.FENParser;

//Kiwipete
public class ChessMainTest01 {

	private ChessMain main;
	
	@Before
	public void setUp() throws Exception {
		main = new ChessMain();
	}

	@Test
	public void test01() {
		Board board = FENParser.parseFEN("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq -");
		Node rootNode = main.start(board, 1);
		
		assertEquals(48, board.getMovimientosPosibles().size());
		assertEquals(48, rootNode.getChildNodesCounter());
	}
	
	@Test
	public void test02() {
		Board board = FENParser.parseFEN("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq -");
		Node rootNode = main.start(board, 2);
		
		assertEquals(48, board.getMovimientosPosibles().size());
		//assertEquals(2034, rootNode.getChildNodesCounter());
		assertEquals(43, rootNode.getChildNode(Square.e1, Square.g1).getChildNodesCounter());
	}		
}
