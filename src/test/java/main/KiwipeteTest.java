package main;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import builder.ChessBuilderGame;
import chess.Game;
import chess.Square;
import parsers.FENParser;

//Kiwipete
public class KiwipeteTest {

	private ChessMain main;
	
	private Game board;
	
	@Before
	public void setUp() throws Exception {
		main = new ChessMain();
		board =  getGame("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1");

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
	
	
	@Test //8segs
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
	public void test_e2b5() {		
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
		assertNull(rootNode.getChildNode(Square.d7 , Square.d6 ));	// Si el peon se mueve, el rey negro queda en jaque
		
		
		assertEquals(39, board.getMovimientosPosibles().size());
		assertEquals(39, rootNode.getChildNodesCounter());
	}
	
	@Test
	public void test_e2b5_e7f8() {
		board.executeMove(Square.e2, Square.c4);
		board.executeMove(Square.e7, Square.f8);
	}
	
	
	@Test
	public void test_e2c4u_e2b5() {
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
	
	@Test
	public void test_divide5() {
		Node rootNode = main.start(board, 5);
		
		assertEquals(4119629, rootNode.getChildNode(Square.e1, Square.g1).getChildNodesCounter());
		assertEquals(3551583, rootNode.getChildNode(Square.e1, Square.c1).getChildNodesCounter());
		assertEquals(3377351, rootNode.getChildNode(Square.e1, Square.f1).getChildNodesCounter());
		assertEquals(3559113, rootNode.getChildNode(Square.e1, Square.d1).getChildNodesCounter());
		assertEquals(3835265, rootNode.getChildNode(Square.d5, Square.d6).getChildNodesCounter());
		assertEquals(4727437, rootNode.getChildNode(Square.d5, Square.e6).getChildNodesCounter());
		assertEquals(4627439, rootNode.getChildNode(Square.a2, Square.a3).getChildNodesCounter());
		assertEquals(4387586, rootNode.getChildNode(Square.a2, Square.a4).getChildNodesCounter());
		assertEquals(3768824, rootNode.getChildNode(Square.b2, Square.b3).getChildNodesCounter());
		assertEquals(3472039, rootNode.getChildNode(Square.g2, Square.g3).getChildNodesCounter());
		assertEquals(3338154, rootNode.getChildNode(Square.g2, Square.g4).getChildNodesCounter());
		assertEquals(3819456, rootNode.getChildNode(Square.g2, Square.h3).getChildNodesCounter());
		assertEquals(3288812, rootNode.getChildNode(Square.e5, Square.d3).getChildNodesCounter());
		assertEquals(4164923, rootNode.getChildNode(Square.e5, Square.f7).getChildNodesCounter());
		assertEquals(3494887, rootNode.getChildNode(Square.e5, Square.c4).getChildNodesCounter());
		assertEquals(3949417, rootNode.getChildNode(Square.e5, Square.g6).getChildNodesCounter());
		assertEquals(3415992, rootNode.getChildNode(Square.e5, Square.g4).getChildNodesCounter());
		assertEquals(4083458, rootNode.getChildNode(Square.e5, Square.c6).getChildNodesCounter());
		assertEquals(4404043, rootNode.getChildNode(Square.e5, Square.d7).getChildNodesCounter());
		assertEquals(3996171, rootNode.getChildNode(Square.c3, Square.b1).getChildNodesCounter());
		assertEquals(4628497, rootNode.getChildNode(Square.c3, Square.a4).getChildNodesCounter());
		assertEquals(3995761, rootNode.getChildNode(Square.c3, Square.d1).getChildNodesCounter());
		assertEquals(4317482, rootNode.getChildNode(Square.c3, Square.b5).getChildNodesCounter());
		assertEquals(4669768, rootNode.getChildNode(Square.f3, Square.g3).getChildNodesCounter());
		assertEquals(5067173, rootNode.getChildNode(Square.f3, Square.h3).getChildNodesCounter());
		assertEquals(4477772, rootNode.getChildNode(Square.f3, Square.e3).getChildNodesCounter());
		assertEquals(3949570, rootNode.getChildNode(Square.f3, Square.d3).getChildNodesCounter());
		assertEquals(4514010, rootNode.getChildNode(Square.f3, Square.g4).getChildNodesCounter());
		assertEquals(4743335, rootNode.getChildNode(Square.f3, Square.h5).getChildNodesCounter());
		assertEquals(4327936, rootNode.getChildNode(Square.f3, Square.f4).getChildNodesCounter());
		assertEquals(5271134, rootNode.getChildNode(Square.f3, Square.f5).getChildNodesCounter());
		assertEquals(3975992, rootNode.getChildNode(Square.f3, Square.f6).getChildNodesCounter());
		assertEquals(3793390, rootNode.getChildNode(Square.d2, Square.c1).getChildNodesCounter());
		assertEquals(4407041, rootNode.getChildNode(Square.d2, Square.e3).getChildNodesCounter());
		assertEquals(3941257, rootNode.getChildNode(Square.d2, Square.f4).getChildNodesCounter());
		assertEquals(4370915, rootNode.getChildNode(Square.d2, Square.g5).getChildNodesCounter());
		assertEquals(3967365, rootNode.getChildNode(Square.d2, Square.h6).getChildNodesCounter());
		assertEquals(3074219, rootNode.getChildNode(Square.e2, Square.d1).getChildNodesCounter());
		assertEquals(4095479, rootNode.getChildNode(Square.e2, Square.f1).getChildNodesCounter());
		assertEquals(4066966, rootNode.getChildNode(Square.e2, Square.d3).getChildNodesCounter());
		assertEquals(4182989, rootNode.getChildNode(Square.e2, Square.c4).getChildNodesCounter());
		assertEquals(4032348, rootNode.getChildNode(Square.e2, Square.b5).getChildNodesCounter());
		assertEquals(3553501, rootNode.getChildNode(Square.e2, Square.a6).getChildNodesCounter());
		assertEquals(3827454, rootNode.getChildNode(Square.a1, Square.b1).getChildNodesCounter());
		assertEquals(3814203, rootNode.getChildNode(Square.a1, Square.c1).getChildNodesCounter());
		assertEquals(3568344, rootNode.getChildNode(Square.a1, Square.d1).getChildNodesCounter());
		assertEquals(3989454, rootNode.getChildNode(Square.h1, Square.g1).getChildNodesCounter());
		assertEquals(3685756, rootNode.getChildNode(Square.h1, Square.f1).getChildNodesCounter());
		
		
		assertEquals(48, board.getMovimientosPosibles().size());
		assertEquals(193690690, rootNode.getChildNodesCounter());		
	}
	
	
	@Test
	public void test_divide5_a1b1() {
		board.executeMove(Square.a1, Square.b1);
		
		Node rootNode = main.start(board, 4);
		
		assertFalse(board.getTablero().getBoardState().isEnroqueBlancoReinaPermitido());
		
		assertEquals(43, board.getMovimientosPosibles().size());
		assertEquals(3827454, rootNode.getChildNodesCounter());		
	}
	
	@Test
	public void test_divide5_e1f1() {
		board.executeMove(Square.e1, Square.f1);
		
		Node rootNode = main.start(board, 4);
		

		assertEquals(77387, rootNode.getChildNode(Square.e8, Square.g8).getChildNodesCounter());
		assertEquals(79777, rootNode.getChildNode(Square.e8, Square.c8).getChildNodesCounter());
		assertEquals(76235, rootNode.getChildNode(Square.e8, Square.f8).getChildNodesCounter());
		assertEquals(77879, rootNode.getChildNode(Square.e8, Square.d8).getChildNodesCounter());
		assertEquals(86454, rootNode.getChildNode(Square.c7, Square.c6).getChildNodesCounter());
		assertEquals(80761, rootNode.getChildNode(Square.c7, Square.c5).getChildNodesCounter());
		assertEquals(79885, rootNode.getChildNode(Square.d7, Square.d6).getChildNodesCounter());
		assertEquals(85346, rootNode.getChildNode(Square.e6, Square.d5).getChildNodesCounter());
		assertEquals(79610, rootNode.getChildNode(Square.g6, Square.g5).getChildNodesCounter());
		assertEquals(90977, rootNode.getChildNode(Square.b4, Square.b3).getChildNodesCounter());
		assertEquals(84682, rootNode.getChildNode(Square.b4, Square.c3).getChildNodesCounter());
		assertEquals(8536, rootNode.getChildNode(Square.h3, Square.g2).getChildNodesCounter());
		assertEquals(85453, rootNode.getChildNode(Square.a8, Square.b8).getChildNodesCounter());
		assertEquals(79310, rootNode.getChildNode(Square.a8, Square.c8).getChildNodesCounter());
		assertEquals(79449, rootNode.getChildNode(Square.a8, Square.d8).getChildNodesCounter());
		assertEquals(73227, rootNode.getChildNode(Square.h8, Square.g8).getChildNodesCounter());
		assertEquals(69251, rootNode.getChildNode(Square.h8, Square.f8).getChildNodesCounter());
		assertEquals(77173, rootNode.getChildNode(Square.h8, Square.h7).getChildNodesCounter());
		assertEquals(77098, rootNode.getChildNode(Square.h8, Square.h6).getChildNodesCounter());
		assertEquals(82926, rootNode.getChildNode(Square.h8, Square.h5).getChildNodesCounter());
		assertEquals(84587, rootNode.getChildNode(Square.h8, Square.h4).getChildNodesCounter());
		assertEquals(84693, rootNode.getChildNode(Square.e7, Square.d6).getChildNodesCounter());
		assertEquals(96672, rootNode.getChildNode(Square.e7, Square.c5).getChildNodesCounter());
		assertEquals(76932, rootNode.getChildNode(Square.e7, Square.f8).getChildNodesCounter());
		assertEquals(77219, rootNode.getChildNode(Square.e7, Square.d8).getChildNodesCounter());
		assertEquals(83986, rootNode.getChildNode(Square.g7, Square.h6).getChildNodesCounter());
		assertEquals(75283, rootNode.getChildNode(Square.g7, Square.f8).getChildNodesCounter());
		assertEquals(89850, rootNode.getChildNode(Square.a6, Square.b7).getChildNodesCounter());
		assertEquals(77062, rootNode.getChildNode(Square.a6, Square.c8).getChildNodesCounter());
		assertEquals(83931, rootNode.getChildNode(Square.a6, Square.b5).getChildNodesCounter());
		assertEquals(80494, rootNode.getChildNode(Square.a6, Square.c4).getChildNodesCounter());
		assertEquals(79395, rootNode.getChildNode(Square.a6, Square.d3).getChildNodesCounter());
		assertEquals(9332, rootNode.getChildNode(Square.a6, Square.e2).getChildNodesCounter());
		assertEquals(79573, rootNode.getChildNode(Square.b6, Square.a4).getChildNodesCounter());
		assertEquals(71184, rootNode.getChildNode(Square.b6, Square.c8).getChildNodesCounter());
		assertEquals(77103, rootNode.getChildNode(Square.b6, Square.d5).getChildNodesCounter());
		assertEquals(80085, rootNode.getChildNode(Square.b6, Square.c4).getChildNodesCounter());
		assertEquals(107224, rootNode.getChildNode(Square.f6, Square.e4).getChildNodesCounter());
		assertEquals(84536, rootNode.getChildNode(Square.f6, Square.g8).getChildNodesCounter());
		assertEquals(88468, rootNode.getChildNode(Square.f6, Square.d5).getChildNodesCounter());
		assertEquals(84406, rootNode.getChildNode(Square.f6, Square.h7).getChildNodesCounter());
		assertEquals(86559, rootNode.getChildNode(Square.f6, Square.h5).getChildNodesCounter());
		assertEquals(87361, rootNode.getChildNode(Square.f6, Square.g4).getChildNodesCounter());

		
		
		assertEquals(43, board.getMovimientosPosibles().size());
		assertEquals(3377351, rootNode.getChildNodesCounter());			
	}
	
	@Test
	public void test_divide5_e1f1_e8g8() {
		board.executeMove(Square.e1, Square.f1);
		board.executeMove(Square.e8, Square.g8);
		
		Node rootNode = main.start(board, 3);
		
		assertEquals(1833, rootNode.getChildNode(Square.f1, Square.g1).getChildNodesCounter());
		assertEquals(1832, rootNode.getChildNode(Square.f1, Square.e1).getChildNodesCounter());
		assertEquals(1659, rootNode.getChildNode(Square.d5, Square.d6).getChildNodesCounter());
		assertEquals(1894, rootNode.getChildNode(Square.d5, Square.e6).getChildNodesCounter());
		assertEquals(1840, rootNode.getChildNode(Square.a2, Square.a3).getChildNodesCounter());
		assertEquals(1808, rootNode.getChildNode(Square.a2, Square.a4).getChildNodesCounter());
		assertEquals(1639, rootNode.getChildNode(Square.b2, Square.b3).getChildNodesCounter());
		assertEquals(1602, rootNode.getChildNode(Square.g2, Square.g3).getChildNodesCounter());
		assertEquals(1567, rootNode.getChildNode(Square.g2, Square.g4).getChildNodesCounter());
		assertEquals(1681, rootNode.getChildNode(Square.g2, Square.h3).getChildNodesCounter());
		assertEquals(1600, rootNode.getChildNode(Square.e5, Square.d3).getChildNodesCounter());
		assertEquals(1828, rootNode.getChildNode(Square.e5, Square.f7).getChildNodesCounter());
		assertEquals(1629, rootNode.getChildNode(Square.e5, Square.c4).getChildNodesCounter());
		assertEquals(1712, rootNode.getChildNode(Square.e5, Square.g6).getChildNodesCounter());
		assertEquals(1566, rootNode.getChildNode(Square.e5, Square.g4).getChildNodesCounter());
		assertEquals(1784, rootNode.getChildNode(Square.e5, Square.c6).getChildNodesCounter());
		assertEquals(1836, rootNode.getChildNode(Square.e5, Square.d7).getChildNodesCounter());
		assertEquals(1702, rootNode.getChildNode(Square.c3, Square.b1).getChildNodesCounter());
		assertEquals(1848, rootNode.getChildNode(Square.c3, Square.a4).getChildNodesCounter());
		assertEquals(1773, rootNode.getChildNode(Square.c3, Square.d1).getChildNodesCounter());
		assertEquals(1853, rootNode.getChildNode(Square.c3, Square.b5).getChildNodesCounter());
		assertEquals(1758, rootNode.getChildNode(Square.f3, Square.g3).getChildNodesCounter());
		assertEquals(1801, rootNode.getChildNode(Square.f3, Square.h3).getChildNodesCounter());
		assertEquals(1725, rootNode.getChildNode(Square.f3, Square.e3).getChildNodesCounter());
		assertEquals(1739, rootNode.getChildNode(Square.f3, Square.d3).getChildNodesCounter());
		assertEquals(1785, rootNode.getChildNode(Square.f3, Square.g4).getChildNodesCounter());
		assertEquals(1797, rootNode.getChildNode(Square.f3, Square.h5).getChildNodesCounter());
		assertEquals(1688, rootNode.getChildNode(Square.f3, Square.f4).getChildNodesCounter());
		assertEquals(1920, rootNode.getChildNode(Square.f3, Square.f5).getChildNodesCounter());
		assertEquals(1665, rootNode.getChildNode(Square.f3, Square.f6).getChildNodesCounter());
		assertEquals(1567, rootNode.getChildNode(Square.d2, Square.c1).getChildNodesCounter());
		assertEquals(1720, rootNode.getChildNode(Square.d2, Square.e3).getChildNodesCounter());
		assertEquals(1599, rootNode.getChildNode(Square.d2, Square.f4).getChildNodesCounter());
		assertEquals(1718, rootNode.getChildNode(Square.d2, Square.g5).getChildNodesCounter());
		assertEquals(1711, rootNode.getChildNode(Square.d2, Square.h6).getChildNodesCounter());
		assertEquals(1453, rootNode.getChildNode(Square.d2, Square.e1).getChildNodesCounter());
		assertEquals(1749, rootNode.getChildNode(Square.e2, Square.d3).getChildNodesCounter());
		assertEquals(1742, rootNode.getChildNode(Square.e2, Square.c4).getChildNodesCounter());
		assertEquals(1705, rootNode.getChildNode(Square.e2, Square.b5).getChildNodesCounter());
		assertEquals(1690, rootNode.getChildNode(Square.e2, Square.a6).getChildNodesCounter());
		assertEquals(1683, rootNode.getChildNode(Square.a1, Square.b1).getChildNodesCounter());
		assertEquals(1682, rootNode.getChildNode(Square.a1, Square.c1).getChildNodesCounter());
		assertEquals(1680, rootNode.getChildNode(Square.a1, Square.d1).getChildNodesCounter());
		assertEquals(1643, rootNode.getChildNode(Square.a1, Square.e1).getChildNodesCounter());
		assertEquals(1681, rootNode.getChildNode(Square.h1, Square.g1).getChildNodesCounter());
		
		
		assertEquals(45, board.getMovimientosPosibles().size());
		assertEquals(77387, rootNode.getChildNodesCounter());			
	}
	
	@Test
	public void test_divide5_e1f1_e8g8_f1e1() {
		board.executeMove(Square.e1, Square.f1);
		board.executeMove(Square.e8, Square.g8);
		board.executeMove(Square.f1, Square.e1);
		
		Node rootNode = main.start(board, 2);
		
		assertEquals(46, rootNode.getChildNode(Square.g8, Square.h8).getChildNodesCounter());
		assertEquals(46, rootNode.getChildNode(Square.g8, Square.h7).getChildNodesCounter());
		assertEquals(47, rootNode.getChildNode(Square.c7, Square.c6).getChildNodesCounter());
		assertEquals(47, rootNode.getChildNode(Square.c7, Square.c5).getChildNodesCounter());
		assertEquals(45, rootNode.getChildNode(Square.d7, Square.d6).getChildNodesCounter());
		assertEquals(46, rootNode.getChildNode(Square.e6, Square.d5).getChildNodesCounter());
		assertEquals(45, rootNode.getChildNode(Square.g6, Square.g5).getChildNodesCounter());
		assertEquals(47, rootNode.getChildNode(Square.b4, Square.b3).getChildNodesCounter());
		assertEquals(46, rootNode.getChildNode(Square.b4, Square.c3).getChildNodesCounter());
		assertEquals(45, rootNode.getChildNode(Square.h3, Square.g2).getChildNodesCounter());
		assertEquals(46, rootNode.getChildNode(Square.a8, Square.b8).getChildNodesCounter());
		assertEquals(46, rootNode.getChildNode(Square.a8, Square.c8).getChildNodesCounter());
		assertEquals(46, rootNode.getChildNode(Square.a8, Square.d8).getChildNodesCounter());
		assertEquals(46, rootNode.getChildNode(Square.a8, Square.e8).getChildNodesCounter());
		assertEquals(46, rootNode.getChildNode(Square.f8, Square.e8).getChildNodesCounter());
		assertEquals(46, rootNode.getChildNode(Square.f8, Square.d8).getChildNodesCounter());
		assertEquals(46, rootNode.getChildNode(Square.f8, Square.c8).getChildNodesCounter());
		assertEquals(46, rootNode.getChildNode(Square.f8, Square.b8).getChildNodesCounter());
		assertEquals(45, rootNode.getChildNode(Square.e7, Square.d6).getChildNodesCounter());
		assertEquals(46, rootNode.getChildNode(Square.e7, Square.c5).getChildNodesCounter());
		assertEquals(46, rootNode.getChildNode(Square.e7, Square.e8).getChildNodesCounter());
		assertEquals(46, rootNode.getChildNode(Square.e7, Square.d8).getChildNodesCounter());
		assertEquals(46, rootNode.getChildNode(Square.g7, Square.h8).getChildNodesCounter());
		assertEquals(46, rootNode.getChildNode(Square.g7, Square.h6).getChildNodesCounter());
		assertEquals(46, rootNode.getChildNode(Square.a6, Square.b7).getChildNodesCounter());
		assertEquals(46, rootNode.getChildNode(Square.a6, Square.c8).getChildNodesCounter());
		assertEquals(45, rootNode.getChildNode(Square.a6, Square.b5).getChildNodesCounter());
		assertEquals(44, rootNode.getChildNode(Square.a6, Square.c4).getChildNodesCounter());
		assertEquals(44, rootNode.getChildNode(Square.a6, Square.d3).getChildNodesCounter());
		assertEquals(41, rootNode.getChildNode(Square.a6, Square.e2).getChildNodesCounter());
		assertEquals(45, rootNode.getChildNode(Square.b6, Square.a4).getChildNodesCounter());
		assertEquals(46, rootNode.getChildNode(Square.b6, Square.c8).getChildNodesCounter());
		assertEquals(46, rootNode.getChildNode(Square.b6, Square.d5).getChildNodesCounter());
		assertEquals(44, rootNode.getChildNode(Square.b6, Square.c4).getChildNodesCounter());
		assertEquals(49, rootNode.getChildNode(Square.f6, Square.e4).getChildNodesCounter());
		assertEquals(47, rootNode.getChildNode(Square.f6, Square.d5).getChildNodesCounter());
		assertEquals(47, rootNode.getChildNode(Square.f6, Square.h7).getChildNodesCounter());
		assertEquals(47, rootNode.getChildNode(Square.f6, Square.h5).getChildNodesCounter());
		assertEquals(45, rootNode.getChildNode(Square.f6, Square.g4).getChildNodesCounter());
		assertEquals(47, rootNode.getChildNode(Square.f6, Square.e8).getChildNodesCounter());
		
		
		assertEquals(40, board.getMovimientosPosibles().size());
		assertEquals(1832, rootNode.getChildNodesCounter());
	}
	
	@Test
	public void test_divide5_e1f1_e8g8_f1e1_g8h8() {
		board.executeMove(Square.e1, Square.f1);
		board.executeMove(Square.e8, Square.g8);
		board.executeMove(Square.f1, Square.e1);
		board.executeMove(Square.g8, Square.h8);
		
		Node rootNode = main.start(board, 1);
		
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
		
		
		assertEquals(46, board.getMovimientosPosibles().size());
		assertEquals(46, rootNode.getChildNodesCounter());		
	}	
	
	private Game getGame(String string) {		
		ChessBuilderGame builder = new ChessBuilderGame();

		FENParser parser = new FENParser(builder);
		
		parser.parseFEN(string);
		
		return builder.getGame();
	}	
}
