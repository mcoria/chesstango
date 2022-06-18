package chess.board.moves.imp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;

import chess.board.iterators.Cardinal;
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
import chess.board.debug.chess.PositionStateDebug;
import chess.board.movesgenerators.legal.MoveFilter;
import chess.board.position.ChessPosition;
import chess.board.position.PiecePlacement;
import chess.board.position.imp.ArrayPiecePlacement;


/**
 * @author Mauricio Coria
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class SaltoDoblePawnMoveTest {

	private PiecePlacement piezaBoard;
	
	private PositionStateDebug boardState;
	
	private ColorBoardDebug colorBoard;
	
	private SaltoDoblePawnMove moveExecutor;
	
	@Mock
	private ChessPosition chessPosition;
	
	@Mock
	private MoveFilter filter;	

	@Before
	public void setUp() throws Exception {
		boardState = new PositionStateDebug();
		boardState.setCurrentTurn(Color.WHITE);
		
		piezaBoard = new ArrayPiecePlacement();
		piezaBoard.setPieza(Square.e2, Piece.PAWN_WHITE);
		
		colorBoard = new ColorBoardDebug();
		colorBoard.init(piezaBoard);	
		
		PiecePositioned origen = PiecePositioned.getPiecePositioned(Square.e2, Piece.PAWN_WHITE);
		PiecePositioned destino = PiecePositioned.getPiecePositioned(Square.e4, null);
		moveExecutor =  new SaltoDoblePawnMove(origen, destino, Square.e3, Cardinal.Norte);
	}
	
	
	@Test
	public void testPosicionPiezaBoard() {
		// execute
		moveExecutor.executeMove(piezaBoard);
		
		// asserts execute		
		assertEquals(Piece.PAWN_WHITE, piezaBoard.getPiece(Square.e4));
		assertTrue(piezaBoard.isEmtpy(Square.e2));
		
		// undos		
		moveExecutor.undoMove(piezaBoard);
		
		// asserts undos		
		assertEquals(Piece.PAWN_WHITE, piezaBoard.getPiece(Square.e2));
		assertTrue(piezaBoard.isEmtpy(Square.e4));		
	}
		
	@Test
	public void testMoveState() {
		// execute
		moveExecutor.executeMove(boardState);
		
		// asserts execute
		assertEquals(Square.e3, boardState.getEnPassantSquare());
		assertEquals(Color.BLACK, boardState.getCurrentTurn());
		
		// undos
		moveExecutor.undoMove(boardState);

		// asserts undos
		assertNull(boardState.getEnPassantSquare());		
		assertEquals(Color.WHITE, boardState.getCurrentTurn());
	}
	
	@Test
	public void testColorBoard() {
		// execute
		moveExecutor.executeMove(colorBoard);

		// asserts execute
		assertTrue(colorBoard.isEmpty(Square.e2));		
		assertEquals(Color.WHITE, colorBoard.getColor(Square.e4));

		// undos
		moveExecutor.undoMove(colorBoard);
		
		// asserts undos
		assertEquals(Color.WHITE, colorBoard.getColor(Square.e2));
		assertTrue(colorBoard.isEmpty(Square.e4));
	}	
	
	@Test
	public void testBoard() {
		piezaBoard = new ArrayPiecePlacement();
		piezaBoard.setPieza(Square.e2, Piece.PAWN_WHITE);
		
		PiecePositioned origen = PiecePositioned.getPiecePositioned(Square.e2, Piece.ROOK_WHITE);
		PiecePositioned destino = PiecePositioned.getPiecePositioned(Square.e4, null);
		moveExecutor =  new SaltoDoblePawnMove(origen, destino, Square.e3, Cardinal.Norte);

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
		moveExecutor.executeMove(piezaBoard);
		moveExecutor.executeMove(boardState);
		moveExecutor.executeMove(colorBoard);

		// asserts execute
		colorBoard.validar(piezaBoard);
		boardState.validar(piezaBoard);
		
		// undos
		moveExecutor.undoMove(piezaBoard);
		moveExecutor.undoMove(boardState);
		moveExecutor.undoMove(colorBoard);

		
		// asserts undos
		colorBoard.validar(piezaBoard);	
		boardState.validar(piezaBoard);		
	}	
}
