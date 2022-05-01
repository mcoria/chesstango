package chess.board.perft;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import chess.board.Game;
import chess.board.Square;
import chess.board.fen.FENDecoder;
import chess.board.moves.Move;
import chess.board.perft.imp.PerftBrute;


/**
 * @author Mauricio Coria
 *
 */
public class InitialPositionTest extends AbstractPerftTest  {

	private Perft pert;

	private Game board;
	
	@Before
	public void setUp() throws Exception {
		pert = new PerftBrute();
		board =  this.getGame(FENDecoder.INITIAL_FEN);
	}

	@Test
	public void test_divide1() {
		PerftResult result = pert.start(board, 1);
		
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
		PerftResult result = pert.start(board, 2);
		
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
		PerftResult result = pert.start(board, 3);
		
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
		PerftResult result = pert.start(board, 4);

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
		PerftResult result = pert.start(board, 5);
		
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


	@Test //139segs 106segs 15segs 10segs 9segs 8segs
	public void test_divide6() {
		PerftResult result = pert.start(board, 6);
		
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


	//@Test //394segs 279segs
	public void test_divide7() {
		PerftResult result = pert.start(board, 7);
		
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
		
		board.executeMove(Square.d2, Square.d3);
		
		PerftResult result = pert.start(board, 4);
		
		assertEquals(20, result.getMovesCount());
		assertEquals(328511, result.getTotalNodes());
	}

	//TODO: Falta detallar movimientos
	@Test
	public void test_d2d3_a7a5_b1a3_a5a4_c2c3() {
		board.executeMove(Square.d2, Square.d3);
		board.executeMove(Square.a7, Square.a5);
		board.executeMove(Square.b1, Square.a3);
		board.executeMove(Square.a5, Square.a4);
		board.executeMove(Square.c2, Square.c3);		
		
		PerftResult result = pert.start(board, 4);
		
		assertEquals(21, result.getMovesCount());
		assertEquals(438360, result.getTotalNodes());
	}
	
	
	@Test
	public void test_d2d3_a7a6_c1h6_a8a7_h6h5() {

		board.executeMove(Square.d2, Square.d3);
		board.executeMove(Square.a7, Square.a6);
		board.executeMove(Square.c1, Square.h6);
		board.executeMove(Square.a8, Square.a7);

		assertFalse( contieneMove(board.getPossibleMoves(), Square.h6, Square.h5) );
		
		PerftResult result = pert.start(board, 4);
		
		assertEquals(29, result.getMovesCount());
		assertEquals(319182, result.getTotalNodes());
	}	
	
	
	@Test
	public void test_d2d3_c7c5() {
		
		board.executeMove(Square.d2, Square.d3);
		board.executeMove(Square.c7, Square.c5);
		
		PerftResult result = pert.start(board, 3);
		
		assertEquals(27, result.getMovesCount());
		assertEquals(15971, result.getTotalNodes());
	}
	
	@Test
	public void test_d2d3_c7c5_e1d2() {
		board.executeMove(Square.d2, Square.d3);
		board.executeMove(Square.c7, Square.c5);
		board.executeMove(Square.e1, Square.d2);
		
		PerftResult result = pert.start(board, 4);
		
		assertEquals(22, result.getMovesCount());
		assertEquals(279506, result.getTotalNodes());
	}	
	
	
	@Test
	public void test_d2d3_c7c5_e1d2_d8a5() {
		board.executeMove(Square.d2, Square.d3);
		board.executeMove(Square.c7, Square.c5);
		board.executeMove(Square.e1, Square.d2);
		board.executeMove(Square.d8, Square.a5);
		
		PerftResult result = pert.start(board, 1);
		
		assertEquals(1, result.getChildNode(Square.b1, Square.c3));
		assertEquals(1, result.getChildNode(Square.b2, Square.b4));
		assertEquals(1, result.getChildNode(Square.d2, Square.e3));
		assertEquals(1, result.getChildNode(Square.c2, Square.c3));
		assertFalse("d2e1 is not valid", result.moveExists(Square.d2, Square.e1));
		
		assertEquals(4, result.getMovesCount());
		assertEquals(4, result.getTotalNodes());
	}

	
	protected boolean contieneMove(Collection<Move> movimientos, Square from, Square to) {
		for (Move move : movimientos) {
			if(from.equals(move.getFrom().getKey()) && to.equals(move.getTo().getKey())){
				return true;
			}
		}
		return false;
	}	

}
