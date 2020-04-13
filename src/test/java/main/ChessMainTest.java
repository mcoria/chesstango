package main;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import chess.Board;
import chess.Square;
import parsers.FENBoarBuilder;

public class ChessMainTest {

	private ChessMain main;
	private FENBoarBuilder builder;
	
	@Before
	public void setUp() throws Exception {
		main = new ChessMain();
		builder = new FENBoarBuilder();
	}

	@Test
	public void test_divide1() {
		Board board = builder.withDefaultBoard().buildBoard();
		
		Node rootNode = main.start(board, 1);
		
		assertEquals(1, rootNode.getChildNode(Square.a2, Square.a3).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.a2, Square.a4).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.b2, Square.b3).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.b2, Square.b4).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.c2, Square.c3).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.c2, Square.c4).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.d2, Square.d3).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.d2, Square.d4).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.e2, Square.e3).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.e2, Square.e4).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.f2, Square.f3).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.f2, Square.f4).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.g2, Square.g3).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.g2, Square.g4).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.h2, Square.h3).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.h2, Square.h4).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.b1, Square.a3).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.b1, Square.c3).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.g1, Square.f3).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.g1, Square.h3).getChildNodesCounter());		
		
		assertEquals(20, board.getMovimientosPosibles().size());
		assertEquals(20, rootNode.getChildNodesCounter());
	}
	
	@Test
	public void test_divide2() {
		Board board = builder.withDefaultBoard().buildBoard();
		
		Node rootNode = main.start(board, 2);
		
		assertEquals(20, rootNode.getChildNode(Square.a2, Square.a3).getChildNodesCounter());
		assertEquals(20, rootNode.getChildNode(Square.a2, Square.a4).getChildNodesCounter());
		assertEquals(20, rootNode.getChildNode(Square.b2, Square.b3).getChildNodesCounter());
		assertEquals(20, rootNode.getChildNode(Square.b2, Square.b4).getChildNodesCounter());
		assertEquals(20, rootNode.getChildNode(Square.c2, Square.c3).getChildNodesCounter());
		assertEquals(20, rootNode.getChildNode(Square.c2, Square.c4).getChildNodesCounter());
		assertEquals(20, rootNode.getChildNode(Square.d2, Square.d3).getChildNodesCounter());
		assertEquals(20, rootNode.getChildNode(Square.d2, Square.d4).getChildNodesCounter());
		assertEquals(20, rootNode.getChildNode(Square.e2, Square.e3).getChildNodesCounter());
		assertEquals(20, rootNode.getChildNode(Square.e2, Square.e4).getChildNodesCounter());
		assertEquals(20, rootNode.getChildNode(Square.f2, Square.f3).getChildNodesCounter());
		assertEquals(20, rootNode.getChildNode(Square.f2, Square.f4).getChildNodesCounter());
		assertEquals(20, rootNode.getChildNode(Square.g2, Square.g3).getChildNodesCounter());
		assertEquals(20, rootNode.getChildNode(Square.g2, Square.g4).getChildNodesCounter());
		assertEquals(20, rootNode.getChildNode(Square.h2, Square.h3).getChildNodesCounter());
		assertEquals(20, rootNode.getChildNode(Square.h2, Square.h4).getChildNodesCounter());
		assertEquals(20, rootNode.getChildNode(Square.b1, Square.a3).getChildNodesCounter());
		assertEquals(20, rootNode.getChildNode(Square.b1, Square.c3).getChildNodesCounter());
		assertEquals(20, rootNode.getChildNode(Square.g1, Square.f3).getChildNodesCounter());
		assertEquals(20, rootNode.getChildNode(Square.g1, Square.h3).getChildNodesCounter());
		
		assertEquals(20, board.getMovimientosPosibles().size());
		assertEquals(400, rootNode.getChildNodesCounter());
				
	} 
	
	@Test
	public void test_divide3() {
		Board board = builder.withDefaultBoard().buildBoard();
		
		Node rootNode = main.start(board, 3);
		
		assertEquals(380, rootNode.getChildNode(Square.a2, Square.a3).getChildNodesCounter());
		assertEquals(420, rootNode.getChildNode(Square.a2, Square.a4).getChildNodesCounter());
		assertEquals(420, rootNode.getChildNode(Square.b2, Square.b3).getChildNodesCounter());
		assertEquals(421, rootNode.getChildNode(Square.b2, Square.b4).getChildNodesCounter());
		assertEquals(420, rootNode.getChildNode(Square.c2, Square.c3).getChildNodesCounter());
		assertEquals(441, rootNode.getChildNode(Square.c2, Square.c4).getChildNodesCounter());
		assertEquals(539, rootNode.getChildNode(Square.d2, Square.d3).getChildNodesCounter());
		assertEquals(560, rootNode.getChildNode(Square.d2, Square.d4).getChildNodesCounter());
		assertEquals(599, rootNode.getChildNode(Square.e2, Square.e3).getChildNodesCounter());
		assertEquals(600, rootNode.getChildNode(Square.e2, Square.e4).getChildNodesCounter());
		assertEquals(380, rootNode.getChildNode(Square.f2, Square.f3).getChildNodesCounter());
		assertEquals(401, rootNode.getChildNode(Square.f2, Square.f4).getChildNodesCounter());
		assertEquals(420, rootNode.getChildNode(Square.g2, Square.g3).getChildNodesCounter());
		assertEquals(421, rootNode.getChildNode(Square.g2, Square.g4).getChildNodesCounter());
		assertEquals(380, rootNode.getChildNode(Square.h2, Square.h3).getChildNodesCounter());
		assertEquals(420, rootNode.getChildNode(Square.h2, Square.h4).getChildNodesCounter());
		assertEquals(400, rootNode.getChildNode(Square.b1, Square.a3).getChildNodesCounter());
		assertEquals(440, rootNode.getChildNode(Square.b1, Square.c3).getChildNodesCounter());
		assertEquals(440, rootNode.getChildNode(Square.g1, Square.f3).getChildNodesCounter());
		assertEquals(400, rootNode.getChildNode(Square.g1, Square.h3).getChildNodesCounter());

		assertEquals(20, board.getMovimientosPosibles().size());
		assertEquals(8902, rootNode.getChildNodesCounter());
	} 	
	
	
	@Test
	public void test_divide4() {
		Board board = builder.withDefaultBoard().buildBoard();
		
		Node rootNode = main.start(board, 4);

		assertEquals(8457, rootNode.getChildNode(Square.a2, Square.a3).getChildNodesCounter());
		assertEquals(9329, rootNode.getChildNode(Square.a2, Square.a4).getChildNodesCounter());
		assertEquals(9345, rootNode.getChildNode(Square.b2, Square.b3).getChildNodesCounter());
		assertEquals(9332, rootNode.getChildNode(Square.b2, Square.b4).getChildNodesCounter());
		assertEquals(9272, rootNode.getChildNode(Square.c2, Square.c3).getChildNodesCounter());
		assertEquals(9744, rootNode.getChildNode(Square.c2, Square.c4).getChildNodesCounter());
		assertEquals(11959, rootNode.getChildNode(Square.d2, Square.d3).getChildNodesCounter());
		assertEquals(12435, rootNode.getChildNode(Square.d2, Square.d4).getChildNodesCounter());
		assertEquals(13134, rootNode.getChildNode(Square.e2, Square.e3).getChildNodesCounter());
		assertEquals(13160, rootNode.getChildNode(Square.e2, Square.e4).getChildNodesCounter());
		assertEquals(8457, rootNode.getChildNode(Square.f2, Square.f3).getChildNodesCounter());
		assertEquals(8929, rootNode.getChildNode(Square.f2, Square.f4).getChildNodesCounter());
		assertEquals(9345, rootNode.getChildNode(Square.g2, Square.g3).getChildNodesCounter());
		assertEquals(9328, rootNode.getChildNode(Square.g2, Square.g4).getChildNodesCounter());
		assertEquals(8457, rootNode.getChildNode(Square.h2, Square.h3).getChildNodesCounter());
		assertEquals(9329, rootNode.getChildNode(Square.h2, Square.h4).getChildNodesCounter());
		assertEquals(8885, rootNode.getChildNode(Square.b1, Square.a3).getChildNodesCounter());
		assertEquals(9755, rootNode.getChildNode(Square.b1, Square.c3).getChildNodesCounter());
		assertEquals(9748, rootNode.getChildNode(Square.g1, Square.f3).getChildNodesCounter());
		assertEquals(8881, rootNode.getChildNode(Square.g1, Square.h3).getChildNodesCounter());
		
		assertEquals(20, board.getMovimientosPosibles().size());
		assertEquals(197281, rootNode.getChildNodesCounter());
	} 	
	
	@Test //126segs 104segs 87segs 83segs 80segs 77segs 76segs 70segs 67segs 60segs 58segs 53segs 50segs
	public void test_divide5() {
		Board board = builder.withDefaultBoard().buildBoard();
		
		Node rootNode = main.start(board, 5);
		
		assertEquals(181046, rootNode.getChildNode(Square.a2, Square.a3).getChildNodesCounter());
		assertEquals(217832, rootNode.getChildNode(Square.a2, Square.a4).getChildNodesCounter());
		assertEquals(215255, rootNode.getChildNode(Square.b2, Square.b3).getChildNodesCounter());
		assertEquals(216145, rootNode.getChildNode(Square.b2, Square.b4).getChildNodesCounter());
		assertEquals(222861, rootNode.getChildNode(Square.c2, Square.c3).getChildNodesCounter());
		assertEquals(240082, rootNode.getChildNode(Square.c2, Square.c4).getChildNodesCounter());
		assertEquals(328511, rootNode.getChildNode(Square.d2, Square.d3).getChildNodesCounter());
		assertEquals(361790, rootNode.getChildNode(Square.d2, Square.d4).getChildNodesCounter());
		assertEquals(402988, rootNode.getChildNode(Square.e2, Square.e3).getChildNodesCounter());
		assertEquals(405385, rootNode.getChildNode(Square.e2, Square.e4).getChildNodesCounter());
		assertEquals(178889, rootNode.getChildNode(Square.f2, Square.f3).getChildNodesCounter());
		assertEquals(198473, rootNode.getChildNode(Square.f2, Square.f4).getChildNodesCounter());
		assertEquals(217210, rootNode.getChildNode(Square.g2, Square.g3).getChildNodesCounter());
		assertEquals(214048, rootNode.getChildNode(Square.g2, Square.g4).getChildNodesCounter());
		assertEquals(181044, rootNode.getChildNode(Square.h2, Square.h3).getChildNodesCounter());
		assertEquals(218829, rootNode.getChildNode(Square.h2, Square.h4).getChildNodesCounter());
		assertEquals(198572, rootNode.getChildNode(Square.b1, Square.a3).getChildNodesCounter());
		assertEquals(234656, rootNode.getChildNode(Square.b1, Square.c3).getChildNodesCounter());
		assertEquals(233491, rootNode.getChildNode(Square.g1, Square.f3).getChildNodesCounter());
		assertEquals(198502, rootNode.getChildNode(Square.g1, Square.h3).getChildNodesCounter());
		
		assertEquals(20, board.getMovimientosPosibles().size());
		assertEquals(4865609, rootNode.getChildNodesCounter());
	} 	
	
	/* NO SE LA BANCA
	@Test
	public void test06() {
		Board board = FENParser.parseFEN(FENParser.INITIAL_FEN);
		
		Node rootNode = main.start(board, 6);
		
		assertEquals(181046, rootNode.getChildNode(Square.a2, Square.a3).getChildNodesCounter());
		assertEquals(217832, rootNode.getChildNode(Square.a2, Square.a4).getChildNodesCounter());
		assertEquals(215255, rootNode.getChildNode(Square.b2, Square.b3).getChildNodesCounter());
		assertEquals(216145, rootNode.getChildNode(Square.b2, Square.b4).getChildNodesCounter());
		assertEquals(222861, rootNode.getChildNode(Square.c2, Square.c3).getChildNodesCounter());
		assertEquals(240082, rootNode.getChildNode(Square.c2, Square.c4).getChildNodesCounter());
		assertEquals(328511, rootNode.getChildNode(Square.d2, Square.d3).getChildNodesCounter());
		assertEquals(361790, rootNode.getChildNode(Square.d2, Square.d4).getChildNodesCounter());
		assertEquals(402988, rootNode.getChildNode(Square.e2, Square.e3).getChildNodesCounter());
		assertEquals(405385, rootNode.getChildNode(Square.e2, Square.e4).getChildNodesCounter());
		assertEquals(178889, rootNode.getChildNode(Square.f2, Square.f3).getChildNodesCounter());
		assertEquals(198473, rootNode.getChildNode(Square.f2, Square.f4).getChildNodesCounter());
		assertEquals(217210, rootNode.getChildNode(Square.g2, Square.g3).getChildNodesCounter());
		assertEquals(214048, rootNode.getChildNode(Square.g2, Square.g4).getChildNodesCounter());
		assertEquals(181044, rootNode.getChildNode(Square.h2, Square.h3).getChildNodesCounter());
		assertEquals(218829, rootNode.getChildNode(Square.h2, Square.h4).getChildNodesCounter());
		assertEquals(198572, rootNode.getChildNode(Square.b1, Square.a3).getChildNodesCounter());
		assertEquals(234656, rootNode.getChildNode(Square.b1, Square.c3).getChildNodesCounter());
		assertEquals(233491, rootNode.getChildNode(Square.g1, Square.f3).getChildNodesCounter());
		assertEquals(198502, rootNode.getChildNode(Square.g1, Square.h3).getChildNodesCounter());
		
		assertEquals(20, board.getMovimientosPosibles().size());
		assertEquals(4865609, rootNode.getChildNodesCounter());		
				
	} */

}
