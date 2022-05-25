package chess.board.moves.imp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

import chess.board.Color;
import chess.board.Piece;
import chess.board.PiecePositioned;
import chess.board.Square;
import chess.board.moves.Move;
import chess.board.position.imp.PositionState;


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
		positionState.setCurrentTurn(Color.WHITE);
		positionState.setCastlingBlackQueenAllowed(true);
		positionState.setCastlingBlackKingAllowed(true);

		PiecePositioned origen = PiecePositioned.getPiecePositioned(Square.e1, Piece.KING_WHITE);
		PiecePositioned destino = PiecePositioned.getPiecePositioned(Square.e2, Piece.QUEEN_BLACK);

		moveExecutor = moveFactoryImp.createCaptureKingMove(origen, destino);
		
		moveExecutor.executeMove(positionState);

		assertEquals(Color.BLACK, positionState.getCurrentTurn());
		assertFalse(positionState.isCastlingWhiteKingAllowed());
		assertFalse(positionState.isCastlingWhiteQueenAllowed());
	}
	
	
	@Test
	public void testSimpleKingMoveBlackPierdeEnroque() {
		positionState.setCurrentTurn(Color.BLACK);
		positionState.setCastlingBlackQueenAllowed(true);
		positionState.setCastlingBlackKingAllowed(true);

		PiecePositioned origen = PiecePositioned.getPiecePositioned(Square.e8, Piece.KING_BLACK);
		PiecePositioned destino = PiecePositioned.getPiecePositioned(Square.e7, null);

		moveExecutor = moveFactoryImp.createSimpleKingMove(origen, destino);

		moveExecutor.executeMove(positionState);

		assertEquals(Color.WHITE, positionState.getCurrentTurn());
		assertFalse(positionState.isCastlingBlackQueenAllowed());
		assertFalse(positionState.isCastlingBlackKingAllowed());
	}
	
	@Test
	public void testCapturaKingMoveBlackPierdeEnroque() {
		positionState.setCurrentTurn(Color.BLACK);
		positionState.setCastlingBlackQueenAllowed(true);
		positionState.setCastlingBlackKingAllowed(true);

		PiecePositioned origen = PiecePositioned.getPiecePositioned(Square.e8, Piece.QUEEN_BLACK);
		PiecePositioned destino = PiecePositioned.getPiecePositioned(Square.e7, Piece.KNIGHT_WHITE);

		moveExecutor = moveFactoryImp.createCaptureKingMove(origen, destino);

		moveExecutor.executeMove(positionState);

		assertEquals(Color.WHITE, positionState.getCurrentTurn());
		assertFalse(positionState.isCastlingBlackQueenAllowed());
		assertFalse(positionState.isCastlingBlackKingAllowed());
	}
	
}
