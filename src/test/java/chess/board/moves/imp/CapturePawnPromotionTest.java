package chess.board.moves.imp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import chess.board.Color;
import chess.board.Piece;
import chess.board.PiecePositioned;
import chess.board.Square;
import chess.board.debug.chess.ColorBoardDebug;
import chess.board.movesgenerators.legal.MoveFilter;
import chess.board.position.ChessPosition;
import chess.board.position.PiecePlacement;
import chess.board.position.imp.ArrayPiecePlacement;
import chess.board.position.imp.ColorBoard;
import chess.board.position.imp.PositionState;


/**
 * @author Mauricio Coria
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class CapturePawnPromotionTest {

	private PiecePlacement piecePlacement;
	
	private PositionState positionState;
	
	private CapturePawnPromotion moveExecutor;
	
	private ColorBoard colorBoard;
	
	@Mock
	private ChessPosition chessPosition;
	
	@Mock
	private MoveFilter filter;	

	@Before
	public void setUp() throws Exception {
		positionState = new PositionState();
		positionState.setCurrentTurn(Color.WHITE);
		positionState.setHalfMoveClock(3);
		
		piecePlacement = new ArrayPiecePlacement();
		piecePlacement.setPieza(Square.e7, Piece.PAWN_WHITE);
		piecePlacement.setPieza(Square.f8, Piece.KNIGHT_BLACK);
		
		colorBoard = new ColorBoardDebug();
		colorBoard.init(piecePlacement);
		
		PiecePositioned origen = PiecePositioned.getPiecePositioned(Square.e7, Piece.PAWN_WHITE);
		PiecePositioned destino = PiecePositioned.getPiecePositioned(Square.f8, Piece.KNIGHT_BLACK);
		
		moveExecutor =  new CapturePawnPromotion(origen, destino, Piece.QUEEN_WHITE);
	}
	
	
	@Test
	public void testPosicionPiezaBoard() {
		// execute
		moveExecutor.executeMove(piecePlacement);
		
		// asserts execute		
		assertEquals(Piece.QUEEN_WHITE, piecePlacement.getPiece(Square.f8));
		assertTrue(piecePlacement.isEmtpy(Square.e7));
		
		// undos		
		moveExecutor.undoMove(piecePlacement);
		
		// asserts undos		
		assertEquals(Piece.PAWN_WHITE, piecePlacement.getPiece(Square.e7));
		assertEquals(Piece.KNIGHT_BLACK, piecePlacement.getPiece(Square.f8));
	}
		
	@Test
	public void testMoveState() {
		// execute
		moveExecutor.executeMove(positionState);
		
		// asserts execute
		assertNull(positionState.getEnPassantSquare());
		assertEquals(Color.BLACK, positionState.getCurrentTurn());
		assertEquals(3, positionState.getHalfMoveClock());
		assertEquals(5, positionState.getFullMoveClock());
		
		// undos
		moveExecutor.undoMove(positionState);

		// asserts undos	
		assertEquals(Color.WHITE, positionState.getCurrentTurn());
		assertEquals(3, positionState.getHalfMoveClock());
		assertEquals(5, positionState.getFullMoveClock());
	}
	
	@Test
	public void testColorBoard() {
		// execute
		moveExecutor.executeMove(colorBoard);

		// asserts execute
		assertEquals(Color.WHITE, colorBoard.getColor(Square.f8));
		assertTrue(colorBoard.isEmpty(Square.e7));

		// undos
		moveExecutor.undoMove(colorBoard);
		
		// asserts undos
		assertEquals(Color.WHITE, colorBoard.getColor(Square.e7));
		assertEquals(Color.BLACK, colorBoard.getColor(Square.f8));
	}	
	
	@Test
	public void testBoard() {
		// execute
		moveExecutor.executeMove(chessPosition);

		// asserts execute
		verify(chessPosition).executeMove(moveExecutor);

		// undos
		moveExecutor.undoMove(chessPosition);

		
		// asserts undos
		verify(chessPosition).undoMove(moveExecutor);
	}
	
	
	@Test
	public void testFilter() {
		// execute
		moveExecutor.filter(filter);

		// asserts execute
		verify(filter).filterMove(moveExecutor);
	}
}
