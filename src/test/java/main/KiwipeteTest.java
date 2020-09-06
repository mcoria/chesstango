package main;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import chess.Game;
import chess.Square;
import parsers.FENBoarBuilder;

//Kiwipete
public class KiwipeteTest {

	private ChessMain main;
	
	private Game board;
	
	@Before
	public void setUp() throws Exception {
		main = new ChessMain();
		board = new FENBoarBuilder().withFEN("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq -").buildGame();
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
	public void test_e2b5_e7f8() {
		board.executeMove(Square.e2, Square.c4);
		board.executeMove(Square.e7, Square.f8);
	}
	
	
	@Test
	public void test_e2b5u() {
		board.executeMove(Square.e2, Square.c4);
		board.undoMove();
		
		board.executeMove(Square.e2, Square.b5);
		
		Node rootNode = main.start(board, 1);
		
		assertEquals(1, rootNode.getChildNode(Square.e8, Square.g8 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.e8, Square.c8 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.e8, Square.f8 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.e8, Square.d8 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.c7, Square.c6 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.c7, Square.c5 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.e6, Square.d5 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.g6, Square.g5 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.b4, Square.b3 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.b4, Square.c3 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.h3, Square.g2 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.a8, Square.b8 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.a8, Square.c8 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.a8, Square.d8 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.h8, Square.g8 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.h8, Square.f8 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.h8, Square.h7 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.h8, Square.h6 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.h8, Square.h5 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.h8, Square.h4 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.e7, Square.d6 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.e7, Square.c5 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.e7, Square.f8 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.e7, Square.d8 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.g7, Square.h6 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.g7, Square.f8 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.a6, Square.b7 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.a6, Square.c8 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.a6, Square.b5 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.b6, Square.a4 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.b6, Square.c8 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.b6, Square.d5 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.b6, Square.c4 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.f6, Square.e4 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.f6, Square.g8 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.f6, Square.d5 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.f6, Square.h7 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.f6, Square.h5 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.f6, Square.g4 ).getChildNodesCounter());
		
		
		assertEquals(39, board.getMovimientosPosibles().size());
		assertEquals(39, rootNode.getChildNodesCounter());
	}
	
	@Test
	public void test_e2a6() {
		board.executeMove(Square.e2, Square.a6);
		
		Node rootNode = main.start(board, 1);
		
		assertEquals(36, board.getMovimientosPosibles().size());
		assertEquals(36, rootNode.getChildNodesCounter());			
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
		assertNull(rootNode.getChildNode(Square.e1 , Square.g1 ));
		
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
	
	
	@Test
	public void test_divide4() {
		Node rootNode = main.start(board, 4);	
		
		assertEquals(86975, rootNode.getChildNode(Square.e1, Square.g1 ).getChildNodesCounter());
		assertEquals(79803, rootNode.getChildNode(Square.e1, Square.c1 ).getChildNodesCounter());
		assertEquals(77887, rootNode.getChildNode(Square.e1, Square.f1 ).getChildNodesCounter());
		assertEquals(79989, rootNode.getChildNode(Square.e1, Square.d1 ).getChildNodesCounter());
		assertEquals(79551, rootNode.getChildNode(Square.d5, Square.d6 ).getChildNodesCounter());
		assertEquals(97464, rootNode.getChildNode(Square.d5, Square.e6 ).getChildNodesCounter());
		assertEquals(94405, rootNode.getChildNode(Square.a2, Square.a3 ).getChildNodesCounter());
		assertEquals(90978, rootNode.getChildNode(Square.a2, Square.a4 ).getChildNodesCounter());
		assertEquals(81066, rootNode.getChildNode(Square.b2, Square.b3 ).getChildNodesCounter());
		assertEquals(77468, rootNode.getChildNode(Square.g2, Square.g3 ).getChildNodesCounter());
		assertEquals(75677, rootNode.getChildNode(Square.g2, Square.g4 ).getChildNodesCounter());
		assertEquals(82759, rootNode.getChildNode(Square.g2, Square.h3 ).getChildNodesCounter());
		assertEquals(77431, rootNode.getChildNode(Square.e5, Square.d3 ).getChildNodesCounter());
		assertEquals(88799, rootNode.getChildNode(Square.e5, Square.f7 ).getChildNodesCounter());
		assertEquals(77752, rootNode.getChildNode(Square.e5, Square.c4 ).getChildNodesCounter());
		assertEquals(83866, rootNode.getChildNode(Square.e5, Square.g6 ).getChildNodesCounter());
		assertEquals(79912, rootNode.getChildNode(Square.e5, Square.g4 ).getChildNodesCounter());
		assertEquals(83885, rootNode.getChildNode(Square.e5, Square.c6 ).getChildNodesCounter());
		assertEquals(93913, rootNode.getChildNode(Square.e5, Square.d7 ).getChildNodesCounter());
		assertEquals(84773, rootNode.getChildNode(Square.c3, Square.b1 ).getChildNodesCounter());
		assertEquals(91447, rootNode.getChildNode(Square.c3, Square.a4 ).getChildNodesCounter());
		assertEquals(84782, rootNode.getChildNode(Square.c3, Square.d1 ).getChildNodesCounter());
		assertEquals(81498, rootNode.getChildNode(Square.c3, Square.b5 ).getChildNodesCounter());
		assertEquals(94461, rootNode.getChildNode(Square.f3, Square.g3 ).getChildNodesCounter());
		assertEquals(98524, rootNode.getChildNode(Square.f3, Square.h3 ).getChildNodesCounter());
		assertEquals(92505, rootNode.getChildNode(Square.f3, Square.e3 ).getChildNodesCounter());
		assertEquals(83727, rootNode.getChildNode(Square.f3, Square.d3 ).getChildNodesCounter());
		assertEquals(92037, rootNode.getChildNode(Square.f3, Square.g4 ).getChildNodesCounter());
		assertEquals(95034, rootNode.getChildNode(Square.f3, Square.h5 ).getChildNodesCounter());
		assertEquals(90488, rootNode.getChildNode(Square.f3, Square.f4 ).getChildNodesCounter());
		assertEquals(104992, rootNode.getChildNode(Square.f3, Square.f5 ).getChildNodesCounter());
		assertEquals(77838, rootNode.getChildNode(Square.f3, Square.f6 ).getChildNodesCounter());
		assertEquals(83037, rootNode.getChildNode(Square.d2, Square.c1 ).getChildNodesCounter());
		assertEquals(90274, rootNode.getChildNode(Square.d2, Square.e3 ).getChildNodesCounter());
		assertEquals(84869, rootNode.getChildNode(Square.d2, Square.f4 ).getChildNodesCounter());
		assertEquals(87951, rootNode.getChildNode(Square.d2, Square.g5 ).getChildNodesCounter());
		assertEquals(82323, rootNode.getChildNode(Square.d2, Square.h6 ).getChildNodesCounter());
		assertEquals(74963, rootNode.getChildNode(Square.e2, Square.d1 ).getChildNodesCounter());
		assertEquals(88728, rootNode.getChildNode(Square.e2, Square.f1 ).getChildNodesCounter());
		assertEquals(85119, rootNode.getChildNode(Square.e2, Square.d3 ).getChildNodesCounter());
		assertEquals(84835, rootNode.getChildNode(Square.e2, Square.c4 ).getChildNodesCounter());
		assertEquals(79739, rootNode.getChildNode(Square.e2, Square.b5 ).getChildNodesCounter());
		assertEquals(69334, rootNode.getChildNode(Square.e2, Square.a6 ).getChildNodesCounter());
		assertEquals(83348, rootNode.getChildNode(Square.a1, Square.b1 ).getChildNodesCounter());
		assertEquals(83263, rootNode.getChildNode(Square.a1, Square.c1 ).getChildNodesCounter());
		assertEquals(79695, rootNode.getChildNode(Square.a1, Square.d1 ).getChildNodesCounter());
		assertEquals(84876, rootNode.getChildNode(Square.h1, Square.g1 ).getChildNodesCounter());
		assertEquals(81563, rootNode.getChildNode(Square.h1, Square.f1 ).getChildNodesCounter());

		
		
		assertEquals(48, board.getMovimientosPosibles().size());
		assertEquals(4085603, rootNode.getChildNodesCounter());		
	}		
	
	@Test
	public void test_e13g1() {
		board.executeMove(Square.e1, Square.g1);
		Node rootNode = main.start(board, 3);
		
		assertEquals(1899, rootNode.getChildNode(Square.e8, Square.g8 ).getChildNodesCounter());
		assertEquals(1962, rootNode.getChildNode(Square.e8, Square.c8 ).getChildNodesCounter());
		assertEquals(1872, rootNode.getChildNode(Square.e8, Square.f8 ).getChildNodesCounter());
		assertEquals(1913, rootNode.getChildNode(Square.e8, Square.d8 ).getChildNodesCounter());
		assertEquals(2080, rootNode.getChildNode(Square.c7, Square.c6 ).getChildNodesCounter());
		assertEquals(1984, rootNode.getChildNode(Square.c7, Square.c5 ).getChildNodesCounter());
		assertEquals(2005, rootNode.getChildNode(Square.d7, Square.d6 ).getChildNodesCounter());
		assertEquals(2086, rootNode.getChildNode(Square.e6, Square.d5 ).getChildNodesCounter());
		assertEquals(1995, rootNode.getChildNode(Square.g6, Square.g5 ).getChildNodesCounter());
		assertEquals(2174, rootNode.getChildNode(Square.b4, Square.b3 ).getChildNodesCounter());
		assertEquals(2123, rootNode.getChildNode(Square.b4, Square.c3 ).getChildNodesCounter());
		assertEquals(2248, rootNode.getChildNode(Square.h3, Square.g2 ).getChildNodesCounter());
		assertEquals(2089, rootNode.getChildNode(Square.a8, Square.b8 ).getChildNodesCounter());
		assertEquals(1946, rootNode.getChildNode(Square.a8, Square.c8 ).getChildNodesCounter());
		assertEquals(1948, rootNode.getChildNode(Square.a8, Square.d8 ).getChildNodesCounter());
		assertEquals(1802, rootNode.getChildNode(Square.h8, Square.g8 ).getChildNodesCounter());
		assertEquals(1708, rootNode.getChildNode(Square.h8, Square.f8 ).getChildNodesCounter());
		assertEquals(1897, rootNode.getChildNode(Square.h8, Square.h7 ).getChildNodesCounter());
		assertEquals(1896, rootNode.getChildNode(Square.h8, Square.h6 ).getChildNodesCounter());
		assertEquals(2040, rootNode.getChildNode(Square.h8, Square.h5 ).getChildNodesCounter());
		assertEquals(2078, rootNode.getChildNode(Square.h8, Square.h4 ).getChildNodesCounter());
		assertEquals(2109, rootNode.getChildNode(Square.e7, Square.d6 ).getChildNodesCounter());
		assertEquals(2412, rootNode.getChildNode(Square.e7, Square.c5 ).getChildNodesCounter());
		assertEquals(1889, rootNode.getChildNode(Square.e7, Square.f8 ).getChildNodesCounter());
		assertEquals(1894, rootNode.getChildNode(Square.e7, Square.d8 ).getChildNodesCounter());
		assertEquals(2072, rootNode.getChildNode(Square.g7, Square.h6 ).getChildNodesCounter());
		assertEquals(1849, rootNode.getChildNode(Square.g7, Square.f8 ).getChildNodesCounter());
		assertEquals(2056, rootNode.getChildNode(Square.a6, Square.b7 ).getChildNodesCounter());
		assertEquals(1770, rootNode.getChildNode(Square.a6, Square.c8 ).getChildNodesCounter());
		assertEquals(2091, rootNode.getChildNode(Square.a6, Square.b5 ).getChildNodesCounter());
		assertEquals(2049, rootNode.getChildNode(Square.a6, Square.c4 ).getChildNodesCounter());
		assertEquals(2038, rootNode.getChildNode(Square.a6, Square.d3 ).getChildNodesCounter());
		assertEquals(2057, rootNode.getChildNode(Square.a6, Square.e2 ).getChildNodesCounter());
		assertEquals(1989, rootNode.getChildNode(Square.b6, Square.a4 ).getChildNodesCounter());
		assertEquals(1753, rootNode.getChildNode(Square.b6, Square.c8 ).getChildNodesCounter());
		assertEquals(1937, rootNode.getChildNode(Square.b6, Square.d5 ).getChildNodesCounter());
		assertEquals(2003, rootNode.getChildNode(Square.b6, Square.c4 ).getChildNodesCounter());
		assertEquals(2566, rootNode.getChildNode(Square.f6, Square.e4 ).getChildNodesCounter());
		assertEquals(2049, rootNode.getChildNode(Square.f6, Square.g8 ).getChildNodesCounter());
		assertEquals(2185, rootNode.getChildNode(Square.f6, Square.d5 ).getChildNodesCounter());
		assertEquals(2048, rootNode.getChildNode(Square.f6, Square.h7 ).getChildNodesCounter());
		assertEquals(2142, rootNode.getChildNode(Square.f6, Square.h5 ).getChildNodesCounter());
		assertEquals(2272, rootNode.getChildNode(Square.f6, Square.g4 ).getChildNodesCounter());
		
		assertEquals(43, board.getMovimientosPosibles().size());
		assertEquals(86975, rootNode.getChildNodesCounter());			
	}
	
	@Test
	public void test_e13g1_h3g2() {
		board.executeMove(Square.e1, Square.g1);
		board.executeMove(Square.h3, Square.g2);
		
		Node rootNode = main.start(board, 2);
		
		assertEquals(44, rootNode.getChildNode(Square.g1, Square.g2).getChildNodesCounter());
		assertEquals(46, rootNode.getChildNode(Square.d5, Square.d6).getChildNodesCounter());
		assertEquals(51, rootNode.getChildNode(Square.d5, Square.e6).getChildNodesCounter());
		assertEquals(49, rootNode.getChildNode(Square.a2, Square.a3 ).getChildNodesCounter());
		assertEquals(49, rootNode.getChildNode(Square.a2, Square.a4 ).getChildNodesCounter());
		assertEquals(47, rootNode.getChildNode(Square.b2, Square.b3 ).getChildNodesCounter());
		assertEquals(47, rootNode.getChildNode(Square.h2, Square.h3 ).getChildNodesCounter());
		assertEquals(46, rootNode.getChildNode(Square.h2, Square.h4 ).getChildNodesCounter());
		assertEquals(48, rootNode.getChildNode(Square.e5, Square.d3 ).getChildNodesCounter());
		assertEquals(49, rootNode.getChildNode(Square.e5, Square.f7 ).getChildNodesCounter());
		assertEquals(47, rootNode.getChildNode(Square.e5, Square.c4 ).getChildNodesCounter());
		assertEquals(47, rootNode.getChildNode(Square.e5, Square.g6 ).getChildNodesCounter());
		assertEquals(49, rootNode.getChildNode(Square.e5, Square.g4 ).getChildNodesCounter());
		assertEquals(46, rootNode.getChildNode(Square.e5, Square.c6 ).getChildNodesCounter());
		assertEquals(50, rootNode.getChildNode(Square.e5, Square.d7 ).getChildNodesCounter());
		assertEquals(47, rootNode.getChildNode(Square.c3, Square.b1 ).getChildNodesCounter());
		assertEquals(47, rootNode.getChildNode(Square.c3, Square.a4 ).getChildNodesCounter());
		assertEquals(47, rootNode.getChildNode(Square.c3, Square.d1 ).getChildNodesCounter());
		assertEquals(44, rootNode.getChildNode(Square.c3, Square.b5 ).getChildNodesCounter());
		assertEquals(48, rootNode.getChildNode(Square.f3, Square.g3 ).getChildNodesCounter());
		assertEquals(47, rootNode.getChildNode(Square.f3, Square.h3 ).getChildNodesCounter());
		assertEquals(48, rootNode.getChildNode(Square.f3, Square.e3 ).getChildNodesCounter());
		assertEquals(47, rootNode.getChildNode(Square.f3, Square.d3 ).getChildNodesCounter());
		assertEquals(44, rootNode.getChildNode(Square.f3, Square.g2 ).getChildNodesCounter());
		assertEquals(48, rootNode.getChildNode(Square.f3, Square.g4 ).getChildNodesCounter());
		assertEquals(46, rootNode.getChildNode(Square.f3, Square.h5 ).getChildNodesCounter());
		assertEquals(48, rootNode.getChildNode(Square.f3, Square.f4 ).getChildNodesCounter());
		assertEquals(50, rootNode.getChildNode(Square.f3, Square.f5 ).getChildNodesCounter());
		assertEquals(44, rootNode.getChildNode(Square.f3, Square.f6 ).getChildNodesCounter());
		assertEquals(48, rootNode.getChildNode(Square.d2, Square.c1 ).getChildNodesCounter());
		assertEquals(48, rootNode.getChildNode(Square.d2, Square.e3 ).getChildNodesCounter());
		assertEquals(48, rootNode.getChildNode(Square.d2, Square.f4 ).getChildNodesCounter());
		assertEquals(47, rootNode.getChildNode(Square.d2, Square.g5 ).getChildNodesCounter());
		assertEquals(44, rootNode.getChildNode(Square.d2, Square.h6 ).getChildNodesCounter());
		assertEquals(48, rootNode.getChildNode(Square.d2, Square.e1 ).getChildNodesCounter());
		assertEquals(49, rootNode.getChildNode(Square.e2, Square.d1 ).getChildNodesCounter());
		assertEquals(47, rootNode.getChildNode(Square.e2, Square.d3 ).getChildNodesCounter());
		assertEquals(46, rootNode.getChildNode(Square.e2, Square.c4 ).getChildNodesCounter());
		assertEquals(44, rootNode.getChildNode(Square.e2, Square.b5 ).getChildNodesCounter());
		assertEquals(41, rootNode.getChildNode(Square.e2, Square.a6 ).getChildNodesCounter());
		assertEquals(48, rootNode.getChildNode(Square.a1, Square.b1 ).getChildNodesCounter());
		assertEquals(48, rootNode.getChildNode(Square.a1, Square.c1 ).getChildNodesCounter());
		assertEquals(48, rootNode.getChildNode(Square.a1, Square.d1 ).getChildNodesCounter());
		assertEquals(48, rootNode.getChildNode(Square.a1, Square.e1 ).getChildNodesCounter());
		assertEquals(44, rootNode.getChildNode(Square.f1, Square.e1 ).getChildNodesCounter());
		assertEquals(44, rootNode.getChildNode(Square.f1, Square.d1 ).getChildNodesCounter());
		assertEquals(44, rootNode.getChildNode(Square.f1, Square.c1 ).getChildNodesCounter());
		assertEquals(44, rootNode.getChildNode(Square.f1, Square.b1 ).getChildNodesCounter());

		
		assertEquals(48, board.getMovimientosPosibles().size());
		assertEquals(2248, rootNode.getChildNodesCounter());	
	}
	
	
	// FALTA EL MOVIMIENTO DE PROMOCION DE PEON
	@Test
	public void test_e13g1_h3g2_d5d6() {
		board.executeMove(Square.e1, Square.g1);
		board.executeMove(Square.h3, Square.g2);
		board.executeMove(Square.d5, Square.d6);
		
		Node rootNode = main.start(board, 1);
		
		assertEquals(1, rootNode.getChildNode(Square.e8, Square.g8 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.e8, Square.c8 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.e8, Square.f8 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.e8, Square.d8 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.c7, Square.c6 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.c7, Square.c5 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.c7, Square.d6 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.g6, Square.g5 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.b4, Square.b3 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.b4, Square.c3 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.g2, Square.f1 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.a8, Square.b8 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.a8, Square.c8 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.a8, Square.d8 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.h8, Square.g8 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.h8, Square.f8 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.h8, Square.h7 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.h8, Square.h6 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.h8, Square.h5 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.h8, Square.h4 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.h8, Square.h3 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.h8, Square.h2 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.e7, Square.d6 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.e7, Square.f8 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.e7, Square.d8 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.g7, Square.h6 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.g7, Square.f8 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.a6, Square.b7 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.a6, Square.c8 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.a6, Square.b5 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.a6, Square.c4 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.a6, Square.d3 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.a6, Square.e2 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.b6, Square.a4 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.b6, Square.c8 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.b6, Square.d5 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.b6, Square.c4 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.f6, Square.e4 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.f6, Square.g8 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.f6, Square.d5 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.f6, Square.h7 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.f6, Square.h5 ).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.f6, Square.g4 ).getChildNodesCounter());

		
		assertEquals(46, board.getMovimientosPosibles().size());
		assertEquals(46, rootNode.getChildNodesCounter());	
	}
}
