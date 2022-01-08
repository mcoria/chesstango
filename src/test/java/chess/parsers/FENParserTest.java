package chess.parsers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import chess.Color;
import chess.Pieza;
import chess.Square;
import chess.builder.ChessBuilder;
import chess.parsers.FENParser;

/**
 * @author Mauricio Coria
 *
 */
public class FENParserTest {

	private FENParser parser = null;
	
	private Color turno;
	private Square peonPasanteSquare;
	private boolean enroqueNegroKingPermitido;
	private boolean enroqueNegroReinaPermitido;
	private boolean enroqueBlancoKingPermitido;
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
			public void withPawnPasanteSquare(Square peonPasanteSquare) {
				FENParserTest.this.peonPasanteSquare = peonPasanteSquare;
			}
			
			@Override
			public void withCastlingBlackKingPermitido(boolean enroqueNegroKingPermitido) {
				FENParserTest.this.enroqueNegroKingPermitido = enroqueNegroKingPermitido;
			}
			
			@Override
			public void withCastlingBlackReinaPermitido(boolean enroqueNegroReinaPermitido) {
				FENParserTest.this.enroqueNegroReinaPermitido = enroqueNegroReinaPermitido;
			}
			
			@Override
			public void withCastlingWhiteKingPermitido(boolean enroqueBlancoKingPermitido) {
				FENParserTest.this.enroqueBlancoKingPermitido = enroqueBlancoKingPermitido;
			}
			
