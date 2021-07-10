package parsers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import builder.ChessBuilder;
import chess.Color;
import chess.Pieza;
import chess.Square;

public class FENParserTest {

	private FENParser parser = null;
	
	private Color turno;
	private Square peonPasanteSquare;
	private boolean enroqueNegroReyPermitido;
	private boolean enroqueNegroReinaPermitido;
	private boolean enroqueBlancoReyPermitido;
	private boolean enroqueBlancoReinaPermitido;
	
	private Pieza[][] tablero = null;
			
	@Before
	public void setUp() throws Exception {
		tablero = new Pieza[8][8];
		
		parser = new FENParser(new ChessBuilder() {
			@Override
			public void withPieza(Square square, Pieza pieza) {
				FENParserTest.this.tablero[square.getRank()][square.getFile()] = pieza;
			}
			
			@Override
			public void withTurno(Color turno) {
				FENParserTest.this.turno = turno;
			}
			
			@Override
			public void withPeonPasanteSquare(Square peonPasanteSquare) {
				FENParserTest.this.peonPasanteSquare = peonPasanteSquare;
			}
			
			@Override
			public void withEnroqueNegroReyPermitido(boolean enroqueNegroReyPermitido) {
				FENParserTest.this.enroqueNegroReyPermitido = enroqueNegroReyPermitido;
			}
			
			@Override
			public void withEnroqueNegroReinaPermitido(boolean enroqueNegroReinaPermitido) {
				FENParserTest.this.enroqueNegroReinaPermitido = enroqueNegroReinaPermitido;
			}
			
			@Override
			public void withEnroqueBlancoReyPermitido(boolean enroqueBlancoReyPermitido) {
				FENParserTest.this.enroqueBlancoReyPermitido = enroqueBlancoReyPermitido;
			}
			
			@Override
			public void withEnroqueBlancoReinaPermitido(boolean enroqueBlancoReinaPermitido) {
				FENParserTest.this.enroqueBlancoReinaPermitido = enroqueBlancoReinaPermitido;
			}
		});
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
		Pieza[][] tablero = parser.parsePieces("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR");
		
		assertEquals(Pieza.TORRE_BLANCO, getPieza(tablero, Square.a1));
		assertEquals(Pieza.CABALLO_BLANCO, getPieza(tablero, Square.b1));
		assertEquals(Pieza.ALFIL_BLANCO, getPieza(tablero, Square.c1));
		assertEquals(Pieza.REINA_BLANCO, getPieza(tablero, Square.d1));
		assertEquals(Pieza.REY_BLANCO, getPieza(tablero, Square.e1));
		assertEquals(Pieza.ALFIL_BLANCO, getPieza(tablero, Square.f1));
		assertEquals(Pieza.CABALLO_BLANCO, getPieza(tablero, Square.g1));
		assertEquals(Pieza.TORRE_BLANCO, getPieza(tablero, Square.h1));
		
		
		assertEquals(Pieza.PEON_BLANCO, getPieza(tablero, Square.a2));
		assertEquals(Pieza.PEON_BLANCO, getPieza(tablero, Square.b2));
		assertEquals(Pieza.PEON_BLANCO, getPieza(tablero, Square.c2));
		assertEquals(Pieza.PEON_BLANCO, getPieza(tablero, Square.d2));
		assertEquals(Pieza.PEON_BLANCO, getPieza(tablero, Square.e2));
		assertEquals(Pieza.PEON_BLANCO, getPieza(tablero, Square.f2));
		assertEquals(Pieza.PEON_BLANCO, getPieza(tablero, Square.g2));
		assertEquals(Pieza.PEON_BLANCO, getPieza(tablero, Square.h2));
		
		assertTrue( isEmtpy(tablero, Square.a3) );
		assertTrue( isEmtpy(tablero, Square.b3) );
		assertTrue( isEmtpy(tablero, Square.c3) );
		assertTrue( isEmtpy(tablero, Square.d3) );
		assertTrue( isEmtpy(tablero, Square.e3) );
		assertTrue( isEmtpy(tablero, Square.f3) );
		assertTrue( isEmtpy(tablero, Square.g3) );
		assertTrue( isEmtpy(tablero, Square.h3) );
		
		assertTrue( isEmtpy(tablero, Square.a4) );
		assertTrue( isEmtpy(tablero, Square.b4) );
		assertTrue( isEmtpy(tablero, Square.c4) );
		assertTrue( isEmtpy(tablero, Square.d4) );
		assertTrue( isEmtpy(tablero, Square.e4) );
		assertTrue( isEmtpy(tablero, Square.f4) );
		assertTrue( isEmtpy(tablero, Square.g4) );
		assertTrue( isEmtpy(tablero, Square.h4) );
		
		assertTrue( isEmtpy(tablero, Square.a5) );
		assertTrue( isEmtpy(tablero, Square.b5) );
		assertTrue( isEmtpy(tablero, Square.c5) );
		assertTrue( isEmtpy(tablero, Square.d5) );
		assertTrue( isEmtpy(tablero, Square.e5) );
		assertTrue( isEmtpy(tablero, Square.f5) );
		assertTrue( isEmtpy(tablero, Square.g5) );
		assertTrue( isEmtpy(tablero, Square.h5) );
		
		assertTrue( isEmtpy(tablero, Square.a6) );
		assertTrue( isEmtpy(tablero, Square.b6) );
		assertTrue( isEmtpy(tablero, Square.c6) );
		assertTrue( isEmtpy(tablero, Square.d6) );
		assertTrue( isEmtpy(tablero, Square.e6) );
		assertTrue( isEmtpy(tablero, Square.f6) );
		assertTrue( isEmtpy(tablero, Square.g6) );
		assertTrue( isEmtpy(tablero, Square.h6) );
		
		assertEquals(Pieza.PEON_NEGRO, getPieza(tablero, Square.a7));
		assertEquals(Pieza.PEON_NEGRO, getPieza(tablero, Square.b7));
		assertEquals(Pieza.PEON_NEGRO, getPieza(tablero, Square.c7));
		assertEquals(Pieza.PEON_NEGRO, getPieza(tablero, Square.d7));
		assertEquals(Pieza.PEON_NEGRO, getPieza(tablero, Square.e7));
		assertEquals(Pieza.PEON_NEGRO, getPieza(tablero, Square.f7));
		assertEquals(Pieza.PEON_NEGRO, getPieza(tablero, Square.g7));
		assertEquals(Pieza.PEON_NEGRO, getPieza(tablero, Square.h7));		
		
		assertEquals(Pieza.TORRE_NEGRO, getPieza(tablero, Square.a8));
		assertEquals(Pieza.CABALLO_NEGRO, getPieza(tablero, Square.b8));
		assertEquals(Pieza.ALFIL_NEGRO, getPieza(tablero, Square.c8));
		assertEquals(Pieza.REINA_NEGRO, getPieza(tablero, Square.d8));
		assertEquals(Pieza.REY_NEGRO, getPieza(tablero, Square.e8));
		assertEquals(Pieza.ALFIL_NEGRO, getPieza(tablero, Square.f8));
		assertEquals(Pieza.CABALLO_NEGRO, getPieza(tablero, Square.g8));
		assertEquals(Pieza.TORRE_NEGRO, getPieza(tablero, Square.h8));
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
	}
	
