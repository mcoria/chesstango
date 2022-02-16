package chess.moves.imp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import chess.Color;
import chess.Piece;
import chess.PiecePositioned;
import chess.Square;
import chess.debug.chess.ColorBoardDebug;
import chess.legalmovesgenerators.MoveFilter;
import chess.position.ChessPosition;
import chess.position.ColorBoard;
import chess.position.PiecePlacement;
import chess.position.PositionState;
import chess.position.imp.ArrayPiecePlacement;


/**
 * @author Mauricio Coria
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class SimpleMoveTest {

	private PiecePlacement piezaBoard;
	
	private PositionState positionState;
	
	private SimpleMove moveExecutor;
	
	private ColorBoard colorBoard;
	
	@Mock
	private ChessPosition chessPosition;
	
	@Mock
	private MoveFilter filter;	

	@Before
	public void setUp() throws Exception {
		positionState = new PositionState();
		positionState.setTurnoActual(Color.WHITE);
		
		piezaBoard = new ArrayPiecePlacement();
		piezaBoard.setPieza(Square.e5, Piece.ROOK_WHITE);
		
		colorBoard = new ColorBoardDebug();
		colorBoard.init(piezaBoard);
		
		PiecePositioned origen = new PiecePositioned(Square.e5, Piece.ROOK_WHITE);
		PiecePositioned destino = new PiecePositioned(Square.e7, null);
		moveExecutor =  new SimpleMove(origen, destino);		
	}
	
	
	@Test
	public void testPosicionPiezaBoard() {
		// execute
		moveExecutor.executeMove(piezaBoard);
		
		// asserts execute		
		assertEquals(Piece.ROOK_WHITE, piezaBoard.getPieza(Square.e7));
		assertTrue(piezaBoard.isEmtpy(Square.e5));
		
		// undos		
		moveExecutor.undoMove(piezaBoard);
		
		// asserts undos		
		assertEquals(Piece.ROOK_WHITE, piezaBoard.getPieza(Square.e5));
		assertTrue(piezaBoard.isEmtpy(Square.e7));		
	}
		
	@Test
	public void testMoveState() {
		// execute
		moveExecutor.executeMove(positionState);
		
		// asserts execute
		assertNull(positionState.getPawnPasanteSquare());
		assertEquals(Color.BLACK, positionState.getTurnoActual());
		
		// undos
		moveExecutor.undoMove(positionState);

		// asserts undos	
		assertEquals(Color.WHITE, positionState.getTurnoActual());
	}
	
	@Test
	public void testColorBoard() {
		// execute
		moveExecutor.executeMove(colorBoard);

		// asserts execute
		assertEquals(Color.WHITE, colorBoard.getColor(Square.e7));
		assertTrue(colorBoard.isEmpty(Square.e5));

		// undos
		moveExecutor.undoMove(colorBoard);
		
		// asserts undos
		assertEquals(Color.WHITE, colorBoard.getColor(Square.e5));
		assertTrue(colorBoard.isEmpty(Square.e7));
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
