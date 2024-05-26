package net.chesstango.tools.perft;

import net.chesstango.board.Game;
import net.chesstango.board.Square;
import net.chesstango.board.representations.fen.FENDecoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;


/**
 * @author Mauricio Coria
 *
 */
public class InitialPositionTest extends AbstractPerftTest  {

	private Perft perft;

	private Game game;
	
	@BeforeEach
	public void setUp() throws Exception {
		perft = createPerft();
		game =  getGame(FENDecoder.INITIAL_FEN);
	}

	@Test
	public void test_divide1() {
		PerftResult result = perft.start(game, 1);
		
		assertEquals(1, result.getChildNode(Square.a2, Square.a3));
		assertEquals(1, result.getChildNode(Square.a2, Square.a4));
		assertEquals(1, result.getChildNode(Square.b2, Square.b3));
		assertEquals(1, result.getChildNode(Square.b2, Square.b4));
		assertEquals(1, result.getChildNode(Square.c2, Square.c3));
		assertEquals(1, result.getChildNode(Square.c2, Square.c4));
		assertEquals(1, result.getChildNode(Square.d2, Square.d3));
		assertEquals(1, result.getChildNode(Square.d2, Square.d4));
		assertEquals(1, result.getChildNode(Square.e2, Square.e3));
		assertEquals(1, result.getChildNode(Square.e2, Square.e4));
		assertEquals(1, result.getChildNode(Square.f2, Square.f3));
		assertEquals(1, result.getChildNode(Square.f2, Square.f4));
		assertEquals(1, result.getChildNode(Square.g2, Square.g3));
		assertEquals(1, result.getChildNode(Square.g2, Square.g4));
		assertEquals(1, result.getChildNode(Square.h2, Square.h3));
		assertEquals(1, result.getChildNode(Square.h2, Square.h4));
		assertEquals(1, result.getChildNode(Square.b1, Square.a3));
		assertEquals(1, result.getChildNode(Square.b1, Square.c3));
		assertEquals(1, result.getChildNode(Square.g1, Square.f3));
		assertEquals(1, result.getChildNode(Square.g1, Square.h3));		
		
		assertEquals(20, result.getMovesCount());
		assertEquals(20, result.getTotalNodes());
	}
	
	@Test
	public void test_divide2() {
		PerftResult result = perft.start(game, 2);
		
		assertEquals(20, result.getChildNode(Square.a2, Square.a3));
		assertEquals(20, result.getChildNode(Square.a2, Square.a4));
		assertEquals(20, result.getChildNode(Square.b2, Square.b3));
		assertEquals(20, result.getChildNode(Square.b2, Square.b4));
		assertEquals(20, result.getChildNode(Square.c2, Square.c3));
		assertEquals(20, result.getChildNode(Square.c2, Square.c4));
		assertEquals(20, result.getChildNode(Square.d2, Square.d3));
		assertEquals(20, result.getChildNode(Square.d2, Square.d4));
		assertEquals(20, result.getChildNode(Square.e2, Square.e3));
		assertEquals(20, result.getChildNode(Square.e2, Square.e4));
		assertEquals(20, result.getChildNode(Square.f2, Square.f3));
		assertEquals(20, result.getChildNode(Square.f2, Square.f4));
		assertEquals(20, result.getChildNode(Square.g2, Square.g3));
		assertEquals(20, result.getChildNode(Square.g2, Square.g4));
		assertEquals(20, result.getChildNode(Square.h2, Square.h3));
		assertEquals(20, result.getChildNode(Square.h2, Square.h4));
		assertEquals(20, result.getChildNode(Square.b1, Square.a3));
		assertEquals(20, result.getChildNode(Square.b1, Square.c3));
		assertEquals(20, result.getChildNode(Square.g1, Square.f3));
		assertEquals(20, result.getChildNode(Square.g1, Square.h3));
		
		assertEquals(20, result.getMovesCount());
		assertEquals(400, result.getTotalNodes());
	} 
	
