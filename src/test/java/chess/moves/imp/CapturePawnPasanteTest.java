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
public class CapturePawnPasanteTest {

	private PiecePlacement piezaBoard;
	
	private PositionState positionState;
	
	private CapturePawnPasante moveExecutor;
	
	private ColorBoardDebug colorBoard;
	
	@Mock
	private ChessPosition chessPosition;
	
	@Mock
	private MoveFilter filter;	

	@Before
	public void setUp() throws Exception {
		positionState = new PositionState();
		positionState.setTurnoActual(Color.WHITE);
		positionState.setPawnPasanteSquare(Square.a6);		
		
		piezaBoard = new ArrayPiecePlacement();
		piezaBoard.setPieza(Square.b5, Piece.PAWN_WHITE);
		piezaBoard.setPieza(Square.a5, Piece.PAWN_BLACK);
		
		colorBoard = new ColorBoardDebug();
		colorBoard.init(piezaBoard);
		
		PiecePositioned peonWhite = new PiecePositioned(Square.b5, Piece.PAWN_WHITE);
		PiecePositioned peonBlack = new PiecePositioned(Square.a5, Piece.PAWN_BLACK);
		PiecePositioned peonPasanteSquare = new PiecePositioned(Square.a6, null);
		
		moveExecutor = new CapturePawnPasante(peonWhite, peonPasanteSquare, peonBlack);		
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
		moveExecutor.executeMove(positionState);		
		
		// asserts execute
		assertNull(positionState.getPawnPasanteSquare());
		assertEquals(Color.BLACK, positionState.getTurnoActual());	
		
		// undos
		moveExecutor.undoMove(positionState);	
		
		// asserts undos
		assertEquals(Square.a6, positionState.getPawnPasanteSquare());
		assertEquals(Color.WHITE, positionState.getTurnoActual());			
		
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
		moveExecutor.executeMove(piezaBoard);
		moveExecutor.executeMove(positionState);
		moveExecutor.executeMove(colorBoard);

		// asserts execute
		assertTrue(piezaBoard.isEmtpy(Square.a5));
		assertTrue(piezaBoard.isEmtpy(Square.b5));
		assertEquals(Piece.PAWN_WHITE, piezaBoard.getPieza(Square.a6));
		
		assertNull(positionState.getPawnPasanteSquare());
		assertEquals(Color.BLACK, positionState.getTurnoActual());	
		
		assertEquals(Color.WHITE, colorBoard.getColor(Square.a6));
		assertTrue(colorBoard.isEmpty(Square.a5));
		assertTrue(colorBoard.isEmpty(Square.b5));		
		colorBoard.validar(piezaBoard);
		
		// undos
		moveExecutor.undoMove(piezaBoard);
		moveExecutor.undoMove(positionState);
		moveExecutor.undoMove(colorBoard);

		
		// asserts undos
		assertTrue(piezaBoard.isEmtpy(Square.a6));
		assertEquals(Piece.PAWN_WHITE, piezaBoard.getPieza(Square.b5));
		assertEquals(Piece.PAWN_BLACK, piezaBoard.getPieza(Square.a5));	
		
		assertEquals(Square.a6, positionState.getPawnPasanteSquare());
		assertEquals(Color.WHITE, positionState.getTurnoActual());	
		
		assertTrue(colorBoard.isEmpty(Square.a6));
		assertEquals(Color.BLACK, colorBoard.getColor(Square.a5));
		assertEquals(Color.WHITE, colorBoard.getColor(Square.b5));
		colorBoard.validar(piezaBoard);	
	}	
}
