package parsers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import chess.Color;
import chess.Pieza;
import chess.Square;
import layers.PosicionPiezaBoard;
import layers.imp.ArrayPosicionPiezaBoard;

public class FENParserTest {

	private FENParser parser;
			
	@Before
	public void setUp() throws Exception {
		parser = new FENParser();
	}
	
	@Test
	public void testParseRankBlakRank01() {
		Pieza[] piezas = parser.parseRank("rnbqkbnr"); 
		
		assertEquals(Pieza.TORRE_NEGRO, piezas[0]);
		assertEquals(Pieza.CABALLO_NEGRO, piezas[1]);
		assertEquals(Pieza.ALFIL_NEGRO, piezas[2]);
		assertEquals(Pieza.REINA_NEGRO, piezas[3]);
		assertEquals(Pieza.REY_NEGRO, piezas[4]);
		assertEquals(Pieza.ALFIL_NEGRO, piezas[5]);
		assertEquals(Pieza.CABALLO_NEGRO, piezas[6]);
		assertEquals(Pieza.TORRE_NEGRO, piezas[7]);
	}
	
	@Test
	public void testParseRankBlakRank02() {
		Pieza[] piezas = parser.parseRank("pppppppp"); 
		
		assertEquals(Pieza.PEON_NEGRO, piezas[0]);
		assertEquals(Pieza.PEON_NEGRO, piezas[1]);
		assertEquals(Pieza.PEON_NEGRO, piezas[2]);
		assertEquals(Pieza.PEON_NEGRO, piezas[3]);
		assertEquals(Pieza.PEON_NEGRO, piezas[4]);
		assertEquals(Pieza.PEON_NEGRO, piezas[5]);
		assertEquals(Pieza.PEON_NEGRO, piezas[6]);
		assertEquals(Pieza.PEON_NEGRO, piezas[7]);
	}
	
	@Test
	public void testParseRankBlakRank03() {
		Pieza[] piezas = parser.parseRank("4R3");
		
		assertNull(piezas[0]);
		assertNull(piezas[1]);
		assertNull(piezas[2]);
		assertNull(piezas[3]);
		assertEquals(Pieza.TORRE_BLANCO, piezas[4]);
		assertNull(piezas[5]);
		assertNull(piezas[6]);
		assertNull(piezas[7]);
	}
	
	
	@Test
	public void testParseRankWhiteRank01() {
		Pieza[] piezas = parser.parseRank("RNBQKBNR");
		
		assertEquals(Pieza.TORRE_BLANCO, piezas[0]);
		assertEquals(Pieza.CABALLO_BLANCO, piezas[1]);
		assertEquals(Pieza.ALFIL_BLANCO, piezas[2]);
		assertEquals(Pieza.REINA_BLANCO, piezas[3]);
		assertEquals(Pieza.REY_BLANCO, piezas[4]);
		assertEquals(Pieza.ALFIL_BLANCO, piezas[5]);
		assertEquals(Pieza.CABALLO_BLANCO, piezas[6]);
		assertEquals(Pieza.TORRE_BLANCO, piezas[7]);
	}	
	
	@Test
	public void testParseRankWhiteRank02() {
		Pieza[] piezas = parser.parseRank("PPPPPPPP");
		
		assertEquals(Pieza.PEON_BLANCO, piezas[0]);
		assertEquals(Pieza.PEON_BLANCO, piezas[1]);
		assertEquals(Pieza.PEON_BLANCO, piezas[2]);
		assertEquals(Pieza.PEON_BLANCO, piezas[3]);
		assertEquals(Pieza.PEON_BLANCO, piezas[4]);
		assertEquals(Pieza.PEON_BLANCO, piezas[5]);
		assertEquals(Pieza.PEON_BLANCO, piezas[6]);
		assertEquals(Pieza.PEON_BLANCO, piezas[7]);
	}		
	