			@Override
			public void withCastlingWhiteReinaPermitido(boolean enroqueBlancoReinaPermitido) {
				FENParserTest.this.enroqueBlancoReinaPermitido = enroqueBlancoReinaPermitido;
			}
		});
	}
	
	@Test
	public void testParseRankBlakRank01() {
		Pieza[] piezas = parser.parseRank("rnbqkbnr"); 
		
		assertEquals(Pieza.ROOK_BLACK, piezas[0]);
		assertEquals(Pieza.KNIGHT_BLACK, piezas[1]);
		assertEquals(Pieza.BISHOP_BLACK, piezas[2]);
		assertEquals(Pieza.QUEEN_BLACK, piezas[3]);
		assertEquals(Pieza.KING_BLACK, piezas[4]);
		assertEquals(Pieza.BISHOP_BLACK, piezas[5]);
		assertEquals(Pieza.KNIGHT_BLACK, piezas[6]);
		assertEquals(Pieza.ROOK_BLACK, piezas[7]);
	}
	
	@Test
	public void testParseRankBlakRank02() {
		Pieza[] piezas = parser.parseRank("pppppppp"); 
		
		assertEquals(Pieza.PAWN_BLACK, piezas[0]);
		assertEquals(Pieza.PAWN_BLACK, piezas[1]);
		assertEquals(Pieza.PAWN_BLACK, piezas[2]);
		assertEquals(Pieza.PAWN_BLACK, piezas[3]);
		assertEquals(Pieza.PAWN_BLACK, piezas[4]);
		assertEquals(Pieza.PAWN_BLACK, piezas[5]);
		assertEquals(Pieza.PAWN_BLACK, piezas[6]);
		assertEquals(Pieza.PAWN_BLACK, piezas[7]);
	}
	
	@Test
	public void testParseRankBlakRank03() {
		Pieza[] piezas = parser.parseRank("4R3");
		
		assertNull(piezas[0]);
		assertNull(piezas[1]);
		assertNull(piezas[2]);
		assertNull(piezas[3]);
		assertEquals(Pieza.ROOK_WHITE, piezas[4]);
		assertNull(piezas[5]);
		assertNull(piezas[6]);
		assertNull(piezas[7]);
	}
	
	
	@Test
	public void testParseRankWhiteRank01() {
		Pieza[] piezas = parser.parseRank("RNBQKBNR");
		
		assertEquals(Pieza.ROOK_WHITE, piezas[0]);
		assertEquals(Pieza.KNIGHT_WHITE, piezas[1]);
		assertEquals(Pieza.BISHOP_WHITE, piezas[2]);
		assertEquals(Pieza.QUEEN_WHITE, piezas[3]);
		assertEquals(Pieza.KING_WHITE, piezas[4]);
		assertEquals(Pieza.BISHOP_WHITE, piezas[5]);
		assertEquals(Pieza.KNIGHT_WHITE, piezas[6]);
		assertEquals(Pieza.ROOK_WHITE, piezas[7]);
	}	
	
	@Test
	public void testParseRankWhiteRank02() {
		Pieza[] piezas = parser.parseRank("PPPPPPPP");
		
		assertEquals(Pieza.PAWN_WHITE, piezas[0]);
		assertEquals(Pieza.PAWN_WHITE, piezas[1]);
		assertEquals(Pieza.PAWN_WHITE, piezas[2]);
		assertEquals(Pieza.PAWN_WHITE, piezas[3]);
		assertEquals(Pieza.PAWN_WHITE, piezas[4]);
		assertEquals(Pieza.PAWN_WHITE, piezas[5]);
		assertEquals(Pieza.PAWN_WHITE, piezas[6]);
		assertEquals(Pieza.PAWN_WHITE, piezas[7]);
	}	
	
	@Test
	public void testParsePiecePlacement() {
		Pieza[][] tablero = parser.parsePieces("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR");
		
		assertEquals(Pieza.ROOK_WHITE, getPieza(tablero, Square.a1));
		assertEquals(Pieza.KNIGHT_WHITE, getPieza(tablero, Square.b1));
		assertEquals(Pieza.BISHOP_WHITE, getPieza(tablero, Square.c1));
		assertEquals(Pieza.QUEEN_WHITE, getPieza(tablero, Square.d1));
		assertEquals(Pieza.KING_WHITE, getPieza(tablero, Square.e1));
		assertEquals(Pieza.BISHOP_WHITE, getPieza(tablero, Square.f1));
		assertEquals(Pieza.KNIGHT_WHITE, getPieza(tablero, Square.g1));
		assertEquals(Pieza.ROOK_WHITE, getPieza(tablero, Square.h1));
		
		
		assertEquals(Pieza.PAWN_WHITE, getPieza(tablero, Square.a2));
		assertEquals(Pieza.PAWN_WHITE, getPieza(tablero, Square.b2));
		assertEquals(Pieza.PAWN_WHITE, getPieza(tablero, Square.c2));
		assertEquals(Pieza.PAWN_WHITE, getPieza(tablero, Square.d2));
		assertEquals(Pieza.PAWN_WHITE, getPieza(tablero, Square.e2));
		assertEquals(Pieza.PAWN_WHITE, getPieza(tablero, Square.f2));
		assertEquals(Pieza.PAWN_WHITE, getPieza(tablero, Square.g2));
		assertEquals(Pieza.PAWN_WHITE, getPieza(tablero, Square.h2));
		
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
		
		assertEquals(Pieza.PAWN_BLACK, getPieza(tablero, Square.a7));
		assertEquals(Pieza.PAWN_BLACK, getPieza(tablero, Square.b7));
		assertEquals(Pieza.PAWN_BLACK, getPieza(tablero, Square.c7));
		assertEquals(Pieza.PAWN_BLACK, getPieza(tablero, Square.d7));
		assertEquals(Pieza.PAWN_BLACK, getPieza(tablero, Square.e7));
		assertEquals(Pieza.PAWN_BLACK, getPieza(tablero, Square.f7));
		assertEquals(Pieza.PAWN_BLACK, getPieza(tablero, Square.g7));
		assertEquals(Pieza.PAWN_BLACK, getPieza(tablero, Square.h7));		
		
		assertEquals(Pieza.ROOK_BLACK, getPieza(tablero, Square.a8));
		assertEquals(Pieza.KNIGHT_BLACK, getPieza(tablero, Square.b8));
		assertEquals(Pieza.BISHOP_BLACK, getPieza(tablero, Square.c8));
		assertEquals(Pieza.QUEEN_BLACK, getPieza(tablero, Square.d8));
		assertEquals(Pieza.KING_BLACK, getPieza(tablero, Square.e8));
		assertEquals(Pieza.BISHOP_BLACK, getPieza(tablero, Square.f8));
		assertEquals(Pieza.KNIGHT_BLACK, getPieza(tablero, Square.g8));
		assertEquals(Pieza.ROOK_BLACK, getPieza(tablero, Square.h8));
	}	
	
	@Test
	public void testParseColorBlanco() {
		Color actualColor = parser.parseTurno("w");
		
		assertEquals(Color.WHITE, actualColor);
	}	
	
	@Test
	public void testParseColorNegro() {
		Color actualColor = parser.parseTurno("b");
		
		assertEquals(Color.BLACK, actualColor);
	}	

	@Test
	public void testParsePawnPasanteSquare01() {
		Square peonPasanteSquare = parser.parsePawnPasanteSquare("-");
		
		assertNull(peonPasanteSquare);
	}
	
	@Test
	public void testParsePawnPasanteSquare02() {
		Square peonPasanteSquare = parser.parsePawnPasanteSquare("a3");
		
		assertEquals(Square.a3, peonPasanteSquare);
	}	
	
	@Test
	public void testParsePawnPasanteSquare03() {
		Square peonPasanteSquare = parser.parsePawnPasanteSquare("h6");
		
		assertEquals(Square.h6, peonPasanteSquare);
	}
	
	@Test
	public void testParseInitialFen() {
		parser.parseFEN(FENParser.INITIAL_FEN);
		
		assertEquals(Color.WHITE, this.turno);
		
		assertTrue(this.enroqueBlancoReinaPermitido);
		assertTrue(this.enroqueBlancoKingPermitido);
		
		assertTrue(this.enroqueNegroReinaPermitido);
		assertTrue(this.enroqueNegroKingPermitido);		
		
		assertNull(this.peonPasanteSquare);
		
		assertEquals(Pieza.ROOK_WHITE, getPieza(tablero, Square.a1));
		assertEquals(Pieza.KNIGHT_WHITE, getPieza(tablero, Square.b1));
		assertEquals(Pieza.BISHOP_WHITE, getPieza(tablero, Square.c1));
		assertEquals(Pieza.QUEEN_WHITE, getPieza(tablero, Square.d1));
		assertEquals(Pieza.KING_WHITE, getPieza(tablero, Square.e1));
		assertEquals(Pieza.BISHOP_WHITE, getPieza(tablero, Square.f1));
		assertEquals(Pieza.KNIGHT_WHITE, getPieza(tablero, Square.g1));
		assertEquals(Pieza.ROOK_WHITE, getPieza(tablero, Square.h1));
		
		
		assertEquals(Pieza.PAWN_WHITE, getPieza(tablero, Square.a2));
		assertEquals(Pieza.PAWN_WHITE, getPieza(tablero, Square.b2));
		assertEquals(Pieza.PAWN_WHITE, getPieza(tablero, Square.c2));
		assertEquals(Pieza.PAWN_WHITE, getPieza(tablero, Square.d2));
		assertEquals(Pieza.PAWN_WHITE, getPieza(tablero, Square.e2));
		assertEquals(Pieza.PAWN_WHITE, getPieza(tablero, Square.f2));
		assertEquals(Pieza.PAWN_WHITE, getPieza(tablero, Square.g2));
		assertEquals(Pieza.PAWN_WHITE, getPieza(tablero, Square.h2));
		
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
		
		assertEquals(Pieza.PAWN_BLACK, getPieza(tablero, Square.a7));
		assertEquals(Pieza.PAWN_BLACK, getPieza(tablero, Square.b7));
		assertEquals(Pieza.PAWN_BLACK, getPieza(tablero, Square.c7));
		assertEquals(Pieza.PAWN_BLACK, getPieza(tablero, Square.d7));
		assertEquals(Pieza.PAWN_BLACK, getPieza(tablero, Square.e7));
		assertEquals(Pieza.PAWN_BLACK, getPieza(tablero, Square.f7));
		assertEquals(Pieza.PAWN_BLACK, getPieza(tablero, Square.g7));
		assertEquals(Pieza.PAWN_BLACK, getPieza(tablero, Square.h7));		
		
		assertEquals(Pieza.ROOK_BLACK, getPieza(tablero, Square.a8));
		assertEquals(Pieza.KNIGHT_BLACK, getPieza(tablero, Square.b8));
		assertEquals(Pieza.BISHOP_BLACK, getPieza(tablero, Square.c8));
		assertEquals(Pieza.QUEEN_BLACK, getPieza(tablero, Square.d8));
		assertEquals(Pieza.KING_BLACK, getPieza(tablero, Square.e8));
		assertEquals(Pieza.BISHOP_BLACK, getPieza(tablero, Square.f8));
		assertEquals(Pieza.KNIGHT_BLACK, getPieza(tablero, Square.g8));
		assertEquals(Pieza.ROOK_BLACK, getPieza(tablero, Square.h8));		
	}

	private boolean isEmtpy(Pieza[][] tablero, Square square) {
		return getPieza(tablero, square) == null;
	}

	private Pieza getPieza(Pieza[][] tablero, Square square) {
		return tablero[square.getRank()][square.getFile()];
	}	
}
