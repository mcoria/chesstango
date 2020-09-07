package chess;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import chess.Game.GameStatus;
import parsers.FENBoarBuilder;

public class GameTest {
	
	private FENBoarBuilder builder;

	@Before
	public void setUp() throws Exception {
		builder = new FENBoarBuilder();
	}
	
	@Test
	public void testPosicionInicial() {
		Game game = builder.withDefaultBoard().buildGame();
		
		assertEquals(Color.BLANCO, game.getTurnoActual());
		assertEquals(GameStatus.IN_PROGRESS, game.getGameStatus());
		assertEquals(20, game.getMovimientosPosibles().size());
	}

	@Test
	public void testJuegoJaqueMate() {
		Game game = builder.withDefaultBoard().buildGame();
		assertEquals(20, game.getMovimientosPosibles().size());
		assertEquals(Color.BLANCO, game.getTurnoActual());
		
		game.executeMove(Square.e2, Square.e4);
		game.executeMove(Square.e7, Square.e5);
		game.executeMove(Square.f1, Square.c4);	
		game.executeMove(Square.b8, Square.c6);
		game.executeMove(Square.d1, Square.f3);
		game.executeMove(Square.f8, Square.c5);
		game.executeMove(Square.f3, Square.f7);
		
		assertEquals(Color.NEGRO, game.getTurnoActual());
		assertEquals(GameStatus.JAQUE_MATE, game.getGameStatus());
		assertTrue(game.getMovimientosPosibles().isEmpty());
	}

	@Test
	public void testJuegoJaqueMateUndo() {
		Game game = builder.withDefaultBoard().buildGame();
		assertEquals(20, game.getMovimientosPosibles().size());
		assertEquals(Color.BLANCO, game.getTurnoActual());
		
		game.executeMove(Square.e2, Square.e4);
		game.executeMove(Square.e7, Square.e5);
		game.executeMove(Square.f1, Square.c4);	
		game.executeMove(Square.b8, Square.c6);
		game.executeMove(Square.d1, Square.f3);
		game.executeMove(Square.f8, Square.c5);
		game.executeMove(Square.f3, Square.f7);
		
		assertEquals(Color.NEGRO, game.getTurnoActual());
		assertEquals(GameStatus.JAQUE_MATE, game.getGameStatus());
		assertTrue(game.getMovimientosPosibles().isEmpty());
		
		game.undoMove();
		game.undoMove();
		game.undoMove();	
		game.undoMove();
		game.undoMove();
		game.undoMove();
		game.undoMove();
		assertEquals(20, game.getMovimientosPosibles().size());
		assertEquals(Color.BLANCO, game.getTurnoActual());		
	}	
	
	@Test
	public void testJuegoJaque() {
		Game game = builder.withDefaultBoard().buildGame();
		
		assertEquals(20, game.getMovimientosPosibles().size());
		assertEquals(Color.BLANCO, game.getTurnoActual());
		
		game.executeMove(Square.e2, Square.e4);
		assertEquals(20, game.getMovimientosPosibles().size());
		assertEquals(Color.NEGRO, game.getTurnoActual());		
		
		game.executeMove(Square.e7, Square.e5);
		assertEquals(29, game.getMovimientosPosibles().size());
		assertEquals(Color.BLANCO, game.getTurnoActual());			
		
		game.executeMove(Square.f1, Square.c4);
		assertEquals(29, game.getMovimientosPosibles().size());
		assertEquals(Color.NEGRO, game.getTurnoActual());			
		
		game.executeMove(Square.b8, Square.c6);
		assertEquals(33, game.getMovimientosPosibles().size());
		assertEquals(Color.BLANCO, game.getTurnoActual());			
		
		game.executeMove(Square.d1, Square.f3);
		assertEquals(31, game.getMovimientosPosibles().size());
		assertEquals(Color.NEGRO, game.getTurnoActual());			
		
		game.executeMove(Square.g8, Square.h6);
		assertEquals(42, game.getMovimientosPosibles().size());
		assertEquals(Color.BLANCO, game.getTurnoActual());			
		
		
		game.executeMove(Square.f3, Square.f7);
		assertEquals(Color.NEGRO, game.getTurnoActual());
		assertEquals(GameStatus.JAQUE, game.getGameStatus());
		assertEquals(1, game.getMovimientosPosibles().size());
	}
	
