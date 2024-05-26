package net.chesstango.tools.perft;

import net.chesstango.board.Game;
import net.chesstango.board.Square;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;



//Kiwipete
/**
 * @author Mauricio Coria
 *
 */
public class KiwipeteTest extends AbstractPerftTest {

	private Perft pert;
	
	private Game board;
	
	@BeforeEach
	public void setUp() throws Exception {
		pert = createPerft();
		board =  getGame("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1");
	}

	@Test
	public void test_divide1() {		
		PerftResult result= pert.start(board, 1);
		
		assertEquals(1, result.getChildNode(Square.e1, Square.g1));
		assertEquals(1, result.getChildNode(Square.e1, Square.c1));
		assertEquals(1, result.getChildNode(Square.e1, Square.f1));
		assertEquals(1, result.getChildNode(Square.e1, Square.d1));
		
		assertEquals(1, result.getChildNode(Square.d5, Square.d6));
		assertEquals(1, result.getChildNode(Square.d5, Square.e6));

		assertEquals(1, result.getChildNode(Square.a2, Square.a3));
		assertEquals(1, result.getChildNode(Square.a2, Square.a4));
		
		assertEquals(1, result.getChildNode(Square.b2, Square.b3));
		
		assertEquals(1, result.getChildNode(Square.g2, Square.g3));
		assertEquals(1, result.getChildNode(Square.g2, Square.g4));
		assertEquals(1, result.getChildNode(Square.g2, Square.h3));
		
		assertEquals(1, result.getChildNode(Square.e5, Square.d3));
		assertEquals(1, result.getChildNode(Square.e5, Square.f7));
		assertEquals(1, result.getChildNode(Square.e5, Square.c4));
		assertEquals(1, result.getChildNode(Square.e5, Square.g6));	
		assertEquals(1, result.getChildNode(Square.e5, Square.g4));
		assertEquals(1, result.getChildNode(Square.e5, Square.c6));
		assertEquals(1, result.getChildNode(Square.e5, Square.d7));	
		
		assertEquals(1, result.getChildNode(Square.c3, Square.b1));
		assertEquals(1, result.getChildNode(Square.c3, Square.a4));
		assertEquals(1, result.getChildNode(Square.c3, Square.d1));
		assertEquals(1, result.getChildNode(Square.c3, Square.b5));
		
		assertEquals(1, result.getChildNode(Square.f3, Square.g3));
		assertEquals(1, result.getChildNode(Square.f3, Square.h3));
		assertEquals(1, result.getChildNode(Square.f3, Square.e3));
		assertEquals(1, result.getChildNode(Square.f3, Square.d3));	
		assertEquals(1, result.getChildNode(Square.f3, Square.g4));
		assertEquals(1, result.getChildNode(Square.f3, Square.h5));
		assertEquals(1, result.getChildNode(Square.f3, Square.f4));
		assertEquals(1, result.getChildNode(Square.f3, Square.f5));
		assertEquals(1, result.getChildNode(Square.f3, Square.f6));	
		
		assertEquals(1, result.getChildNode(Square.d2, Square.c1));
		assertEquals(1, result.getChildNode(Square.d2, Square.e3));
		assertEquals(1, result.getChildNode(Square.d2, Square.f4));
		assertEquals(1, result.getChildNode(Square.d2, Square.g5));	
		assertEquals(1, result.getChildNode(Square.d2, Square.h6));		
		
		assertEquals(1, result.getChildNode(Square.e2, Square.d1));
		assertEquals(1, result.getChildNode(Square.e2, Square.f1));
		assertEquals(1, result.getChildNode(Square.e2, Square.d3));
		assertEquals(1, result.getChildNode(Square.e2, Square.c4));	
		assertEquals(1, result.getChildNode(Square.e2, Square.b5));
		assertEquals(1, result.getChildNode(Square.e2, Square.a6));
		
		assertEquals(1, result.getChildNode(Square.a1, Square.b1));
		assertEquals(1, result.getChildNode(Square.a1, Square.c1));
		assertEquals(1, result.getChildNode(Square.a1, Square.d1));
		
		assertEquals(1, result.getChildNode(Square.h1, Square.g1));
		assertEquals(1, result.getChildNode(Square.h1, Square.f1));			
		
		assertEquals(48, result.getMovesCount());
		assertEquals(48, result.getTotalNodes());
	}
	
	@Test
	public void test_divide2() {
		PerftResult result= pert.start(board, 2);
		
		assertEquals(43, result.getChildNode(Square.e1, Square.g1));
		assertEquals(43, result.getChildNode(Square.e1, Square.c1));
		assertEquals(43, result.getChildNode(Square.e1, Square.f1));
		assertEquals(43, result.getChildNode(Square.e1, Square.d1));
		
		assertEquals(41, result.getChildNode(Square.d5, Square.d6));
		assertEquals(46, result.getChildNode(Square.d5, Square.e6));

		assertEquals(44, result.getChildNode(Square.a2, Square.a3));
		assertEquals(44, result.getChildNode(Square.a2, Square.a4));
		
		assertEquals(42, result.getChildNode(Square.b2, Square.b3));
		
		assertEquals(42, result.getChildNode(Square.g2, Square.g3));
		assertEquals(42, result.getChildNode(Square.g2, Square.g4));
		assertEquals(43, result.getChildNode(Square.g2, Square.h3));
		
		assertEquals(43, result.getChildNode(Square.e5, Square.d3));
		assertEquals(44, result.getChildNode(Square.e5, Square.f7));
		assertEquals(42, result.getChildNode(Square.e5, Square.c4));
		assertEquals(42, result.getChildNode(Square.e5, Square.g6));	
		assertEquals(44, result.getChildNode(Square.e5, Square.g4));
		assertEquals(41, result.getChildNode(Square.e5, Square.c6));
		assertEquals(45, result.getChildNode(Square.e5, Square.d7));	
		
		assertEquals(42, result.getChildNode(Square.c3, Square.b1));
		assertEquals(42, result.getChildNode(Square.c3, Square.a4));
		assertEquals(42, result.getChildNode(Square.c3, Square.d1));
		assertEquals(39, result.getChildNode(Square.c3, Square.b5));
		
		assertEquals(43, result.getChildNode(Square.f3, Square.g3));
		assertEquals(43, result.getChildNode(Square.f3, Square.h3));
		assertEquals(43, result.getChildNode(Square.f3, Square.e3));
		assertEquals(42, result.getChildNode(Square.f3, Square.d3));	
		assertEquals(43, result.getChildNode(Square.f3, Square.g4));
		assertEquals(43, result.getChildNode(Square.f3, Square.h5));
		assertEquals(43, result.getChildNode(Square.f3, Square.f4));
		assertEquals(45, result.getChildNode(Square.f3, Square.f5));
		assertEquals(39, result.getChildNode(Square.f3, Square.f6));	
		
		assertEquals(43, result.getChildNode(Square.d2, Square.c1));
		assertEquals(43, result.getChildNode(Square.d2, Square.e3));
		assertEquals(43, result.getChildNode(Square.d2, Square.f4));
		assertEquals(42, result.getChildNode(Square.d2, Square.g5));	
		assertEquals(41, result.getChildNode(Square.d2, Square.h6));		
		
		assertEquals(44, result.getChildNode(Square.e2, Square.d1));
		assertEquals(44, result.getChildNode(Square.e2, Square.f1));
		assertEquals(42, result.getChildNode(Square.e2, Square.d3));
		assertEquals(41, result.getChildNode(Square.e2, Square.c4));	
		assertEquals(39, result.getChildNode(Square.e2, Square.b5));
		assertEquals(36, result.getChildNode(Square.e2, Square.a6));
		
		assertEquals(43, result.getChildNode(Square.a1, Square.b1));
		assertEquals(43, result.getChildNode(Square.a1, Square.c1));
		assertEquals(43, result.getChildNode(Square.a1, Square.d1));
		
		assertEquals(43, result.getChildNode(Square.h1, Square.g1));
		assertEquals(43, result.getChildNode(Square.h1, Square.f1));		
		
		assertEquals(48, result.getMovesCount());
		assertEquals(2039, result.getTotalNodes());
	}
	
	
	@Test
	public void test_divide3() {
		PerftResult result= pert.start(board, 3);
		
		assertEquals(2059, result.getChildNode(Square.e1, Square.g1));
		assertEquals(1887, result.getChildNode(Square.e1, Square.c1 ));
		assertEquals(1855, result.getChildNode(Square.e1, Square.f1 ));
		assertEquals(1894, result.getChildNode(Square.e1, Square.d1 ));
		assertEquals(1991, result.getChildNode(Square.d5, Square.d6 ));
		assertEquals(2241, result.getChildNode(Square.d5, Square.e6 ));
		assertEquals(2186, result.getChildNode(Square.a2, Square.a3 ));
		assertEquals(2149, result.getChildNode(Square.a2, Square.a4 ));
		assertEquals(1964, result.getChildNode(Square.b2, Square.b3 ));
		assertEquals(1882, result.getChildNode(Square.g2, Square.g3 ));
		assertEquals(1843, result.getChildNode(Square.g2, Square.g4 ));
		assertEquals(1970, result.getChildNode(Square.g2, Square.h3 ));
		assertEquals(1803, result.getChildNode(Square.e5, Square.d3 ));
		assertEquals(2080, result.getChildNode(Square.e5, Square.f7 ));
		assertEquals(1880, result.getChildNode(Square.e5, Square.c4 ));
		assertEquals(1997, result.getChildNode(Square.e5, Square.g6 ));
		assertEquals(1878, result.getChildNode(Square.e5, Square.g4 ));
		assertEquals(2027, result.getChildNode(Square.e5, Square.c6 ));
		assertEquals(2124, result.getChildNode(Square.e5, Square.d7 ));
		assertEquals(2038, result.getChildNode(Square.c3, Square.b1 ));
		assertEquals(2203, result.getChildNode(Square.c3, Square.a4 ));
		assertEquals(2040, result.getChildNode(Square.c3, Square.d1 ));
		assertEquals(2138, result.getChildNode(Square.c3, Square.b5 ));
		assertEquals(2214, result.getChildNode(Square.f3, Square.g3 ));
		assertEquals(2360, result.getChildNode(Square.f3, Square.h3 ));
		assertEquals(2174, result.getChildNode(Square.f3, Square.e3 ));
		assertEquals(2005, result.getChildNode(Square.f3, Square.d3 ));
		assertEquals(2169, result.getChildNode(Square.f3, Square.g4 ));
		assertEquals(2267, result.getChildNode(Square.f3, Square.h5 ));
		assertEquals(2132, result.getChildNode(Square.f3, Square.f4 ));
		assertEquals(2396, result.getChildNode(Square.f3, Square.f5 ));
		assertEquals(2111, result.getChildNode(Square.f3, Square.f6 ));
		assertEquals(1963, result.getChildNode(Square.d2, Square.c1 ));
		assertEquals(2136, result.getChildNode(Square.d2, Square.e3 ));
		assertEquals(2000, result.getChildNode(Square.d2, Square.f4 ));
		assertEquals(2134, result.getChildNode(Square.d2, Square.g5 ));
		assertEquals(2019, result.getChildNode(Square.d2, Square.h6 ));
		assertEquals(1733, result.getChildNode(Square.e2, Square.d1 ));
		assertEquals(2060, result.getChildNode(Square.e2, Square.f1 ));
		assertEquals(2050, result.getChildNode(Square.e2, Square.d3 ));
		assertEquals(2082, result.getChildNode(Square.e2, Square.c4 ));
		assertEquals(2057, result.getChildNode(Square.e2, Square.b5 ));
		assertEquals(1907, result.getChildNode(Square.e2, Square.a6 ));
		assertEquals(1969, result.getChildNode(Square.a1, Square.b1 ));
		assertEquals(1968, result.getChildNode(Square.a1, Square.c1 ));
		assertEquals(1885, result.getChildNode(Square.a1, Square.d1 ));
		assertEquals(2013, result.getChildNode(Square.h1, Square.g1 ));
		assertEquals(1929, result.getChildNode(Square.h1, Square.f1 ));
		
		assertEquals(48, result.getMovesCount());
		assertEquals(97862, result.getTotalNodes());
	}
	
	
	@Test //8segs
	public void test_divide4() {
		PerftResult result= pert.start(board, 4);
		
		assertEquals(86975, result.getChildNode(Square.e1, Square.g1 ));
		assertEquals(79803, result.getChildNode(Square.e1, Square.c1 ));
		assertEquals(77887, result.getChildNode(Square.e1, Square.f1 ));
		assertEquals(79989, result.getChildNode(Square.e1, Square.d1 ));
		assertEquals(79551, result.getChildNode(Square.d5, Square.d6 ));
		assertEquals(97464, result.getChildNode(Square.d5, Square.e6 ));
		assertEquals(94405, result.getChildNode(Square.a2, Square.a3 ));
		assertEquals(90978, result.getChildNode(Square.a2, Square.a4 ));
		assertEquals(81066, result.getChildNode(Square.b2, Square.b3 ));
		assertEquals(77468, result.getChildNode(Square.g2, Square.g3 ));
		assertEquals(75677, result.getChildNode(Square.g2, Square.g4 ));
		assertEquals(82759, result.getChildNode(Square.g2, Square.h3 ));
		assertEquals(77431, result.getChildNode(Square.e5, Square.d3 ));
		assertEquals(88799, result.getChildNode(Square.e5, Square.f7 ));
		assertEquals(77752, result.getChildNode(Square.e5, Square.c4 ));
		assertEquals(83866, result.getChildNode(Square.e5, Square.g6 ));
		assertEquals(79912, result.getChildNode(Square.e5, Square.g4 ));
		assertEquals(83885, result.getChildNode(Square.e5, Square.c6 ));
		assertEquals(93913, result.getChildNode(Square.e5, Square.d7 ));
		assertEquals(84773, result.getChildNode(Square.c3, Square.b1 ));
		assertEquals(91447, result.getChildNode(Square.c3, Square.a4 ));
		assertEquals(84782, result.getChildNode(Square.c3, Square.d1 ));
		assertEquals(81498, result.getChildNode(Square.c3, Square.b5 ));
		assertEquals(94461, result.getChildNode(Square.f3, Square.g3 ));
		assertEquals(98524, result.getChildNode(Square.f3, Square.h3 ));
		assertEquals(92505, result.getChildNode(Square.f3, Square.e3 ));
		assertEquals(83727, result.getChildNode(Square.f3, Square.d3 ));
		assertEquals(92037, result.getChildNode(Square.f3, Square.g4 ));
		assertEquals(95034, result.getChildNode(Square.f3, Square.h5 ));
		assertEquals(90488, result.getChildNode(Square.f3, Square.f4 ));
		assertEquals(104992, result.getChildNode(Square.f3, Square.f5 ));
		assertEquals(77838, result.getChildNode(Square.f3, Square.f6 ));
		assertEquals(83037, result.getChildNode(Square.d2, Square.c1 ));
		assertEquals(90274, result.getChildNode(Square.d2, Square.e3 ));
		assertEquals(84869, result.getChildNode(Square.d2, Square.f4 ));
		assertEquals(87951, result.getChildNode(Square.d2, Square.g5 ));
		assertEquals(82323, result.getChildNode(Square.d2, Square.h6 ));
		assertEquals(74963, result.getChildNode(Square.e2, Square.d1 ));
		assertEquals(88728, result.getChildNode(Square.e2, Square.f1 ));
		assertEquals(85119, result.getChildNode(Square.e2, Square.d3 ));
		assertEquals(84835, result.getChildNode(Square.e2, Square.c4 ));
		assertEquals(79739, result.getChildNode(Square.e2, Square.b5 ));
		assertEquals(69334, result.getChildNode(Square.e2, Square.a6 ));
		assertEquals(83348, result.getChildNode(Square.a1, Square.b1 ));
		assertEquals(83263, result.getChildNode(Square.a1, Square.c1 ));
		assertEquals(79695, result.getChildNode(Square.a1, Square.d1 ));
		assertEquals(84876, result.getChildNode(Square.h1, Square.g1 ));
		assertEquals(81563, result.getChildNode(Square.h1, Square.f1 ));

		assertEquals(48, result.getMovesCount());
		assertEquals(4085603, result.getTotalNodes());
	}
	
