package chess.moves;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

import chess.BoardState;
import chess.Color;
import chess.Piece;
import chess.PiecePositioned;
import chess.Square;
import chess.moves.Move;
import chess.moves.MoveFactory;


/**
 * @author Mauricio Coria
 *
 */
public class MoveFactoryTest {
	private MoveFactory moveFactory;
	
	private Move moveExecutor;
	
	private BoardState boardState;

	@Before
	public void setUp() throws Exception {
		moveFactory = new MoveFactory();
		boardState = new BoardState();
		moveExecutor = null;
	}	
	
	@Test
	public void testSimpleKingMoveBlancoPierdeEnroque() {
		boardState.setTurnoActual(Color.WHITE);
		boardState.setCastlingBlackQueenPermitido(true);
		boardState.setCastlingBlackKingPermitido(true);

		PiecePositioned origen = new PiecePositioned(Square.e1, Piece.KING_WHITE);
		PiecePositioned destino = new PiecePositioned(Square.e2, Piece.QUEEN_BLACK);

		moveExecutor = moveFactory.createCaptureKingMoveNegro(origen, destino);
		
		moveExecutor.executeMove(boardState);

		assertEquals(Color.BLACK, boardState.getTurnoActual());
		assertFalse(boardState.isCastlingWhiteKingPermitido());
		assertFalse(boardState.isCastlingWhiteQueenPermitido());
	}
	
	
	@Test
	public void testSimpleKingMoveNegroPierdeEnroque() {
		boardState.setTurnoActual(Color.BLACK);
		boardState.setCastlingBlackQueenPermitido(true);
		boardState.setCastlingBlackKingPermitido(true);

		PiecePositioned origen = new PiecePositioned(Square.e8, Piece.KING_BLACK);
		PiecePositioned destino = new PiecePositioned(Square.e7, null);

		moveExecutor = moveFactory.createSimpleKingMoveNegro(origen, destino);

		moveExecutor.executeMove(boardState);

		assertEquals(Color.WHITE, boardState.getTurnoActual());
		assertFalse(boardState.isCastlingBlackQueenPermitido());
		assertFalse(boardState.isCastlingBlackKingPermitido());
	}
	
	@Test
	public void testCapturaKingMoveNegroPierdeEnroque() {
		boardState.setTurnoActual(Color.BLACK);
		boardState.setCastlingBlackQueenPermitido(true);
		boardState.setCastlingBlackKingPermitido(true);

		PiecePositioned origen = new PiecePositioned(Square.e8, Piece.QUEEN_BLACK);
		PiecePositioned destino = new PiecePositioned(Square.e7, Piece.KNIGHT_WHITE);

		moveExecutor = moveFactory.createCaptureKingMoveNegro(origen, destino);

		moveExecutor.executeMove(boardState);

		assertEquals(Color.WHITE, boardState.getTurnoActual());
		assertFalse(boardState.isCastlingBlackQueenPermitido());
		assertFalse(boardState.isCastlingBlackKingPermitido());
	}
	
	@Test
	public void testCapturaKingMoveBlancoPierdeEnroque() {
		boardState.setTurnoActual(Color.WHITE);
		boardState.setCastlingWhiteKingPermitido(true);
		boardState.setCastlingWhiteQueenPermitido(true);

		PiecePositioned origen = new PiecePositioned(Square.e1, Piece.KING_WHITE);
		PiecePositioned destino = new PiecePositioned(Square.e2, Piece.KNIGHT_BLACK);

		moveExecutor = moveFactory.createCaptureKingMoveBlanco(origen, destino);

		moveExecutor.executeMove(boardState);

		assertEquals(Color.BLACK, boardState.getTurnoActual());
		assertFalse(boardState.isCastlingWhiteKingPermitido());
		assertFalse(boardState.isCastlingWhiteQueenPermitido());
	}	
	
}
