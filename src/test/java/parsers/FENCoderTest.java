package parsers;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import chess.Color;
import chess.Pieza;
import chess.Square;

public class FENCoderTest {

	private FENCoder coder ;
	
	@Before
	public void setUp() throws Exception {
		coder = new FENCoder();
	}
	
	@Test
	public void testTurnoBlanco() {
		coder.withTurno(Color.BLANCO);
		
		String actual = coder.getTurno();
		
		assertEquals("w", actual);
	}
	
	@Test
	public void testTurnoNegro() {
		coder.withTurno(Color.NEGRO);
		
		String actual = coder.getTurno();
		
		assertEquals("b", actual);
	}
	
	@Test
	public void testPeonPasanteC3() {	
		coder.withPeonPasanteSquare(Square.c3);
		
		String actual = coder.getPeonPasante();
		
		assertEquals("c3", actual);
	}
	
	@Test
	public void testPeonPasanteNull() {	
		coder.withPeonPasanteSquare(null);
		
		String actual = coder.getPeonPasante();
		
		assertEquals("-", actual);
	}	
	
	@Test
	public void testCodePiecePlacement01() {
		String actual = coder.codePiecePlacementRank(new Pieza[]{Pieza.ALFIL_BLANCO, null, null, Pieza.PEON_BLANCO, null, Pieza.ALFIL_BLANCO, null, Pieza.PEON_BLANCO});
		
		assertEquals("B2P1B1P", actual);
	}
	
	@Test
	public void testCodePiecePlacement02() {
		String actual = coder.codePiecePlacementRank(new Pieza[]{null, null, null, null, null, null, null, null});
		
		assertEquals("8", actual);
	}	
	
	@Test
	public void testCodePiecePlacement03() {
		coder.withPieza(Square.a1, Pieza.TORRE_BLANCO);
		
		String actual = coder.getPiecePlacement();
		
		assertEquals("8/8/8/8/8/8/8/R7", actual);		
	}
	
	@Test
	public void testCodePiecePlacement04() {
		coder.withPieza(Square.h1, Pieza.TORRE_BLANCO);
		
		String actual = coder.getPiecePlacement();
		
		assertEquals("8/8/8/8/8/8/8/7R", actual);		
	}
	
	@Test
	public void testCodePiecePlacement05() {
		coder.withPieza(Square.a8, Pieza.TORRE_NEGRO);
		
		String actual = coder.getPiecePlacement();
		
		assertEquals("r7/8/8/8/8/8/8/8", actual);		
	}		
	

	@Test
	public void testCodePiecePlacement06() {
		coder.withPieza(Square.a8, Pieza.TORRE_NEGRO);
		coder.withPieza(Square.b8, Pieza.CABALLO_NEGRO);
		coder.withPieza(Square.c8, Pieza.ALFIL_NEGRO);
		coder.withPieza(Square.d8, Pieza.REINA_NEGRO);
		coder.withPieza(Square.e8, Pieza.REY_NEGRO);
		coder.withPieza(Square.f8, Pieza.ALFIL_NEGRO);
		coder.withPieza(Square.g8, Pieza.CABALLO_NEGRO);
		coder.withPieza(Square.h8, Pieza.TORRE_NEGRO);
		
		coder.withPieza(Square.a7, Pieza.PEON_NEGRO);
		coder.withPieza(Square.b7, Pieza.PEON_NEGRO);
		coder.withPieza(Square.c7, Pieza.PEON_NEGRO);
		coder.withPieza(Square.d7, Pieza.PEON_NEGRO);
		coder.withPieza(Square.e7, Pieza.PEON_NEGRO);
		coder.withPieza(Square.f7, Pieza.PEON_NEGRO);
		coder.withPieza(Square.g7, Pieza.PEON_NEGRO);
		coder.withPieza(Square.h7, Pieza.PEON_NEGRO);
		

		coder.withPieza(Square.a2, Pieza.PEON_BLANCO);
		coder.withPieza(Square.b2, Pieza.PEON_BLANCO);
		coder.withPieza(Square.c2, Pieza.PEON_BLANCO);
		coder.withPieza(Square.d2, Pieza.PEON_BLANCO);
		coder.withPieza(Square.e2, Pieza.PEON_BLANCO);
		coder.withPieza(Square.f2, Pieza.PEON_BLANCO);
		coder.withPieza(Square.g2, Pieza.PEON_BLANCO);
		coder.withPieza(Square.h2, Pieza.PEON_BLANCO);
		
		coder.withPieza(Square.a1, Pieza.TORRE_BLANCO);
		coder.withPieza(Square.b1, Pieza.CABALLO_BLANCO);
		coder.withPieza(Square.c1, Pieza.ALFIL_BLANCO);
		coder.withPieza(Square.d1, Pieza.REINA_BLANCO);
		coder.withPieza(Square.e1, Pieza.REY_BLANCO);
		coder.withPieza(Square.f1, Pieza.ALFIL_BLANCO);
		coder.withPieza(Square.g1, Pieza.CABALLO_BLANCO);
		coder.withPieza(Square.h1, Pieza.TORRE_BLANCO);		

		
		String actual = coder.getPiecePlacement();
		
		assertEquals("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR", actual);		
	}

}