	@Test
	public void test_divide3() {
		PerftResult result = perft.start(game, 3);
		
		assertEquals(380, result.getChildNode(Square.a2, Square.a3));
		assertEquals(420, result.getChildNode(Square.a2, Square.a4));
		assertEquals(420, result.getChildNode(Square.b2, Square.b3));
		assertEquals(421, result.getChildNode(Square.b2, Square.b4));
		assertEquals(420, result.getChildNode(Square.c2, Square.c3));
		assertEquals(441, result.getChildNode(Square.c2, Square.c4));
		assertEquals(539, result.getChildNode(Square.d2, Square.d3));
		assertEquals(560, result.getChildNode(Square.d2, Square.d4));
		assertEquals(599, result.getChildNode(Square.e2, Square.e3));
		assertEquals(600, result.getChildNode(Square.e2, Square.e4));
		assertEquals(380, result.getChildNode(Square.f2, Square.f3));
		assertEquals(401, result.getChildNode(Square.f2, Square.f4));
		assertEquals(420, result.getChildNode(Square.g2, Square.g3));
		assertEquals(421, result.getChildNode(Square.g2, Square.g4));
		assertEquals(380, result.getChildNode(Square.h2, Square.h3));
		assertEquals(420, result.getChildNode(Square.h2, Square.h4));
		assertEquals(400, result.getChildNode(Square.b1, Square.a3));
		assertEquals(440, result.getChildNode(Square.b1, Square.c3));
		assertEquals(440, result.getChildNode(Square.g1, Square.f3));
		assertEquals(400, result.getChildNode(Square.g1, Square.h3));

		assertEquals(20, result.getMovesCount());
		assertEquals(8902, result.getTotalNodes());
	} 	
	
	
	@Test
	public void test_divide4() {
		PerftResult result = perft.start(game, 4);

		assertEquals(8457, result.getChildNode(Square.a2, Square.a3));
		assertEquals(9329, result.getChildNode(Square.a2, Square.a4));
		assertEquals(9345, result.getChildNode(Square.b2, Square.b3));
		assertEquals(9332, result.getChildNode(Square.b2, Square.b4));
		assertEquals(9272, result.getChildNode(Square.c2, Square.c3));
		assertEquals(9744, result.getChildNode(Square.c2, Square.c4));
		assertEquals(11959, result.getChildNode(Square.d2, Square.d3));
		assertEquals(12435, result.getChildNode(Square.d2, Square.d4));
		assertEquals(13134, result.getChildNode(Square.e2, Square.e3));
		assertEquals(13160, result.getChildNode(Square.e2, Square.e4));
		assertEquals(8457, result.getChildNode(Square.f2, Square.f3));
		assertEquals(8929, result.getChildNode(Square.f2, Square.f4));
		assertEquals(9345, result.getChildNode(Square.g2, Square.g3));
		assertEquals(9328, result.getChildNode(Square.g2, Square.g4));
		assertEquals(8457, result.getChildNode(Square.h2, Square.h3));
		assertEquals(9329, result.getChildNode(Square.h2, Square.h4));
		assertEquals(8885, result.getChildNode(Square.b1, Square.a3));
		assertEquals(9755, result.getChildNode(Square.b1, Square.c3));
		assertEquals(9748, result.getChildNode(Square.g1, Square.f3));
		assertEquals(8881, result.getChildNode(Square.g1, Square.h3));
		
		assertEquals(20, result.getMovesCount());
		assertEquals(197281, result.getTotalNodes());
	} 	
	
	@Test //126segs 104segs 87segs 83segs 80segs 77segs 76segs 70segs 67segs 60segs 58segs 53segs 
		  // 50segs  46segs 44segs 42segs 37segs 32segs 24segs 20segs 17segs 13segs 10segs
	      //  8segs   7segs  1segs
	public void test_divide5() {
		PerftResult result = perft.start(game, 5);
		
		assertEquals(181046, result.getChildNode(Square.a2, Square.a3));
		assertEquals(217832, result.getChildNode(Square.a2, Square.a4));
		assertEquals(215255, result.getChildNode(Square.b2, Square.b3));
		assertEquals(216145, result.getChildNode(Square.b2, Square.b4));
		assertEquals(222861, result.getChildNode(Square.c2, Square.c3));
		assertEquals(240082, result.getChildNode(Square.c2, Square.c4));
		assertEquals(328511, result.getChildNode(Square.d2, Square.d3));
		assertEquals(361790, result.getChildNode(Square.d2, Square.d4));
		assertEquals(402988, result.getChildNode(Square.e2, Square.e3));
		assertEquals(405385, result.getChildNode(Square.e2, Square.e4));
		assertEquals(178889, result.getChildNode(Square.f2, Square.f3));
		assertEquals(198473, result.getChildNode(Square.f2, Square.f4));
		assertEquals(217210, result.getChildNode(Square.g2, Square.g3));
		assertEquals(214048, result.getChildNode(Square.g2, Square.g4));
		assertEquals(181044, result.getChildNode(Square.h2, Square.h3));
		assertEquals(218829, result.getChildNode(Square.h2, Square.h4));
		assertEquals(198572, result.getChildNode(Square.b1, Square.a3));
		assertEquals(234656, result.getChildNode(Square.b1, Square.c3));
		assertEquals(233491, result.getChildNode(Square.g1, Square.f3));
		assertEquals(198502, result.getChildNode(Square.g1, Square.h3));
		
		assertEquals(20, result.getMovesCount());
		assertEquals(4865609, result.getTotalNodes());
	}