	@Test
	public void test_d5d6() {
		board.executeMove(Square.d5, Square.d6);
		
		PerftResult result= pert.start(board, 2);
		
		assertEquals(49, result.getChildNode(Square.e8, Square.g8 ));
		assertEquals(49, result.getChildNode(Square.e8, Square.c8 ));
		assertEquals(49, result.getChildNode(Square.e8, Square.f8 ));
		assertEquals(49, result.getChildNode(Square.e8, Square.d8 ));
		assertEquals(48, result.getChildNode(Square.c7, Square.c6 ));
		assertEquals(48, result.getChildNode(Square.c7, Square.c5 ));
		assertEquals(47, result.getChildNode(Square.c7, Square.d6 ));
		assertEquals(48, result.getChildNode(Square.g6, Square.g5 ));
		assertEquals(50, result.getChildNode(Square.b4, Square.b3 ));
		assertEquals(48, result.getChildNode(Square.b4, Square.c3 ));
		assertEquals(47, result.getChildNode(Square.h3, Square.g2 ));
		assertEquals(49, result.getChildNode(Square.a8, Square.b8 ));
		assertEquals(49, result.getChildNode(Square.a8, Square.c8 ));
		assertEquals(49, result.getChildNode(Square.a8, Square.d8 ));
		assertEquals(49, result.getChildNode(Square.h8, Square.g8 ));
		assertEquals(49, result.getChildNode(Square.h8, Square.f8 ));
		assertEquals(49, result.getChildNode(Square.h8, Square.h7 ));
		assertEquals(49, result.getChildNode(Square.h8, Square.h6 ));
		assertEquals(49, result.getChildNode(Square.h8, Square.h5 ));
		assertEquals(49, result.getChildNode(Square.h8, Square.h4 ));
		assertEquals(47, result.getChildNode(Square.e7, Square.d6 ));
		assertEquals(48, result.getChildNode(Square.e7, Square.f8 ));
		assertEquals(48, result.getChildNode(Square.e7, Square.d8 ));
		assertEquals(49, result.getChildNode(Square.g7, Square.h6 ));
		assertEquals(49, result.getChildNode(Square.g7, Square.f8 ));
		assertEquals(49, result.getChildNode(Square.a6, Square.b7 ));
		assertEquals(49, result.getChildNode(Square.a6, Square.c8 ));
		assertEquals(48, result.getChildNode(Square.a6, Square.b5 ));
		assertEquals(47, result.getChildNode(Square.a6, Square.c4 ));
		assertEquals(47, result.getChildNode(Square.a6, Square.d3 ));
		assertEquals(42, result.getChildNode(Square.a6, Square.e2 ));
		assertEquals(48, result.getChildNode(Square.b6, Square.a4 ));
		assertEquals(49, result.getChildNode(Square.b6, Square.c8 ));
		assertEquals(50, result.getChildNode(Square.b6, Square.d5 ));
		assertEquals(47, result.getChildNode(Square.b6, Square.c4 ));
		assertEquals(52, result.getChildNode(Square.f6, Square.e4 ));
		assertEquals(50, result.getChildNode(Square.f6, Square.g8 ));
		assertEquals(51, result.getChildNode(Square.f6, Square.d5 ));
		assertEquals(50, result.getChildNode(Square.f6, Square.h7 ));
		assertEquals(50, result.getChildNode(Square.f6, Square.h5 ));
		assertEquals(48, result.getChildNode(Square.f6, Square.g4 ));
		
		assertEquals(41, result.getMovesCount());
		assertEquals(1991, result.getTotalNodes());
	}
	
	@Test
	public void test_d5d6_h3g2() {
		board.executeMove(Square.d5, Square.d6);
		board.executeMove(Square.h3, Square.g2);
		
		PerftResult result= pert.start(board, 1);
		
		assertEquals(1, result.getChildNode(Square.e1, Square.c1 ));
		assertEquals(1, result.getChildNode(Square.e1, Square.d1 ));
		assertEquals(1, result.getChildNode(Square.d6, Square.e7 ));
		assertEquals(1, result.getChildNode(Square.d6, Square.c7 ));
		assertEquals(1, result.getChildNode(Square.a2, Square.a3 ));
		assertEquals(1, result.getChildNode(Square.a2, Square.a4 ));
		assertEquals(1, result.getChildNode(Square.b2, Square.b3 ));
		assertEquals(1, result.getChildNode(Square.h2, Square.h3 ));
		assertEquals(1, result.getChildNode(Square.h2, Square.h4 ));
		assertEquals(1, result.getChildNode(Square.e5, Square.d3 ));
		assertEquals(1, result.getChildNode(Square.e5, Square.f7 ));
		assertEquals(1, result.getChildNode(Square.e5, Square.c4 ));
		assertEquals(1, result.getChildNode(Square.e5, Square.g6 ));
		assertEquals(1, result.getChildNode(Square.e5, Square.g4 ));
		assertEquals(1, result.getChildNode(Square.e5, Square.c6 ));
		assertEquals(1, result.getChildNode(Square.e5, Square.d7 ));
		assertEquals(1, result.getChildNode(Square.c3, Square.b1 ));
		assertEquals(1, result.getChildNode(Square.c3, Square.d5 ));
		assertEquals(1, result.getChildNode(Square.c3, Square.a4 ));
		assertEquals(1, result.getChildNode(Square.c3, Square.d1 ));
		assertEquals(1, result.getChildNode(Square.c3, Square.b5 ));
		assertEquals(1, result.getChildNode(Square.f3, Square.g3 ));
		assertEquals(1, result.getChildNode(Square.f3, Square.h3 ));
		assertEquals(1, result.getChildNode(Square.f3, Square.e3 ));
		assertEquals(1, result.getChildNode(Square.f3, Square.d3 ));
		assertEquals(1, result.getChildNode(Square.f3, Square.g2 ));
		assertEquals(1, result.getChildNode(Square.f3, Square.g4 ));
		assertEquals(1, result.getChildNode(Square.f3, Square.h5 ));
		assertEquals(1, result.getChildNode(Square.f3, Square.f4 ));
		assertEquals(1, result.getChildNode(Square.f3, Square.f5 ));
		assertEquals(1, result.getChildNode(Square.f3, Square.f6 ));
		assertEquals(1, result.getChildNode(Square.d2, Square.c1 ));
		assertEquals(1, result.getChildNode(Square.d2, Square.e3 ));
		assertEquals(1, result.getChildNode(Square.d2, Square.f4 ));
		assertEquals(1, result.getChildNode(Square.d2, Square.g5 ));
		assertEquals(1, result.getChildNode(Square.d2, Square.h6 ));
		assertEquals(1, result.getChildNode(Square.e2, Square.d1 ));
		assertEquals(1, result.getChildNode(Square.e2, Square.f1 ));
		assertEquals(1, result.getChildNode(Square.e2, Square.d3 ));
		assertEquals(1, result.getChildNode(Square.e2, Square.c4 ));
		assertEquals(1, result.getChildNode(Square.e2, Square.b5 ));
		assertEquals(1, result.getChildNode(Square.e2, Square.a6 ));
		assertEquals(1, result.getChildNode(Square.a1, Square.b1 ));
		assertEquals(1, result.getChildNode(Square.a1, Square.c1 ));
		assertEquals(1, result.getChildNode(Square.a1, Square.d1 ));
		assertEquals(1, result.getChildNode(Square.h1, Square.g1 ));
		assertEquals(1, result.getChildNode(Square.h1, Square.f1 ));
		
		assertFalse(result.moveExists(Square.e1 , Square.g1 ));
		
		assertEquals(47, result.getMovesCount());
		assertEquals(47, result.getTotalNodes());
	}
	
	@Test
	public void test_c3b2() {
		board.executeMove(Square.c3, Square.b1);
		
		PerftResult result= pert.start(board, 2);
		
		assertEquals(49, result.getChildNode(Square.e8, Square.g8 ));
		assertEquals(49, result.getChildNode(Square.e8, Square.c8 ));
		assertEquals(49, result.getChildNode(Square.e8, Square.f8 ));
		assertEquals(49, result.getChildNode(Square.e8, Square.d8 ));
		assertEquals(50, result.getChildNode(Square.c7, Square.c6 ));
		assertEquals(50, result.getChildNode(Square.c7, Square.c5 ));
		assertEquals(48, result.getChildNode(Square.d7, Square.d6 ));
		assertEquals(48, result.getChildNode(Square.e6, Square.d5 ));
		assertEquals(48, result.getChildNode(Square.g6, Square.g5 ));
		assertEquals(50, result.getChildNode(Square.b4, Square.b3 ));
		assertEquals(47, result.getChildNode(Square.h3, Square.g2 ));
		assertEquals(49, result.getChildNode(Square.a8, Square.b8 ));
		assertEquals(49, result.getChildNode(Square.a8, Square.c8 ));
		assertEquals(49, result.getChildNode(Square.a8, Square.d8 ));
		assertEquals(49, result.getChildNode(Square.h8, Square.g8 ));
		assertEquals(49, result.getChildNode(Square.h8, Square.f8 ));
		assertEquals(49, result.getChildNode(Square.h8, Square.h7 ));
		assertEquals(49, result.getChildNode(Square.h8, Square.h6 ));
		assertEquals(49, result.getChildNode(Square.h8, Square.h5 ));
		assertEquals(49, result.getChildNode(Square.h8, Square.h4 ));
		assertEquals(48, result.getChildNode(Square.e7, Square.d6 ));
		assertEquals(49, result.getChildNode(Square.e7, Square.c5 ));
		assertEquals(49, result.getChildNode(Square.e7, Square.f8 ));
		assertEquals(49, result.getChildNode(Square.e7, Square.d8 ));
		assertEquals(49, result.getChildNode(Square.g7, Square.h6 ));
		assertEquals(49, result.getChildNode(Square.g7, Square.f8 ));
		assertEquals(49, result.getChildNode(Square.a6, Square.b7 ));
		assertEquals(49, result.getChildNode(Square.a6, Square.c8 ));
		assertEquals(48, result.getChildNode(Square.a6, Square.b5 ));
		assertEquals(46, result.getChildNode(Square.a6, Square.c4 ));
		assertEquals(44, result.getChildNode(Square.a6, Square.d3 ));
		assertEquals(42, result.getChildNode(Square.a6, Square.e2 ));
		assertEquals(48, result.getChildNode(Square.b6, Square.a4 ));
		assertEquals(49, result.getChildNode(Square.b6, Square.c8 ));
		assertEquals(48, result.getChildNode(Square.b6, Square.d5 ));
		assertEquals(46, result.getChildNode(Square.b6, Square.c4 ));
		assertEquals(51, result.getChildNode(Square.f6, Square.e4 ));
		assertEquals(50, result.getChildNode(Square.f6, Square.g8 ));
		assertEquals(49, result.getChildNode(Square.f6, Square.d5 ));
		assertEquals(50, result.getChildNode(Square.f6, Square.h7 ));
		assertEquals(50, result.getChildNode(Square.f6, Square.h5 ));
		assertEquals(48, result.getChildNode(Square.f6, Square.g4 ));
		
		assertEquals(42, result.getMovesCount());
		assertEquals(2038, result.getTotalNodes());
	}
	
