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
import chess.BoardState;
import chess.Color;
import chess.Piece;
import chess.PiecePositioned;
import chess.Square;
import chess.debug.chess.ColorBoardDebug;
import chess.layers.KingCacheBoard;
import chess.layers.PosicionPiezaBoard;
import chess.layers.imp.ArrayPosicionPiezaBoard;
import chess.moves.CapturePawnPasante;
import chess.pseudomovesfilters.MoveFilter;


/**
 * @author Mauricio Coria
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class CapturePawnPasanteTest {

	private PosicionPiezaBoard piezaBoard;
	
	private BoardState boardState;
	
	private CapturePawnPasante moveExecutor;
	
	private ColorBoardDebug colorBoard;
	
	@Mock
	private Board board;
	
	@Mock
	private MoveFilter filter;	

	@Before
	public void setUp() throws Exception {
		boardState = new BoardState();
		boardState.setTurnoActual(Color.WHITE);
		boardState.setPawnPasanteSquare(Square.a6);		
		
		piezaBoard = new ArrayPosicionPiezaBoard();
		piezaBoard.setPieza(Square.b5, Piece.PAWN_WHITE);
		piezaBoard.setPieza(Square.a5, Piece.PAWN_BLACK);
		
		colorBoard = new ColorBoardDebug(piezaBoard);
		
		PiecePositioned peonBlanco = new PiecePositioned(Square.b5, Piece.PAWN_WHITE);
		PiecePositioned peonNegro = new PiecePositioned(Square.a5, Piece.PAWN_BLACK);
		PiecePositioned peonPasanteSquare = new PiecePositioned(Square.a6, null);
		
		moveExecutor = new CapturePawnPasante(peonBlanco, peonPasanteSquare, peonNegro);		
	}
	
	@Test
	public void testPosicionPiezaBoard() {		
		// execute
		moveExecutor.executeMove(piezaBoard);
		
		// asserts execute
		assertTrue(piezaBoard.isEmtpy(Square.a5));
		assertTrue(piezaBoard.isEmtpy(Square.b5));
		assertEquals(Piece.PAWN_WHITE, piezaBoard.getPieza(Square.a6));
		
		// undos
		moveExecutor.undoMove(piezaBoard);
		
		// asserts undos
		assertTrue(piezaBoard.isEmtpy(Square.a6));
		assertEquals(Piece.PAWN_WHITE, piezaBoard.getPieza(Square.b5));
		assertEquals(Piece.PAWN_BLACK, piezaBoard.getPieza(Square.a5));		
		
	}
	
	@Test
	public void testMoveState() {
		// execute
		moveExecutor.executeMove(boardState);		
		
		// asserts execute
		assertNull(boardState.getPawnPasanteSquare());
		assertEquals(Color.BLACK, boardState.getTurnoActual());	
		
		// undos
		moveExecutor.undoMove(boardState);	
		
		// asserts undos
		assertEquals(Square.a6, boardState.getPawnPasanteSquare());
		assertEquals(Color.WHITE, boardState.getTurnoActual());			
		
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
	
	@Test(expected = RuntimeException.class)
	public void testKingCacheBoardMoveRuntimeException() {
		moveExecutor.executeMove(new KingCacheBoard());
	}
	
	@Test(expected = RuntimeException.class)
	public void testKingCacheBoardUndoMoveRuntimeException() {
		moveExecutor.undoMove(new KingCacheBoard());
	}		

	@Test
	public void testIntegrated() {
		// execute
		moveExecutor.executeMove(piezaBoard);
		moveExecutor.executeMove(boardState);
		moveExecutor.executeMove(colorBoard);

		// asserts execute
		assertTrue(piezaBoard.isEmtpy(Square.a5));
		assertTrue(piezaBoard.isEmtpy(Square.b5));
		assertEquals(Piece.PAWN_WHITE, piezaBoard.getPieza(Square.a6));
		
		assertNull(boardState.getPawnPasanteSquare());
		assertEquals(Color.BLACK, boardState.getTurnoActual());	
		
		assertEquals(Color.WHITE, colorBoard.getColor(Square.a6));
		assertTrue(colorBoard.isEmpty(Square.a5));
		assertTrue(colorBoard.isEmpty(Square.b5));		
		colorBoard.validar(piezaBoard);
		
		// undos
		moveExecutor.undoMove(piezaBoard);
		moveExecutor.undoMove(boardState);
		moveExecutor.undoMove(colorBoard);

		
		// asserts undos
		assertTrue(piezaBoard.isEmtpy(Square.a6));
		assertEquals(Piece.PAWN_WHITE, piezaBoard.getPieza(Square.b5));
		assertEquals(Piece.PAWN_BLACK, piezaBoard.getPieza(Square.a5));	
		
		assertEquals(Square.a6, boardState.getPawnPasanteSquare());
		assertEquals(Color.WHITE, boardState.getTurnoActual());	
		
		assertTrue(colorBoard.isEmpty(Square.a6));
		assertEquals(Color.BLACK, colorBoard.getColor(Square.a5));
		assertEquals(Color.WHITE, colorBoard.getColor(Square.b5));
		colorBoard.validar(piezaBoard);	
	}	
}