	//PerftBrute (movimientos herencia): 139segs 106segs 15segs 10segs 9segs 7segs
	//PerftWithMap (movimientos bridge):   4segs
	@Test
	public void test_divide6() {
		PerftResult result = perft.start(game, 6);
		
		assertEquals(4463267, result.getChildNode(Square.a2, Square.a3 ));
		assertEquals(5363555, result.getChildNode(Square.a2, Square.a4 ));
		assertEquals(5310358, result.getChildNode(Square.b2, Square.b3 ));
		assertEquals(5293555, result.getChildNode(Square.b2, Square.b4 ));
		assertEquals(5417640, result.getChildNode(Square.c2, Square.c3 ));
		assertEquals(5866666, result.getChildNode(Square.c2, Square.c4 ));
		assertEquals(8073082, result.getChildNode(Square.d2, Square.d3 ));
		assertEquals(8879566, result.getChildNode(Square.d2, Square.d4 ));
		assertEquals(9726018, result.getChildNode(Square.e2, Square.e3 ));
		assertEquals(9771632, result.getChildNode(Square.e2, Square.e4 ));
		assertEquals(4404141, result.getChildNode(Square.f2, Square.f3 ));
		assertEquals(4890429, result.getChildNode(Square.f2, Square.f4 ));
		assertEquals(5346260, result.getChildNode(Square.g2, Square.g3 ));
		assertEquals(5239875, result.getChildNode(Square.g2, Square.g4 ));
		assertEquals(4463070, result.getChildNode(Square.h2, Square.h3 ));
		assertEquals(5385554, result.getChildNode(Square.h2, Square.h4 ));
		assertEquals(5708064, result.getChildNode(Square.b1, Square.c3 ));
		assertEquals(4856835, result.getChildNode(Square.b1, Square.a3 ));
		assertEquals(4877234, result.getChildNode(Square.g1, Square.h3 ));
		assertEquals(5723523, result.getChildNode(Square.g1, Square.f3 ));
		
		assertEquals(20, result.getMovesCount());
		assertEquals(119060324, result.getTotalNodes());
	}

	//PerftBrute (movimientos bridge):  204s 181s 174s
	//PerftWithMap (Zobrist + bridge):  45s
	//PerftWithMapIterateDeeping: 		43s 35s
	@Test
	@Disabled
	public void test_divide7() {
		PerftResult result = perft.start(game, 7);
		
		assertEquals(106743106, result.getChildNode(Square.a2, Square.a3));
		assertEquals(133233975, result.getChildNode(Square.b2, Square.b3));
		assertEquals(144074944, result.getChildNode(Square.c2, Square.c3));
		assertEquals(227598692, result.getChildNode(Square.d2, Square.d3));
		assertEquals(306138410, result.getChildNode(Square.e2, Square.e3));
		assertEquals(102021008, result.getChildNode(Square.f2, Square.f3));
		assertEquals(135987651, result.getChildNode(Square.g2, Square.g3));
		assertEquals(106678423, result.getChildNode(Square.h2, Square.h3));
		assertEquals(137077337, result.getChildNode(Square.a2, Square.a4));
		assertEquals(134087476, result.getChildNode(Square.b2, Square.b4));
		assertEquals(157756443, result.getChildNode(Square.c2, Square.c4));
		assertEquals(269605599, result.getChildNode(Square.d2, Square.d4));
		assertEquals(309478263, result.getChildNode(Square.e2, Square.e4));
		assertEquals(119614841, result.getChildNode(Square.f2, Square.f4));
		assertEquals(130293018, result.getChildNode(Square.g2, Square.g4));
		assertEquals(138495290, result.getChildNode(Square.h2, Square.h4));
		assertEquals(120142144, result.getChildNode(Square.b1, Square.a3));
		assertEquals(148527161, result.getChildNode(Square.b1, Square.c3));
		assertEquals(147678554, result.getChildNode(Square.g1, Square.f3));
		assertEquals(120669525, result.getChildNode(Square.g1, Square.h3));
		
		assertEquals(20, result.getMovesCount());
		assertEquals(3195901860L, result.getTotalNodes());
	}

