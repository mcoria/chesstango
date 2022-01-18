package chess.moves;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

import chess.Color;
import chess.Piece;
import chess.PiecePositioned;
import chess.Square;
import chess.position.PositionState;


/**
 * @author Mauricio Coria
 *
 */
public class MoveFactoryBlackTest {
	private MoveFactoryBlack moveFactoryImp;
	
	private Move moveExecutor;
	
	private PositionState positionState;

	@Before
	public void setUp() throws Exception {
		moveFactoryImp = new MoveFactoryBlack();
		positionState = new PositionState();
		moveExecutor = null;
	}	
	
	@Test
	public void testSimpleKingMoveWhitePierdeEnroque() {
		positionState.setTurnoActual(Color.WHITE);
		positionState.setCastlingBlackQueenAllowed(true);
		positionState.setCastlingBlackKingAllowed(true);

		PiecePositioned origen = new PiecePositioned(Square.e1, Piece.KING_WHITE);
		PiecePositioned destino = new PiecePositioned(Square.e2, Piece.QUEEN_BLACK);

		moveExecutor = moveFactoryImp.createCaptureKingMove(origen, destino);
		
		moveExecutor.executeMove(positionState);

		assertEquals(Color.BLACK, positionState.getTurnoActual());
		assertFalse(positionState.isCastlingWhiteKingAllowed());
		assertFalse(positionState.isCastlingWhiteQueenAllowed());
	}
	
	
	@Test
	public void testSimpleKingMoveBlackPierdeEnroque() {
		positionState.setTurnoActual(Color.BLACK);
		positionState.setCastlingBlackQueenAllowed(true);
		positionState.setCastlingBlackKingAllowed(true);

		PiecePositioned origen = new PiecePositioned(Square.e8, Piece.KING_BLACK);
		PiecePositioned destino = new PiecePositioned(Square.e7, null);

		moveExecutor = moveFactoryImp.createSimpleKingMove(origen, destino);

		moveExecutor.executeMove(positionState);

		assertEquals(Color.WHITE, positionState.getTurnoActual());
		assertFalse(positionState.isCastlingBlackQueenAllowed());
		assertFalse(positionState.isCastlingBlackKingAllowed());
	}
	
	@Test
	public void testCapturaKingMoveBlackPierdeEnroque() {
		positionState.setTurnoActual(Color.BLACK);
		positionState.setCastlingBlackQueenAllowed(true);
		positionState.setCastlingBlackKingAllowed(true);

		PiecePositioned origen = new PiecePositioned(Square.e8, Piece.QUEEN_BLACK);
		PiecePositioned destino = new PiecePositioned(Square.e7, Piece.KNIGHT_WHITE);

		moveExecutor = moveFactoryImp.createCaptureKingMove(origen, destino);

		moveExecutor.executeMove(positionState);

		assertEquals(Color.WHITE, positionState.getTurnoActual());
		assertFalse(positionState.isCastlingBlackQueenAllowed());
		assertFalse(positionState.isCastlingBlackKingAllowed());
	}
	
}
