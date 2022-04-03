package chess.board.moves;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

import chess.board.Color;
import chess.board.Piece;
import chess.board.PiecePositioned;
import chess.board.Square;
import chess.board.moves.Move;
import chess.board.moves.imp.MoveFactoryWhite;
import chess.board.position.imp.PositionState;


/**
 * @author Mauricio Coria
 *
 */
public class MoveFactoryWhiteTest {
	private MoveFactoryWhite moveFactoryImp;
	
	private Move moveExecutor;
	
	private PositionState positionState;

	@Before
	public void setUp() throws Exception {
		moveFactoryImp = new MoveFactoryWhite();
		positionState = new PositionState();
		moveExecutor = null;
	}	
	
	@Test
	public void testSimpleKingMoveWhitePierdeEnroque() {
		positionState.setTurnoActual(Color.WHITE);
		positionState.setCastlingBlackQueenAllowed(true);
		positionState.setCastlingBlackKingAllowed(true);

		PiecePositioned origen = PiecePositioned.getPiecePositioned(Square.e1, Piece.KING_WHITE);
		PiecePositioned destino = PiecePositioned.getPiecePositioned(Square.e2, Piece.QUEEN_BLACK);

		moveExecutor = moveFactoryImp.createCaptureKingMove(origen, destino);
		
		moveExecutor.executeMove(positionState);

		assertEquals(Color.BLACK, positionState.getTurnoActual());
		assertFalse(positionState.isCastlingWhiteKingAllowed());
		assertFalse(positionState.isCastlingWhiteQueenAllowed());
	}
	
	@Test
	public void testCapturaKingMoveWhitePierdeEnroque() {
		positionState.setTurnoActual(Color.WHITE);
		positionState.setCastlingWhiteKingAllowed(true);
		positionState.setCastlingWhiteQueenAllowed(true);

		PiecePositioned origen = PiecePositioned.getPiecePositioned(Square.e1, Piece.KING_WHITE);
		PiecePositioned destino = PiecePositioned.getPiecePositioned(Square.e2, Piece.KNIGHT_BLACK);

		moveExecutor = moveFactoryImp.createCaptureKingMove(origen, destino);

		moveExecutor.executeMove(positionState);

		assertEquals(Color.BLACK, positionState.getTurnoActual());
		assertFalse(positionState.isCastlingWhiteKingAllowed());
		assertFalse(positionState.isCastlingWhiteQueenAllowed());
	}	
	
}