	@Test
	public void test_d2d3() {
		game.executeMove(Square.d2, Square.d3);
		
		PerftResult result = perft.start(game, 4);

		assertEquals(20079, result.getChildNode(Square.d7, Square.d6));
		assertEquals(16371, result.getChildNode(Square.b8, Square.c6));
		assertEquals(14182, result.getChildNode(Square.f7, Square.f6));
		assertEquals(15660, result.getChildNode(Square.b7, Square.b6));
		assertEquals(20887, result.getChildNode(Square.d7, Square.d5));
		assertEquals(15705, result.getChildNode(Square.h7, Square.h5));
		assertEquals(15653, result.getChildNode(Square.a7, Square.a5));
		assertEquals(14899, result.getChildNode(Square.b8, Square.a6));
		assertEquals(14176, result.getChildNode(Square.a7, Square.a6));
		assertEquals(14876, result.getChildNode(Square.g8, Square.h6));
		assertEquals(14280, result.getChildNode(Square.h7, Square.h6));
		assertEquals(15206, result.getChildNode(Square.c7, Square.c6));
		assertEquals(15971, result.getChildNode(Square.c7, Square.c5));
		assertEquals(14882, result.getChildNode(Square.f7, Square.f5));
		assertEquals(16343, result.getChildNode(Square.g8, Square.f6));
		assertEquals(14753, result.getChildNode(Square.g7, Square.g5));
		assertEquals(21624, result.getChildNode(Square.e7, Square.e6));
		assertEquals(15700, result.getChildNode(Square.b7, Square.b5));
		assertEquals(15625, result.getChildNode(Square.g7, Square.g6));
		assertEquals(21639, result.getChildNode(Square.e7, Square.e5));

		assertEquals(20, result.getMovesCount());
		assertEquals(328511, result.getTotalNodes());
	}

