package chess.board.fen;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import chess.board.Color;
import chess.board.Piece;
import chess.board.Square;
import chess.board.builder.ChessPositionBuilder;
import chess.board.fen.FENDecoder;

/**
 * @author Mauricio Coria
 *
 */
public class FENDecoderTest {

	private FENDecoder parser = null;
	
	private Color turno;
	private Square pawnPasanteSquare;
	private boolean castlingBlackKingAllowed;
	private boolean castlingBlackQueenAllowed;
	private boolean castlingWhiteKingAllowed;
	private boolean castlingWhiteQueenAllowed;
	
	private Piece[][] tablero = null;
			
	@Before
	public void setUp() throws Exception {
		tablero = new Piece[8][8];
		
		parser = new FENDecoder(new ChessPositionBuilder<Object>() {
			@Override
			public ChessPositionBuilder<Object> withPieza(Square square, Piece piece) {
				tablero[square.getRank()][square.getFile()] = piece;
				return this;
			}
			
			@Override
			public ChessPositionBuilder<Object> withTurno(Color turno) {
				FENDecoderTest.this.turno = turno;
				return this;
			}
			
			@Override
			public ChessPositionBuilder<Object> withEnPassantSquare(Square pawnPasanteSquare) {
				FENDecoderTest.this.pawnPasanteSquare = pawnPasanteSquare;
				return this;
			}
			
			@Override
			public ChessPositionBuilder<Object> withCastlingBlackKingAllowed(boolean castlingBlackKingAllowed) {
				FENDecoderTest.this.castlingBlackKingAllowed = castlingBlackKingAllowed;
				return this;
			}
			
			@Override
			public ChessPositionBuilder<Object> withCastlingBlackQueenAllowed(boolean castlingBlackQueenAllowed) {
				FENDecoderTest.this.castlingBlackQueenAllowed = castlingBlackQueenAllowed;
				return this;
			}
			
			@Override
			public ChessPositionBuilder<Object> withCastlingWhiteKingAllowed(boolean castlingWhiteKingAllowed) {
				FENDecoderTest.this.castlingWhiteKingAllowed = castlingWhiteKingAllowed;
				return this;
			}
			
			@Override
			public ChessPositionBuilder<Object> withCastlingWhiteQueenAllowed(boolean castlingWhiteQueenAllowed) {
				FENDecoderTest.this.castlingWhiteQueenAllowed = castlingWhiteQueenAllowed;
				return this;
			}

			@Override
			public Object getResult() {
				// TODO Auto-generated method stub
				return null;
			}
		});
	}
	
	@Test
	public void testParseRankBlakRank01() {
		Piece[] piezas = parser.parseRank("rnbqkbnr"); 
		
		assertEquals(Piece.ROOK_BLACK, piezas[0]);
		assertEquals(Piece.KNIGHT_BLACK, piezas[1]);
		assertEquals(Piece.BISHOP_BLACK, piezas[2]);
		assertEquals(Piece.QUEEN_BLACK, piezas[3]);
		assertEquals(Piece.KING_BLACK, piezas[4]);
		assertEquals(Piece.BISHOP_BLACK, piezas[5]);
		assertEquals(Piece.KNIGHT_BLACK, piezas[6]);
		assertEquals(Piece.ROOK_BLACK, piezas[7]);
	}
	
	@Test
	public void testParseRankBlakRank02() {
		Piece[] piezas = parser.parseRank("pppppppp"); 
		
		assertEquals(Piece.PAWN_BLACK, piezas[0]);
		assertEquals(Piece.PAWN_BLACK, piezas[1]);
		assertEquals(Piece.PAWN_BLACK, piezas[2]);
		assertEquals(Piece.PAWN_BLACK, piezas[3]);
		assertEquals(Piece.PAWN_BLACK, piezas[4]);
		assertEquals(Piece.PAWN_BLACK, piezas[5]);
		assertEquals(Piece.PAWN_BLACK, piezas[6]);
		assertEquals(Piece.PAWN_BLACK, piezas[7]);
	}
	
	@Test
	public void testParseRankBlakRank03() {
		Piece[] piezas = parser.parseRank("4R3");
		
		assertNull(piezas[0]);
		assertNull(piezas[1]);
		assertNull(piezas[2]);
		assertNull(piezas[3]);
		assertEquals(Piece.ROOK_WHITE, piezas[4]);
		assertNull(piezas[5]);
		assertNull(piezas[6]);
		assertNull(piezas[7]);
	}
	
	
	@Test
	public void testParseRankWhiteRank01() {
		Piece[] piezas = parser.parseRank("RNBQKBNR");
		
		assertEquals(Piece.ROOK_WHITE, piezas[0]);
		assertEquals(Piece.KNIGHT_WHITE, piezas[1]);
		assertEquals(Piece.BISHOP_WHITE, piezas[2]);
		assertEquals(Piece.QUEEN_WHITE, piezas[3]);
		assertEquals(Piece.KING_WHITE, piezas[4]);
		assertEquals(Piece.BISHOP_WHITE, piezas[5]);
		assertEquals(Piece.KNIGHT_WHITE, piezas[6]);
		assertEquals(Piece.ROOK_WHITE, piezas[7]);
	}	
	