	@Test
	public void test_c3b2_e8g8() {
		board.executeMove(Square.c3, Square.b1);
		board.executeMove(Square.e8, Square.g8);
		
		PerftResult result= pert.start(board, 1);
		
		assertEquals(1, result.getChildNode(Square.e1, Square.g1 ));
		assertEquals(1, result.getChildNode(Square.e1 , Square.f1 ));
		assertEquals(1, result.getChildNode(Square.e1 , Square.d1 ));
		assertEquals(1, result.getChildNode(Square.d5 , Square.d6 ));
		assertEquals(1, result.getChildNode(Square.d5 , Square.e6 ));
		assertEquals(1, result.getChildNode(Square.a2 , Square.a3 ));
		assertEquals(1, result.getChildNode(Square.a2 , Square.a4 ));
		assertEquals(1, result.getChildNode(Square.b2 , Square.b3 ));
		assertEquals(1, result.getChildNode(Square.c2 , Square.c3 ));
		assertEquals(1, result.getChildNode(Square.c2 , Square.c4 ));
		assertEquals(1, result.getChildNode(Square.g2 , Square.g3 ));
		assertEquals(1, result.getChildNode(Square.g2 , Square.g4 ));
		assertEquals(1, result.getChildNode(Square.g2 , Square.h3 ));
		assertEquals(1, result.getChildNode(Square.e5 , Square.d3 ));
		assertEquals(1, result.getChildNode(Square.e5 , Square.f7 ));
		assertEquals(1, result.getChildNode(Square.e5 , Square.c4 ));
		assertEquals(1, result.getChildNode(Square.e5 , Square.g6 ));
		assertEquals(1, result.getChildNode(Square.e5 , Square.g4 ));
		assertEquals(1, result.getChildNode(Square.e5 , Square.c6 ));
		assertEquals(1, result.getChildNode(Square.e5 , Square.d7 ));
		assertEquals(1, result.getChildNode(Square.b1 , Square.c3 ));
		assertEquals(1, result.getChildNode(Square.b1 , Square.a3 ));
		assertEquals(1, result.getChildNode(Square.f3 , Square.g3 ));
		assertEquals(1, result.getChildNode(Square.f3 , Square.h3 ));
		assertEquals(1, result.getChildNode(Square.f3 , Square.e3 ));
		assertEquals(1, result.getChildNode(Square.f3 , Square.d3 ));
		assertEquals(1, result.getChildNode(Square.f3 , Square.c3 ));
		assertEquals(1, result.getChildNode(Square.f3 , Square.b3 ));
		assertEquals(1, result.getChildNode(Square.f3 , Square.a3 ));
		assertEquals(1, result.getChildNode(Square.f3 , Square.g4 ));
		assertEquals(1, result.getChildNode(Square.f3 , Square.h5 ));
		assertEquals(1, result.getChildNode(Square.f3 , Square.f4 ));
		assertEquals(1, result.getChildNode(Square.f3 , Square.f5 ));
		assertEquals(1, result.getChildNode(Square.f3 , Square.f6 ));
		assertEquals(1, result.getChildNode(Square.d2 , Square.c1 ));
		assertEquals(1, result.getChildNode(Square.d2 , Square.e3 ));
		assertEquals(1, result.getChildNode(Square.d2 , Square.f4 ));
		assertEquals(1, result.getChildNode(Square.d2 , Square.g5 ));
		assertEquals(1, result.getChildNode(Square.d2 , Square.h6 ));
		assertEquals(1, result.getChildNode(Square.d2 , Square.c3 ));
		assertEquals(1, result.getChildNode(Square.d2 , Square.b4 ));
		assertEquals(1, result.getChildNode(Square.e2 , Square.d1 ));
		assertEquals(1, result.getChildNode(Square.e2 , Square.f1 ));
		assertEquals(1, result.getChildNode(Square.e2 , Square.d3 ));
		assertEquals(1, result.getChildNode(Square.e2 , Square.c4 ));
		assertEquals(1, result.getChildNode(Square.e2 , Square.b5 ));
		assertEquals(1, result.getChildNode(Square.e2 , Square.a6 ));
		assertEquals(1, result.getChildNode(Square.h1 , Square.g1 ));
		assertEquals(1, result.getChildNode(Square.h1 , Square.f1 ));
		
		assertFalse(result.moveExists(Square.e1 , Square.c1 ));
		
		assertEquals(49, result.getMovesCount());
		assertEquals(49, result.getTotalNodes());
	}
	
	@Test
	public void test_e2b5() {		
		board.executeMove(Square.e2, Square.b5);
		
		PerftResult result= pert.start(board, 1);
		
		assertEquals(1, result.getChildNode(Square.e8, Square.g8 ));
		assertEquals(1, result.getChildNode(Square.e8, Square.c8 ));
		assertEquals(1, result.getChildNode(Square.e8, Square.f8 ));
		assertEquals(1, result.getChildNode(Square.e8, Square.d8 ));
		assertEquals(1, result.getChildNode(Square.c7, Square.c6 ));
		assertEquals(1, result.getChildNode(Square.c7, Square.c5 ));
		assertEquals(1, result.getChildNode(Square.e6, Square.d5 ));
		assertEquals(1, result.getChildNode(Square.g6, Square.g5 ));
		assertEquals(1, result.getChildNode(Square.b4, Square.b3 ));
		assertEquals(1, result.getChildNode(Square.b4, Square.c3 ));
		assertEquals(1, result.getChildNode(Square.h3, Square.g2 ));
		assertEquals(1, result.getChildNode(Square.a8, Square.b8 ));
		assertEquals(1, result.getChildNode(Square.a8, Square.c8 ));
		assertEquals(1, result.getChildNode(Square.a8, Square.d8 ));
		assertEquals(1, result.getChildNode(Square.h8, Square.g8 ));
		assertEquals(1, result.getChildNode(Square.h8, Square.f8 ));
		assertEquals(1, result.getChildNode(Square.h8, Square.h7 ));
		assertEquals(1, result.getChildNode(Square.h8, Square.h6 ));
		assertEquals(1, result.getChildNode(Square.h8, Square.h5 ));
		assertEquals(1, result.getChildNode(Square.h8, Square.h4 ));
		assertEquals(1, result.getChildNode(Square.e7, Square.d6 ));
		assertEquals(1, result.getChildNode(Square.e7, Square.c5 ));
		assertEquals(1, result.getChildNode(Square.e7, Square.f8 ));
		assertEquals(1, result.getChildNode(Square.e7, Square.d8 ));
		assertEquals(1, result.getChildNode(Square.g7, Square.h6 ));
		assertEquals(1, result.getChildNode(Square.g7, Square.f8 ));
		assertEquals(1, result.getChildNode(Square.a6, Square.b7 ));
		assertEquals(1, result.getChildNode(Square.a6, Square.c8 ));
		assertEquals(1, result.getChildNode(Square.a6, Square.b5 ));
		assertEquals(1, result.getChildNode(Square.b6, Square.a4 ));
		assertEquals(1, result.getChildNode(Square.b6, Square.c8 ));
		assertEquals(1, result.getChildNode(Square.b6, Square.d5 ));
		assertEquals(1, result.getChildNode(Square.b6, Square.c4 ));
		assertEquals(1, result.getChildNode(Square.f6, Square.e4 ));
		assertEquals(1, result.getChildNode(Square.f6, Square.g8 ));
		assertEquals(1, result.getChildNode(Square.f6, Square.d5 ));
		assertEquals(1, result.getChildNode(Square.f6, Square.h7 ));
		assertEquals(1, result.getChildNode(Square.f6, Square.h5 ));
		assertEquals(1, result.getChildNode(Square.f6, Square.g4 ));
		
		assertFalse(result.moveExists(Square.d7 , Square.d6 ));	// Si el pawn se mueve, el king negro queda en jaque

		assertEquals(39, result.getMovesCount());
		assertEquals(39, result.getTotalNodes());
	}
	
	@Test
	public void test_e2b5_e7f8() {
		board.executeMove(Square.e2, Square.c4);
		board.executeMove(Square.e7, Square.f8);
	}
	
	
	@Test
	public void test_e2c4_e2b5() {
		board.executeMove(Square.e2, Square.c4);
		board.undoMove();
		
		board.executeMove(Square.e2, Square.b5);
		
		PerftResult result= pert.start(board, 1);
		
		assertEquals(1, result.getChildNode(Square.e8, Square.g8 ));
		assertEquals(1, result.getChildNode(Square.e8, Square.c8 ));
		assertEquals(1, result.getChildNode(Square.e8, Square.f8 ));
		assertEquals(1, result.getChildNode(Square.e8, Square.d8 ));
		assertEquals(1, result.getChildNode(Square.c7, Square.c6 ));
		assertEquals(1, result.getChildNode(Square.c7, Square.c5 ));
		assertEquals(1, result.getChildNode(Square.e6, Square.d5 ));
		assertEquals(1, result.getChildNode(Square.g6, Square.g5 ));
		assertEquals(1, result.getChildNode(Square.b4, Square.b3 ));
		assertEquals(1, result.getChildNode(Square.b4, Square.c3 ));
		assertEquals(1, result.getChildNode(Square.h3, Square.g2 ));
		assertEquals(1, result.getChildNode(Square.a8, Square.b8 ));
		assertEquals(1, result.getChildNode(Square.a8, Square.c8 ));
		assertEquals(1, result.getChildNode(Square.a8, Square.d8 ));
		assertEquals(1, result.getChildNode(Square.h8, Square.g8 ));
		assertEquals(1, result.getChildNode(Square.h8, Square.f8 ));
		assertEquals(1, result.getChildNode(Square.h8, Square.h7 ));
		assertEquals(1, result.getChildNode(Square.h8, Square.h6 ));
		assertEquals(1, result.getChildNode(Square.h8, Square.h5 ));
		assertEquals(1, result.getChildNode(Square.h8, Square.h4 ));
		assertEquals(1, result.getChildNode(Square.e7, Square.d6 ));
		assertEquals(1, result.getChildNode(Square.e7, Square.c5 ));
		assertEquals(1, result.getChildNode(Square.e7, Square.f8 ));
		assertEquals(1, result.getChildNode(Square.e7, Square.d8 ));
		assertEquals(1, result.getChildNode(Square.g7, Square.h6 ));
		assertEquals(1, result.getChildNode(Square.g7, Square.f8 ));
		assertEquals(1, result.getChildNode(Square.a6, Square.b7 ));
		assertEquals(1, result.getChildNode(Square.a6, Square.c8 ));
		assertEquals(1, result.getChildNode(Square.a6, Square.b5 ));
		assertEquals(1, result.getChildNode(Square.b6, Square.a4 ));
		assertEquals(1, result.getChildNode(Square.b6, Square.c8 ));
		assertEquals(1, result.getChildNode(Square.b6, Square.d5 ));
		assertEquals(1, result.getChildNode(Square.b6, Square.c4 ));
		assertEquals(1, result.getChildNode(Square.f6, Square.e4 ));
		assertEquals(1, result.getChildNode(Square.f6, Square.g8 ));
		assertEquals(1, result.getChildNode(Square.f6, Square.d5 ));
		assertEquals(1, result.getChildNode(Square.f6, Square.h7 ));
		assertEquals(1, result.getChildNode(Square.f6, Square.h5 ));
		assertEquals(1, result.getChildNode(Square.f6, Square.g4 ));
		
		
		assertEquals(39, result.getMovesCount());
		assertEquals(39, result.getTotalNodes());
	}
	
	@Test
	public void test_e2a6() {
		board.executeMove(Square.e2, Square.a6);
		
		PerftResult result= pert.start(board, 1);

		assertEquals(1, result.getChildNode(Square.f6, Square.g8));
		assertEquals(1, result.getChildNode(Square.h8, Square.g8));
		assertEquals(1, result.getChildNode(Square.f6, Square.h5));
		assertEquals(1, result.getChildNode(Square.b6, Square.d5));
		assertEquals(1, result.getChildNode(Square.g7, Square.h6));
		assertEquals(1, result.getChildNode(Square.g6, Square.g5));
		assertEquals(1, result.getChildNode(Square.e7, Square.c5));
		assertEquals(1, result.getChildNode(Square.b6, Square.c8));
		assertEquals(1, result.getChildNode(Square.h8, Square.h6));
		assertEquals(1, result.getChildNode(Square.f6, Square.g4));
		assertEquals(1, result.getChildNode(Square.e8, Square.f8));
		assertEquals(1, result.getChildNode(Square.e8, Square.g8));
		assertEquals(1, result.getChildNode(Square.b6, Square.c4));
		assertEquals(1, result.getChildNode(Square.b4, Square.c3));
		assertEquals(1, result.getChildNode(Square.a8, Square.c8));
		assertEquals(1, result.getChildNode(Square.b6, Square.a4));
		assertEquals(1, result.getChildNode(Square.f6, Square.h7));
		assertEquals(1, result.getChildNode(Square.h3, Square.g2));
		assertEquals(1, result.getChildNode(Square.c7, Square.c5));
		assertEquals(1, result.getChildNode(Square.e7, Square.f8));
		assertEquals(1, result.getChildNode(Square.a8, Square.d8));
		assertEquals(1, result.getChildNode(Square.h8, Square.h7));
		assertEquals(1, result.getChildNode(Square.c7, Square.c6));
		assertEquals(1, result.getChildNode(Square.d7, Square.d6));
		assertEquals(1, result.getChildNode(Square.e6, Square.d5));
		assertEquals(1, result.getChildNode(Square.e7, Square.d6));
		assertEquals(1, result.getChildNode(Square.b4, Square.b3));
		assertEquals(1, result.getChildNode(Square.f6, Square.e4));
		assertEquals(1, result.getChildNode(Square.h8, Square.h5));
		assertEquals(1, result.getChildNode(Square.e8, Square.d8));
		assertEquals(1, result.getChildNode(Square.a8, Square.b8));
		assertEquals(1, result.getChildNode(Square.h8, Square.f8));
		assertEquals(1, result.getChildNode(Square.f6, Square.d5));
		assertEquals(1, result.getChildNode(Square.e7, Square.d8));
		assertEquals(1, result.getChildNode(Square.g7, Square.f8));
		assertEquals(1, result.getChildNode(Square.h8, Square.h4));
		assertEquals(36, result.getMovesCount());
		assertEquals(36, result.getTotalNodes());
	}	
	
