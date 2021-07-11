package main;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import builder.ChessBuilderGame;
import chess.Game;
import chess.Move;
import chess.Square;
import parsers.FENParser;

public class ChessMainTest {
	private ChessMain main;
	
	@Before
	public void setUp() throws Exception {
		main = new ChessMain();
	}

	@Test
	public void test_divide1() {
		Game board =  getDefaultGame();
		
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
		Game board =  getDefaultGame();
		
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
		Game board =  getDefaultGame();
		
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
		Game board = getDefaultGame();
		
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
	
	@Test //126segs 104segs 87segs 83segs 80segs 77segs 76segs 70segs 67segs 60segs 58segs 53segs 50segs 46segs 44segs 42segs 37segs 32segs 24segs 20segs 17segs 13segs 10segs
	public void test_divide5() {
		Game board = getDefaultGame();
		
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

	@Test //139segs
	public void test_divide6() {
		Game board = getDefaultGame();
		
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
	}	
	
	@Test
	public void test_d2d3() {
		Game board = getDefaultGame();
		
		board.executeMove(Square.d2, Square.d3);
		
		Node rootNode = main.start(board, 4);
		
		assertEquals(20, board.getMovimientosPosibles().size());
		assertEquals(328511, rootNode.getChildNodesCounter());
	}
	
	@Test
	public void test_d2d3_a7a5_b1a3_a5a4_c2c3() {
		Game board =  getDefaultGame();
		
		board.executeMove(Square.d2, Square.d3);
		board.executeMove(Square.a7, Square.a5);
		board.executeMove(Square.b1, Square.a3);
		board.executeMove(Square.a5, Square.a4);
		board.executeMove(Square.c2, Square.c3);		
		
		Node rootNode = main.start(board, 4);
		
		assertEquals(21, board.getMovimientosPosibles().size());
		assertEquals(438360, rootNode.getChildNodesCounter());
	}
	
	
	@Test
	public void test_d2d3_a7a6_c1h6_a8a7_h6h5() {
		Game board =  getDefaultGame();
		
		board.executeMove(Square.d2, Square.d3);
		board.executeMove(Square.a7, Square.a6);
		board.executeMove(Square.c1, Square.h6);
		board.executeMove(Square.a8, Square.a7);

		assertFalse( contieneMove(board.getMovimientosPosibles(), Square.h6, Square.h5) );
		
		Node rootNode = main.start(board, 4);
		
		assertEquals(29, board.getMovimientosPosibles().size());
		assertEquals(319182, rootNode.getChildNodesCounter());		
	}	
	
	
	@Test
	public void test_d2d3_c7c5() {
		Game board = getDefaultGame();
		
		board.executeMove(Square.d2, Square.d3);
		board.executeMove(Square.c7, Square.c5);
		
		Node rootNode = main.start(board, 3);
		
		assertEquals(27, board.getMovimientosPosibles().size());
		assertEquals(15971, rootNode.getChildNodesCounter());
	}
	
	@Test
	public void test_d2d3_c7c5_e1d2() {
		Game board =  getDefaultGame();
		
		board.executeMove(Square.d2, Square.d3);
		board.executeMove(Square.c7, Square.c5);
		board.executeMove(Square.e1, Square.d2);
		
		Node rootNode = main.start(board, 4);
		
		assertEquals(22, board.getMovimientosPosibles().size());
		assertEquals(279506, rootNode.getChildNodesCounter());
	}	
	
	
	@Test
	public void test_d2d3_c7c5_e1d2_d8a5() {
		Game board = getDefaultGame();
		
		board.executeMove(Square.d2, Square.d3);
		board.executeMove(Square.c7, Square.c5);
		board.executeMove(Square.e1, Square.d2);
		board.executeMove(Square.d8, Square.a5);
		
		Node rootNode = main.start(board, 1);
		
		assertEquals(1, rootNode.getChildNode(Square.b1, Square.c3).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.b2, Square.b4).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.d2, Square.e3).getChildNodesCounter());
		assertEquals(1, rootNode.getChildNode(Square.c2, Square.c3).getChildNodesCounter());
		
		assertNull("d2e1 is not valid", rootNode.getChildNode(Square.d2, Square.e1));
		
		assertEquals(4, board.getMovimientosPosibles().size());
		assertEquals(4, rootNode.getChildNodesCounter());
	}

	
	protected boolean contieneMove(Collection<Move> movimientos, Square from, Square to) {
		for (Move move : movimientos) {
			if(from.equals(move.getFrom().getKey()) && to.equals(move.getTo().getKey())){
				return true;
			}
		}
		return false;
	}
	
	
	private Game getDefaultGame() {		
		ChessBuilderGame builder = new ChessBuilderGame();

		FENParser parser = new FENParser(builder);
		
		parser.parseFEN(FENParser.INITIAL_FEN);
		
		return builder.getGame();
	}	

}