	@Test
	public void test_d2d3_a7a5_b1a3_a5a4_c2c3() {
		game.executeMove(Square.d2, Square.d3);
		game.executeMove(Square.a7, Square.a5);
		game.executeMove(Square.b1, Square.a3);
		game.executeMove(Square.a5, Square.a4);
		game.executeMove(Square.c2, Square.c3);
		
		PerftResult result = perft.start(game, 4);

		assertEquals(23833, result.getChildNode(Square.d7, Square.d6));
		assertEquals(23870, result.getChildNode(Square.a8, Square.a6));
		assertEquals(17696, result.getChildNode(Square.f7, Square.f6));
		assertEquals(24732, result.getChildNode(Square.d7, Square.d5));
		assertEquals(19512, result.getChildNode(Square.h7, Square.h5));
		assertEquals(20264, result.getChildNode(Square.g8, Square.f6));
		assertEquals(18579, result.getChildNode(Square.a8, Square.a7));
		assertEquals(18532, result.getChildNode(Square.b8, Square.a6));
		assertEquals(19534, result.getChildNode(Square.c7, Square.c6));
		assertEquals(17827, result.getChildNode(Square.h7, Square.h6));
		assertEquals(20305, result.getChildNode(Square.c7, Square.c5));
		assertEquals(18518, result.getChildNode(Square.f7, Square.f5));
		assertEquals(24502, result.getChildNode(Square.a8, Square.a5));
		assertEquals(18360, result.getChildNode(Square.g7, Square.g5));
		assertEquals(19530, result.getChildNode(Square.b7, Square.b5));
		assertEquals(21300, result.getChildNode(Square.b8, Square.c6));
		assertEquals(27014, result.getChildNode(Square.e7, Square.e6));
		assertEquals(19438, result.getChildNode(Square.b7, Square.b6));
		assertEquals(19418, result.getChildNode(Square.g7, Square.g6));
		assertEquals(18535, result.getChildNode(Square.g8, Square.h6));
		assertEquals(27061, result.getChildNode(Square.e7, Square.e5));

		assertEquals(21, result.getMovesCount());
		assertEquals(438360, result.getTotalNodes());
	}
	
	
	@Test
	public void test_d2d3_a7a6_c1h6_a8a7_h6h5() {
		game.executeMove(Square.d2, Square.d3);
		game.executeMove(Square.a7, Square.a6);
		game.executeMove(Square.c1, Square.h6);
		game.executeMove(Square.a8, Square.a7);

		assertFalse( contieneMove(game.getPossibleMoves(), Square.h6, Square.h5) );
		
		PerftResult result = perft.start(game, 4);

		assertEquals(10938, result.getChildNode(Square.h6, Square.g7));
		assertEquals(10476, result.getChildNode(Square.g1, Square.h3));
		assertEquals(11568, result.getChildNode(Square.h6, Square.g5));
		assertEquals(11645, result.getChildNode(Square.e2, Square.e3));
		assertEquals(10896, result.getChildNode(Square.d3, Square.d4));
		assertEquals(10796, result.getChildNode(Square.c2, Square.c3));
		assertEquals(10127, result.getChildNode(Square.f2, Square.f3));
		assertEquals(10861, result.getChildNode(Square.g2, Square.g3));
		assertEquals(10864, result.getChildNode(Square.b1, Square.a3));
		assertEquals(12536, result.getChildNode(Square.h6, Square.f4));
		assertEquals(10127, result.getChildNode(Square.b2, Square.b3));
		assertEquals(11201, result.getChildNode(Square.c2, Square.c4));
		assertEquals(11670, result.getChildNode(Square.d1, Square.d2));
		assertEquals(9202, result.getChildNode(Square.f2, Square.f4));
		assertEquals(10846, result.getChildNode(Square.a2, Square.a4));
		assertEquals(10127, result.getChildNode(Square.h2, Square.h3));
		assertEquals(11561, result.getChildNode(Square.g1, Square.f3));
		assertEquals(12069, result.getChildNode(Square.h6, Square.e3));
		assertEquals(11652, result.getChildNode(Square.d1, Square.c1));
		assertEquals(10904, result.getChildNode(Square.h2, Square.h4));
		assertEquals(10569, result.getChildNode(Square.e1, Square.d2));
		assertEquals(10794, result.getChildNode(Square.g2, Square.g4));
		assertEquals(10939, result.getChildNode(Square.b1, Square.d2));
		assertEquals(10121, result.getChildNode(Square.b2, Square.b4));
		assertEquals(11582, result.getChildNode(Square.b1, Square.c3));
		assertEquals(10127, result.getChildNode(Square.a2, Square.a3));
		assertEquals(10963, result.getChildNode(Square.h6, Square.c1));
		assertEquals(12670, result.getChildNode(Square.e2, Square.e4));
		assertEquals(11351, result.getChildNode(Square.h6, Square.d2));

		assertEquals(29, result.getMovesCount());
		assertEquals(319182, result.getTotalNodes());
	}	
	
	
	@Test
	public void test_d2d3_c7c5() {
		game.executeMove(Square.d2, Square.d3);
		game.executeMove(Square.c7, Square.c5);
		
		PerftResult result = perft.start(game, 3);

		assertEquals(566, result.getChildNode(Square.c2, Square.c4));
		assertEquals(636, result.getChildNode(Square.g1, Square.f3));
		assertEquals(501, result.getChildNode(Square.b1, Square.d2));
		assertEquals(662, result.getChildNode(Square.b2, Square.b4));
		assertEquals(512, result.getChildNode(Square.f2, Square.f4));
		assertEquals(614, result.getChildNode(Square.c2, Square.c3));
		assertEquals(700, result.getChildNode(Square.e2, Square.e4));
		assertEquals(594, result.getChildNode(Square.g2, Square.g4));
		assertEquals(593, result.getChildNode(Square.h2, Square.h4));
		assertEquals(610, result.getChildNode(Square.c1, Square.d2));
		assertEquals(616, result.getChildNode(Square.e2, Square.e3));
		assertEquals(593, result.getChildNode(Square.a2, Square.a4));
		assertEquals(552, result.getChildNode(Square.f2, Square.f3));
		assertEquals(572, result.getChildNode(Square.c1, Square.h6));
		assertEquals(551, result.getChildNode(Square.h2, Square.h3));
		assertEquals(551, result.getChildNode(Square.a2, Square.a3));
		assertEquals(610, result.getChildNode(Square.d1, Square.d2));
		assertEquals(671, result.getChildNode(Square.c1, Square.f4));
		assertEquals(626, result.getChildNode(Square.c1, Square.g5));
		assertEquals(549, result.getChildNode(Square.b1, Square.a3));
		assertEquals(640, result.getChildNode(Square.d3, Square.d4));
		assertEquals(611, result.getChildNode(Square.b1, Square.c3));
		assertEquals(594, result.getChildNode(Square.b2, Square.b3));
		assertEquals(595, result.getChildNode(Square.c1, Square.e3));
		assertEquals(572, result.getChildNode(Square.g1, Square.h3));
		assertEquals(487, result.getChildNode(Square.e1, Square.d2));
		assertEquals(593, result.getChildNode(Square.g2, Square.g3));

		assertEquals(27, result.getMovesCount());
		assertEquals(15971, result.getTotalNodes());
	}
	
	@Test
	public void test_d2d3_c7c5_e1d2() {
		game.executeMove(Square.d2, Square.d3);
		game.executeMove(Square.c7, Square.c5);
		game.executeMove(Square.e1, Square.d2);
		
		PerftResult result = perft.start(game, 4);

		assertEquals(12052, result.getChildNode(Square.c5, Square.c4));
		assertEquals(11402, result.getChildNode(Square.a7, Square.a6));
		assertEquals(12331, result.getChildNode(Square.a7, Square.a5));
		assertEquals(11403, result.getChildNode(Square.f7, Square.f6));
		assertEquals(11933, result.getChildNode(Square.f7, Square.f5));
		assertEquals(11402, result.getChildNode(Square.h7, Square.h6));
		assertEquals(12503, result.getChildNode(Square.h7, Square.h5));
		assertEquals(15819, result.getChildNode(Square.d7, Square.d6));
		assertEquals(16284, result.getChildNode(Square.d7, Square.d5));
		assertEquals(11937, result.getChildNode(Square.b8, Square.a6));
		assertEquals(14147, result.getChildNode(Square.b8, Square.c6));
		assertEquals(15436, result.getChildNode(Square.e7, Square.e6));
		assertEquals(15489, result.getChildNode(Square.e7, Square.e5));
		assertEquals(11791, result.getChildNode(Square.b7, Square.b6));
		assertEquals(12487, result.getChildNode(Square.b7, Square.b5));
		assertEquals(15234, result.getChildNode(Square.d8, Square.c7));
		assertEquals(16352, result.getChildNode(Square.d8, Square.b6));
		assertEquals(2615, result.getChildNode(Square.d8, Square.a5));
		assertEquals(12102, result.getChildNode(Square.g7, Square.g6));
		assertEquals(12443, result.getChildNode(Square.g7, Square.g5));
		assertEquals(12481, result.getChildNode(Square.g8, Square.f6));
		assertEquals(11863, result.getChildNode(Square.g8, Square.h6));

		assertEquals(22, result.getMovesCount());
		assertEquals(279506, result.getTotalNodes());
	}

