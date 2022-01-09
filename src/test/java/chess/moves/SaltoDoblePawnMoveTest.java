package chess.moves;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import chess.Board;
import chess.Color;
import chess.Piece;
import chess.PiecePositioned;
import chess.Square;
import chess.debug.chess.BoardStateDebug;
import chess.debug.chess.ColorBoardDebug;
import chess.layers.KingCacheBoard;
import chess.layers.PosicionPiezaBoard;
import chess.layers.imp.ArrayPosicionPiezaBoard;
import chess.moves.SaltoDoblePawnMove;
import chess.pseudomovesfilters.MoveFilter;


/**
 * @author Mauricio Coria
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class SaltoDoblePawnMoveTest {

	private PosicionPiezaBoard piezaBoard;
	
	private BoardStateDebug boardState;
	
	private ColorBoardDebug colorBoard;
	
	private SaltoDoblePawnMove moveExecutor;
	
	@Mock
	private Board board;
	
	@Mock
	private MoveFilter filter;	

	@Before
	public void setUp() throws Exception {
		boardState = new BoardStateDebug();
		boardState.setTurnoActual(Color.WHITE);
		
		piezaBoard = new ArrayPosicionPiezaBoard();
		piezaBoard.setPieza(Square.e2, Piece.PAWN_WHITE);
		
		colorBoard = new ColorBoardDebug(piezaBoard);		
		
		PiecePositioned origen = new PiecePositioned(Square.e2, Piece.PAWN_WHITE);
		PiecePositioned destino = new PiecePositioned(Square.e4, null);
		moveExecutor =  new SaltoDoblePawnMove(origen, destino, Square.e3);		
	}
	
	
	@Test
	public void testPosicionPiezaBoard() {
		// execute
		moveExecutor.executeMove(piezaBoard);
		
		// asserts execute		
		assertEquals(Piece.PAWN_WHITE, piezaBoard.getPieza(Square.e4));
		assertTrue(piezaBoard.isEmtpy(Square.e2));
		
		// undos		
		moveExecutor.undoMove(piezaBoard);
		
		// asserts undos		
		assertEquals(Piece.PAWN_WHITE, piezaBoard.getPieza(Square.e2));
		assertTrue(piezaBoard.isEmtpy(Square.e4));		
	}
		
	@Test
	public void testMoveState() {
		// execute
		moveExecutor.executeMove(boardState);
		
		// asserts execute
		assertEquals(Square.e3, boardState.getPawnPasanteSquare());
		assertEquals(Color.BLACK, boardState.getTurnoActual());
		
		// undos
		moveExecutor.undoMove(boardState);

		// asserts undos
		assertNull(boardState.getPawnPasanteSquare());		
		assertEquals(Color.WHITE, boardState.getTurnoActual());
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
	
	@Test(expected = RuntimeException.class)
	public void testKingCacheBoardMoveRuntimeException() {
		moveExecutor.executeMove(new KingCacheBoard());
	}
	
	@Test(expected = RuntimeException.class)
	public void testKingCacheBoardUndoMoveRuntimeException() {
		moveExecutor.undoMove(new KingCacheBoard());
	}	
	
	@Test
	public void testBoard() {
		piezaBoard = new ArrayPosicionPiezaBoard();
		piezaBoard.setPieza(Square.e2, Piece.PAWN_WHITE);
		
		PiecePositioned origen = new PiecePositioned(Square.e2, Piece.ROOK_WHITE);
		PiecePositioned destino = new PiecePositioned(Square.e4, null);
		moveExecutor =  new SaltoDoblePawnMove(origen, destino, Square.e3);

		// execute
		moveExecutor.executeMove(board);

		// asserts execute
		verify(board).executeMove(moveExecutor);

		// undos
		moveExecutor.undoMove(board);

		
		// asserts undos
		verify(board).undoMove(moveExecutor);
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
