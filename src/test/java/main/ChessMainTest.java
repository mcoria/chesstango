package main;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import chess.Board;
import chess.Square;
import parsers.FENParser;

public class ChessMainTest {

	private ChessMain main;
	
	@Before
	public void setUp() throws Exception {
		main = new ChessMain();
	}

	@Test
	public void test01() {
		Board board = FENParser.parseFEN(FENParser.INITIAL_FEN);
		Node rootNode = main.start(board, 1);
		
		assertEquals(20, board.getMovimientosPosibles().size());
		assertEquals(20, rootNode.getChildNodesCounter());
	}
	
	@Test
	public void test02() {
		Board board = FENParser.parseFEN(FENParser.INITIAL_FEN);
		
		Node rootNode = main.start(board, 2);
		
		assertEquals(20, board.getMovimientosPosibles().size());
		assertEquals(400, rootNode.getChildNodesCounter());
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
				
	} 
	
	@Test
	public void test03() {
		Board board = FENParser.parseFEN(FENParser.INITIAL_FEN);
		
		Node rootNode = main.start(board, 3);
		
		assertEquals(20, board.getMovimientosPosibles().size());
		assertEquals(8902, rootNode.getChildNodesCounter());
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
				
	} 	
	
	
	@Test
	public void test04() {
		Board board = FENParser.parseFEN(FENParser.INITIAL_FEN);
		
		Node rootNode = main.start(board, 4);
		
		assertEquals(20, board.getMovimientosPosibles().size());
		assertEquals(197281, rootNode.getChildNodesCounter());
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
				
	} 	

}