	@Test
	public void test_d2d3_c7c5_e1d2_c5c4() {
		game.executeMove(Square.d2, Square.d3);
		game.executeMove(Square.c7, Square.c5);
		game.executeMove(Square.e1, Square.d2);
		game.executeMove(Square.c5, Square.c4);

		PerftResult result = perft.start(game, 3);

		assertEquals(494, result.getChildNode(Square.d1, Square.e1));
		assertEquals(451, result.getChildNode(Square.f2, Square.f3));
		assertEquals(514, result.getChildNode(Square.a2, Square.a4));
		assertEquals(556, result.getChildNode(Square.g1, Square.f3));
		assertEquals(602, result.getChildNode(Square.d2, Square.c3));
		assertEquals(569, result.getChildNode(Square.d2, Square.e3));
		assertEquals(598, result.getChildNode(Square.e2, Square.e3));
		assertEquals(472, result.getChildNode(Square.h2, Square.h3));
		assertEquals(621, result.getChildNode(Square.e2, Square.e4));
		assertEquals(514, result.getChildNode(Square.g2, Square.g3));
		assertEquals(450, result.getChildNode(Square.d3, Square.d4));
		assertEquals(491, result.getChildNode(Square.b1, Square.a3));
		assertEquals(527, result.getChildNode(Square.c2, Square.c3));
		assertEquals(545, result.getChildNode(Square.b1, Square.c3));
		assertEquals(558, result.getChildNode(Square.b2, Square.b3));
		assertEquals(473, result.getChildNode(Square.f2, Square.f4));
		assertEquals(558, result.getChildNode(Square.b2, Square.b4));
		assertEquals(599, result.getChildNode(Square.d2, Square.e1));
		assertEquals(515, result.getChildNode(Square.g2, Square.g4));
		assertEquals(493, result.getChildNode(Square.g1, Square.h3));
		assertEquals(466, result.getChildNode(Square.d3, Square.c4));
		assertEquals(514, result.getChildNode(Square.h2, Square.h4));
		assertEquals(472, result.getChildNode(Square.a2, Square.a3));

		assertEquals(23, result.getMovesCount());
		assertEquals(12052, result.getTotalNodes());
	}

	@Test
	public void test_d2d3_c7c5_e1d2_c5c4_b2b4() {
		game.executeMove(Square.d2, Square.d3);
		game.executeMove(Square.c7, Square.c5);
		game.executeMove(Square.e1, Square.d2);
		game.executeMove(Square.c5, Square.c4);
		game.executeMove(Square.b2, Square.b4);

		PerftResult result = perft.start(game, 2);

		assertEquals(24, result.getChildNode(Square.f7, Square.f6));
		assertEquals(24, result.getChildNode(Square.f7, Square.f5));
		assertEquals(24, result.getChildNode(Square.g7, Square.g6));
		assertEquals(24, result.getChildNode(Square.g7, Square.g5));
		assertEquals(24, result.getChildNode(Square.b8, Square.a6));
		assertEquals(24, result.getChildNode(Square.b8, Square.c6));
		assertEquals(24, result.getChildNode(Square.e7, Square.e6));
		assertEquals(24, result.getChildNode(Square.e7, Square.e5));
		assertEquals(24, result.getChildNode(Square.g8, Square.f6));
		assertEquals(24, result.getChildNode(Square.g8, Square.h6));
		assertEquals(24, result.getChildNode(Square.d7, Square.d6));
		assertEquals(24, result.getChildNode(Square.d7, Square.d5));
		assertEquals(24, result.getChildNode(Square.d8, Square.c7));
		assertEquals(23, result.getChildNode(Square.d8, Square.b6));
		assertEquals(24, result.getChildNode(Square.d8, Square.a5));
		assertEquals(24, result.getChildNode(Square.a7, Square.a6));
		assertEquals(25, result.getChildNode(Square.a7, Square.a5));
		assertEquals(24, result.getChildNode(Square.h7, Square.h6));
		assertEquals(24, result.getChildNode(Square.h7, Square.h5));
		assertEquals(24, result.getChildNode(Square.b7, Square.b6));
		assertEquals(23, result.getChildNode(Square.b7, Square.b5));
		assertEquals(25, result.getChildNode(Square.c4, Square.b3));
		assertEquals(4, result.getChildNode(Square.c4, Square.c3));
		assertEquals(26, result.getChildNode(Square.c4, Square.d3));

		assertEquals(24, result.getMovesCount());
		assertEquals(558, result.getTotalNodes());

		//printForUnitTest(result);
	}