	@Test
	public void testParsePiecePlacement() {
		PosicionPiezaBoard tablero = new ArrayPosicionPiezaBoard(parser.parsePiecePlacement("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR"));
		
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(Square.a1));
		assertEquals(Pieza.CABALLO_BLANCO, tablero.getPieza(Square.b1));
		assertEquals(Pieza.ALFIL_BLANCO, tablero.getPieza(Square.c1));
		assertEquals(Pieza.REINA_BLANCO, tablero.getPieza(Square.d1));
		assertEquals(Pieza.REY_BLANCO, tablero.getPieza(Square.e1));
		assertEquals(Pieza.ALFIL_BLANCO, tablero.getPieza(Square.f1));
		assertEquals(Pieza.CABALLO_BLANCO, tablero.getPieza(Square.g1));
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(Square.h1));
		
		
		assertEquals(Pieza.PEON_BLANCO, tablero.getPieza(Square.a2));
		assertEquals(Pieza.PEON_BLANCO, tablero.getPieza(Square.b2));
		assertEquals(Pieza.PEON_BLANCO, tablero.getPieza(Square.c2));
		assertEquals(Pieza.PEON_BLANCO, tablero.getPieza(Square.d2));
		assertEquals(Pieza.PEON_BLANCO, tablero.getPieza(Square.e2));
		assertEquals(Pieza.PEON_BLANCO, tablero.getPieza(Square.f2));
		assertEquals(Pieza.PEON_BLANCO, tablero.getPieza(Square.g2));
		assertEquals(Pieza.PEON_BLANCO, tablero.getPieza(Square.h2));
		
		assertTrue(tablero.isEmtpy(Square.a3));
		assertTrue(tablero.isEmtpy(Square.b3));
		assertTrue(tablero.isEmtpy(Square.c3));
		assertTrue(tablero.isEmtpy(Square.d3));
		assertTrue(tablero.isEmtpy(Square.e3));
		assertTrue(tablero.isEmtpy(Square.f3));
		assertTrue(tablero.isEmtpy(Square.g3));
		assertTrue(tablero.isEmtpy(Square.h3));
		
		assertTrue(tablero.isEmtpy(Square.a4));
		assertTrue(tablero.isEmtpy(Square.b4));
		assertTrue(tablero.isEmtpy(Square.c4));
		assertTrue(tablero.isEmtpy(Square.d4));
		assertTrue(tablero.isEmtpy(Square.e4));
		assertTrue(tablero.isEmtpy(Square.f4));
		assertTrue(tablero.isEmtpy(Square.g4));
		assertTrue(tablero.isEmtpy(Square.h4));
		
		assertTrue(tablero.isEmtpy(Square.a5));
		assertTrue(tablero.isEmtpy(Square.b5));
		assertTrue(tablero.isEmtpy(Square.c5));
		assertTrue(tablero.isEmtpy(Square.d5));
		assertTrue(tablero.isEmtpy(Square.e5));
		assertTrue(tablero.isEmtpy(Square.f5));
		assertTrue(tablero.isEmtpy(Square.g5));
		assertTrue(tablero.isEmtpy(Square.h5));
		
		assertTrue(tablero.isEmtpy(Square.a6));
		assertTrue(tablero.isEmtpy(Square.b6));
		assertTrue(tablero.isEmtpy(Square.c6));
		assertTrue(tablero.isEmtpy(Square.d6));
		assertTrue(tablero.isEmtpy(Square.e6));
		assertTrue(tablero.isEmtpy(Square.f6));
		assertTrue(tablero.isEmtpy(Square.g6));
		assertTrue(tablero.isEmtpy(Square.h6));
		
		assertEquals(Pieza.PEON_NEGRO, tablero.getPieza(Square.a7));
		assertEquals(Pieza.PEON_NEGRO, tablero.getPieza(Square.b7));
		assertEquals(Pieza.PEON_NEGRO, tablero.getPieza(Square.c7));
		assertEquals(Pieza.PEON_NEGRO, tablero.getPieza(Square.d7));
		assertEquals(Pieza.PEON_NEGRO, tablero.getPieza(Square.e7));
		assertEquals(Pieza.PEON_NEGRO, tablero.getPieza(Square.f7));
		assertEquals(Pieza.PEON_NEGRO, tablero.getPieza(Square.g7));
		assertEquals(Pieza.PEON_NEGRO, tablero.getPieza(Square.h7));		
		
		assertEquals(Pieza.TORRE_NEGRO, tablero.getPieza(Square.a8));
		assertEquals(Pieza.CABALLO_NEGRO, tablero.getPieza(Square.b8));
		assertEquals(Pieza.ALFIL_NEGRO, tablero.getPieza(Square.c8));
		assertEquals(Pieza.REINA_NEGRO, tablero.getPieza(Square.d8));
		assertEquals(Pieza.REY_NEGRO, tablero.getPieza(Square.e8));
		assertEquals(Pieza.ALFIL_NEGRO, tablero.getPieza(Square.f8));
		assertEquals(Pieza.CABALLO_NEGRO, tablero.getPieza(Square.g8));
		assertEquals(Pieza.TORRE_NEGRO, tablero.getPieza(Square.h8));
	}
	
	@Test
	public void testParseColorBlanco() {
		Color actualColor = parser.parseTurno("w");
		
		assertEquals(Color.BLANCO, actualColor);
	}	
	
	@Test
	public void testParseColorNegro() {
		Color actualColor = parser.parseTurno("b");
		
		assertEquals(Color.NEGRO, actualColor);
	}	

	@Test
	public void testParsePeonPasanteSquare01() {
		Square peonPasanteSquare = parser.parsePeonPasanteSquare("-");
		
		assertNull(peonPasanteSquare);
		assertNull(parser.getPeonPasanteSquare());
	}
	
	@Test
	public void testParsePeonPasanteSquare02() {
		Square peonPasanteSquare = parser.parsePeonPasanteSquare("a3");
		
		assertEquals(Square.a3, peonPasanteSquare);
		assertEquals(Square.a3, parser.getPeonPasanteSquare());
	}	
	
	@Test
	public void testParsePeonPasanteSquare03() {
		Square peonPasanteSquare = parser.parsePeonPasanteSquare("h6");
		
		assertEquals(Square.h6, peonPasanteSquare);
		assertEquals(Square.h6, parser.getPeonPasanteSquare());
	}
	
	@Test
	public void testParseInitialFen() {
		parser.parseFEN(FENParser.INITIAL_FEN);
		
		assertEquals(Color.BLANCO, parser.getTurno());
		assertNull(parser.getPeonPasanteSquare());
		
		assertTrue(parser.isEnroqueBlancoReinaPermitido());
		assertTrue(parser.isEnroqueBlancoReyPermitido());
		
		assertTrue(parser.isEnroqueNegroReinaPermitido());
		assertTrue(parser.isEnroqueNegroReyPermitido());		
	}	
}
