package chess;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import parsers.FENCoder;
import parsers.FENParser;

public class BoardTest {
	
	@Test
	public void testPosicionInicial() {
		Board board = FENParser.parseFEN(FENParser.INITIAL_FEN);
		assertEquals(Color.BLANCO, board.getTurnoActual());
		assertEquals(GameStatus.IN_PROGRESS, board.getGameStatus());
		assertEquals(20, board.getMovimientosPosibles().size());
	}

	@Test
	public void testJuegoJaqueMate() {
		Board board = FENParser.parseFEN(FENParser.INITIAL_FEN);
		assertEquals(20, board.getMovimientosPosibles().size());
		assertEquals(Color.BLANCO, board.getTurnoActual());
		
		board.executeMove(Square.e2, Square.e4);
		board.executeMove(Square.e7, Square.e5);
		board.executeMove(Square.f1, Square.c4);	
		board.executeMove(Square.b8, Square.c6);
		board.executeMove(Square.d1, Square.f3);
		board.executeMove(Square.f8, Square.c5);
		board.executeMove(Square.f3, Square.f7);
		
		assertEquals(Color.NEGRO, board.getTurnoActual());
		assertEquals(GameStatus.JAQUE_MATE, board.getGameStatus());
		assertTrue(board.getMovimientosPosibles().isEmpty());
	}

	
	@Test
	public void testJuegoJaque() {
		Board board = FENParser.parseFEN(FENParser.INITIAL_FEN);
		assertEquals(20, board.getMovimientosPosibles().size());
		assertEquals(Color.BLANCO, board.getTurnoActual());
		
		board.executeMove(Square.e2, Square.e4);
		board.executeMove(Square.e7, Square.e5);
		board.executeMove(Square.f1, Square.c4);
		board.executeMove(Square.b8, Square.c6);
		board.executeMove(Square.d1, Square.f3);
		board.executeMove(Square.g8, Square.h6);
		board.executeMove(Square.f3, Square.f7);
		
		assertEquals(Color.NEGRO, board.getTurnoActual());
		assertEquals(GameStatus.IN_PROGRESS, board.getGameStatus());
		assertEquals(1, board.getMovimientosPosibles().size());
	}
	
}
