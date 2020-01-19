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
		assertEquals(2039, rootNode.getChildNodesCounter());
		assertEquals(43, rootNode.getChildNode(Square.e1, Square.g1).getChildNodesCounter());
		assertEquals(43, rootNode.getChildNode(Square.e1, Square.c1).getChildNodesCounter());
		assertEquals(43, rootNode.getChildNode(Square.e1, Square.f1).getChildNodesCounter());
		assertEquals(43, rootNode.getChildNode(Square.e1, Square.d1).getChildNodesCounter());
		
		assertEquals(41, rootNode.getChildNode(Square.d5, Square.d6).getChildNodesCounter());
		assertEquals(46, rootNode.getChildNode(Square.d5, Square.e6).getChildNodesCounter());

		assertEquals(44, rootNode.getChildNode(Square.a2, Square.a3).getChildNodesCounter());
		assertEquals(44, rootNode.getChildNode(Square.a2, Square.a4).getChildNodesCounter());
		
		assertEquals(42, rootNode.getChildNode(Square.b2, Square.b3).getChildNodesCounter());
		
		assertEquals(42, rootNode.getChildNode(Square.g2, Square.g3).getChildNodesCounter());
		assertEquals(42, rootNode.getChildNode(Square.g2, Square.g4).getChildNodesCounter());
		assertEquals(43, rootNode.getChildNode(Square.g2, Square.h3).getChildNodesCounter());
		
		
		assertEquals(43, rootNode.getChildNode(Square.e5, Square.d3).getChildNodesCounter());
		assertEquals(44, rootNode.getChildNode(Square.e5, Square.f7).getChildNodesCounter());
		assertEquals(42, rootNode.getChildNode(Square.e5, Square.c4).getChildNodesCounter());
		assertEquals(42, rootNode.getChildNode(Square.e5, Square.g6).getChildNodesCounter());	
		assertEquals(44, rootNode.getChildNode(Square.e5, Square.g4).getChildNodesCounter());
		assertEquals(41, rootNode.getChildNode(Square.e5, Square.c6).getChildNodesCounter());
		assertEquals(45, rootNode.getChildNode(Square.e5, Square.d7).getChildNodesCounter());	
		
		assertEquals(42, rootNode.getChildNode(Square.c3, Square.b1).getChildNodesCounter());
		assertEquals(42, rootNode.getChildNode(Square.c3, Square.a4).getChildNodesCounter());
		assertEquals(42, rootNode.getChildNode(Square.c3, Square.d1).getChildNodesCounter());
		assertEquals(39, rootNode.getChildNode(Square.c3, Square.b5).getChildNodesCounter());
		
		assertEquals(43, rootNode.getChildNode(Square.f3, Square.g3).getChildNodesCounter());
		assertEquals(43, rootNode.getChildNode(Square.f3, Square.h3).getChildNodesCounter());
		assertEquals(43, rootNode.getChildNode(Square.f3, Square.e3).getChildNodesCounter());
		assertEquals(42, rootNode.getChildNode(Square.f3, Square.d3).getChildNodesCounter());	
		assertEquals(43, rootNode.getChildNode(Square.f3, Square.g4).getChildNodesCounter());
		assertEquals(43, rootNode.getChildNode(Square.f3, Square.h5).getChildNodesCounter());
		assertEquals(43, rootNode.getChildNode(Square.f3, Square.f4).getChildNodesCounter());
		assertEquals(45, rootNode.getChildNode(Square.f3, Square.f5).getChildNodesCounter());
		assertEquals(39, rootNode.getChildNode(Square.f3, Square.f6).getChildNodesCounter());	
		
		assertEquals(43, rootNode.getChildNode(Square.d2, Square.c1).getChildNodesCounter());
		assertEquals(43, rootNode.getChildNode(Square.d2, Square.e3).getChildNodesCounter());
		assertEquals(43, rootNode.getChildNode(Square.d2, Square.f4).getChildNodesCounter());
		assertEquals(42, rootNode.getChildNode(Square.d2, Square.g5).getChildNodesCounter());	
		assertEquals(41, rootNode.getChildNode(Square.d2, Square.h6).getChildNodesCounter());		
		
		assertEquals(44, rootNode.getChildNode(Square.e2, Square.d1).getChildNodesCounter());
		assertEquals(44, rootNode.getChildNode(Square.e2, Square.f1).getChildNodesCounter());
		assertEquals(42, rootNode.getChildNode(Square.e2, Square.d3).getChildNodesCounter());
		assertEquals(41, rootNode.getChildNode(Square.e2, Square.c4).getChildNodesCounter());	
		assertEquals(39, rootNode.getChildNode(Square.e2, Square.b5).getChildNodesCounter());
		assertEquals(36, rootNode.getChildNode(Square.e2, Square.a6).getChildNodesCounter());
		
		assertEquals(43, rootNode.getChildNode(Square.a1, Square.b1).getChildNodesCounter());
		assertEquals(43, rootNode.getChildNode(Square.a1, Square.c1).getChildNodesCounter());
		assertEquals(43, rootNode.getChildNode(Square.a1, Square.d1).getChildNodesCounter());
		
		assertEquals(43, rootNode.getChildNode(Square.h1, Square.g1).getChildNodesCounter());
		assertEquals(43, rootNode.getChildNode(Square.h1, Square.f1).getChildNodesCounter());		
	}
	
	@Test
	public void test03() {
		Board board = FENParser.parseFEN("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq -");
		Node rootNode = main.start(board, 3);
		
		assertEquals(48, board.getMovimientosPosibles().size());
		assertEquals(97862, rootNode.getChildNodesCounter());
	}	
}
