package chess.parsers;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import chess.Color;
import chess.Piece;
import chess.Square;
import chess.parsers.FENCoder;

/**
 * @author Mauricio Coria
 *
 */
public class FENCoderTest {

	private FENCoder coder ;
	private StringBuilder stringBuilder;
	
	@Before
	public void setUp() throws Exception {
		coder = new FENCoder();
		stringBuilder = new StringBuilder();
	}
	
	@Test
	public void testTurnoWhite() {
		coder.withTurno(Color.WHITE);
		
		String actual = coder.getTurno(stringBuilder).toString();
		
		assertEquals("w", actual);
	}
	
	@Test
	public void testTurnoBlack() {
		coder.withTurno(Color.BLACK);
		
		String actual = coder.getTurno(stringBuilder).toString();
		
		assertEquals("b", actual);
	}
	
	@Test
	public void testPawnPasanteC3() {	
		coder.withPawnPasanteSquare(Square.c3);
		
		String actual = coder.getPawnPasante(stringBuilder).toString();
		
		assertEquals("c3", actual);
	}
	
	@Test
	public void testPawnPasanteNull() {	
		coder.withPawnPasanteSquare(null);
		
		String actual = coder.getPawnPasante(stringBuilder).toString();
		
		assertEquals("-", actual);
	}	
	
	@Test
	public void testCodePiecePlacementRank01() {
		String actual = coder.codePiecePlacementRank(new Piece[]{Piece.BISHOP_WHITE, null, null, Piece.PAWN_WHITE, null, Piece.BISHOP_WHITE, null, Piece.PAWN_WHITE}, stringBuilder).toString();
		
		assertEquals("B2P1B1P", actual);
	}
	
	@Test
	public void testCodePiecePlacementRank02() {
		String actual = coder.codePiecePlacementRank(new Piece[]{null, null, null, null, null, null, null, null}, stringBuilder).toString();
		
		assertEquals("8", actual);
	}	
	
	@Test
	public void testCodePiecePlacement03() {
		coder.withPieza(Square.a1, Piece.ROOK_WHITE);
		
		String actual = coder.getPiecePlacement(stringBuilder).toString();
		
		assertEquals("8/8/8/8/8/8/8/R7", actual);		
	}
	
	@Test
	public void testCodePiecePlacement04() {
		coder.withPieza(Square.h1, Piece.ROOK_WHITE);
		
		String actual = coder.getPiecePlacement(stringBuilder).toString();
		
		assertEquals("8/8/8/8/8/8/8/7R", actual);		
	}
	
	@Test
	public void testCodePiecePlacement05() {
		coder.withPieza(Square.a8, Piece.ROOK_BLACK);
		
		String actual = coder.getPiecePlacement(stringBuilder).toString();
		
		assertEquals("r7/8/8/8/8/8/8/8", actual);		
	}		
	
	
	@Test
	public void withCastlingWhiteKingAllowed() {
		coder.withCastlingWhiteKingAllowed(true);
		
		String actual = coder.getEnroques(stringBuilder).toString();
		
		assertEquals("K", actual);		
	}
	
	@Test
	public void withCastlingWhiteQueenAllowed() {
		coder.withCastlingWhiteQueenAllowed(true);
		
		String actual = coder.getEnroques(stringBuilder).toString();
		
		assertEquals("Q", actual);		
	}
	
	@Test
	public void withCastlingBlackKingAllowed() {
		coder.withCastlingBlackKingAllowed(true);
		
		String actual = coder.getEnroques(stringBuilder).toString();
		
		assertEquals("k", actual);		
	}
	
	@Test
	public void withCastlingBlackQueenAllowed() {
		coder.withCastlingBlackQueenAllowed(true);
		
		String actual = coder.getEnroques(stringBuilder).toString();
		
		assertEquals("q", actual);		
	}
	
	@Test
	public void withoutEnroques() {
		String actual = coder.getEnroques(stringBuilder).toString();
		
		assertEquals("-", actual);		
	}		
	