	@Test
	public void testParseRankWhiteRank02() {
		Piece[] piezas = parser.parseRank("PPPPPPPP");
		
		assertEquals(Piece.PAWN_WHITE, piezas[0]);
		assertEquals(Piece.PAWN_WHITE, piezas[1]);
		assertEquals(Piece.PAWN_WHITE, piezas[2]);
		assertEquals(Piece.PAWN_WHITE, piezas[3]);
		assertEquals(Piece.PAWN_WHITE, piezas[4]);
		assertEquals(Piece.PAWN_WHITE, piezas[5]);
		assertEquals(Piece.PAWN_WHITE, piezas[6]);
		assertEquals(Piece.PAWN_WHITE, piezas[7]);
	}	
	
	@Test
	public void testParsePiecePlacement() {
		Piece[][] tablero = parser.parsePieces("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR");
		
		assertEquals(Piece.ROOK_WHITE, getPieza(tablero, Square.a1));
		assertEquals(Piece.KNIGHT_WHITE, getPieza(tablero, Square.b1));
		assertEquals(Piece.BISHOP_WHITE, getPieza(tablero, Square.c1));
		assertEquals(Piece.QUEEN_WHITE, getPieza(tablero, Square.d1));
		assertEquals(Piece.KING_WHITE, getPieza(tablero, Square.e1));
		assertEquals(Piece.BISHOP_WHITE, getPieza(tablero, Square.f1));
		assertEquals(Piece.KNIGHT_WHITE, getPieza(tablero, Square.g1));
		assertEquals(Piece.ROOK_WHITE, getPieza(tablero, Square.h1));
		
		
		assertEquals(Piece.PAWN_WHITE, getPieza(tablero, Square.a2));
		assertEquals(Piece.PAWN_WHITE, getPieza(tablero, Square.b2));
		assertEquals(Piece.PAWN_WHITE, getPieza(tablero, Square.c2));
		assertEquals(Piece.PAWN_WHITE, getPieza(tablero, Square.d2));
		assertEquals(Piece.PAWN_WHITE, getPieza(tablero, Square.e2));
		assertEquals(Piece.PAWN_WHITE, getPieza(tablero, Square.f2));
		assertEquals(Piece.PAWN_WHITE, getPieza(tablero, Square.g2));
		assertEquals(Piece.PAWN_WHITE, getPieza(tablero, Square.h2));
		
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
		
		assertEquals(Piece.PAWN_BLACK, getPieza(tablero, Square.a7));
		assertEquals(Piece.PAWN_BLACK, getPieza(tablero, Square.b7));
		assertEquals(Piece.PAWN_BLACK, getPieza(tablero, Square.c7));
		assertEquals(Piece.PAWN_BLACK, getPieza(tablero, Square.d7));
		assertEquals(Piece.PAWN_BLACK, getPieza(tablero, Square.e7));
		assertEquals(Piece.PAWN_BLACK, getPieza(tablero, Square.f7));
		assertEquals(Piece.PAWN_BLACK, getPieza(tablero, Square.g7));
		assertEquals(Piece.PAWN_BLACK, getPieza(tablero, Square.h7));		
		
		assertEquals(Piece.ROOK_BLACK, getPieza(tablero, Square.a8));
		assertEquals(Piece.KNIGHT_BLACK, getPieza(tablero, Square.b8));
		assertEquals(Piece.BISHOP_BLACK, getPieza(tablero, Square.c8));
		assertEquals(Piece.QUEEN_BLACK, getPieza(tablero, Square.d8));
		assertEquals(Piece.KING_BLACK, getPieza(tablero, Square.e8));
		assertEquals(Piece.BISHOP_BLACK, getPieza(tablero, Square.f8));
		assertEquals(Piece.KNIGHT_BLACK, getPieza(tablero, Square.g8));
		assertEquals(Piece.ROOK_BLACK, getPieza(tablero, Square.h8));
	}	
	
	@Test
	public void testParseColorWhite() {
		Color actualColor = parser.parseTurno("w");
		
		assertEquals(Color.WHITE, actualColor);
	}	
	
