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
import chess.layers.ColorBoard;
import chess.layers.KingCacheBoard;
import chess.layers.PosicionPiezaBoard;
import chess.layers.imp.ArrayPosicionPiezaBoard;
import chess.moves.SimplePawnPromocion;
import chess.pseudomovesfilters.MoveFilter;


/**
 * @author Mauricio Coria
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class SimplePawnPromocionTest {

	private PosicionPiezaBoard piezaBoard;
	
	private BoardState boardState;
	
	private SimplePawnPromocion moveExecutor;
	
	private ColorBoard colorBoard;
	
	@Mock
	private Board board;
	
	@Mock
	private MoveFilter filter;	

	@Before
	public void setUp() throws Exception {
		boardState = new BoardState();
		boardState.setTurnoActual(Color.WHITE);
		
		piezaBoard = new ArrayPosicionPiezaBoard();
		piezaBoard.setPieza(Square.e7, Piece.PAWN_WHITE);
		
		colorBoard = new ColorBoard(piezaBoard);		
		
		PiecePositioned origen = new PiecePositioned(Square.e7, Piece.PAWN_WHITE);
		PiecePositioned destino = new PiecePositioned(Square.e8, null);
		moveExecutor =  new SimplePawnPromocion(origen, destino, Piece.QUEEN_WHITE);		
	}
	
	
	@Test
	public void testPosicionPiezaBoard() {
		// execute
		moveExecutor.executeMove(piezaBoard);
		
		// asserts execute		
		assertEquals(Piece.QUEEN_WHITE, piezaBoard.getPieza(Square.e8));
		assertTrue(piezaBoard.isEmtpy(Square.e7));
		
		// undos		
		moveExecutor.undoMove(piezaBoard);
		
		// asserts undos		
		assertEquals(Piece.PAWN_WHITE, piezaBoard.getPieza(Square.e7));
		assertTrue(piezaBoard.isEmtpy(Square.e8));		
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
		assertEquals(Color.WHITE, boardState.getTurnoActual());
	}
	
	@Test
	public void testColorBoard() {
		// execute
		moveExecutor.executeMove(colorBoard);

		// asserts execute
		assertEquals(Color.WHITE, colorBoard.getColor(Square.e8));
		assertTrue(colorBoard.isEmpty(Square.e7));

		// undos
		moveExecutor.undoMove(colorBoard);
		
		// asserts undos
		assertEquals(Color.WHITE, colorBoard.getColor(Square.e7));
		assertTrue(colorBoard.isEmpty(Square.e8));
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
}