	@Test
	public void testCodePiecePlacement06() {
		coder.withPieza(Square.a8, Piece.ROOK_BLACK);
		coder.withPieza(Square.b8, Piece.KNIGHT_BLACK);
		coder.withPieza(Square.c8, Piece.BISHOP_BLACK);
		coder.withPieza(Square.d8, Piece.QUEEN_BLACK);
		coder.withPieza(Square.e8, Piece.KING_BLACK);
		coder.withPieza(Square.f8, Piece.BISHOP_BLACK);
		coder.withPieza(Square.g8, Piece.KNIGHT_BLACK);
		coder.withPieza(Square.h8, Piece.ROOK_BLACK);
		
		coder.withPieza(Square.a7, Piece.PAWN_BLACK);
		coder.withPieza(Square.b7, Piece.PAWN_BLACK);
		coder.withPieza(Square.c7, Piece.PAWN_BLACK);
		coder.withPieza(Square.d7, Piece.PAWN_BLACK);
		coder.withPieza(Square.e7, Piece.PAWN_BLACK);
		coder.withPieza(Square.f7, Piece.PAWN_BLACK);
		coder.withPieza(Square.g7, Piece.PAWN_BLACK);
		coder.withPieza(Square.h7, Piece.PAWN_BLACK);
		

		coder.withPieza(Square.a2, Piece.PAWN_WHITE);
		coder.withPieza(Square.b2, Piece.PAWN_WHITE);
		coder.withPieza(Square.c2, Piece.PAWN_WHITE);
		coder.withPieza(Square.d2, Piece.PAWN_WHITE);
		coder.withPieza(Square.e2, Piece.PAWN_WHITE);
		coder.withPieza(Square.f2, Piece.PAWN_WHITE);
		coder.withPieza(Square.g2, Piece.PAWN_WHITE);
		coder.withPieza(Square.h2, Piece.PAWN_WHITE);
		
		coder.withPieza(Square.a1, Piece.ROOK_WHITE);
		coder.withPieza(Square.b1, Piece.KNIGHT_WHITE);
		coder.withPieza(Square.c1, Piece.BISHOP_WHITE);
		coder.withPieza(Square.d1, Piece.QUEEN_WHITE);
		coder.withPieza(Square.e1, Piece.KING_WHITE);
		coder.withPieza(Square.f1, Piece.BISHOP_WHITE);
		coder.withPieza(Square.g1, Piece.KNIGHT_WHITE);
		coder.withPieza(Square.h1, Piece.ROOK_WHITE);		

		
		String actual = coder.getPiecePlacement(stringBuilder).toString();
		
		assertEquals("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR", actual);		
	}
	
	@Test
	public void testGetFEN() {
		coder.withPieza(Square.a8, Piece.ROOK_BLACK);
		coder.withPieza(Square.b8, Piece.KNIGHT_BLACK);
		coder.withPieza(Square.c8, Piece.BISHOP_BLACK);
		coder.withPieza(Square.d8, Piece.QUEEN_BLACK);
		coder.withPieza(Square.e8, Piece.KING_BLACK);
		coder.withPieza(Square.f8, Piece.BISHOP_BLACK);
		coder.withPieza(Square.g8, Piece.KNIGHT_BLACK);
		coder.withPieza(Square.h8, Piece.ROOK_BLACK);
		
		coder.withPieza(Square.a7, Piece.PAWN_BLACK);
		coder.withPieza(Square.b7, Piece.PAWN_BLACK);
		coder.withPieza(Square.c7, Piece.PAWN_BLACK);
		coder.withPieza(Square.d7, Piece.PAWN_BLACK);
		coder.withPieza(Square.e7, Piece.PAWN_BLACK);
		coder.withPieza(Square.f7, Piece.PAWN_BLACK);
		coder.withPieza(Square.g7, Piece.PAWN_BLACK);
		coder.withPieza(Square.h7, Piece.PAWN_BLACK);
		

		coder.withPieza(Square.a2, Piece.PAWN_WHITE);
		coder.withPieza(Square.b2, Piece.PAWN_WHITE);
		coder.withPieza(Square.c2, Piece.PAWN_WHITE);
		coder.withPieza(Square.d2, Piece.PAWN_WHITE);
		coder.withPieza(Square.e2, Piece.PAWN_WHITE);
		coder.withPieza(Square.f2, Piece.PAWN_WHITE);
		coder.withPieza(Square.g2, Piece.PAWN_WHITE);
		coder.withPieza(Square.h2, Piece.PAWN_WHITE);
		
		coder.withPieza(Square.a1, Piece.ROOK_WHITE);
		coder.withPieza(Square.b1, Piece.KNIGHT_WHITE);
		coder.withPieza(Square.c1, Piece.BISHOP_WHITE);
		coder.withPieza(Square.d1, Piece.QUEEN_WHITE);
		coder.withPieza(Square.e1, Piece.KING_WHITE);
		coder.withPieza(Square.f1, Piece.BISHOP_WHITE);
		coder.withPieza(Square.g1, Piece.KNIGHT_WHITE);
		coder.withPieza(Square.h1, Piece.ROOK_WHITE);
		
		coder.withTurno(Color.WHITE);
		
		coder.withPawnPasanteSquare(null);
		
		coder.withCastlingWhiteQueenAllowed(true);
		coder.withCastlingWhiteKingAllowed(true);
		coder.withCastlingBlackQueenAllowed(true);
		coder.withCastlingBlackKingAllowed(true);
		
		
		String actual = coder.getFEN();
		
		assertEquals("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", actual);		
	}	

}