	@Test
	public void testParseColorBlack() {
		Color actualColor = parser.parseTurno("b");
		
		assertEquals(Color.BLACK, actualColor);
	}	

	@Test
	public void testParseEnPassantSquare01() {
		Square pawnPasanteSquare = parser.parseEnPassantSquare("-");
		
		assertNull(pawnPasanteSquare);
	}
	
	@Test
	public void testParseEnPassantSquare02() {
		Square pawnPasanteSquare = parser.parseEnPassantSquare("a3");
		
		assertEquals(Square.a3, pawnPasanteSquare);
	}	
	
	@Test
	public void testParseEnPassantSquare03() {
		Square pawnPasanteSquare = parser.parseEnPassantSquare("h6");
		
		assertEquals(Square.h6, pawnPasanteSquare);
	}
	
	@Test
	public void testParseInitialFen() {
		parser.parseFEN(FENDecoder.INITIAL_FEN);
		
		assertEquals(Color.WHITE, this.turno);
		
		assertTrue(this.castlingWhiteQueenAllowed);
		assertTrue(this.castlingWhiteKingAllowed);
		
		assertTrue(this.castlingBlackQueenAllowed);
		assertTrue(this.castlingBlackKingAllowed);		
		
		assertNull(this.pawnPasanteSquare);
		
		assertEquals(Piece.ROOK_WHITE, getPieza(tablero, Square.a1));
		assertEquals(Piece.KNIGHT_WHITE, getPieza(tablero, Square.b1));
		assertEquals(Piece.BISHOP_WHITE, getPieza(tablero, Square.c1));
		assertEquals(Piece.QUEEN_WHITE, getPieza(tablero, Square.d1));
		assertEquals(Piece.KING_WHITE, getPieza(tablero, Square.e1));
		assertEquals(Piece.BISHOP_WHITE, getPieza(tablero, Square.f1));
		assertEquals(Piece.KNIGHT_WHITE, getPieza(tablero, Square.g1));
		assertEquals(Piece.ROOK_WHITE, getPieza(tablero, Square.h1));
		
		
		assertEquals(Piece.PAWN_WHITE, getPieza(tablero, Square.a2));
		assertEquals(Piece.PAWN_WHITE, getPieza(tablero, Square.b2));
		assertEquals(Piece.PAWN_WHITE, getPieza(tablero, Square.c2));
		assertEquals(Piece.PAWN_WHITE, getPieza(tablero, Square.d2));
		assertEquals(Piece.PAWN_WHITE, getPieza(tablero, Square.e2));
		assertEquals(Piece.PAWN_WHITE, getPieza(tablero, Square.f2));
		assertEquals(Piece.PAWN_WHITE, getPieza(tablero, Square.g2));
		assertEquals(Piece.PAWN_WHITE, getPieza(tablero, Square.h2));
		
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
		
		assertEquals(Piece.PAWN_BLACK, getPieza(tablero, Square.a7));
		assertEquals(Piece.PAWN_BLACK, getPieza(tablero, Square.b7));
		assertEquals(Piece.PAWN_BLACK, getPieza(tablero, Square.c7));
		assertEquals(Piece.PAWN_BLACK, getPieza(tablero, Square.d7));
		assertEquals(Piece.PAWN_BLACK, getPieza(tablero, Square.e7));
		assertEquals(Piece.PAWN_BLACK, getPieza(tablero, Square.f7));
		assertEquals(Piece.PAWN_BLACK, getPieza(tablero, Square.g7));
		assertEquals(Piece.PAWN_BLACK, getPieza(tablero, Square.h7));		
		
		assertEquals(Piece.ROOK_BLACK, getPieza(tablero, Square.a8));
		assertEquals(Piece.KNIGHT_BLACK, getPieza(tablero, Square.b8));
		assertEquals(Piece.BISHOP_BLACK, getPieza(tablero, Square.c8));
		assertEquals(Piece.QUEEN_BLACK, getPieza(tablero, Square.d8));
		assertEquals(Piece.KING_BLACK, getPieza(tablero, Square.e8));
		assertEquals(Piece.BISHOP_BLACK, getPieza(tablero, Square.f8));
		assertEquals(Piece.KNIGHT_BLACK, getPieza(tablero, Square.g8));
		assertEquals(Piece.ROOK_BLACK, getPieza(tablero, Square.h8));		
	}

	private boolean isEmtpy(Piece[][] tablero, Square square) {
		return getPieza(tablero, square) == null;
	}

	private Piece getPieza(Piece[][] tablero, Square square) {
		return tablero[square.getRank()][square.getFile()];
	}	
}