	@Test
	public void test_e1g1() {
		board.executeMove(Square.e1, Square.g1);
		PerftResult result= pert.start(board, 3);
		
		assertEquals(1899, result.getChildNode(Square.e8, Square.g8 ));
		assertEquals(1962, result.getChildNode(Square.e8, Square.c8 ));
		assertEquals(1872, result.getChildNode(Square.e8, Square.f8 ));
		assertEquals(1913, result.getChildNode(Square.e8, Square.d8 ));
		assertEquals(2080, result.getChildNode(Square.c7, Square.c6 ));
		assertEquals(1984, result.getChildNode(Square.c7, Square.c5 ));
		assertEquals(2005, result.getChildNode(Square.d7, Square.d6 ));
		assertEquals(2086, result.getChildNode(Square.e6, Square.d5 ));
		assertEquals(1995, result.getChildNode(Square.g6, Square.g5 ));
		assertEquals(2174, result.getChildNode(Square.b4, Square.b3 ));
		assertEquals(2123, result.getChildNode(Square.b4, Square.c3 ));
		assertEquals(2248, result.getChildNode(Square.h3, Square.g2 ));
		assertEquals(2089, result.getChildNode(Square.a8, Square.b8 ));
		assertEquals(1946, result.getChildNode(Square.a8, Square.c8 ));
		assertEquals(1948, result.getChildNode(Square.a8, Square.d8 ));
		assertEquals(1802, result.getChildNode(Square.h8, Square.g8 ));
		assertEquals(1708, result.getChildNode(Square.h8, Square.f8 ));
		assertEquals(1897, result.getChildNode(Square.h8, Square.h7 ));
		assertEquals(1896, result.getChildNode(Square.h8, Square.h6 ));
		assertEquals(2040, result.getChildNode(Square.h8, Square.h5 ));
		assertEquals(2078, result.getChildNode(Square.h8, Square.h4 ));
		assertEquals(2109, result.getChildNode(Square.e7, Square.d6 ));
		assertEquals(2412, result.getChildNode(Square.e7, Square.c5 ));
		assertEquals(1889, result.getChildNode(Square.e7, Square.f8 ));
		assertEquals(1894, result.getChildNode(Square.e7, Square.d8 ));
		assertEquals(2072, result.getChildNode(Square.g7, Square.h6 ));
		assertEquals(1849, result.getChildNode(Square.g7, Square.f8 ));
		assertEquals(2056, result.getChildNode(Square.a6, Square.b7 ));
		assertEquals(1770, result.getChildNode(Square.a6, Square.c8 ));
		assertEquals(2091, result.getChildNode(Square.a6, Square.b5 ));
		assertEquals(2049, result.getChildNode(Square.a6, Square.c4 ));
		assertEquals(2038, result.getChildNode(Square.a6, Square.d3 ));
		assertEquals(2057, result.getChildNode(Square.a6, Square.e2 ));
		assertEquals(1989, result.getChildNode(Square.b6, Square.a4 ));
		assertEquals(1753, result.getChildNode(Square.b6, Square.c8 ));
		assertEquals(1937, result.getChildNode(Square.b6, Square.d5 ));
		assertEquals(2003, result.getChildNode(Square.b6, Square.c4 ));
		assertEquals(2566, result.getChildNode(Square.f6, Square.e4 ));
		assertEquals(2049, result.getChildNode(Square.f6, Square.g8 ));
		assertEquals(2185, result.getChildNode(Square.f6, Square.d5 ));
		assertEquals(2048, result.getChildNode(Square.f6, Square.h7 ));
		assertEquals(2142, result.getChildNode(Square.f6, Square.h5 ));
		assertEquals(2272, result.getChildNode(Square.f6, Square.g4 ));
		
		assertEquals(43, result.getMovesCount());
		assertEquals(86975, result.getTotalNodes());	
	}
	
	@Test
	public void test_e1g1_h3g2() {
		board.executeMove(Square.e1, Square.g1);
		board.executeMove(Square.h3, Square.g2);
		
		PerftResult result= pert.start(board, 2);
		
		assertEquals(44, result.getChildNode(Square.g1, Square.g2));
		assertEquals(46, result.getChildNode(Square.d5, Square.d6));
		assertEquals(51, result.getChildNode(Square.d5, Square.e6));
		assertEquals(49, result.getChildNode(Square.a2, Square.a3 ));
		assertEquals(49, result.getChildNode(Square.a2, Square.a4 ));
		assertEquals(47, result.getChildNode(Square.b2, Square.b3 ));
		assertEquals(47, result.getChildNode(Square.h2, Square.h3 ));
		assertEquals(46, result.getChildNode(Square.h2, Square.h4 ));
		assertEquals(48, result.getChildNode(Square.e5, Square.d3 ));
		assertEquals(49, result.getChildNode(Square.e5, Square.f7 ));
		assertEquals(47, result.getChildNode(Square.e5, Square.c4 ));
		assertEquals(47, result.getChildNode(Square.e5, Square.g6 ));
		assertEquals(49, result.getChildNode(Square.e5, Square.g4 ));
		assertEquals(46, result.getChildNode(Square.e5, Square.c6 ));
		assertEquals(50, result.getChildNode(Square.e5, Square.d7 ));
		assertEquals(47, result.getChildNode(Square.c3, Square.b1 ));
		assertEquals(47, result.getChildNode(Square.c3, Square.a4 ));
		assertEquals(47, result.getChildNode(Square.c3, Square.d1 ));
		assertEquals(44, result.getChildNode(Square.c3, Square.b5 ));
		assertEquals(48, result.getChildNode(Square.f3, Square.g3 ));
		assertEquals(47, result.getChildNode(Square.f3, Square.h3 ));
		assertEquals(48, result.getChildNode(Square.f3, Square.e3 ));
		assertEquals(47, result.getChildNode(Square.f3, Square.d3 ));
		assertEquals(44, result.getChildNode(Square.f3, Square.g2 ));
		assertEquals(48, result.getChildNode(Square.f3, Square.g4 ));
		assertEquals(46, result.getChildNode(Square.f3, Square.h5 ));
		assertEquals(48, result.getChildNode(Square.f3, Square.f4 ));
		assertEquals(50, result.getChildNode(Square.f3, Square.f5 ));
		assertEquals(44, result.getChildNode(Square.f3, Square.f6 ));
		assertEquals(48, result.getChildNode(Square.d2, Square.c1 ));
		assertEquals(48, result.getChildNode(Square.d2, Square.e3 ));
		assertEquals(48, result.getChildNode(Square.d2, Square.f4 ));
		assertEquals(47, result.getChildNode(Square.d2, Square.g5 ));
		assertEquals(44, result.getChildNode(Square.d2, Square.h6 ));
		assertEquals(48, result.getChildNode(Square.d2, Square.e1 ));
		assertEquals(49, result.getChildNode(Square.e2, Square.d1 ));
		assertEquals(47, result.getChildNode(Square.e2, Square.d3 ));
		assertEquals(46, result.getChildNode(Square.e2, Square.c4 ));
		assertEquals(44, result.getChildNode(Square.e2, Square.b5 ));
		assertEquals(41, result.getChildNode(Square.e2, Square.a6 ));
		assertEquals(48, result.getChildNode(Square.a1, Square.b1 ));
		assertEquals(48, result.getChildNode(Square.a1, Square.c1 ));
		assertEquals(48, result.getChildNode(Square.a1, Square.d1 ));
		assertEquals(48, result.getChildNode(Square.a1, Square.e1 ));
		assertEquals(44, result.getChildNode(Square.f1, Square.e1 ));
		assertEquals(44, result.getChildNode(Square.f1, Square.d1 ));
		assertEquals(44, result.getChildNode(Square.f1, Square.c1 ));
		assertEquals(44, result.getChildNode(Square.f1, Square.b1 ));

		
		assertEquals(48, result.getMovesCount());
		assertEquals(2248, result.getTotalNodes());
	}
	
	
	// FALTA EL MOVIMIENTO DE PROMOCION DE PEON
	@Test
	public void test_e1g1_h3g2_d5d6() {
		board.executeMove(Square.e1, Square.g1);
		board.executeMove(Square.h3, Square.g2);
		board.executeMove(Square.d5, Square.d6);
		
		PerftResult result= pert.start(board, 1);
		
		assertEquals(1, result.getChildNode(Square.e8, Square.g8 ));
		assertEquals(1, result.getChildNode(Square.e8, Square.c8 ));
		assertEquals(1, result.getChildNode(Square.e8, Square.f8 ));
		assertEquals(1, result.getChildNode(Square.e8, Square.d8 ));
		assertEquals(1, result.getChildNode(Square.c7, Square.c6 ));
		assertEquals(1, result.getChildNode(Square.c7, Square.c5 ));
		assertEquals(1, result.getChildNode(Square.c7, Square.d6 ));
		assertEquals(1, result.getChildNode(Square.g6, Square.g5 ));
		assertEquals(1, result.getChildNode(Square.b4, Square.b3 ));
		assertEquals(1, result.getChildNode(Square.b4, Square.c3 ));
		assertEquals(1, result.getChildNode(Square.g2, Square.f1 ));
		assertEquals(1, result.getChildNode(Square.a8, Square.b8 ));
		assertEquals(1, result.getChildNode(Square.a8, Square.c8 ));
		assertEquals(1, result.getChildNode(Square.a8, Square.d8 ));
		assertEquals(1, result.getChildNode(Square.h8, Square.g8 ));
		assertEquals(1, result.getChildNode(Square.h8, Square.f8 ));
		assertEquals(1, result.getChildNode(Square.h8, Square.h7 ));
		assertEquals(1, result.getChildNode(Square.h8, Square.h6 ));
		assertEquals(1, result.getChildNode(Square.h8, Square.h5 ));
		assertEquals(1, result.getChildNode(Square.h8, Square.h4 ));
		assertEquals(1, result.getChildNode(Square.h8, Square.h3 ));
		assertEquals(1, result.getChildNode(Square.h8, Square.h2 ));
		assertEquals(1, result.getChildNode(Square.e7, Square.d6 ));
		assertEquals(1, result.getChildNode(Square.e7, Square.f8 ));
		assertEquals(1, result.getChildNode(Square.e7, Square.d8 ));
		assertEquals(1, result.getChildNode(Square.g7, Square.h6 ));
		assertEquals(1, result.getChildNode(Square.g7, Square.f8 ));
		assertEquals(1, result.getChildNode(Square.a6, Square.b7 ));
		assertEquals(1, result.getChildNode(Square.a6, Square.c8 ));
		assertEquals(1, result.getChildNode(Square.a6, Square.b5 ));
		assertEquals(1, result.getChildNode(Square.a6, Square.c4 ));
		assertEquals(1, result.getChildNode(Square.a6, Square.d3 ));
		assertEquals(1, result.getChildNode(Square.a6, Square.e2 ));
		assertEquals(1, result.getChildNode(Square.b6, Square.a4 ));
		assertEquals(1, result.getChildNode(Square.b6, Square.c8 ));
		assertEquals(1, result.getChildNode(Square.b6, Square.d5 ));
		assertEquals(1, result.getChildNode(Square.b6, Square.c4 ));
		assertEquals(1, result.getChildNode(Square.f6, Square.e4 ));
		assertEquals(1, result.getChildNode(Square.f6, Square.g8 ));
		assertEquals(1, result.getChildNode(Square.f6, Square.d5 ));
		assertEquals(1, result.getChildNode(Square.f6, Square.h7 ));
		assertEquals(1, result.getChildNode(Square.f6, Square.h5 ));
		assertEquals(1, result.getChildNode(Square.f6, Square.g4 ));

		
		assertEquals(46, result.getMovesCount());
		assertEquals(46, result.getTotalNodes());
	}