	@Test
	public void test_d2d3_c7c5_e1d2_c5c4_b2b4_d8a5() {
		game.executeMove(Square.d2, Square.d3);
		game.executeMove(Square.c7, Square.c5);
		game.executeMove(Square.e1, Square.d2);
		game.executeMove(Square.c5, Square.c4);
		game.executeMove(Square.b2, Square.b4);
		game.executeMove(Square.d8, Square.a5);


		PerftResult result = perft.start(game, 1);

		assertEquals(1, result.getChildNode(Square.c1, Square.b2));
		assertEquals(1, result.getChildNode(Square.c1, Square.a3));
		assertEquals(1, result.getChildNode(Square.d1, Square.e1));
		assertEquals(1, result.getChildNode(Square.a2, Square.a3));
		assertEquals(1, result.getChildNode(Square.a2, Square.a4));
		assertEquals(1, result.getChildNode(Square.g2, Square.g3));
		assertEquals(1, result.getChildNode(Square.g2, Square.g4));
		assertEquals(1, result.getChildNode(Square.b1, Square.a3));
		assertEquals(1, result.getChildNode(Square.b1, Square.c3));
		assertEquals(1, result.getChildNode(Square.e2, Square.e3));
		assertEquals(1, result.getChildNode(Square.e2, Square.e4));
		assertEquals(1, result.getChildNode(Square.d3, Square.d4));
		assertEquals(1, result.getChildNode(Square.d3, Square.c4));
		assertEquals(1, result.getChildNode(Square.f2, Square.f3));
		assertEquals(1, result.getChildNode(Square.f2, Square.f4));
		assertEquals(1, result.getChildNode(Square.d2, Square.e1));
		assertEquals(1, result.getChildNode(Square.d2, Square.c3));
		assertEquals(1, result.getChildNode(Square.d2, Square.e3));
		assertEquals(1, result.getChildNode(Square.c2, Square.c3));
		assertEquals(1, result.getChildNode(Square.g1, Square.f3));
		assertEquals(1, result.getChildNode(Square.g1, Square.h3));
		assertEquals(1, result.getChildNode(Square.b4, Square.a5));
		assertEquals(1, result.getChildNode(Square.h2, Square.h3));
		assertEquals(1, result.getChildNode(Square.h2, Square.h4));

		assertEquals(24, result.getMovesCount());
		assertEquals(24, result.getTotalNodes());

		//printForUnitTest(result);
	}


	@Test
	public void test_d2d3_c7c5_e1d2_d8a5() {
		game.executeMove(Square.d2, Square.d3);
		game.executeMove(Square.c7, Square.c5);
		game.executeMove(Square.e1, Square.d2);
		game.executeMove(Square.d8, Square.a5);
		
		PerftResult result = perft.start(game, 3);

		assertEquals(718, result.getChildNode(Square.d2, Square.e3));
		assertEquals(673, result.getChildNode(Square.b2, Square.b4));
		assertEquals(651, result.getChildNode(Square.c2, Square.c3));
		assertEquals(573, result.getChildNode(Square.b1, Square.c3));

		assertFalse(result.moveExists(Square.d2, Square.e1), "d2e1 is not valid");
		
		assertEquals(4, result.getMovesCount());
		assertEquals(2615, result.getTotalNodes());
	}

