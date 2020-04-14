package main;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import chess.Game;
import chess.Square;
import parsers.FENBoarBuilder;

//Kiwipete
public class ChessMainTest01 {

	private ChessMain main;
	
	private Game board;
	
	@Before
	public void setUp() throws Exception {
		main = new ChessMain();
		board = new FENBoarBuilder().withFEN("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq -").buildBoard();
	}

	@Test
	public void test_divide1() {		
		Node rootNode = main.start(board, 1);
		
		assertEquals(1, rootNode.getChildNode(Square.e1, Square.g1).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.e1, Square.c1).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.e1, Square.f1).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.e1, Square.d1).getChildNodesCounter());
		
		assertEquals(1, rootNode.getChildNode(Square.d5, Square.d6).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.d5, Square.e6).getChildNodesCounter());

		assertEquals(1, rootNode.getChildNode(Square.a2, Square.a3).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.a2, Square.a4).getChildNodesCounter());
		
		assertEquals(1, rootNode.getChildNode(Square.b2, Square.b3).getChildNodesCounter());
		
		assertEquals(1, rootNode.getChildNode(Square.g2, Square.g3).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.g2, Square.g4).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.g2, Square.h3).getChildNodesCounter());
		
		assertEquals(1, rootNode.getChildNode(Square.e5, Square.d3).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.e5, Square.f7).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.e5, Square.c4).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.e5, Square.g6).getChildNodesCounter());	
		assertEquals(1, rootNode.getChildNode(Square.e5, Square.g4).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.e5, Square.c6).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.e5, Square.d7).getChildNodesCounter());	
		
		assertEquals(1, rootNode.getChildNode(Square.c3, Square.b1).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.c3, Square.a4).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.c3, Square.d1).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.c3, Square.b5).getChildNodesCounter());
		
		assertEquals(1, rootNode.getChildNode(Square.f3, Square.g3).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.f3, Square.h3).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.f3, Square.e3).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.f3, Square.d3).getChildNodesCounter());	
		assertEquals(1, rootNode.getChildNode(Square.f3, Square.g4).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.f3, Square.h5).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.f3, Square.f4).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.f3, Square.f5).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.f3, Square.f6).getChildNodesCounter());	
		
		assertEquals(1, rootNode.getChildNode(Square.d2, Square.c1).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.d2, Square.e3).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.d2, Square.f4).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.d2, Square.g5).getChildNodesCounter());	
		assertEquals(1, rootNode.getChildNode(Square.d2, Square.h6).getChildNodesCounter());		
		
		assertEquals(1, rootNode.getChildNode(Square.e2, Square.d1).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.e2, Square.f1).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.e2, Square.d3).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.e2, Square.c4).getChildNodesCounter());	
		assertEquals(1, rootNode.getChildNode(Square.e2, Square.b5).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.e2, Square.a6).getChildNodesCounter());
		
		assertEquals(1, rootNode.getChildNode(Square.a1, Square.b1).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.a1, Square.c1).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.a1, Square.d1).getChildNodesCounter());
		
		assertEquals(1, rootNode.getChildNode(Square.h1, Square.g1).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.h1, Square.f1).getChildNodesCounter());			
		
		assertEquals(48, board.getMovimientosPosibles().size());
		assertEquals(48, rootNode.getChildNodesCounter());
	}
	
	@Test
	public void test_divide2() {
		Node rootNode = main.start(board, 2);
		
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
		
		assertEquals(48, board.getMovimientosPosibles().size());
		assertEquals(2039, rootNode.getChildNodesCounter());
	}
	
	@Test
	public void test_divide3() {
		Node rootNode = main.start(board, 3);
		
		assertEquals(2059, rootNode.getChildNode(Square.e1, Square.g1).getChildNodesCounter());
		assertEquals(1887, rootNode.getChildNode(Square.e1, Square.c1 ).getChildNodesCounter());
		assertEquals(1855, rootNode.getChildNode(Square.e1, Square.f1 ).getChildNodesCounter());
		assertEquals(1894, rootNode.getChildNode(Square.e1, Square.d1 ).getChildNodesCounter());
		assertEquals(1991, rootNode.getChildNode(Square.d5, Square.d6 ).getChildNodesCounter());
		assertEquals(2241, rootNode.getChildNode(Square.d5, Square.e6 ).getChildNodesCounter());
		assertEquals(2186, rootNode.getChildNode(Square.a2, Square.a3 ).getChildNodesCounter());
		assertEquals(2149, rootNode.getChildNode(Square.a2, Square.a4 ).getChildNodesCounter());
		assertEquals(1964, rootNode.getChildNode(Square.b2, Square.b3 ).getChildNodesCounter());
		assertEquals(1882, rootNode.getChildNode(Square.g2, Square.g3 ).getChildNodesCounter());
		assertEquals(1843, rootNode.getChildNode(Square.g2, Square.g4 ).getChildNodesCounter());
		assertEquals(1970, rootNode.getChildNode(Square.g2, Square.h3 ).getChildNodesCounter());
		assertEquals(1803, rootNode.getChildNode(Square.e5, Square.d3 ).getChildNodesCounter());
		assertEquals(2080, rootNode.getChildNode(Square.e5, Square.f7 ).getChildNodesCounter());
		assertEquals(1880, rootNode.getChildNode(Square.e5, Square.c4 ).getChildNodesCounter());
		assertEquals(1997, rootNode.getChildNode(Square.e5, Square.g6 ).getChildNodesCounter());
		assertEquals(1878, rootNode.getChildNode(Square.e5, Square.g4 ).getChildNodesCounter());
		assertEquals(2027, rootNode.getChildNode(Square.e5, Square.c6 ).getChildNodesCounter());
		assertEquals(2124, rootNode.getChildNode(Square.e5, Square.d7 ).getChildNodesCounter());
		assertEquals(2038, rootNode.getChildNode(Square.c3, Square.b1 ).getChildNodesCounter());
		assertEquals(2203, rootNode.getChildNode(Square.c3, Square.a4 ).getChildNodesCounter());
		assertEquals(2040, rootNode.getChildNode(Square.c3, Square.d1 ).getChildNodesCounter());
		assertEquals(2138, rootNode.getChildNode(Square.c3, Square.b5 ).getChildNodesCounter());
		assertEquals(2214, rootNode.getChildNode(Square.f3, Square.g3 ).getChildNodesCounter());
		assertEquals(2360, rootNode.getChildNode(Square.f3, Square.h3 ).getChildNodesCounter());
		assertEquals(2174, rootNode.getChildNode(Square.f3, Square.e3 ).getChildNodesCounter());
		assertEquals(2005, rootNode.getChildNode(Square.f3, Square.d3 ).getChildNodesCounter());
		assertEquals(2169, rootNode.getChildNode(Square.f3, Square.g4 ).getChildNodesCounter());
		assertEquals(2267, rootNode.getChildNode(Square.f3, Square.h5 ).getChildNodesCounter());
		assertEquals(2132, rootNode.getChildNode(Square.f3, Square.f4 ).getChildNodesCounter());
		assertEquals(2396, rootNode.getChildNode(Square.f3, Square.f5 ).getChildNodesCounter());
		assertEquals(2111, rootNode.getChildNode(Square.f3, Square.f6 ).getChildNodesCounter());
		assertEquals(1963, rootNode.getChildNode(Square.d2, Square.c1 ).getChildNodesCounter());
		assertEquals(2136, rootNode.getChildNode(Square.d2, Square.e3 ).getChildNodesCounter());
		assertEquals(2000, rootNode.getChildNode(Square.d2, Square.f4 ).getChildNodesCounter());
		assertEquals(2134, rootNode.getChildNode(Square.d2, Square.g5 ).getChildNodesCounter());
		assertEquals(2019, rootNode.getChildNode(Square.d2, Square.h6 ).getChildNodesCounter());
		assertEquals(1733, rootNode.getChildNode(Square.e2, Square.d1 ).getChildNodesCounter());
		assertEquals(2060, rootNode.getChildNode(Square.e2, Square.f1 ).getChildNodesCounter());
		assertEquals(2050, rootNode.getChildNode(Square.e2, Square.d3 ).getChildNodesCounter());
		assertEquals(2082, rootNode.getChildNode(Square.e2, Square.c4 ).getChildNodesCounter());
		assertEquals(2057, rootNode.getChildNode(Square.e2, Square.b5 ).getChildNodesCounter());
		assertEquals(1907, rootNode.getChildNode(Square.e2, Square.a6 ).getChildNodesCounter());
		assertEquals(1969, rootNode.getChildNode(Square.a1, Square.b1 ).getChildNodesCounter());
		assertEquals(1968, rootNode.getChildNode(Square.a1, Square.c1 ).getChildNodesCounter());
		assertEquals(1885, rootNode.getChildNode(Square.a1, Square.d1 ).getChildNodesCounter());
		assertEquals(2013, rootNode.getChildNode(Square.h1, Square.g1 ).getChildNodesCounter());
		assertEquals(1929, rootNode.getChildNode(Square.h1, Square.f1 ).getChildNodesCounter());
		
		assertEquals(48, board.getMovimientosPosibles().size());
		assertEquals(97862, rootNode.getChildNodesCounter());		
	}
	
	
	@Test
	public void test_d5d6() {
		board.executeMove(Square.d5, Square.d6);
		
		Node rootNode = main.start(board, 2);
		
		assertEquals(49, rootNode.getChildNode(Square.e8, Square.g8 ).getChildNodesCounter());
		assertEquals(49, rootNode.getChildNode(Square.e8, Square.c8 ).getChildNodesCounter());
		assertEquals(49, rootNode.getChildNode(Square.e8, Square.f8 ).getChildNodesCounter());
		assertEquals(49, rootNode.getChildNode(Square.e8, Square.d8 ).getChildNodesCounter());
		assertEquals(48, rootNode.getChildNode(Square.c7, Square.c6 ).getChildNodesCounter());
		assertEquals(48, rootNode.getChildNode(Square.c7, Square.c5 ).getChildNodesCounter());
		assertEquals(47, rootNode.getChildNode(Square.c7, Square.d6 ).getChildNodesCounter());
		assertEquals(48, rootNode.getChildNode(Square.g6, Square.g5 ).getChildNodesCounter());
		assertEquals(50, rootNode.getChildNode(Square.b4, Square.b3 ).getChildNodesCounter());
		assertEquals(48, rootNode.getChildNode(Square.b4, Square.c3 ).getChildNodesCounter());
		assertEquals(47, rootNode.getChildNode(Square.h3, Square.g2 ).getChildNodesCounter());
		assertEquals(49, rootNode.getChildNode(Square.a8, Square.b8 ).getChildNodesCounter());
		assertEquals(49, rootNode.getChildNode(Square.a8, Square.c8 ).getChildNodesCounter());
		assertEquals(49, rootNode.getChildNode(Square.a8, Square.d8 ).getChildNodesCounter());
		assertEquals(49, rootNode.getChildNode(Square.h8, Square.g8 ).getChildNodesCounter());
		assertEquals(49, rootNode.getChildNode(Square.h8, Square.f8 ).getChildNodesCounter());
		assertEquals(49, rootNode.getChildNode(Square.h8, Square.h7 ).getChildNodesCounter());
		assertEquals(49, rootNode.getChildNode(Square.h8, Square.h6 ).getChildNodesCounter());
		assertEquals(49, rootNode.getChildNode(Square.h8, Square.h5 ).getChildNodesCounter());
		assertEquals(49, rootNode.getChildNode(Square.h8, Square.h4 ).getChildNodesCounter());
		assertEquals(47, rootNode.getChildNode(Square.e7, Square.d6 ).getChildNodesCounter());
		assertEquals(48, rootNode.getChildNode(Square.e7, Square.f8 ).getChildNodesCounter());
		assertEquals(48, rootNode.getChildNode(Square.e7, Square.d8 ).getChildNodesCounter());
		assertEquals(49, rootNode.getChildNode(Square.g7, Square.h6 ).getChildNodesCounter());
		assertEquals(49, rootNode.getChildNode(Square.g7, Square.f8 ).getChildNodesCounter());
		assertEquals(49, rootNode.getChildNode(Square.a6, Square.b7 ).getChildNodesCounter());
		assertEquals(49, rootNode.getChildNode(Square.a6, Square.c8 ).getChildNodesCounter());
		assertEquals(48, rootNode.getChildNode(Square.a6, Square.b5 ).getChildNodesCounter());
		assertEquals(47, rootNode.getChildNode(Square.a6, Square.c4 ).getChildNodesCounter());
		assertEquals(47, rootNode.getChildNode(Square.a6, Square.d3 ).getChildNodesCounter());
		assertEquals(42, rootNode.getChildNode(Square.a6, Square.e2 ).getChildNodesCounter());
		assertEquals(48, rootNode.getChildNode(Square.b6, Square.a4 ).getChildNodesCounter());
		assertEquals(49, rootNode.getChildNode(Square.b6, Square.c8 ).getChildNodesCounter());
		assertEquals(50, rootNode.getChildNode(Square.b6, Square.d5 ).getChildNodesCounter());
		assertEquals(47, rootNode.getChildNode(Square.b6, Square.c4 ).getChildNodesCounter());
		assertEquals(52, rootNode.getChildNode(Square.f6, Square.e4 ).getChildNodesCounter());
		assertEquals(50, rootNode.getChildNode(Square.f6, Square.g8 ).getChildNodesCounter());
		assertEquals(51, rootNode.getChildNode(Square.f6, Square.d5 ).getChildNodesCounter());
		assertEquals(50, rootNode.getChildNode(Square.f6, Square.h7 ).getChildNodesCounter());
		assertEquals(50, rootNode.getChildNode(Square.f6, Square.h5 ).getChildNodesCounter());
		assertEquals(48, rootNode.getChildNode(Square.f6, Square.g4 ).getChildNodesCounter());
		
		assertEquals(41, board.getMovimientosPosibles().size());
		assertEquals(1991, rootNode.getChildNodesCounter());		
	}
	
	@Test
	public void test_d5d6_h3g2() {
		board.executeMove(Square.d5, Square.d6);
		board.executeMove(Square.h3, Square.g2);
		
		Node rootNode = main.start(board, 1);
		
		assertEquals(1, rootNode.getChildNode(Square.e1, Square.c1 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.e1, Square.d1 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.d6, Square.e7 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.d6, Square.c7 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.a2, Square.a3 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.a2, Square.a4 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.b2, Square.b3 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.h2, Square.h3 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.h2, Square.h4 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.e5, Square.d3 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.e5, Square.f7 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.e5, Square.c4 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.e5, Square.g6 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.e5, Square.g4 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.e5, Square.c6 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.e5, Square.d7 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.c3, Square.b1 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.c3, Square.d5 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.c3, Square.a4 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.c3, Square.d1 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.c3, Square.b5 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.f3, Square.g3 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.f3, Square.h3 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.f3, Square.e3 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.f3, Square.d3 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.f3, Square.g2 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.f3, Square.g4 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.f3, Square.h5 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.f3, Square.f4 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.f3, Square.f5 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.f3, Square.f6 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.d2, Square.c1 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.d2, Square.e3 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.d2, Square.f4 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.d2, Square.g5 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.d2, Square.h6 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.e2, Square.d1 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.e2, Square.f1 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.e2, Square.d3 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.e2, Square.c4 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.e2, Square.b5 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.e2, Square.a6 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.a1, Square.b1 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.a1, Square.c1 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.a1, Square.d1 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.h1, Square.g1 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.h1, Square.f1 ).getChildNodesCounter());
		
		assertEquals(47, board.getMovimientosPosibles().size());
		assertEquals(47, rootNode.getChildNodesCounter());		
	}
	
	@Test
	public void test_c3b2() {
		board.executeMove(Square.c3, Square.b1);
		
		Node rootNode = main.start(board, 2);
		
		assertEquals(49, rootNode.getChildNode(Square.e8, Square.g8 ).getChildNodesCounter());
		assertEquals(49, rootNode.getChildNode(Square.e8, Square.c8 ).getChildNodesCounter());
		assertEquals(49, rootNode.getChildNode(Square.e8, Square.f8 ).getChildNodesCounter());
		assertEquals(49, rootNode.getChildNode(Square.e8, Square.d8 ).getChildNodesCounter());
		assertEquals(50, rootNode.getChildNode(Square.c7, Square.c6 ).getChildNodesCounter());
		assertEquals(50, rootNode.getChildNode(Square.c7, Square.c5 ).getChildNodesCounter());
		assertEquals(48, rootNode.getChildNode(Square.d7, Square.d6 ).getChildNodesCounter());
		assertEquals(48, rootNode.getChildNode(Square.e6, Square.d5 ).getChildNodesCounter());
		assertEquals(48, rootNode.getChildNode(Square.g6, Square.g5 ).getChildNodesCounter());
		assertEquals(50, rootNode.getChildNode(Square.b4, Square.b3 ).getChildNodesCounter());
		assertEquals(47, rootNode.getChildNode(Square.h3, Square.g2 ).getChildNodesCounter());
		assertEquals(49, rootNode.getChildNode(Square.a8, Square.b8 ).getChildNodesCounter());
		assertEquals(49, rootNode.getChildNode(Square.a8, Square.c8 ).getChildNodesCounter());
		assertEquals(49, rootNode.getChildNode(Square.a8, Square.d8 ).getChildNodesCounter());
		assertEquals(49, rootNode.getChildNode(Square.h8, Square.g8 ).getChildNodesCounter());
		assertEquals(49, rootNode.getChildNode(Square.h8, Square.f8 ).getChildNodesCounter());
		assertEquals(49, rootNode.getChildNode(Square.h8, Square.h7 ).getChildNodesCounter());
		assertEquals(49, rootNode.getChildNode(Square.h8, Square.h6 ).getChildNodesCounter());
		assertEquals(49, rootNode.getChildNode(Square.h8, Square.h5 ).getChildNodesCounter());
		assertEquals(49, rootNode.getChildNode(Square.h8, Square.h4 ).getChildNodesCounter());
		assertEquals(48, rootNode.getChildNode(Square.e7, Square.d6 ).getChildNodesCounter());
		assertEquals(49, rootNode.getChildNode(Square.e7, Square.c5 ).getChildNodesCounter());
		assertEquals(49, rootNode.getChildNode(Square.e7, Square.f8 ).getChildNodesCounter());
		assertEquals(49, rootNode.getChildNode(Square.e7, Square.d8 ).getChildNodesCounter());
		assertEquals(49, rootNode.getChildNode(Square.g7, Square.h6 ).getChildNodesCounter());
		assertEquals(49, rootNode.getChildNode(Square.g7, Square.f8 ).getChildNodesCounter());
		assertEquals(49, rootNode.getChildNode(Square.a6, Square.b7 ).getChildNodesCounter());
		assertEquals(49, rootNode.getChildNode(Square.a6, Square.c8 ).getChildNodesCounter());
		assertEquals(48, rootNode.getChildNode(Square.a6, Square.b5 ).getChildNodesCounter());
		assertEquals(46, rootNode.getChildNode(Square.a6, Square.c4 ).getChildNodesCounter());
		assertEquals(44, rootNode.getChildNode(Square.a6, Square.d3 ).getChildNodesCounter());
		assertEquals(42, rootNode.getChildNode(Square.a6, Square.e2 ).getChildNodesCounter());
		assertEquals(48, rootNode.getChildNode(Square.b6, Square.a4 ).getChildNodesCounter());
		assertEquals(49, rootNode.getChildNode(Square.b6, Square.c8 ).getChildNodesCounter());
		assertEquals(48, rootNode.getChildNode(Square.b6, Square.d5 ).getChildNodesCounter());
		assertEquals(46, rootNode.getChildNode(Square.b6, Square.c4 ).getChildNodesCounter());
		assertEquals(51, rootNode.getChildNode(Square.f6, Square.e4 ).getChildNodesCounter());
		assertEquals(50, rootNode.getChildNode(Square.f6, Square.g8 ).getChildNodesCounter());
		assertEquals(49, rootNode.getChildNode(Square.f6, Square.d5 ).getChildNodesCounter());
		assertEquals(50, rootNode.getChildNode(Square.f6, Square.h7 ).getChildNodesCounter());
		assertEquals(50, rootNode.getChildNode(Square.f6, Square.h5 ).getChildNodesCounter());
		assertEquals(48, rootNode.getChildNode(Square.f6, Square.g4 ).getChildNodesCounter());
		
		assertEquals(42, board.getMovimientosPosibles().size());
		assertEquals(2038, rootNode.getChildNodesCounter());		
	}
	
	@Test
	public void test_c3b2_e8g8() {
		board.executeMove(Square.c3, Square.b1);
		board.executeMove(Square.e8, Square.g8);
		
		Node rootNode = main.start(board, 1);
		
		assertEquals(1, rootNode.getChildNode(Square.e1, Square.g1 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.e1 , Square.f1 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.e1 , Square.d1 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.d5 , Square.d6 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.d5 , Square.e6 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.a2 , Square.a3 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.a2 , Square.a4 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.b2 , Square.b3 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.c2 , Square.c3 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.c2 , Square.c4 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.g2 , Square.g3 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.g2 , Square.g4 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.g2 , Square.h3 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.e5 , Square.d3 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.e5 , Square.f7 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.e5 , Square.c4 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.e5 , Square.g6 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.e5 , Square.g4 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.e5 , Square.c6 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.e5 , Square.d7 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.b1 , Square.c3 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.b1 , Square.a3 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.f3 , Square.g3 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.f3 , Square.h3 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.f3 , Square.e3 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.f3 , Square.d3 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.f3 , Square.c3 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.f3 , Square.b3 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.f3 , Square.a3 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.f3 , Square.g4 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.f3 , Square.h5 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.f3 , Square.f4 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.f3 , Square.f5 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.f3 , Square.f6 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.d2 , Square.c1 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.d2 , Square.e3 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.d2 , Square.f4 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.d2 , Square.g5 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.d2 , Square.h6 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.d2 , Square.c3 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.d2 , Square.b4 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.e2 , Square.d1 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.e2 , Square.f1 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.e2 , Square.d3 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.e2 , Square.c4 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.e2 , Square.b5 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.e2 , Square.a6 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.h1 , Square.g1 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.h1 , Square.f1 ).getChildNodesCounter());
		assertNull(rootNode.getChildNode(Square.e1 , Square.c1 ));
		
		assertEquals(49, board.getMovimientosPosibles().size());
		assertEquals(49, rootNode.getChildNodesCounter());		
	}
	
	//@Test //Este TEST esta fallando
	public void test_divide4() {
		Node rootNode = main.start(board, 4);	
		
		assertEquals(48, board.getMovimientosPosibles().size());
		assertEquals(4085603, rootNode.getChildNodesCounter());		
	}		
	
}
