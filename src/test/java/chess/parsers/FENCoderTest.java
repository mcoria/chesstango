package chess.parsers;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import chess.Color;
import chess.Pieza;
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
	public void testTurnoBlanco() {
		coder.withTurno(Color.WHITE);
		
		String actual = coder.getTurno(stringBuilder).toString();
		
		assertEquals("w", actual);
	}
	
	@Test
	public void testTurnoNegro() {
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
		String actual = coder.codePiecePlacementRank(new Pieza[]{Pieza.BISHOP_WHITE, null, null, Pieza.PAWN_WHITE, null, Pieza.BISHOP_WHITE, null, Pieza.PAWN_WHITE}, stringBuilder).toString();
		
		assertEquals("B2P1B1P", actual);
	}
	
	@Test
	public void testCodePiecePlacementRank02() {
		String actual = coder.codePiecePlacementRank(new Pieza[]{null, null, null, null, null, null, null, null}, stringBuilder).toString();
		
		assertEquals("8", actual);
	}	
	
	@Test
	public void testCodePiecePlacement03() {
		coder.withPieza(Square.a1, Pieza.ROOK_WHITE);
		
		String actual = coder.getPiecePlacement(stringBuilder).toString();
		
		assertEquals("8/8/8/8/8/8/8/R7", actual);		
	}
	
	@Test
	public void testCodePiecePlacement04() {
		coder.withPieza(Square.h1, Pieza.ROOK_WHITE);
		
		String actual = coder.getPiecePlacement(stringBuilder).toString();
		
		assertEquals("8/8/8/8/8/8/8/7R", actual);		
	}
	
	@Test
	public void testCodePiecePlacement05() {
		coder.withPieza(Square.a8, Pieza.ROOK_BLACK);
		
		String actual = coder.getPiecePlacement(stringBuilder).toString();
		
		assertEquals("r7/8/8/8/8/8/8/8", actual);		
	}		
	
	
	@Test
	public void withCastlingWhiteKingPermitido() {
		coder.withCastlingWhiteKingPermitido(true);
		
		String actual = coder.getEnroques(stringBuilder).toString();
		
		assertEquals("K", actual);		
	}
	
	@Test
	public void withCastlingWhiteReinaPermitido() {
		coder.withCastlingWhiteReinaPermitido(true);
		
		String actual = coder.getEnroques(stringBuilder).toString();
		
		assertEquals("Q", actual);		
	}
	
	@Test
	public void withCastlingBlackKingPermitido() {
		coder.withCastlingBlackKingPermitido(true);
		
		String actual = coder.getEnroques(stringBuilder).toString();
		
		assertEquals("k", actual);		
	}
	
	@Test
	public void withCastlingBlackReinaPermitido() {
		coder.withCastlingBlackReinaPermitido(true);
		
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
		coder.withPieza(Square.a8, Pieza.ROOK_BLACK);
		coder.withPieza(Square.b8, Pieza.KNIGHT_BLACK);
		coder.withPieza(Square.c8, Pieza.BISHOP_BLACK);
		coder.withPieza(Square.d8, Pieza.QUEEN_BLACK);
		coder.withPieza(Square.e8, Pieza.KING_BLACK);
		coder.withPieza(Square.f8, Pieza.BISHOP_BLACK);
		coder.withPieza(Square.g8, Pieza.KNIGHT_BLACK);
		coder.withPieza(Square.h8, Pieza.ROOK_BLACK);
		
		coder.withPieza(Square.a7, Pieza.PAWN_BLACK);
		coder.withPieza(Square.b7, Pieza.PAWN_BLACK);
		coder.withPieza(Square.c7, Pieza.PAWN_BLACK);
		coder.withPieza(Square.d7, Pieza.PAWN_BLACK);
		coder.withPieza(Square.e7, Pieza.PAWN_BLACK);
		coder.withPieza(Square.f7, Pieza.PAWN_BLACK);
		coder.withPieza(Square.g7, Pieza.PAWN_BLACK);
		coder.withPieza(Square.h7, Pieza.PAWN_BLACK);
		

		coder.withPieza(Square.a2, Pieza.PAWN_WHITE);
		coder.withPieza(Square.b2, Pieza.PAWN_WHITE);
		coder.withPieza(Square.c2, Pieza.PAWN_WHITE);
		coder.withPieza(Square.d2, Pieza.PAWN_WHITE);
		coder.withPieza(Square.e2, Pieza.PAWN_WHITE);
		coder.withPieza(Square.f2, Pieza.PAWN_WHITE);
		coder.withPieza(Square.g2, Pieza.PAWN_WHITE);
		coder.withPieza(Square.h2, Pieza.PAWN_WHITE);
		
		coder.withPieza(Square.a1, Pieza.ROOK_WHITE);
		coder.withPieza(Square.b1, Pieza.KNIGHT_WHITE);
		coder.withPieza(Square.c1, Pieza.BISHOP_WHITE);
		coder.withPieza(Square.d1, Pieza.QUEEN_WHITE);
		coder.withPieza(Square.e1, Pieza.KING_WHITE);
		coder.withPieza(Square.f1, Pieza.BISHOP_WHITE);
		coder.withPieza(Square.g1, Pieza.KNIGHT_WHITE);
		coder.withPieza(Square.h1, Pieza.ROOK_WHITE);		

		
		String actual = coder.getPiecePlacement(stringBuilder).toString();
		
		assertEquals("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR", actual);		
	}
	
	@Test
	public void testGetFEN() {
		coder.withPieza(Square.a8, Pieza.ROOK_BLACK);
		coder.withPieza(Square.b8, Pieza.KNIGHT_BLACK);
		coder.withPieza(Square.c8, Pieza.BISHOP_BLACK);
		coder.withPieza(Square.d8, Pieza.QUEEN_BLACK);
		coder.withPieza(Square.e8, Pieza.KING_BLACK);
		coder.withPieza(Square.f8, Pieza.BISHOP_BLACK);
		coder.withPieza(Square.g8, Pieza.KNIGHT_BLACK);
		coder.withPieza(Square.h8, Pieza.ROOK_BLACK);
		
		coder.withPieza(Square.a7, Pieza.PAWN_BLACK);
		coder.withPieza(Square.b7, Pieza.PAWN_BLACK);
		coder.withPieza(Square.c7, Pieza.PAWN_BLACK);
		coder.withPieza(Square.d7, Pieza.PAWN_BLACK);
		coder.withPieza(Square.e7, Pieza.PAWN_BLACK);
		coder.withPieza(Square.f7, Pieza.PAWN_BLACK);
		coder.withPieza(Square.g7, Pieza.PAWN_BLACK);
		coder.withPieza(Square.h7, Pieza.PAWN_BLACK);
		

		coder.withPieza(Square.a2, Pieza.PAWN_WHITE);
		coder.withPieza(Square.b2, Pieza.PAWN_WHITE);
		coder.withPieza(Square.c2, Pieza.PAWN_WHITE);
		coder.withPieza(Square.d2, Pieza.PAWN_WHITE);
		coder.withPieza(Square.e2, Pieza.PAWN_WHITE);
		coder.withPieza(Square.f2, Pieza.PAWN_WHITE);
		coder.withPieza(Square.g2, Pieza.PAWN_WHITE);
		coder.withPieza(Square.h2, Pieza.PAWN_WHITE);
		
		coder.withPieza(Square.a1, Pieza.ROOK_WHITE);
		coder.withPieza(Square.b1, Pieza.KNIGHT_WHITE);
		coder.withPieza(Square.c1, Pieza.BISHOP_WHITE);
		coder.withPieza(Square.d1, Pieza.QUEEN_WHITE);
		coder.withPieza(Square.e1, Pieza.KING_WHITE);
		coder.withPieza(Square.f1, Pieza.BISHOP_WHITE);
		coder.withPieza(Square.g1, Pieza.KNIGHT_WHITE);
		coder.withPieza(Square.h1, Pieza.ROOK_WHITE);
		
		coder.withTurno(Color.WHITE);
		
		coder.withPawnPasanteSquare(null);
		
		coder.withCastlingWhiteReinaPermitido(true);
		coder.withCastlingWhiteKingPermitido(true);
		coder.withCastlingBlackReinaPermitido(true);
		coder.withCastlingBlackKingPermitido(true);
		
		
		String actual = coder.getFEN();
		
		assertEquals("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", actual);		
	}	

}