	@Test
	public void testParsePeonPasanteSquare02() {
		Square peonPasanteSquare = parser.parsePeonPasanteSquare("a3");
		
		assertEquals(Square.a3, peonPasanteSquare);
	}	
	
	@Test
	public void testParsePeonPasanteSquare03() {
		Square peonPasanteSquare = parser.parsePeonPasanteSquare("h6");
		
		assertEquals(Square.h6, peonPasanteSquare);
	}
	
	@Test
	public void testParseInitialFen() {
		parser.parseFEN(FENParser.INITIAL_FEN);
		
		assertEquals(Color.BLANCO, this.turno);
		
		assertTrue(this.enroqueBlancoReinaPermitido);
		assertTrue(this.enroqueBlancoReyPermitido);
		
		assertTrue(this.enroqueNegroReinaPermitido);
		assertTrue(this.enroqueNegroReyPermitido);		
		
		assertNull(this.peonPasanteSquare);
		
		assertEquals(Pieza.TORRE_BLANCO, getPieza(tablero, Square.a1));
		assertEquals(Pieza.CABALLO_BLANCO, getPieza(tablero, Square.b1));
		assertEquals(Pieza.ALFIL_BLANCO, getPieza(tablero, Square.c1));
		assertEquals(Pieza.REINA_BLANCO, getPieza(tablero, Square.d1));
		assertEquals(Pieza.REY_BLANCO, getPieza(tablero, Square.e1));
		assertEquals(Pieza.ALFIL_BLANCO, getPieza(tablero, Square.f1));
		assertEquals(Pieza.CABALLO_BLANCO, getPieza(tablero, Square.g1));
		assertEquals(Pieza.TORRE_BLANCO, getPieza(tablero, Square.h1));
		
		
		assertEquals(Pieza.PEON_BLANCO, getPieza(tablero, Square.a2));
		assertEquals(Pieza.PEON_BLANCO, getPieza(tablero, Square.b2));
		assertEquals(Pieza.PEON_BLANCO, getPieza(tablero, Square.c2));
		assertEquals(Pieza.PEON_BLANCO, getPieza(tablero, Square.d2));
		assertEquals(Pieza.PEON_BLANCO, getPieza(tablero, Square.e2));
		assertEquals(Pieza.PEON_BLANCO, getPieza(tablero, Square.f2));
		assertEquals(Pieza.PEON_BLANCO, getPieza(tablero, Square.g2));
		assertEquals(Pieza.PEON_BLANCO, getPieza(tablero, Square.h2));
		
		assertTrue( isEmtpy(tablero, Square.a3) );
		assertTrue( isEmtpy(tablero, Square.b3) );
		assertTrue( isEmtpy(tablero, Square.c3) );
		assertTrue( isEmtpy(tablero, Square.d3) );
		assertTrue( isEmtpy(tablero, Square.e3) );
		assertTrue( isEmtpy(tablero, Square.f3) );
		assertTrue( isEmtpy(tablero, Square.g3) );
		assertTrue( isEmtpy(tablero, Square.h3) );
		
		assertTrue( isEmtpy(tablero, Square.a4) );
		assertTrue( isEmtpy(tablero, Square.b4) );
		assertTrue( isEmtpy(tablero, Square.c4) );
		assertTrue( isEmtpy(tablero, Square.d4) );
		assertTrue( isEmtpy(tablero, Square.e4) );
		assertTrue( isEmtpy(tablero, Square.f4) );
		assertTrue( isEmtpy(tablero, Square.g4) );
		assertTrue( isEmtpy(tablero, Square.h4) );
		
		assertTrue( isEmtpy(tablero, Square.a5) );
		assertTrue( isEmtpy(tablero, Square.b5) );
		assertTrue( isEmtpy(tablero, Square.c5) );
		assertTrue( isEmtpy(tablero, Square.d5) );
		assertTrue( isEmtpy(tablero, Square.e5) );
		assertTrue( isEmtpy(tablero, Square.f5) );
		assertTrue( isEmtpy(tablero, Square.g5) );
		assertTrue( isEmtpy(tablero, Square.h5) );
		
		assertTrue( isEmtpy(tablero, Square.a6) );
		assertTrue( isEmtpy(tablero, Square.b6) );
		assertTrue( isEmtpy(tablero, Square.c6) );
		assertTrue( isEmtpy(tablero, Square.d6) );
		assertTrue( isEmtpy(tablero, Square.e6) );
		assertTrue( isEmtpy(tablero, Square.f6) );
		assertTrue( isEmtpy(tablero, Square.g6) );
		assertTrue( isEmtpy(tablero, Square.h6) );
		
		assertEquals(Pieza.PEON_NEGRO, getPieza(tablero, Square.a7));
		assertEquals(Pieza.PEON_NEGRO, getPieza(tablero, Square.b7));
		assertEquals(Pieza.PEON_NEGRO, getPieza(tablero, Square.c7));
		assertEquals(Pieza.PEON_NEGRO, getPieza(tablero, Square.d7));
		assertEquals(Pieza.PEON_NEGRO, getPieza(tablero, Square.e7));
		assertEquals(Pieza.PEON_NEGRO, getPieza(tablero, Square.f7));
		assertEquals(Pieza.PEON_NEGRO, getPieza(tablero, Square.g7));
		assertEquals(Pieza.PEON_NEGRO, getPieza(tablero, Square.h7));		
		
		assertEquals(Pieza.TORRE_NEGRO, getPieza(tablero, Square.a8));
		assertEquals(Pieza.CABALLO_NEGRO, getPieza(tablero, Square.b8));
		assertEquals(Pieza.ALFIL_NEGRO, getPieza(tablero, Square.c8));
		assertEquals(Pieza.REINA_NEGRO, getPieza(tablero, Square.d8));
		assertEquals(Pieza.REY_NEGRO, getPieza(tablero, Square.e8));
		assertEquals(Pieza.ALFIL_NEGRO, getPieza(tablero, Square.f8));
		assertEquals(Pieza.CABALLO_NEGRO, getPieza(tablero, Square.g8));
		assertEquals(Pieza.TORRE_NEGRO, getPieza(tablero, Square.h8));		
	}

	private boolean isEmtpy(Pieza[][] tablero, Square square) {
		return getPieza(tablero, square) == null;
	}

	private Pieza getPieza(Pieza[][] tablero, Square square) {
		return tablero[square.getRank()][square.getFile()];
	}	
}