	//PerftBrute           (movimientos herencia): 18s 14s 12s 11s 10s
	//PerftWithMap (Zobrist + movimientos bridge):  7s
	//PerftWithMapIterateDeeping: 		            6s
	@Test
	public void test_divide5() {
		PerftResult result= pert.start(board, 5);
		
		assertEquals(4119629, result.getChildNode(Square.e1, Square.g1));
		assertEquals(3551583, result.getChildNode(Square.e1, Square.c1));
		assertEquals(3377351, result.getChildNode(Square.e1, Square.f1));
		assertEquals(3559113, result.getChildNode(Square.e1, Square.d1));
		assertEquals(3835265, result.getChildNode(Square.d5, Square.d6));
		assertEquals(4727437, result.getChildNode(Square.d5, Square.e6));
		assertEquals(4627439, result.getChildNode(Square.a2, Square.a3));
		assertEquals(4387586, result.getChildNode(Square.a2, Square.a4));
		assertEquals(3768824, result.getChildNode(Square.b2, Square.b3));
		assertEquals(3472039, result.getChildNode(Square.g2, Square.g3));
		assertEquals(3338154, result.getChildNode(Square.g2, Square.g4));
		assertEquals(3819456, result.getChildNode(Square.g2, Square.h3));
		assertEquals(3288812, result.getChildNode(Square.e5, Square.d3));
		assertEquals(4164923, result.getChildNode(Square.e5, Square.f7));
		assertEquals(3494887, result.getChildNode(Square.e5, Square.c4));
		assertEquals(3949417, result.getChildNode(Square.e5, Square.g6));
		assertEquals(3415992, result.getChildNode(Square.e5, Square.g4));
		assertEquals(4083458, result.getChildNode(Square.e5, Square.c6));
		assertEquals(4404043, result.getChildNode(Square.e5, Square.d7));
		assertEquals(3996171, result.getChildNode(Square.c3, Square.b1));
		assertEquals(4628497, result.getChildNode(Square.c3, Square.a4));
		assertEquals(3995761, result.getChildNode(Square.c3, Square.d1));
		assertEquals(4317482, result.getChildNode(Square.c3, Square.b5));
		assertEquals(4669768, result.getChildNode(Square.f3, Square.g3));
		assertEquals(5067173, result.getChildNode(Square.f3, Square.h3));
		assertEquals(4477772, result.getChildNode(Square.f3, Square.e3));
		assertEquals(3949570, result.getChildNode(Square.f3, Square.d3));
		assertEquals(4514010, result.getChildNode(Square.f3, Square.g4));
		assertEquals(4743335, result.getChildNode(Square.f3, Square.h5));
		assertEquals(4327936, result.getChildNode(Square.f3, Square.f4));
		assertEquals(5271134, result.getChildNode(Square.f3, Square.f5));
		assertEquals(3975992, result.getChildNode(Square.f3, Square.f6));
		assertEquals(3793390, result.getChildNode(Square.d2, Square.c1));
		assertEquals(4407041, result.getChildNode(Square.d2, Square.e3));
		assertEquals(3941257, result.getChildNode(Square.d2, Square.f4));
		assertEquals(4370915, result.getChildNode(Square.d2, Square.g5));
		assertEquals(3967365, result.getChildNode(Square.d2, Square.h6));
		assertEquals(3074219, result.getChildNode(Square.e2, Square.d1));
		assertEquals(4095479, result.getChildNode(Square.e2, Square.f1));
		assertEquals(4066966, result.getChildNode(Square.e2, Square.d3));
		assertEquals(4182989, result.getChildNode(Square.e2, Square.c4));
		assertEquals(4032348, result.getChildNode(Square.e2, Square.b5));
		assertEquals(3553501, result.getChildNode(Square.e2, Square.a6));
		assertEquals(3827454, result.getChildNode(Square.a1, Square.b1));
		assertEquals(3814203, result.getChildNode(Square.a1, Square.c1));
		assertEquals(3568344, result.getChildNode(Square.a1, Square.d1));
		assertEquals(3989454, result.getChildNode(Square.h1, Square.g1));
		assertEquals(3685756, result.getChildNode(Square.h1, Square.f1));
		
		assertEquals(48, result.getMovesCount());
		assertEquals(193690690, result.getTotalNodes());
	}
	
	//Test 842s 772s 701s 673s 554s 522s 506s 490s 465s 455s
	@Test
	@Disabled
	public void test_divide6() {
		PerftResult result= pert.start(board, 6);
		
		assertEquals(197413067, result.getChildNode(Square.a2, Square.a3));
		assertEquals(153953689, result.getChildNode(Square.b2, Square.b3));
		assertEquals(141076301, result.getChildNode(Square.g2, Square.g3));
		assertEquals(151133066, result.getChildNode(Square.d5, Square.d6));
		assertEquals(183872225, result.getChildNode(Square.a2, Square.a4));
		assertEquals(135208177, result.getChildNode(Square.g2, Square.g4));
		assertEquals(158328615, result.getChildNode(Square.g2, Square.h3));
		assertEquals(203255191, result.getChildNode(Square.d5, Square.e6));
		assertEquals(140737072, result.getChildNode(Square.e5, Square.d3));
		assertEquals(145182844, result.getChildNode(Square.e5, Square.c4));
		assertEquals(144264874, result.getChildNode(Square.e5, Square.g4));
		assertEquals(169836097, result.getChildNode(Square.e5, Square.c6));
		assertEquals(165477768, result.getChildNode(Square.e5, Square.g6));
		assertEquals(193856446, result.getChildNode(Square.e5, Square.d7));
		assertEquals(176070755, result.getChildNode(Square.e5, Square.f7));
		assertEquals(165673862, result.getChildNode(Square.c3, Square.b1));
		assertEquals(165415976, result.getChildNode(Square.c3, Square.d1));
		assertEquals(191260040, result.getChildNode(Square.c3, Square.a4));
		assertEquals(166970874, result.getChildNode(Square.c3, Square.b5));
		assertEquals(158801466, result.getChildNode(Square.d2, Square.c1));
		assertEquals(184114087, result.getChildNode(Square.d2, Square.e3));
		assertEquals(165805784, result.getChildNode(Square.d2, Square.f4));
		assertEquals(177883051, result.getChildNode(Square.d2, Square.g5));
		assertEquals(161319567, result.getChildNode(Square.d2, Square.h6));
		assertEquals(131348645, result.getChildNode(Square.e2, Square.d1));
		assertEquals(174218453, result.getChildNode(Square.e2, Square.f1));
		assertEquals(167737155, result.getChildNode(Square.e2, Square.d3));
		assertEquals(170094798, result.getChildNode(Square.e2, Square.c4));
		assertEquals(158033152, result.getChildNode(Square.e2, Square.b5));
		assertEquals(130642863, result.getChildNode(Square.e2, Square.a6));
		assertEquals(160413321, result.getChildNode(Square.a1, Square.b1));
		assertEquals(159720218, result.getChildNode(Square.a1, Square.c1));
		assertEquals(149265033, result.getChildNode(Square.a1, Square.d1));
		assertEquals(154273720, result.getChildNode(Square.h1, Square.f1));
		assertEquals(166086672, result.getChildNode(Square.h1, Square.g1));
		assertEquals(164583144, result.getChildNode(Square.f3, Square.d3));
		assertEquals(189120807, result.getChildNode(Square.f3, Square.e3));
		assertEquals(198078522, result.getChildNode(Square.f3, Square.g3));
		assertEquals(210100865, result.getChildNode(Square.f3, Square.h3));
		assertEquals(181938761, result.getChildNode(Square.f3, Square.f4));
		assertEquals(189789456, result.getChildNode(Square.f3, Square.g4));
		assertEquals(226135507, result.getChildNode(Square.f3, Square.f5));
		assertEquals(197839051, result.getChildNode(Square.f3, Square.h5));
		assertEquals(146338070, result.getChildNode(Square.f3, Square.f6));
		assertEquals(148612404, result.getChildNode(Square.e1, Square.d1));
		assertEquals(139601450, result.getChildNode(Square.e1, Square.f1));
		assertEquals(172063416, result.getChildNode(Square.e1, Square.g1));
		assertEquals(148701308, result.getChildNode(Square.e1, Square.c1));
		
		assertEquals(48, result.getMovesCount());
		assertEquals(8031647685L, result.getTotalNodes());
	}

	//TODO: Falta detallar movimientos
	@Test
	public void test_a1b1() {
		board.executeMove(Square.a1, Square.b1);
		
		PerftResult result= pert.start(board, 4);

		assertEquals(87817, result.getChildNode(Square.a6, Square.c4));
		assertEquals(83974, result.getChildNode(Square.e7, Square.f8));
		assertEquals(92224, result.getChildNode(Square.a6, Square.b7));
		assertEquals(91311, result.getChildNode(Square.a6, Square.b5));
		assertEquals(92015, result.getChildNode(Square.e7, Square.d6));
		assertEquals(74818, result.getChildNode(Square.a6, Square.e2));
		assertEquals(94025, result.getChildNode(Square.c7, Square.c6));
		assertEquals(75881, result.getChildNode(Square.h8, Square.f8));
		assertEquals(91970, result.getChildNode(Square.f6, Square.g8));
		assertEquals(87007, result.getChildNode(Square.d7, Square.d6));
		assertEquals(95779, result.getChildNode(Square.f6, Square.h5));
		assertEquals(84444, result.getChildNode(Square.e8, Square.g8));
		assertEquals(95037, result.getChildNode(Square.b4, Square.c3));
		assertEquals(86620, result.getChildNode(Square.a8, Square.d8));
		assertEquals(98333, result.getChildNode(Square.b4, Square.b3));
		assertEquals(94098, result.getChildNode(Square.h3, Square.g2));
		assertEquals(92922, result.getChildNode(Square.e6, Square.d5));
		assertEquals(82235, result.getChildNode(Square.g7, Square.f8));
		assertEquals(84920, result.getChildNode(Square.e8, Square.d8));
		assertEquals(119483, result.getChildNode(Square.f6, Square.e4));
		assertEquals(79352, result.getChildNode(Square.a6, Square.c8));
		assertEquals(85466, result.getChildNode(Square.b6, Square.c4));
		assertEquals(78031, result.getChildNode(Square.b6, Square.c8));
		assertEquals(87981, result.getChildNode(Square.c7, Square.c5));
		assertEquals(86863, result.getChildNode(Square.a6, Square.d3));
		assertEquals(86795, result.getChildNode(Square.b6, Square.a4));
		assertEquals(86531, result.getChildNode(Square.a8, Square.c8));
		assertEquals(84232, result.getChildNode(Square.h8, Square.h7));
		assertEquals(87068, result.getChildNode(Square.e8, Square.c8));
		assertEquals(91837, result.getChildNode(Square.f6, Square.h7));
		assertEquals(92948, result.getChildNode(Square.a8, Square.b8));
		assertEquals(84150, result.getChildNode(Square.h8, Square.h6));
		assertEquals(83189, result.getChildNode(Square.e8, Square.f8));
		assertEquals(104208, result.getChildNode(Square.e7, Square.c5));
		assertEquals(97660, result.getChildNode(Square.f6, Square.g4));
		assertEquals(80075, result.getChildNode(Square.h8, Square.g8));
		assertEquals(97648, result.getChildNode(Square.f6, Square.d5));
		assertEquals(84275, result.getChildNode(Square.e7, Square.d8));
		assertEquals(90283, result.getChildNode(Square.h8, Square.h5));
		assertEquals(90002, result.getChildNode(Square.g7, Square.h6));
		assertEquals(91599, result.getChildNode(Square.h8, Square.h4));
		assertEquals(85713, result.getChildNode(Square.b6, Square.d5));
		assertEquals(86635, result.getChildNode(Square.g6, Square.g5));

		assertFalse(board.getChessPosition().isCastlingWhiteQueenAllowed());
		
		assertEquals(43, result.getMovesCount());
		assertEquals(3827454, result.getTotalNodes());
	}
	
	@Test
	public void test_e1f1() {
		board.executeMove(Square.e1, Square.f1);
		
		PerftResult result= pert.start(board, 4);

		assertEquals(77387, result.getChildNode(Square.e8, Square.g8));
		assertEquals(79777, result.getChildNode(Square.e8, Square.c8));
		assertEquals(76235, result.getChildNode(Square.e8, Square.f8));
		assertEquals(77879, result.getChildNode(Square.e8, Square.d8));
		assertEquals(86454, result.getChildNode(Square.c7, Square.c6));
		assertEquals(80761, result.getChildNode(Square.c7, Square.c5));
		assertEquals(79885, result.getChildNode(Square.d7, Square.d6));
		assertEquals(85346, result.getChildNode(Square.e6, Square.d5));
		assertEquals(79610, result.getChildNode(Square.g6, Square.g5));
		assertEquals(90977, result.getChildNode(Square.b4, Square.b3));
		assertEquals(84682, result.getChildNode(Square.b4, Square.c3));
		assertEquals(8536, result.getChildNode(Square.h3, Square.g2));
		assertEquals(85453, result.getChildNode(Square.a8, Square.b8));
		assertEquals(79310, result.getChildNode(Square.a8, Square.c8));
		assertEquals(79449, result.getChildNode(Square.a8, Square.d8));
		assertEquals(73227, result.getChildNode(Square.h8, Square.g8));
		assertEquals(69251, result.getChildNode(Square.h8, Square.f8));
		assertEquals(77173, result.getChildNode(Square.h8, Square.h7));
		assertEquals(77098, result.getChildNode(Square.h8, Square.h6));
		assertEquals(82926, result.getChildNode(Square.h8, Square.h5));
		assertEquals(84587, result.getChildNode(Square.h8, Square.h4));
		assertEquals(84693, result.getChildNode(Square.e7, Square.d6));
		assertEquals(96672, result.getChildNode(Square.e7, Square.c5));
		assertEquals(76932, result.getChildNode(Square.e7, Square.f8));
		assertEquals(77219, result.getChildNode(Square.e7, Square.d8));
		assertEquals(83986, result.getChildNode(Square.g7, Square.h6));
		assertEquals(75283, result.getChildNode(Square.g7, Square.f8));
		assertEquals(89850, result.getChildNode(Square.a6, Square.b7));
		assertEquals(77062, result.getChildNode(Square.a6, Square.c8));
		assertEquals(83931, result.getChildNode(Square.a6, Square.b5));
		assertEquals(80494, result.getChildNode(Square.a6, Square.c4));
		assertEquals(79395, result.getChildNode(Square.a6, Square.d3));
		assertEquals(9332, result.getChildNode(Square.a6, Square.e2));
		assertEquals(79573, result.getChildNode(Square.b6, Square.a4));
		assertEquals(71184, result.getChildNode(Square.b6, Square.c8));
		assertEquals(77103, result.getChildNode(Square.b6, Square.d5));
		assertEquals(80085, result.getChildNode(Square.b6, Square.c4));
		assertEquals(107224, result.getChildNode(Square.f6, Square.e4));
		assertEquals(84536, result.getChildNode(Square.f6, Square.g8));
		assertEquals(88468, result.getChildNode(Square.f6, Square.d5));
		assertEquals(84406, result.getChildNode(Square.f6, Square.h7));
		assertEquals(86559, result.getChildNode(Square.f6, Square.h5));
		assertEquals(87361, result.getChildNode(Square.f6, Square.g4));
		
		assertEquals(43, result.getMovesCount());
		assertEquals(3377351, result.getTotalNodes());	
	}
	