	@Test
	public void testJuegoTablas() {
		Game game = builder.withFEN("k7/7Q/K7/8/8/8/8/8 w KQkq - 0 1").buildGame();
		
		assertEquals(Color.BLANCO, game.getTurnoActual());
		
		game.executeMove(Square.h7, Square.c7);

		assertEquals(Color.NEGRO, game.getTurnoActual());
		assertEquals(GameStatus.TABLAS, game.getGameStatus());
		assertEquals(0, game.getMovimientosPosibles().size());
	}
	
	@Test
	public void testJuegoUndo() {
		Game game = builder.withDefaultBoard().buildGame();
		
		assertEquals(20, game.getMovimientosPosibles().size());
		assertEquals(Color.BLANCO, game.getTurnoActual());
		
		game.executeMove(Square.e2, Square.e4);
		
		game.undoMove();
		assertEquals(20, game.getMovimientosPosibles().size());
		assertEquals(Color.BLANCO, game.getTurnoActual());
	}
	
	@Test
	public void testJuegoNoPeonPasante() {
		Game game = builder.withFEN("rnbqkbnr/p1pppppp/1p6/P7/8/8/1PPPPPPP/RNBQKBNR b KQkq - 0 2").buildGame();
		
		game.executeMove(Square.b6, Square.b5);
		
		assertEquals(22, game.getMovimientosPosibles().size());
	}
	
	@Test
	public void testJuegoPeonPasante() {
		Game game = builder.withFEN("rnbqkbnr/1ppppppp/8/pP6/8/8/P1PPPPPP/RNBQKBNR b KQkq - 0 2").buildGame();
		
		game.executeMove(Square.c7, Square.c5);
		
		assertNotNull(game.getMovimiento(Square.b5, Square.c6));
		assertEquals(22, game.getMovimientosPosibles().size());
	}
	

