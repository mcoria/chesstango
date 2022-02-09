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
import chess.moves.imp.SimplePawnPromocion;
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
public class SimplePawnPromocionTest {

	private PiecePlacement piezaBoard;
	
	private PositionState positionState;
	
	private SimplePawnPromocion moveExecutor;
	
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
		piezaBoard.setPieza(Square.e7, Piece.PAWN_WHITE);
		
		colorBoard = new ColorBoardDebug();
		colorBoard.init(piezaBoard);	
		
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
		assertEquals(Color.WHITE, colorBoard.getColor(Square.e8));
		assertTrue(colorBoard.isEmpty(Square.e7));

		// undos
		moveExecutor.undoMove(colorBoard);
		
		// asserts undos
		assertEquals(Color.WHITE, colorBoard.getColor(Square.e7));
		assertTrue(colorBoard.isEmpty(Square.e8));
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