	@Test
	public void test_e1f1_e8g8() {
		board.executeMove(Square.e1, Square.f1);
		board.executeMove(Square.e8, Square.g8);
		
		PerftResult result= pert.start(board, 3);
		
		assertEquals(1833, result.getChildNode(Square.f1, Square.g1));
		assertEquals(1832, result.getChildNode(Square.f1, Square.e1));
		assertEquals(1659, result.getChildNode(Square.d5, Square.d6));
		assertEquals(1894, result.getChildNode(Square.d5, Square.e6));
		assertEquals(1840, result.getChildNode(Square.a2, Square.a3));
		assertEquals(1808, result.getChildNode(Square.a2, Square.a4));
		assertEquals(1639, result.getChildNode(Square.b2, Square.b3));
		assertEquals(1602, result.getChildNode(Square.g2, Square.g3));
		assertEquals(1567, result.getChildNode(Square.g2, Square.g4));
		assertEquals(1681, result.getChildNode(Square.g2, Square.h3));
		assertEquals(1600, result.getChildNode(Square.e5, Square.d3));
		assertEquals(1828, result.getChildNode(Square.e5, Square.f7));
		assertEquals(1629, result.getChildNode(Square.e5, Square.c4));
		assertEquals(1712, result.getChildNode(Square.e5, Square.g6));
		assertEquals(1566, result.getChildNode(Square.e5, Square.g4));
		assertEquals(1784, result.getChildNode(Square.e5, Square.c6));
		assertEquals(1836, result.getChildNode(Square.e5, Square.d7));
		assertEquals(1702, result.getChildNode(Square.c3, Square.b1));
		assertEquals(1848, result.getChildNode(Square.c3, Square.a4));
		assertEquals(1773, result.getChildNode(Square.c3, Square.d1));
		assertEquals(1853, result.getChildNode(Square.c3, Square.b5));
		assertEquals(1758, result.getChildNode(Square.f3, Square.g3));
		assertEquals(1801, result.getChildNode(Square.f3, Square.h3));
		assertEquals(1725, result.getChildNode(Square.f3, Square.e3));
		assertEquals(1739, result.getChildNode(Square.f3, Square.d3));
		assertEquals(1785, result.getChildNode(Square.f3, Square.g4));
		assertEquals(1797, result.getChildNode(Square.f3, Square.h5));
		assertEquals(1688, result.getChildNode(Square.f3, Square.f4));
		assertEquals(1920, result.getChildNode(Square.f3, Square.f5));
		assertEquals(1665, result.getChildNode(Square.f3, Square.f6));
		assertEquals(1567, result.getChildNode(Square.d2, Square.c1));
		assertEquals(1720, result.getChildNode(Square.d2, Square.e3));
		assertEquals(1599, result.getChildNode(Square.d2, Square.f4));
		assertEquals(1718, result.getChildNode(Square.d2, Square.g5));
		assertEquals(1711, result.getChildNode(Square.d2, Square.h6));
		assertEquals(1453, result.getChildNode(Square.d2, Square.e1));
		assertEquals(1749, result.getChildNode(Square.e2, Square.d3));
		assertEquals(1742, result.getChildNode(Square.e2, Square.c4));
		assertEquals(1705, result.getChildNode(Square.e2, Square.b5));
		assertEquals(1690, result.getChildNode(Square.e2, Square.a6));
		assertEquals(1683, result.getChildNode(Square.a1, Square.b1));
		assertEquals(1682, result.getChildNode(Square.a1, Square.c1));
		assertEquals(1680, result.getChildNode(Square.a1, Square.d1));
		assertEquals(1643, result.getChildNode(Square.a1, Square.e1));
		assertEquals(1681, result.getChildNode(Square.h1, Square.g1));
		
		assertEquals(45, result.getMovesCount());
		assertEquals(77387, result.getTotalNodes());	
	}
	
	@Test
	public void test_e1f1_e8g8_f1e1() {
		board.executeMove(Square.e1, Square.f1);
		board.executeMove(Square.e8, Square.g8);
		board.executeMove(Square.f1, Square.e1);
		
		PerftResult result= pert.start(board, 2);
		
		assertEquals(46, result.getChildNode(Square.g8, Square.h8));
		assertEquals(46, result.getChildNode(Square.g8, Square.h7));
		assertEquals(47, result.getChildNode(Square.c7, Square.c6));
		assertEquals(47, result.getChildNode(Square.c7, Square.c5));
		assertEquals(45, result.getChildNode(Square.d7, Square.d6));
		assertEquals(46, result.getChildNode(Square.e6, Square.d5));
		assertEquals(45, result.getChildNode(Square.g6, Square.g5));
		assertEquals(47, result.getChildNode(Square.b4, Square.b3));
		assertEquals(46, result.getChildNode(Square.b4, Square.c3));
		assertEquals(45, result.getChildNode(Square.h3, Square.g2));
		assertEquals(46, result.getChildNode(Square.a8, Square.b8));
		assertEquals(46, result.getChildNode(Square.a8, Square.c8));
		assertEquals(46, result.getChildNode(Square.a8, Square.d8));
		assertEquals(46, result.getChildNode(Square.a8, Square.e8));
		assertEquals(46, result.getChildNode(Square.f8, Square.e8));
		assertEquals(46, result.getChildNode(Square.f8, Square.d8));
		assertEquals(46, result.getChildNode(Square.f8, Square.c8));
		assertEquals(46, result.getChildNode(Square.f8, Square.b8));
		assertEquals(45, result.getChildNode(Square.e7, Square.d6));
		assertEquals(46, result.getChildNode(Square.e7, Square.c5));
		assertEquals(46, result.getChildNode(Square.e7, Square.e8));
		assertEquals(46, result.getChildNode(Square.e7, Square.d8));
		assertEquals(46, result.getChildNode(Square.g7, Square.h8));
		assertEquals(46, result.getChildNode(Square.g7, Square.h6));
		assertEquals(46, result.getChildNode(Square.a6, Square.b7));
		assertEquals(46, result.getChildNode(Square.a6, Square.c8));
		assertEquals(45, result.getChildNode(Square.a6, Square.b5));
		assertEquals(44, result.getChildNode(Square.a6, Square.c4));
		assertEquals(44, result.getChildNode(Square.a6, Square.d3));
		assertEquals(41, result.getChildNode(Square.a6, Square.e2));
		assertEquals(45, result.getChildNode(Square.b6, Square.a4));
		assertEquals(46, result.getChildNode(Square.b6, Square.c8));
		assertEquals(46, result.getChildNode(Square.b6, Square.d5));
		assertEquals(44, result.getChildNode(Square.b6, Square.c4));
		assertEquals(49, result.getChildNode(Square.f6, Square.e4));
		assertEquals(47, result.getChildNode(Square.f6, Square.d5));
		assertEquals(47, result.getChildNode(Square.f6, Square.h7));
		assertEquals(47, result.getChildNode(Square.f6, Square.h5));
		assertEquals(45, result.getChildNode(Square.f6, Square.g4));
		assertEquals(47, result.getChildNode(Square.f6, Square.e8));
		
		assertEquals(40, result.getMovesCount());
		assertEquals(1832, result.getTotalNodes());
	}
	
	@Test
	public void test_e1f1_e8g8_f1e1_g8h8() {
		board.executeMove(Square.e1, Square.f1);
		board.executeMove(Square.e8, Square.g8);
		board.executeMove(Square.f1, Square.e1);
		board.executeMove(Square.g8, Square.h8);
		
		PerftResult result= pert.start(board, 1);
		
		assertEquals(1, result.getChildNode(Square.e1, Square.f1));
		assertEquals(1, result.getChildNode(Square.e1, Square.d1));
		assertEquals(1, result.getChildNode(Square.d5, Square.d6));
		assertEquals(1, result.getChildNode(Square.d5, Square.e6));
		assertEquals(1, result.getChildNode(Square.a2, Square.a3));
		assertEquals(1, result.getChildNode(Square.a2, Square.a4));
		assertEquals(1, result.getChildNode(Square.b2, Square.b3));
		assertEquals(1, result.getChildNode(Square.g2, Square.g3));
		assertEquals(1, result.getChildNode(Square.g2, Square.g4));
		assertEquals(1, result.getChildNode(Square.g2, Square.h3));
		assertEquals(1, result.getChildNode(Square.e5, Square.d3));
		assertEquals(1, result.getChildNode(Square.e5, Square.f7));
		assertEquals(1, result.getChildNode(Square.e5, Square.c4));
		assertEquals(1, result.getChildNode(Square.e5, Square.g6));
		assertEquals(1, result.getChildNode(Square.e5, Square.g4));
		assertEquals(1, result.getChildNode(Square.e5, Square.c6));
		assertEquals(1, result.getChildNode(Square.e5, Square.d7));
		assertEquals(1, result.getChildNode(Square.c3, Square.b1));
		assertEquals(1, result.getChildNode(Square.c3, Square.a4));
		assertEquals(1, result.getChildNode(Square.c3, Square.d1));
		assertEquals(1, result.getChildNode(Square.c3, Square.b5));
		assertEquals(1, result.getChildNode(Square.f3, Square.g3));
		assertEquals(1, result.getChildNode(Square.f3, Square.h3));
		assertEquals(1, result.getChildNode(Square.f3, Square.e3));
		assertEquals(1, result.getChildNode(Square.f3, Square.d3));
		assertEquals(1, result.getChildNode(Square.f3, Square.g4));
		assertEquals(1, result.getChildNode(Square.f3, Square.h5));
		assertEquals(1, result.getChildNode(Square.f3, Square.f4));
		assertEquals(1, result.getChildNode(Square.f3, Square.f5));
		assertEquals(1, result.getChildNode(Square.f3, Square.f6));
		assertEquals(1, result.getChildNode(Square.d2, Square.c1));
		assertEquals(1, result.getChildNode(Square.d2, Square.e3));
		assertEquals(1, result.getChildNode(Square.d2, Square.f4));
		assertEquals(1, result.getChildNode(Square.d2, Square.g5));
		assertEquals(1, result.getChildNode(Square.d2, Square.h6));
		assertEquals(1, result.getChildNode(Square.e2, Square.d1));
		assertEquals(1, result.getChildNode(Square.e2, Square.f1));
		assertEquals(1, result.getChildNode(Square.e2, Square.d3));
		assertEquals(1, result.getChildNode(Square.e2, Square.c4));
		assertEquals(1, result.getChildNode(Square.e2, Square.b5));
		assertEquals(1, result.getChildNode(Square.e2, Square.a6));
		assertEquals(1, result.getChildNode(Square.a1, Square.b1));
		assertEquals(1, result.getChildNode(Square.a1, Square.c1));
		assertEquals(1, result.getChildNode(Square.a1, Square.d1));
		assertEquals(1, result.getChildNode(Square.h1, Square.g1));
		assertEquals(1, result.getChildNode(Square.h1, Square.f1));
		
		assertEquals(46, result.getMovesCount());
		assertEquals(46, result.getTotalNodes());
	}
	
	
	@Test
	public void test_c3b1() {
		board.executeMove(Square.c3, Square.b1);
		
		PerftResult result = pert.start(board, 4);

		assertEquals(90953, result.getChildNode(Square.e8, Square.g8));
		assertEquals(94015, result.getChildNode(Square.e8, Square.c8 ));
		assertEquals(89711, result.getChildNode(Square.e8, Square.f8 ));
		assertEquals(91763, result.getChildNode(Square.e8, Square.d8 ));
		assertEquals(101162, result.getChildNode(Square.c7, Square.c6 ));
		assertEquals(94493, result.getChildNode(Square.c7, Square.c5 ));
		assertEquals(94170, result.getChildNode(Square.d7, Square.d6 ));
		assertEquals(96273, result.getChildNode(Square.e6, Square.d5 ));
		assertEquals(93766, result.getChildNode(Square.g6, Square.g5 ));
		assertEquals(107622, result.getChildNode(Square.b4, Square.b3 ));
		assertEquals(101809, result.getChildNode(Square.h3, Square.g2));
		assertEquals(100339, result.getChildNode(Square.a8, Square.b8));
		assertEquals(93254, result.getChildNode(Square.a8, Square.c8));
		assertEquals(93350, result.getChildNode(Square.a8, Square.d8));
		assertEquals(86127, result.getChildNode(Square.h8, Square.g8 ));
		assertEquals(81495, result.getChildNode(Square.h8, Square.f8 ));
		assertEquals(90731, result.getChildNode(Square.h8, Square.h7 ));
		assertEquals(90654, result.getChildNode(Square.h8, Square.h6 ));
		assertEquals(97438, result.getChildNode(Square.h8, Square.h5 ));
		assertEquals(98883, result.getChildNode(Square.h8, Square.h4 ));
		assertEquals(99474, result.getChildNode(Square.e7, Square.d6 ));
		assertEquals(114085, result.getChildNode(Square.e7, Square.c5 ));
		assertEquals(90443, result.getChildNode(Square.e7, Square.f8 ));
		assertEquals(90649, result.getChildNode(Square.e7, Square.d8 ));
		assertEquals(97131, result.getChildNode(Square.g7, Square.h6 ));
		assertEquals(88512, result.getChildNode(Square.g7, Square.f8 ));
		assertEquals(99542, result.getChildNode(Square.a6, Square.b7 ));
		assertEquals(85373, result.getChildNode(Square.a6, Square.c8 ));
		assertEquals(99040, result.getChildNode(Square.a6, Square.b5 ));
		assertEquals(91673, result.getChildNode(Square.a6, Square.c4 ));
		assertEquals(84553, result.getChildNode(Square.a6, Square.d3 ));
		assertEquals(77536, result.getChildNode(Square.a6, Square.e2 ));
		assertEquals(93770, result.getChildNode(Square.b6, Square.a4 ));
		assertEquals(83945, result.getChildNode(Square.b6, Square.c8 ));
		assertEquals(88693, result.getChildNode(Square.b6, Square.d5 ));
		assertEquals(89211, result.getChildNode(Square.b6, Square.c4 ));
		assertEquals(124119, result.getChildNode(Square.f6, Square.e4 ));
		assertEquals(99495, result.getChildNode(Square.f6, Square.g8 ));
		assertEquals(101493, result.getChildNode(Square.f6, Square.d5 ));
		assertEquals(99358, result.getChildNode(Square.f6, Square.h7 ));
		assertEquals(103725, result.getChildNode(Square.f6, Square.h5 ));
		assertEquals(106343, result.getChildNode(Square.f6, Square.g4 ));
		
		assertEquals(42, result.getMovesCount());
		assertEquals(3996171, result.getTotalNodes());	
	}	
	
	
	@Test
	public void test_c3b1_e8g8() {
		board.executeMove(Square.c3, Square.b1);
		board.executeMove(Square.e8, Square.g8);
		
		PerftResult result = pert.start(board, 3);
		
		assertEquals(1857, result.getChildNode(Square.e1, Square.g1));
		assertEquals(1702, result.getChildNode(Square.e1, Square.f1));
		assertEquals(1855, result.getChildNode(Square.e1, Square.d1));
		assertEquals(1790, result.getChildNode(Square.d5, Square.d6));
		assertEquals(2040, result.getChildNode(Square.d5, Square.e6));
		assertEquals(1904, result.getChildNode(Square.a2, Square.a3));
		assertEquals(1983, result.getChildNode(Square.a2, Square.a4));
		assertEquals(1732, result.getChildNode(Square.b2, Square.b3));
		assertEquals(1703, result.getChildNode(Square.c2, Square.c3));
		assertEquals(1676, result.getChildNode(Square.c2, Square.c4));
		assertEquals(1730, result.getChildNode(Square.g2, Square.g3));
		assertEquals(1695, result.getChildNode(Square.g2, Square.g4));
		assertEquals(1770, result.getChildNode(Square.g2, Square.h3));
		assertEquals(1553, result.getChildNode(Square.e5, Square.d3));
		assertEquals(2011, result.getChildNode(Square.e5, Square.f7));
		assertEquals(1696, result.getChildNode(Square.e5, Square.c4));
		assertEquals(1883, result.getChildNode(Square.e5, Square.g6));
		assertEquals(1737, result.getChildNode(Square.e5, Square.g4));
		assertEquals(1957, result.getChildNode(Square.e5, Square.c6));
		assertEquals(2019, result.getChildNode(Square.e5, Square.d7));
		assertEquals(1909, result.getChildNode(Square.b1, Square.c3));
		assertEquals(2023, result.getChildNode(Square.b1, Square.a3));
		assertEquals(2036, result.getChildNode(Square.f3, Square.g3));
		assertEquals(2016, result.getChildNode(Square.f3, Square.h3));
		assertEquals(2000, result.getChildNode(Square.f3, Square.e3));
		assertEquals(1842, result.getChildNode(Square.f3, Square.d3));
		assertEquals(1989, result.getChildNode(Square.f3, Square.c3));
		assertEquals(1945, result.getChildNode(Square.f3, Square.b3));
		assertEquals(2003, result.getChildNode(Square.f3, Square.a3));
		assertEquals(1883, result.getChildNode(Square.f3, Square.g4));
		assertEquals(1929, result.getChildNode(Square.f3, Square.h5));
		assertEquals(1850, result.getChildNode(Square.f3, Square.f4));
		assertEquals(2094, result.getChildNode(Square.f3, Square.f5));
		assertEquals(1819, result.getChildNode(Square.f3, Square.f6));
		assertEquals(1887, result.getChildNode(Square.d2, Square.c1));
		assertEquals(1816, result.getChildNode(Square.d2, Square.e3));
		assertEquals(1804, result.getChildNode(Square.d2, Square.f4));
		assertEquals(1922, result.getChildNode(Square.d2, Square.g5));
		assertEquals(1918, result.getChildNode(Square.d2, Square.h6));
		assertEquals(1624, result.getChildNode(Square.d2, Square.c3));
		assertEquals(1917, result.getChildNode(Square.d2, Square.b4));
		assertEquals(1688, result.getChildNode(Square.e2, Square.d1));
		assertEquals(1865, result.getChildNode(Square.e2, Square.f1));
		assertEquals(1737, result.getChildNode(Square.e2, Square.d3));
		assertEquals(1838, result.getChildNode(Square.e2, Square.c4));
		assertEquals(1928, result.getChildNode(Square.e2, Square.b5));
		assertEquals(1744, result.getChildNode(Square.e2, Square.a6));
		assertEquals(1855, result.getChildNode(Square.h1, Square.g1));
		assertEquals(1779, result.getChildNode(Square.h1, Square.f1));

		assertEquals(49, result.getMovesCount());
		assertEquals(90953, result.getTotalNodes());			
	}
	
