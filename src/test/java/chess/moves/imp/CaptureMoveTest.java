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
import chess.position.PiecePlacement;
import chess.position.imp.ArrayPiecePlacement;
import chess.position.imp.PositionState;


/**
 * @author Mauricio Coria
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class CaptureMoveTest {

	private PiecePlacement piezaBoard;
	
	private PositionState positionState;
	
	private CaptureMove moveExecutor;
	
	private ColorBoardDebug colorBoard;
	
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
		piezaBoard.setPieza(Square.e7, Piece.PAWN_BLACK);

		colorBoard = new ColorBoardDebug();
		colorBoard.init(piezaBoard);		
		
		PiecePositioned origen = new PiecePositioned(Square.e5, Piece.ROOK_WHITE);
		PiecePositioned destino = new PiecePositioned(Square.e7, Piece.PAWN_BLACK);

		moveExecutor = new CaptureMove(origen, destino);
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
		assertEquals(Piece.PAWN_BLACK, piezaBoard.getPieza(Square.e7));
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
		assertEquals(Color.BLACK, colorBoard.getColor(Square.e7));
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
		moveExecutor.executeMove(piezaBoard);		
		moveExecutor.executeMove(colorBoard);
		moveExecutor.executeMove(positionState);
		
		colorBoard.validar(piezaBoard);
		
		// undos
		moveExecutor.undoMove(piezaBoard);		
		moveExecutor.undoMove(colorBoard);
		moveExecutor.undoMove(positionState);		
		
		colorBoard.validar(piezaBoard);
	}
	
	
}
