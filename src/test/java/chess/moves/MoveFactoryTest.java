package chess.moves;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

import chess.Color;
import chess.Piece;
import chess.PiecePositioned;
import chess.Square;
import chess.layers.ChessPositionState;
import chess.moves.Move;
import chess.moves.MoveFactory;


/**
 * @author Mauricio Coria
 *
 */
public class MoveFactoryTest {
	private MoveFactory moveFactory;
	
	private Move moveExecutor;
	
	private ChessPositionState chessPositionState;

	@Before
	public void setUp() throws Exception {
		moveFactory = new MoveFactory();
		chessPositionState = new ChessPositionState();
		moveExecutor = null;
	}	
	
	@Test
	public void testSimpleKingMoveBlancoPierdeEnroque() {
		chessPositionState.setTurnoActual(Color.WHITE);
		chessPositionState.setCastlingBlackQueenPermitido(true);
		chessPositionState.setCastlingBlackKingPermitido(true);

		PiecePositioned origen = new PiecePositioned(Square.e1, Piece.KING_WHITE);
		PiecePositioned destino = new PiecePositioned(Square.e2, Piece.QUEEN_BLACK);

		moveExecutor = moveFactory.createCaptureKingMoveNegro(origen, destino);
		
		moveExecutor.executeMove(chessPositionState);

		assertEquals(Color.BLACK, chessPositionState.getTurnoActual());
		assertFalse(chessPositionState.isCastlingWhiteKingPermitido());
		assertFalse(chessPositionState.isCastlingWhiteQueenPermitido());
	}
	
	
	@Test
	public void testSimpleKingMoveNegroPierdeEnroque() {
		chessPositionState.setTurnoActual(Color.BLACK);
		chessPositionState.setCastlingBlackQueenPermitido(true);
		chessPositionState.setCastlingBlackKingPermitido(true);

		PiecePositioned origen = new PiecePositioned(Square.e8, Piece.KING_BLACK);
		PiecePositioned destino = new PiecePositioned(Square.e7, null);

		moveExecutor = moveFactory.createSimpleKingMoveNegro(origen, destino);

		moveExecutor.executeMove(chessPositionState);

		assertEquals(Color.WHITE, chessPositionState.getTurnoActual());
		assertFalse(chessPositionState.isCastlingBlackQueenPermitido());
		assertFalse(chessPositionState.isCastlingBlackKingPermitido());
	}
	
	@Test
	public void testCapturaKingMoveNegroPierdeEnroque() {
		chessPositionState.setTurnoActual(Color.BLACK);
		chessPositionState.setCastlingBlackQueenPermitido(true);
		chessPositionState.setCastlingBlackKingPermitido(true);

		PiecePositioned origen = new PiecePositioned(Square.e8, Piece.QUEEN_BLACK);
		PiecePositioned destino = new PiecePositioned(Square.e7, Piece.KNIGHT_WHITE);

		moveExecutor = moveFactory.createCaptureKingMoveNegro(origen, destino);

		moveExecutor.executeMove(chessPositionState);

		assertEquals(Color.WHITE, chessPositionState.getTurnoActual());
		assertFalse(chessPositionState.isCastlingBlackQueenPermitido());
		assertFalse(chessPositionState.isCastlingBlackKingPermitido());
	}
	
	@Test
	public void testCapturaKingMoveBlancoPierdeEnroque() {
		chessPositionState.setTurnoActual(Color.WHITE);
		chessPositionState.setCastlingWhiteKingPermitido(true);
		chessPositionState.setCastlingWhiteQueenPermitido(true);

		PiecePositioned origen = new PiecePositioned(Square.e1, Piece.KING_WHITE);
		PiecePositioned destino = new PiecePositioned(Square.e2, Piece.KNIGHT_BLACK);

		moveExecutor = moveFactory.createCaptureKingMoveBlanco(origen, destino);

		moveExecutor.executeMove(chessPositionState);

		assertEquals(Color.BLACK, chessPositionState.getTurnoActual());
		assertFalse(chessPositionState.isCastlingWhiteKingPermitido());
		assertFalse(chessPositionState.isCastlingWhiteQueenPermitido());
	}	
	
}