	@Test
	public void test_c3b1_e8g8_b1c3() {
		board.executeMove(Square.c3, Square.b1);
		board.executeMove(Square.e8, Square.g8);
		board.executeMove(Square.b1, Square.c3);
		
		PerftResult result = pert.start(board, 2);
		
		assertEquals(48, result.getChildNode(Square.g8, Square.h8));
		assertEquals(48, result.getChildNode(Square.g8, Square.h7));
		assertEquals(49, result.getChildNode(Square.c7, Square.c6));
		assertEquals(49, result.getChildNode(Square.c7, Square.c5));
		assertEquals(47, result.getChildNode(Square.d7, Square.d6));
		assertEquals(48, result.getChildNode(Square.e6, Square.d5));
		assertEquals(47, result.getChildNode(Square.g6, Square.g5));
		assertEquals(49, result.getChildNode(Square.b4, Square.b3));
		assertEquals(48, result.getChildNode(Square.b4, Square.c3));
		assertEquals(46, result.getChildNode(Square.h3, Square.g2));
		assertEquals(48, result.getChildNode(Square.a8, Square.b8));
		assertEquals(48, result.getChildNode(Square.a8, Square.c8));
		assertEquals(48, result.getChildNode(Square.a8, Square.d8));
		assertEquals(48, result.getChildNode(Square.a8, Square.e8));
		assertEquals(48, result.getChildNode(Square.f8, Square.e8));
		assertEquals(48, result.getChildNode(Square.f8, Square.d8));
		assertEquals(48, result.getChildNode(Square.f8, Square.c8));
		assertEquals(48, result.getChildNode(Square.f8, Square.b8));
		assertEquals(47, result.getChildNode(Square.e7, Square.d6));
		assertEquals(48, result.getChildNode(Square.e7, Square.c5));
		assertEquals(48, result.getChildNode(Square.e7, Square.e8));
		assertEquals(48, result.getChildNode(Square.e7, Square.d8));
		assertEquals(48, result.getChildNode(Square.g7, Square.h8));
		assertEquals(48, result.getChildNode(Square.g7, Square.h6));
		assertEquals(48, result.getChildNode(Square.a6, Square.b7));
		assertEquals(48, result.getChildNode(Square.a6, Square.c8));
		assertEquals(47, result.getChildNode(Square.a6, Square.b5));
		assertEquals(46, result.getChildNode(Square.a6, Square.c4));
		assertEquals(46, result.getChildNode(Square.a6, Square.d3));
		assertEquals(41, result.getChildNode(Square.a6, Square.e2));
		assertEquals(47, result.getChildNode(Square.b6, Square.a4));
		assertEquals(48, result.getChildNode(Square.b6, Square.c8));
		assertEquals(48, result.getChildNode(Square.b6, Square.d5));
		assertEquals(46, result.getChildNode(Square.b6, Square.c4));
		assertEquals(51, result.getChildNode(Square.f6, Square.e4));
		assertEquals(49, result.getChildNode(Square.f6, Square.d5));
		assertEquals(49, result.getChildNode(Square.f6, Square.h7));
		assertEquals(49, result.getChildNode(Square.f6, Square.h5));
		assertEquals(47, result.getChildNode(Square.f6, Square.g4));
		assertEquals(49, result.getChildNode(Square.f6, Square.e8));		
		
		assertEquals(40, result.getMovesCount());
		assertEquals(1909, result.getTotalNodes());			
	}
	
	
	@Test
	public void test_c3b1_e8g8_b1c3_g8h8() {
		board.executeMove(Square.c3, Square.b1);
		board.executeMove(Square.e8, Square.g8);
		board.executeMove(Square.b1, Square.c3);
		board.executeMove(Square.g8, Square.h8);
		
		PerftResult result = pert.start(board, 1);
		
		assertEquals(1, result.getChildNode(Square.e1, Square.g1));
		assertEquals(1, result.getChildNode(Square.e1, Square.c1));
		assertEquals(1, result.getChildNode(Square.e1, Square.f1));
		assertEquals(1, result.getChildNode(Square.e1, Square.d1));
		assertEquals(1, result.getChildNode(Square.d5, Square.d6));
		assertEquals(1, result.getChildNode(Square.d5, Square.e6));
		assertEquals(1, result.getChildNode(Square.a2, Square.a3));
		assertEquals(1, result.getChildNode(Square.a2, Square.a4));
		assertEquals(1, result.getChildNode(Square.b2, Square.b3));
		assertEquals(1, result.getChildNode(Square.g2, Square.g3));
		assertEquals(1, result.getChildNode(Square.g2, Square.g4));
		assertEquals(1, result.getChildNode(Square.g2, Square.h3));
		assertEquals(1, result.getChildNode(Square.e5, Square.d3));
		assertEquals(1, result.getChildNode(Square.e5, Square.f7));
		assertEquals(1, result.getChildNode(Square.e5, Square.c4));
		assertEquals(1, result.getChildNode(Square.e5, Square.g6));
		assertEquals(1, result.getChildNode(Square.e5, Square.g4));
		assertEquals(1, result.getChildNode(Square.e5, Square.c6));
		assertEquals(1, result.getChildNode(Square.e5, Square.d7));
		assertEquals(1, result.getChildNode(Square.c3, Square.b1));
		assertEquals(1, result.getChildNode(Square.c3, Square.a4));
		assertEquals(1, result.getChildNode(Square.c3, Square.d1));
		assertEquals(1, result.getChildNode(Square.c3, Square.b5));
		assertEquals(1, result.getChildNode(Square.f3, Square.g3));
		assertEquals(1, result.getChildNode(Square.f3, Square.h3));
		assertEquals(1, result.getChildNode(Square.f3, Square.e3));
		assertEquals(1, result.getChildNode(Square.f3, Square.d3));
		assertEquals(1, result.getChildNode(Square.f3, Square.g4));
		assertEquals(1, result.getChildNode(Square.f3, Square.h5));
		assertEquals(1, result.getChildNode(Square.f3, Square.f4));
		assertEquals(1, result.getChildNode(Square.f3, Square.f5));
		assertEquals(1, result.getChildNode(Square.f3, Square.f6));
		assertEquals(1, result.getChildNode(Square.d2, Square.c1));
		assertEquals(1, result.getChildNode(Square.d2, Square.e3));
		assertEquals(1, result.getChildNode(Square.d2, Square.f4));
		assertEquals(1, result.getChildNode(Square.d2, Square.g5));
		assertEquals(1, result.getChildNode(Square.d2, Square.h6));
		assertEquals(1, result.getChildNode(Square.e2, Square.d1));
		assertEquals(1, result.getChildNode(Square.e2, Square.f1));
		assertEquals(1, result.getChildNode(Square.e2, Square.d3));
		assertEquals(1, result.getChildNode(Square.e2, Square.c4));
		assertEquals(1, result.getChildNode(Square.e2, Square.b5));
		assertEquals(1, result.getChildNode(Square.e2, Square.a6));
		assertEquals(1, result.getChildNode(Square.a1, Square.b1));
		assertEquals(1, result.getChildNode(Square.a1, Square.c1));
		assertEquals(1, result.getChildNode(Square.a1, Square.d1));
		assertEquals(1, result.getChildNode(Square.h1, Square.g1));
		assertEquals(1, result.getChildNode(Square.h1, Square.f1));

		assertEquals(48, result.getMovesCount());
		assertEquals(48, result.getTotalNodes());			
	}

