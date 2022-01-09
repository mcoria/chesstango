package chess.moves;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

import chess.Color;
import chess.Piece;
import chess.PiecePositioned;
import chess.Square;
import chess.moves.Move;
import chess.moves.MoveFactory;
import chess.position.PositionState;


/**
 * @author Mauricio Coria
 *
 */
public class MoveFactoryTest {
	private MoveFactory moveFactory;
	
	private Move moveExecutor;
	
	private PositionState positionState;

	@Before
	public void setUp() throws Exception {
		moveFactory = new MoveFactory();
		positionState = new PositionState();
		moveExecutor = null;
	}	
	
	@Test
	public void testSimpleKingMoveBlancoPierdeEnroque() {
		positionState.setTurnoActual(Color.WHITE);
		positionState.setCastlingBlackQueenAllowed(true);
		positionState.setCastlingBlackKingAllowed(true);

		PiecePositioned origen = new PiecePositioned(Square.e1, Piece.KING_WHITE);
		PiecePositioned destino = new PiecePositioned(Square.e2, Piece.QUEEN_BLACK);

		moveExecutor = moveFactory.createCaptureKingMoveNegro(origen, destino);
		
		moveExecutor.executeMove(positionState);

		assertEquals(Color.BLACK, positionState.getTurnoActual());
		assertFalse(positionState.isCastlingWhiteKingAllowed());
		assertFalse(positionState.isCastlingWhiteQueenAllowed());
	}
	
	
	@Test
	public void testSimpleKingMoveNegroPierdeEnroque() {
		positionState.setTurnoActual(Color.BLACK);
		positionState.setCastlingBlackQueenAllowed(true);
		positionState.setCastlingBlackKingAllowed(true);

		PiecePositioned origen = new PiecePositioned(Square.e8, Piece.KING_BLACK);
		PiecePositioned destino = new PiecePositioned(Square.e7, null);

		moveExecutor = moveFactory.createSimpleKingMoveNegro(origen, destino);

		moveExecutor.executeMove(positionState);

		assertEquals(Color.WHITE, positionState.getTurnoActual());
		assertFalse(positionState.isCastlingBlackQueenAllowed());
		assertFalse(positionState.isCastlingBlackKingAllowed());
	}
	
	@Test
	public void testCapturaKingMoveNegroPierdeEnroque() {
		positionState.setTurnoActual(Color.BLACK);
		positionState.setCastlingBlackQueenAllowed(true);
		positionState.setCastlingBlackKingAllowed(true);

		PiecePositioned origen = new PiecePositioned(Square.e8, Piece.QUEEN_BLACK);
		PiecePositioned destino = new PiecePositioned(Square.e7, Piece.KNIGHT_WHITE);

		moveExecutor = moveFactory.createCaptureKingMoveNegro(origen, destino);

		moveExecutor.executeMove(positionState);

		assertEquals(Color.WHITE, positionState.getTurnoActual());
		assertFalse(positionState.isCastlingBlackQueenAllowed());
		assertFalse(positionState.isCastlingBlackKingAllowed());
	}
	
	@Test
	public void testCapturaKingMoveBlancoPierdeEnroque() {
		positionState.setTurnoActual(Color.WHITE);
		positionState.setCastlingWhiteKingAllowed(true);
		positionState.setCastlingWhiteQueenAllowed(true);

		PiecePositioned origen = new PiecePositioned(Square.e1, Piece.KING_WHITE);
		PiecePositioned destino = new PiecePositioned(Square.e2, Piece.KNIGHT_BLACK);

		moveExecutor = moveFactory.createCaptureKingMoveBlanco(origen, destino);

		moveExecutor.executeMove(positionState);

		assertEquals(Color.BLACK, positionState.getTurnoActual());
		assertFalse(positionState.isCastlingWhiteKingAllowed());
		assertFalse(positionState.isCastlingWhiteQueenAllowed());
	}	
	
}