	@Test
	public void test_d2d3_c7c5_e1d2_d8a5_b2b4() {
		game.executeMove(Square.d2, Square.d3);
		game.executeMove(Square.c7, Square.c5);
		game.executeMove(Square.e1, Square.d2);
		game.executeMove(Square.d8, Square.a5);
		game.executeMove(Square.b2, Square.b4);

		PerftResult result = perft.start(game, 2);

		assertEquals(24, result.getChildNode(Square.e8, Square.d8 ));
		assertEquals(24, result.getChildNode(Square.a7, Square.a6 ));
		assertEquals(24, result.getChildNode(Square.b7, Square.b6 ));
		assertEquals(24, result.getChildNode(Square.b7, Square.b5 ));
		assertEquals(24, result.getChildNode(Square.d7, Square.d6 ));
		assertEquals(24, result.getChildNode(Square.d7, Square.d5 ));
		assertEquals(24, result.getChildNode(Square.e7, Square.e6 ));
		assertEquals(24, result.getChildNode(Square.e7, Square.e5 ));
		assertEquals(24, result.getChildNode(Square.f7, Square.f6 ));
		assertEquals(24, result.getChildNode(Square.f7, Square.f5 ));
		assertEquals(24, result.getChildNode(Square.g7, Square.g6 ));
		assertEquals(24, result.getChildNode(Square.g7, Square.g5 ));
		assertEquals(24, result.getChildNode(Square.h7, Square.h6 ));
		assertEquals(24, result.getChildNode(Square.h7, Square.h5 ));
		assertEquals(24, result.getChildNode(Square.c5, Square.c4 ));
		assertEquals(22, result.getChildNode(Square.c5, Square.b4 ));
		assertEquals(24, result.getChildNode(Square.b8, Square.a6));
		assertEquals(24, result.getChildNode(Square.b8, Square.c6));
		assertEquals(24, result.getChildNode(Square.g8, Square.f6 ));
		assertEquals(24, result.getChildNode(Square.g8, Square.h6 ));
		assertEquals(24, result.getChildNode(Square.a5, Square.b5 ));
		assertEquals(24, result.getChildNode(Square.a5, Square.a4 ));
		assertEquals(22, result.getChildNode(Square.a5, Square.a3 ));
		assertEquals(22, result.getChildNode(Square.a5, Square.a2 ));
		assertEquals(3, result.getChildNode(Square.a5, Square.b4 ));
		assertEquals(25, result.getChildNode(Square.a5, Square.b6 ));
		assertEquals(25, result.getChildNode(Square.a5, Square.c7 ));
		assertEquals(25, result.getChildNode(Square.a5, Square.d8 ));
		assertEquals(25, result.getChildNode(Square.a5, Square.a6 ));

		assertEquals(29, result.getMovesCount());
		assertEquals(673, result.getTotalNodes());
	}

	@Test
	public void test_d2d3_c7c5_e1d2_d8a5_b2b4_d7d6() {
		game.executeMove(Square.d2, Square.d3);
		game.executeMove(Square.c7, Square.c5);
		game.executeMove(Square.e1, Square.d2);
		game.executeMove(Square.d8, Square.a5);
		game.executeMove(Square.b2, Square.b4);
		game.executeMove(Square.d7, Square.d6);

		PerftResult result = perft.start(game, 1);

		assertEquals(1, result.getChildNode(Square.d2, Square.e3));
		assertEquals(1, result.getChildNode(Square.d2, Square.e1));
		assertEquals(1, result.getChildNode(Square.d2, Square.c3));
		assertEquals(1, result.getChildNode(Square.b4, Square.a5));
		assertEquals(1, result.getChildNode(Square.d3, Square.d4));
		assertEquals(1, result.getChildNode(Square.a2, Square.a3));
		assertEquals(1, result.getChildNode(Square.a2, Square.a4));
		assertEquals(1, result.getChildNode(Square.c2, Square.c3));
		assertEquals(1, result.getChildNode(Square.c2, Square.c4));
		assertEquals(1, result.getChildNode(Square.e2, Square.e3));
		assertEquals(1, result.getChildNode(Square.e2, Square.e4));
		assertEquals(1, result.getChildNode(Square.f2, Square.f3));
		assertEquals(1, result.getChildNode(Square.f2, Square.f4));
		assertEquals(1, result.getChildNode(Square.g2, Square.g3));
		assertEquals(1, result.getChildNode(Square.g2, Square.g4));
		assertEquals(1, result.getChildNode(Square.h2, Square.h3));
		assertEquals(1, result.getChildNode(Square.h2, Square.h4));
		assertEquals(1, result.getChildNode(Square.b1, Square.c3 ));
		assertEquals(1, result.getChildNode(Square.b1, Square.a3 ));
		assertEquals(1, result.getChildNode(Square.c1, Square.b2));
		assertEquals(1, result.getChildNode(Square.c1, Square.a3 ));
		assertEquals(1, result.getChildNode(Square.d1, Square.e1 ));
		assertEquals(1, result.getChildNode(Square.g1, Square.h3 ));
		assertEquals(1, result.getChildNode(Square.g1, Square.f3 ));

		assertFalse(result.moveExists(Square.b4, Square.b5), "b4b5 is not valid");
		assertFalse(result.moveExists(Square.b4, Square.c5), "b4c5 is not valid");

		assertEquals(24, result.getMovesCount());
		assertEquals(24, result.getTotalNodes());
	}

}