	@Test
	public void testJuegoKiwipeteTestUndo() {
		Game game = new FENBoarBuilder().withFEN("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq -").buildGame();
		
		assertEquals(48, game.getMovimientosPosibles().size());
		
		game.executeMove(Square.e1, Square.g1);
		game.undoMove();
		assertEquals(48, game.getMovimientosPosibles().size());
		
		game.executeMove(Square.e1, Square.c1);
		game.undoMove();
		assertEquals(48, game.getMovimientosPosibles().size());
		
		game.executeMove(Square.e1, Square.f1);
		game.undoMove();
		assertEquals(48, game.getMovimientosPosibles().size());
		
		game.executeMove(Square.e1, Square.d1);
		game.undoMove();
		assertEquals(48, game.getMovimientosPosibles().size());
		
		game.executeMove(Square.d5, Square.d6);
		game.undoMove();
		assertEquals(48, game.getMovimientosPosibles().size());
		
		game.executeMove(Square.d5, Square.e6);
		game.undoMove();
		assertEquals(48, game.getMovimientosPosibles().size());
		
		game.executeMove(Square.a2, Square.a3);
		game.undoMove();
		assertEquals(48, game.getMovimientosPosibles().size());
		
		game.executeMove(Square.a2, Square.a4);
		game.undoMove();
		assertEquals(48, game.getMovimientosPosibles().size());
		
		game.executeMove(Square.b2, Square.b3);
		game.undoMove();
		assertEquals(48, game.getMovimientosPosibles().size());
		
		game.executeMove(Square.g2, Square.g3);
		game.undoMove();
		assertEquals(48, game.getMovimientosPosibles().size());
		
		game.executeMove(Square.g2, Square.g4);
		game.undoMove();
		assertEquals(48, game.getMovimientosPosibles().size());
		
		game.executeMove(Square.g2, Square.h3);
		game.undoMove();
		assertEquals(48, game.getMovimientosPosibles().size());
		
		game.executeMove(Square.e5, Square.d3);
		game.undoMove();
		assertEquals(48, game.getMovimientosPosibles().size());
		
		game.executeMove(Square.e5, Square.f7);
		game.undoMove();
		assertEquals(48, game.getMovimientosPosibles().size());
		
		game.executeMove(Square.e5, Square.c4);
		game.undoMove();
		assertEquals(48, game.getMovimientosPosibles().size());
		
		game.executeMove(Square.e5, Square.g6);
		game.undoMove();
		assertEquals(48, game.getMovimientosPosibles().size());
		
		game.executeMove(Square.e5, Square.g4);
		game.undoMove();
		assertEquals(48, game.getMovimientosPosibles().size());
		
		game.executeMove(Square.e5, Square.c6);
		game.undoMove();
		assertEquals(48, game.getMovimientosPosibles().size());
		
		game.executeMove(Square.e5, Square.d7);
		game.undoMove();
		assertEquals(48, game.getMovimientosPosibles().size());
		
		game.executeMove(Square.c3, Square.b1);
		game.undoMove();
		assertEquals(48, game.getMovimientosPosibles().size());
		
		game.executeMove(Square.c3, Square.a4);
		game.undoMove();
		assertEquals(48, game.getMovimientosPosibles().size());
		
		game.executeMove(Square.c3, Square.d1);
		game.undoMove();
		assertEquals(48, game.getMovimientosPosibles().size());
		
		game.executeMove(Square.c3, Square.b5);
		game.undoMove();
		assertEquals(48, game.getMovimientosPosibles().size());
		
		game.executeMove(Square.f3, Square.g3);
		game.undoMove();
		assertEquals(48, game.getMovimientosPosibles().size());
		
		game.executeMove(Square.f3, Square.h3);
		game.undoMove();
		assertEquals(48, game.getMovimientosPosibles().size());
		
		game.executeMove(Square.f3, Square.e3);
		game.undoMove();
		assertEquals(48, game.getMovimientosPosibles().size());
		
		game.executeMove(Square.f3, Square.d3);
		game.undoMove();
		assertEquals(48, game.getMovimientosPosibles().size());
		
		game.executeMove(Square.f3, Square.g4);
		game.undoMove();
		assertEquals(48, game.getMovimientosPosibles().size());
		
		game.executeMove(Square.f3, Square.h5);
		game.undoMove();
		assertEquals(48, game.getMovimientosPosibles().size());
		
		game.executeMove(Square.f3, Square.f4);
		game.undoMove();
		assertEquals(48, game.getMovimientosPosibles().size());
		
		game.executeMove(Square.f3, Square.f5);
		game.undoMove();
		assertEquals(48, game.getMovimientosPosibles().size());
		
		game.executeMove(Square.f3, Square.f6);
		game.undoMove();
		assertEquals(48, game.getMovimientosPosibles().size());
		
		game.executeMove(Square.d2, Square.c1);
		game.undoMove();
		assertEquals(48, game.getMovimientosPosibles().size());
		
		game.executeMove(Square.d2, Square.e3);
		game.undoMove();
		assertEquals(48, game.getMovimientosPosibles().size());
		
		game.executeMove(Square.d2, Square.f4);
		game.undoMove();
		assertEquals(48, game.getMovimientosPosibles().size());
		
		game.executeMove(Square.d2, Square.g5);
		game.undoMove();
		assertEquals(48, game.getMovimientosPosibles().size());
		
		game.executeMove(Square.d2, Square.h6);
		game.undoMove();
		assertEquals(48, game.getMovimientosPosibles().size());
		
		game.executeMove(Square.e2, Square.d1);
		game.undoMove();
		assertEquals(48, game.getMovimientosPosibles().size());
		
		game.executeMove(Square.e2, Square.f1);
		game.undoMove();
		assertEquals(48, game.getMovimientosPosibles().size());
		
		game.executeMove(Square.e2, Square.d3);
		game.undoMove();
		assertEquals(48, game.getMovimientosPosibles().size());
		
		game.executeMove(Square.e2, Square.c4);
		game.undoMove();
		assertEquals(48, game.getMovimientosPosibles().size());
		
		game.executeMove(Square.e2, Square.b5);
		game.undoMove();
		assertEquals(48, game.getMovimientosPosibles().size());
		
		game.executeMove(Square.e2, Square.a6);
		game.undoMove();
		assertEquals(48, game.getMovimientosPosibles().size());
		
		game.executeMove(Square.a1, Square.b1);
		game.undoMove();
		assertEquals(48, game.getMovimientosPosibles().size());
		
		game.executeMove(Square.a1, Square.c1);
		game.undoMove();
		assertEquals(48, game.getMovimientosPosibles().size());
		
		game.executeMove(Square.a1, Square.d1);
		game.undoMove();
		assertEquals(48, game.getMovimientosPosibles().size());
		
		game.executeMove(Square.h1, Square.g1);
		game.undoMove();
		assertEquals(48, game.getMovimientosPosibles().size());
		
		game.executeMove(Square.h1, Square.f1);
		game.undoMove();
		assertEquals(48, game.getMovimientosPosibles().size());
		
	}	
	
	
}
