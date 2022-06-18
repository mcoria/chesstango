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
import chess.board.position.imp.PositionState;


/**
 * @author Mauricio Coria
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class CaptureEnPassantTest {

	private PiecePlacement piecePlacement;
	
	private PositionState positionState;
	
	private CaptureEnPassant moveExecutor;
	
	private ColorBoardDebug colorBoard;
	
	@Mock
	private ChessPosition chessPosition;
	
	@Mock
	private MoveFilter filter;	

	@Before
	public void setUp() throws Exception {
		positionState = new PositionState();
		positionState.setCurrentTurn(Color.WHITE);
		positionState.setEnPassantSquare(Square.a6);
		positionState.setHalfMoveClock(2);
		positionState.setFullMoveClock(5);
		
		piecePlacement = new ArrayPiecePlacement();
		piecePlacement.setPieza(Square.b5, Piece.PAWN_WHITE);
		piecePlacement.setPieza(Square.a5, Piece.PAWN_BLACK);
		
		colorBoard = new ColorBoardDebug();
		colorBoard.init(piecePlacement);
		
		PiecePositioned pawnWhite = PiecePositioned.getPiecePositioned(Square.b5, Piece.PAWN_WHITE);
		PiecePositioned pawnBlack = PiecePositioned.getPiecePositioned(Square.a5, Piece.PAWN_BLACK);
		PiecePositioned pawnPasanteSquare = PiecePositioned.getPiecePositioned(Square.a6, null);
		
		moveExecutor = new CaptureEnPassant(pawnWhite, pawnPasanteSquare, pawnBlack);		
	}
	
	@Test
	public void testPosicionPiezaBoard() {		
		// execute
		moveExecutor.executeMove(piecePlacement);
		
		// asserts execute
		assertTrue(piecePlacement.isEmtpy(Square.a5));
		assertTrue(piecePlacement.isEmtpy(Square.b5));
		assertEquals(Piece.PAWN_WHITE, piecePlacement.getPiece(Square.a6));
		
		// undos
		moveExecutor.undoMove(piecePlacement);
		
		// asserts undos
		assertTrue(piecePlacement.isEmtpy(Square.a6));
		assertEquals(Piece.PAWN_WHITE, piecePlacement.getPiece(Square.b5));
		assertEquals(Piece.PAWN_BLACK, piecePlacement.getPiece(Square.a5));
		
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
		assertEquals(Square.a6, positionState.getEnPassantSquare());
		assertEquals(Color.WHITE, positionState.getCurrentTurn());
		assertEquals(3, positionState.getHalfMoveClock());
		assertEquals(5, positionState.getFullMoveClock());
		
	}
	
	@Test
	public void testColorBoard() {
		// execute
		moveExecutor.executeMove(colorBoard);
		
		// asserts execute
		assertEquals(Color.WHITE, colorBoard.getColor(Square.a6));
		assertTrue(colorBoard.isEmpty(Square.a5));
		assertTrue(colorBoard.isEmpty(Square.b5));

		// undos
		moveExecutor.undoMove(colorBoard);
		
		// asserts undos
		assertTrue(colorBoard.isEmpty(Square.a6));
		assertEquals(Color.BLACK, colorBoard.getColor(Square.a5));
		assertEquals(Color.WHITE, colorBoard.getColor(Square.b5));
		
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

	@Test
	public void testIntegrated() {
		// execute
		moveExecutor.executeMove(piecePlacement);
		moveExecutor.executeMove(positionState);
		moveExecutor.executeMove(colorBoard);

		// asserts execute
		assertTrue(piecePlacement.isEmtpy(Square.a5));
		assertTrue(piecePlacement.isEmtpy(Square.b5));
		assertEquals(Piece.PAWN_WHITE, piecePlacement.getPiece(Square.a6));
		
		assertNull(positionState.getEnPassantSquare());
		assertEquals(Color.BLACK, positionState.getCurrentTurn());
		
		assertEquals(Color.WHITE, colorBoard.getColor(Square.a6));
		assertTrue(colorBoard.isEmpty(Square.a5));
		assertTrue(colorBoard.isEmpty(Square.b5));		
		colorBoard.validar(piecePlacement);
		
		// undos
		moveExecutor.undoMove(piecePlacement);
		moveExecutor.undoMove(positionState);
		moveExecutor.undoMove(colorBoard);

		
		// asserts undos
		assertTrue(piecePlacement.isEmtpy(Square.a6));
		assertEquals(Piece.PAWN_WHITE, piecePlacement.getPiece(Square.b5));
		assertEquals(Piece.PAWN_BLACK, piecePlacement.getPiece(Square.a5));
		
		assertEquals(Square.a6, positionState.getEnPassantSquare());
		assertEquals(Color.WHITE, positionState.getCurrentTurn());
		
		assertTrue(colorBoard.isEmpty(Square.a6));
		assertEquals(Color.BLACK, colorBoard.getColor(Square.a5));
		assertEquals(Color.WHITE, colorBoard.getColor(Square.b5));
		colorBoard.validar(piecePlacement);
	}	
}
