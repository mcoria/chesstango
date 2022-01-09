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

import chess.ChessPosition;
import chess.Color;
import chess.Piece;
import chess.PiecePositioned;
import chess.Square;
import chess.layers.ChessPositionState;
import chess.layers.ColorBoard;
import chess.layers.KingCacheBoard;
import chess.layers.PiecePlacement;
import chess.layers.imp.ArrayPiecePlacement;
import chess.moves.CapturaPawnPromocion;
import chess.pseudomovesfilters.MoveFilter;


/**
 * @author Mauricio Coria
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class CapturePawnPromocionTest {

	private PiecePlacement piezaBoard;
	
	private ChessPositionState chessPositionState;
	
	private CapturaPawnPromocion moveExecutor;
	
	private ColorBoard colorBoard;
	
	@Mock
	private ChessPosition chessPosition;
	
	@Mock
	private MoveFilter filter;	

	@Before
	public void setUp() throws Exception {
		chessPositionState = new ChessPositionState();
		chessPositionState.setTurnoActual(Color.WHITE);
		
		piezaBoard = new ArrayPiecePlacement();
		piezaBoard.setPieza(Square.e7, Piece.PAWN_WHITE);
		piezaBoard.setPieza(Square.f8, Piece.KNIGHT_BLACK);
		
		colorBoard = new ColorBoard(piezaBoard);
		
		PiecePositioned origen = new PiecePositioned(Square.e7, Piece.PAWN_WHITE);
		PiecePositioned destino = new PiecePositioned(Square.f8, Piece.KNIGHT_BLACK);
		
		moveExecutor =  new CapturaPawnPromocion(origen, destino, Piece.QUEEN_WHITE);		
	}
	
	
	@Test
	public void testPosicionPiezaBoard() {
		// execute
		moveExecutor.executeMove(piezaBoard);
		
		// asserts execute		
		assertEquals(Piece.QUEEN_WHITE, piezaBoard.getPieza(Square.f8));
		assertTrue(piezaBoard.isEmtpy(Square.e7));
		
		// undos		
		moveExecutor.undoMove(piezaBoard);
		
		// asserts undos		
		assertEquals(Piece.PAWN_WHITE, piezaBoard.getPieza(Square.e7));
		assertEquals(Piece.KNIGHT_BLACK, piezaBoard.getPieza(Square.f8));		
	}
		
	@Test
	public void testMoveState() {
		// execute
		moveExecutor.executeMove(chessPositionState);
		
		// asserts execute
		assertNull(chessPositionState.getPawnPasanteSquare());
		assertEquals(Color.BLACK, chessPositionState.getTurnoActual());
		
		// undos
		moveExecutor.undoMove(chessPositionState);

		// asserts undos	
		assertEquals(Color.WHITE, chessPositionState.getTurnoActual());
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
	
	@Test(expected = RuntimeException.class)
	public void testKingCacheBoardMoveRuntimeException() {
		piezaBoard = new ArrayPiecePlacement();
		piezaBoard.setPieza(Square.e7, Piece.PAWN_WHITE);

		PiecePositioned origen = new PiecePositioned(Square.e7, Piece.PAWN_WHITE);
		PiecePositioned destino = new PiecePositioned(Square.f8, Piece.KNIGHT_BLACK);
		moveExecutor =  new CapturaPawnPromocion(origen, destino, Piece.QUEEN_WHITE);

		moveExecutor.executeMove(new KingCacheBoard());
	}
	
	@Test(expected = RuntimeException.class)
	public void testKingCacheBoardUndoMoveRuntimeException() {
		moveExecutor.undoMove(new KingCacheBoard());
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
