package net.chesstango.board.perft;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import net.chesstango.board.moves.MoveContainerReader;
import net.chesstango.board.perft.imp.PerftWithMap;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import net.chesstango.board.Game;
import net.chesstango.board.Square;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.board.moves.Move;


/**
 * @author Mauricio Coria
 *
 */
public class InitialPositionTest extends AbstractPerftTest  {

	private Perft pert;

	private Game game;
	
	@Before
	public void setUp() throws Exception {
		//pert = new PerftBrute();
		//pert = new PerftWithMap<String>(PerftWithMap::getStringGameId);
		pert = createPerft();
		game =  this.getGame(FENDecoder.INITIAL_FEN);
	}

	@Test
	public void test_divide1() {
		PerftResult result = pert.start(game, 1);
		
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
		PerftResult result = pert.start(game, 2);
		
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
		PerftResult result = pert.start(game, 3);
		
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
		PerftResult result = pert.start(game, 4);

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
		PerftResult result = pert.start(game, 5);
		
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


	@Test //139segs 106segs 15segs 10segs 9segs 7segs
	public void test_divide6() {
		PerftResult result = pert.start(game, 6);
		
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


	@Test //394segs 279segs 217segs 196segs
	public void test_divide7() {
		PerftResult result = pert.start(game, 7);
		
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

	//TODO: Falta detallar movimientos
	@Test
	public void test_d2d3() {
		
		game.executeMove(Square.d2, Square.d3);
		
		PerftResult result = pert.start(game, 4);
		
		assertEquals(20, result.getMovesCount());
		assertEquals(328511, result.getTotalNodes());
	}

	//TODO: Falta detallar movimientos
	@Test
	public void test_d2d3_a7a5_b1a3_a5a4_c2c3() {
		game.executeMove(Square.d2, Square.d3);
		game.executeMove(Square.a7, Square.a5);
		game.executeMove(Square.b1, Square.a3);
		game.executeMove(Square.a5, Square.a4);
		game.executeMove(Square.c2, Square.c3);
		
		PerftResult result = pert.start(game, 4);
		
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
		
		PerftResult result = pert.start(game, 4);
		
		assertEquals(29, result.getMovesCount());
		assertEquals(319182, result.getTotalNodes());
	}	
	
	
	@Test
	public void test_d2d3_c7c5() {
		
		game.executeMove(Square.d2, Square.d3);
		game.executeMove(Square.c7, Square.c5);
		
		PerftResult result = pert.start(game, 3);
		
		assertEquals(27, result.getMovesCount());
		assertEquals(15971, result.getTotalNodes());
	}
	
	@Test
	public void test_d2d3_c7c5_e1d2() {
		game.executeMove(Square.d2, Square.d3);
		game.executeMove(Square.c7, Square.c5);
		game.executeMove(Square.e1, Square.d2);
		
		PerftResult result = pert.start(game, 4);

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

		PerftResult result = pert.start(game, 3);

		assertEquals(494, result.getChildNode(Square.d1, Square.e1));
		assertEquals(472, result.getChildNode(Square.a2, Square.a3));
		assertEquals(514, result.getChildNode(Square.a2, Square.a4));
		assertEquals(527, result.getChildNode(Square.c2, Square.c3));
		assertEquals(514, result.getChildNode(Square.g2, Square.g3));
		assertEquals(515, result.getChildNode(Square.g2, Square.g4));
		assertEquals(472, result.getChildNode(Square.h2, Square.h3));
		assertEquals(514, result.getChildNode(Square.h2, Square.h4));
		assertEquals(451, result.getChildNode(Square.f2, Square.f3));
		assertEquals(473, result.getChildNode(Square.f2, Square.f4));
		assertEquals(558, result.getChildNode(Square.b2, Square.b3));
		assertEquals(558, result.getChildNode(Square.b2, Square.b4));
		assertEquals(491, result.getChildNode(Square.b1, Square.a3));
		assertEquals(545, result.getChildNode(Square.b1, Square.c3));
		assertEquals(599, result.getChildNode(Square.d2, Square.e1));
		assertEquals(602, result.getChildNode(Square.d2, Square.c3));
		assertEquals(569, result.getChildNode(Square.d2, Square.e3));
		assertEquals(556, result.getChildNode(Square.g1, Square.f3));
		assertEquals(493, result.getChildNode(Square.g1, Square.h3));
		assertEquals(450, result.getChildNode(Square.d3, Square.d4));
		assertEquals(466, result.getChildNode(Square.d3, Square.c4));
		assertEquals(598, result.getChildNode(Square.e2, Square.e3));
		assertEquals(621, result.getChildNode(Square.e2, Square.e4));
		assertEquals(23, result.getMovesCount());

	}

	@Test
	public void test_d2d3_c7c5_e1d2_c5c4_b2b4() {
		game.executeMove(Square.d2, Square.d3);
		game.executeMove(Square.c7, Square.c5);
		game.executeMove(Square.e1, Square.d2);
		game.executeMove(Square.c5, Square.c4);
		game.executeMove(Square.b2, Square.b4);

		PerftResult result = pert.start(game, 2);

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


		PerftResult result = pert.start(game, 1);

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
		
		PerftResult result = pert.start(game, 1);
		
		assertEquals(1, result.getChildNode(Square.b1, Square.c3));
		assertEquals(1, result.getChildNode(Square.b2, Square.b4));
		assertEquals(1, result.getChildNode(Square.d2, Square.e3));
		assertEquals(1, result.getChildNode(Square.c2, Square.c3));
		assertFalse("d2e1 is not valid", result.moveExists(Square.d2, Square.e1));
		
		assertEquals(4, result.getMovesCount());
		assertEquals(4, result.getTotalNodes());
	}

	
	protected boolean contieneMove(MoveContainerReader movimientos, Square from, Square to) {
		for (Move move : movimientos) {
			if(from.equals(move.getFrom().getSquare()) && to.equals(move.getTo().getSquare())){
				return true;
			}
		}
		return false;
	}	

}