	@Test
	public void test_e1d1() {
		board.executeMove(Square.e1, Square.d1);
		PerftResult result = pert.start(board, 4);

		assertEquals(91040, result.getChildNode(Square.f6, Square.h5));
		assertEquals(73853, result.getChildNode(Square.b6, Square.c8));
		assertEquals(80004, result.getChildNode(Square.e8, Square.g8));
		assertEquals(88544, result.getChildNode(Square.a6, Square.b7));
		assertEquals(78389, result.getChildNode(Square.b6, Square.d5));
		assertEquals(82444, result.getChildNode(Square.d7, Square.d6));
		assertEquals(77881, result.getChildNode(Square.g7, Square.f8));
		assertEquals(89189, result.getChildNode(Square.c7, Square.c6));
		assertEquals(91937, result.getChildNode(Square.b4, Square.b3));
		assertEquals(86493, result.getChildNode(Square.a6, Square.b5));
		assertEquals(80525, result.getChildNode(Square.a6, Square.d3));
		assertEquals(82014, result.getChildNode(Square.a8, Square.c8));
		assertEquals(79799, result.getChildNode(Square.h8, Square.h7));
		assertEquals(96666, result.getChildNode(Square.h3, Square.g2));
		assertEquals(88215, result.getChildNode(Square.e6, Square.d5));
		assertEquals(78805, result.getChildNode(Square.e8, Square.f8));
		assertEquals(86501, result.getChildNode(Square.g7, Square.h6));
		assertEquals(82091, result.getChildNode(Square.a8, Square.d8));
		assertEquals(88176, result.getChildNode(Square.a8, Square.b8));
		assertEquals(90819, result.getChildNode(Square.b4, Square.c3));
		assertEquals(83023, result.getChildNode(Square.a6, Square.c4));
		assertEquals(79723, result.getChildNode(Square.h8, Square.h6));
		assertEquals(80461, result.getChildNode(Square.e8, Square.d8));
		assertEquals(78877, result.getChildNode(Square.b6, Square.a4));
		assertEquals(79545, result.getChildNode(Square.e7, Square.f8));
		assertEquals(110180, result.getChildNode(Square.f6, Square.e4));
		assertEquals(87144, result.getChildNode(Square.f6, Square.h7));
		assertEquals(82189, result.getChildNode(Square.g6, Square.g5));
		assertEquals(87261, result.getChildNode(Square.h8, Square.h4));
		assertEquals(71776, result.getChildNode(Square.h8, Square.f8));
		assertEquals(87274, result.getChildNode(Square.f6, Square.g8));
		assertEquals(78957, result.getChildNode(Square.b6, Square.c4));
		assertEquals(79852, result.getChildNode(Square.e7, Square.d8));
		assertEquals(9382, result.getChildNode(Square.a6, Square.e2));
		assertEquals(82490, result.getChildNode(Square.e8, Square.c8));
		assertEquals(85577, result.getChildNode(Square.h8, Square.h5));
		assertEquals(86479, result.getChildNode(Square.e7, Square.d6));
		assertEquals(75805, result.getChildNode(Square.h8, Square.g8));
		assertEquals(83385, result.getChildNode(Square.c7, Square.c5));
		assertEquals(89830, result.getChildNode(Square.f6, Square.d5));
		assertEquals(100539, result.getChildNode(Square.e7, Square.c5));
		assertEquals(76199, result.getChildNode(Square.a6, Square.c8));
		assertEquals(89780, result.getChildNode(Square.f6, Square.g4));

		assertEquals(43, result.getMovesCount());
		assertEquals(3559113, result.getTotalNodes());

		//printForUnitTest(result);
	}

	@Test
	public void test_e1d1_e7d6() {
		board.executeMove(Square.e1, Square.d1);
		board.executeMove(Square.e7, Square.d6);

		PerftResult result = pert.start(board, 3);

		assertEquals(2026, result.getChildNode(Square.f3, Square.f4));
		assertEquals(2123, result.getChildNode(Square.c3, Square.b5));
		assertEquals(1808, result.getChildNode(Square.d2, Square.h6));
		assertEquals(2070, result.getChildNode(Square.a2, Square.a3));
		assertEquals(1983, result.getChildNode(Square.e2, Square.f1));
		assertEquals(1983, result.getChildNode(Square.e2, Square.d3));
		assertEquals(2069, result.getChildNode(Square.f3, Square.e3));
		assertEquals(1881, result.getChildNode(Square.e5, Square.d3));
		assertEquals(1940, result.getChildNode(Square.h1, Square.g1));
		assertEquals(1923, result.getChildNode(Square.d2, Square.g5));
		assertEquals(1934, result.getChildNode(Square.f3, Square.d3));
		assertEquals(1853, result.getChildNode(Square.b2, Square.b3));
		assertEquals(1778, result.getChildNode(Square.d2, Square.f4));
		assertEquals(1856, result.getChildNode(Square.f3, Square.f6));
		assertEquals(1879, result.getChildNode(Square.e5, Square.g4));
		assertEquals(1980, result.getChildNode(Square.e5, Square.g6));
		assertEquals(1851, result.getChildNode(Square.a1, Square.c1));
		assertEquals(1863, result.getChildNode(Square.e2, Square.a6));
		assertEquals(2283, result.getChildNode(Square.f3, Square.f5));
		assertEquals(2107, result.getChildNode(Square.f3, Square.g3));
		assertEquals(2032, result.getChildNode(Square.a2, Square.a4));
		assertEquals(2022, result.getChildNode(Square.d1, Square.e1));
		assertEquals(2009, result.getChildNode(Square.d5, Square.e6));
		assertEquals(2053, result.getChildNode(Square.c3, Square.b1));
		assertEquals(2012, result.getChildNode(Square.e5, Square.c6));
		assertEquals(1609, result.getChildNode(Square.d2, Square.e1));
		assertEquals(2161, result.getChildNode(Square.f3, Square.h5));
		assertEquals(2061, result.getChildNode(Square.e5, Square.f7));
		assertEquals(2137, result.getChildNode(Square.c3, Square.a4));
		assertEquals(1728, result.getChildNode(Square.g2, Square.g4));
		assertEquals(2022, result.getChildNode(Square.d1, Square.c1));
		assertEquals(1852, result.getChildNode(Square.g2, Square.h3));
		assertEquals(1895, result.getChildNode(Square.h1, Square.f1));
		assertEquals(2018, result.getChildNode(Square.e2, Square.c4));
		assertEquals(2183, result.getChildNode(Square.e5, Square.d7));
		assertEquals(1913, result.getChildNode(Square.d2, Square.e3));
		assertEquals(2050, result.getChildNode(Square.e2, Square.b5));
		assertEquals(2254, result.getChildNode(Square.f3, Square.h3));
		assertEquals(1896, result.getChildNode(Square.a1, Square.b1));
		assertEquals(1767, result.getChildNode(Square.g2, Square.g3));
		assertEquals(2059, result.getChildNode(Square.f3, Square.g4));
		assertEquals(1780, result.getChildNode(Square.d2, Square.c1));
		assertEquals(1852, result.getChildNode(Square.h1, Square.e1));
		assertEquals(1924, result.getChildNode(Square.e5, Square.c4));

		assertEquals(44, result.getMovesCount());
		assertEquals(86479, result.getTotalNodes());

		//printForUnitTest(result);
	}

	@Test
	public void test_e1d1_e7d6_d2h6() {
		board.executeMove(Square.e1, Square.d1);
		board.executeMove(Square.e7, Square.d6);
		board.executeMove(Square.d2, Square.h6);

		PerftResult result = pert.start(board, 2);

		assertEquals(46, result.getChildNode(Square.d6, Square.c5));
		assertEquals(44, result.getChildNode(Square.e8, Square.g8));
		assertEquals(46, result.getChildNode(Square.b6, Square.d5));
		assertEquals(44, result.getChildNode(Square.e8, Square.f8));
		assertEquals(44, result.getChildNode(Square.a6, Square.b7));
		assertEquals(38, result.getChildNode(Square.h8, Square.h6));
		assertEquals(44, result.getChildNode(Square.e8, Square.c8));
		assertEquals(41, result.getChildNode(Square.b6, Square.c4));
		assertEquals(47, result.getChildNode(Square.f6, Square.d5));
		assertEquals(46, result.getChildNode(Square.f6, Square.e4));
		assertEquals(45, result.getChildNode(Square.b4, Square.b3));
		assertEquals(43, result.getChildNode(Square.a6, Square.b5));
		assertEquals(43, result.getChildNode(Square.a6, Square.d3));
		assertEquals(36, result.getChildNode(Square.g7, Square.h6));
		assertEquals(44, result.getChildNode(Square.a8, Square.d8));
		assertEquals(44, result.getChildNode(Square.h3, Square.g2));
		assertEquals(44, result.getChildNode(Square.b6, Square.c8));
		assertEquals(44, result.getChildNode(Square.e8, Square.e7));
		assertEquals(44, result.getChildNode(Square.c7, Square.c6));
		assertEquals(45, result.getChildNode(Square.g7, Square.f8));
		assertEquals(44, result.getChildNode(Square.c7, Square.c5));
		assertEquals(43, result.getChildNode(Square.b4, Square.c3));
		assertEquals(42, result.getChildNode(Square.a6, Square.c4));
		assertEquals(44, result.getChildNode(Square.h8, Square.g8));
		assertEquals(6, result.getChildNode(Square.a6, Square.e2));
		assertEquals(45, result.getChildNode(Square.f6, Square.h5));
		assertEquals(46, result.getChildNode(Square.d6, Square.e7));
		assertEquals(47, result.getChildNode(Square.d6, Square.c6));
		assertEquals(46, result.getChildNode(Square.e6, Square.d5));
		assertEquals(44, result.getChildNode(Square.h8, Square.h7));
		assertEquals(44, result.getChildNode(Square.a8, Square.c8));
		assertEquals(8, result.getChildNode(Square.d6, Square.d5));
		assertEquals(43, result.getChildNode(Square.b6, Square.a4));
		assertEquals(40, result.getChildNode(Square.g6, Square.g5));
		assertEquals(44, result.getChildNode(Square.e8, Square.d8));
		assertEquals(44, result.getChildNode(Square.h8, Square.f8));
		assertEquals(45, result.getChildNode(Square.f6, Square.h7));
		assertEquals(44, result.getChildNode(Square.a8, Square.b8));
		assertEquals(43, result.getChildNode(Square.f6, Square.g4));
		assertEquals(39, result.getChildNode(Square.d6, Square.e5));
		assertEquals(45, result.getChildNode(Square.f6, Square.g8));
		assertEquals(44, result.getChildNode(Square.a6, Square.c8));
		assertEquals(46, result.getChildNode(Square.d6, Square.f8));

		assertEquals(43, result.getMovesCount());
		assertEquals(1808, result.getTotalNodes());

		//printForUnitTest(result);
	}

	@Test
	public void test_e1d1_e7d6_d2h6_c7c5() {
		board.executeMove(Square.e1, Square.d1);
		board.executeMove(Square.e7, Square.d6);
		board.executeMove(Square.d2, Square.h6);
		board.executeMove(Square.c7, Square.c5);

		PerftResult result = pert.start(board, 1);

		assertEquals(1, result.getChildNode(Square.f3, Square.g4));
		assertEquals(1, result.getChildNode(Square.a1, Square.c1));
		assertEquals(1, result.getChildNode(Square.g2, Square.g4));
		assertEquals(1, result.getChildNode(Square.d1, Square.d2));
		assertEquals(1, result.getChildNode(Square.a2, Square.a3));
		assertEquals(1, result.getChildNode(Square.a1, Square.b1));
		assertEquals(1, result.getChildNode(Square.g2, Square.g3));
		assertEquals(1, result.getChildNode(Square.f3, Square.f4));
		assertEquals(1, result.getChildNode(Square.e5, Square.c6));
		assertEquals(1, result.getChildNode(Square.f3, Square.e3));
		assertEquals(1, result.getChildNode(Square.h1, Square.f1));
		assertEquals(1, result.getChildNode(Square.b2, Square.b3));
		assertEquals(1, result.getChildNode(Square.e2, Square.d3));
		assertEquals(1, result.getChildNode(Square.e5, Square.d7));
		assertEquals(1, result.getChildNode(Square.h6, Square.e3));
		assertEquals(1, result.getChildNode(Square.h1, Square.g1));
		assertEquals(1, result.getChildNode(Square.g2, Square.h3));
		assertEquals(1, result.getChildNode(Square.d1, Square.c1));
		assertEquals(1, result.getChildNode(Square.e5, Square.g4));
		assertEquals(1, result.getChildNode(Square.e5, Square.f7));
		assertEquals(1, result.getChildNode(Square.e5, Square.g6));
		assertEquals(1, result.getChildNode(Square.h1, Square.e1));
		assertEquals(1, result.getChildNode(Square.e2, Square.f1));
		assertEquals(1, result.getChildNode(Square.h6, Square.d2));
		assertEquals(1, result.getChildNode(Square.e2, Square.b5));
		assertEquals(1, result.getChildNode(Square.f3, Square.f5));
		assertEquals(1, result.getChildNode(Square.c3, Square.b1));
		assertEquals(1, result.getChildNode(Square.f3, Square.h5));
		assertEquals(1, result.getChildNode(Square.f3, Square.h3));
		assertEquals(1, result.getChildNode(Square.h6, Square.g7));
		assertEquals(1, result.getChildNode(Square.h6, Square.f4));
		assertEquals(1, result.getChildNode(Square.f3, Square.g3));
		assertEquals(1, result.getChildNode(Square.e2, Square.a6));
		assertEquals(1, result.getChildNode(Square.e5, Square.c4));
		assertEquals(1, result.getChildNode(Square.e2, Square.c4));
		assertEquals(1, result.getChildNode(Square.d1, Square.e1));
		assertEquals(1, result.getChildNode(Square.h6, Square.c1));
		assertEquals(1, result.getChildNode(Square.f3, Square.f6));
		assertEquals(1, result.getChildNode(Square.h6, Square.g5));
		assertEquals(1, result.getChildNode(Square.f3, Square.d3));
		assertEquals(1, result.getChildNode(Square.c3, Square.b5));
		assertEquals(1, result.getChildNode(Square.e5, Square.d3));
		assertEquals(1, result.getChildNode(Square.a2, Square.a4));
		assertEquals(1, result.getChildNode(Square.c3, Square.a4));

		assertFalse(result.moveExists(Square.d5 , Square.c6 ));	// En Passant capture deja el rey al descubierto

		assertEquals(44, result.getMovesCount());
		assertEquals(44, result.getTotalNodes());

		//printForUnitTest(result);
	}
}
