package net.chesstango.board.moves;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.debug.chess.ColorBoardDebug;
import net.chesstango.board.debug.chess.PositionStateDebug;
import net.chesstango.board.factory.SingletonMoveFactories;
import net.chesstango.board.movesgenerators.legal.MoveFilter;
import net.chesstango.board.position.ChessPosition;
import net.chesstango.board.position.PiecePlacement;
import net.chesstango.board.position.imp.ArrayPiecePlacement;
import net.chesstango.board.position.imp.ColorBoard;
import net.chesstango.board.position.imp.PositionState;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;


/**
 * @author Mauricio Coria
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class CapturePawnPromotionTest {

	private PiecePlacement piecePlacement;
	
	private PositionStateDebug positionState;
	
	private Move moveExecutor;
	
	private ColorBoardDebug colorBoard;
	
	@Mock
	private ChessPosition chessPosition;
	
	@Mock
	private MoveFilter filter;	

	@Before
	public void setUp() throws Exception {
		positionState = new PositionStateDebug();
		positionState.setCurrentTurn(Color.WHITE);
		positionState.setEnPassantSquare(null);
		positionState.setHalfMoveClock(3);
		positionState.setFullMoveClock(5);
		
		piecePlacement = new ArrayPiecePlacement();
		piecePlacement.setPieza(Square.e7, Piece.PAWN_WHITE);
		piecePlacement.setPieza(Square.f8, Piece.KNIGHT_BLACK);
		
		colorBoard = new ColorBoardDebug();
		colorBoard.init(piecePlacement);
		
		PiecePositioned origen = piecePlacement.getPosicion(Square.e7);
		PiecePositioned destino = piecePlacement.getPosicion(Square.f8);
		
		moveExecutor = SingletonMoveFactories.getDefaultMoveFactoryWhite().createCapturePawnPromotion(origen, destino, Piece.QUEEN_WHITE);
	}

	@Test
	public void testEquals() {
		assertEquals(SingletonMoveFactories.getDefaultMoveFactoryWhite().createSimplePawnPromotion(piecePlacement.getPosicion(Square.e7), piecePlacement.getPosicion(Square.e8), Piece.QUEEN_WHITE), moveExecutor);
	}

	@Test
	public void testPosicionPiezaBoard() {
		// execute
		moveExecutor.executeMove(piecePlacement);
		
		// asserts execute		
		assertEquals(Piece.QUEEN_WHITE, piecePlacement.getPiece(Square.f8));
		assertTrue(piecePlacement.isEmpty(Square.e7));
		
		// undos		
		moveExecutor.undoMove(piecePlacement);
		
		// asserts undos		
		assertEquals(Piece.PAWN_WHITE, piecePlacement.getPiece(Square.e7));
		assertEquals(Piece.KNIGHT_BLACK, piecePlacement.getPiece(Square.f8));
	}
		
	@Test
	public void testMoveState() {
		// execute
		moveExecutor.executeMove(positionState);
		
		// asserts execute
		assertNull(positionState.getEnPassantSquare());
		assertEquals(Color.BLACK, positionState.getCurrentTurn());
		assertEquals(0, positionState.getHalfMoveClock());
		assertEquals(5, positionState.getFullMoveClock());
		
		// undos
		moveExecutor.undoMove(positionState);

		// asserts undos	
		assertEquals(Color.WHITE, positionState.getCurrentTurn());
		assertEquals(3, positionState.getHalfMoveClock());
		assertEquals(5, positionState.getFullMoveClock());
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
		assertEquals(Piece.QUEEN_WHITE, piecePlacement.getPiece(Square.f8));
		assertTrue(piecePlacement.isEmpty(Square.e7));

		assertNull(positionState.getEnPassantSquare());
		assertEquals(Color.BLACK, positionState.getCurrentTurn());
		assertEquals(0, positionState.getHalfMoveClock());
		assertEquals(5, positionState.getFullMoveClock());

		assertEquals(Color.WHITE, colorBoard.getColor(Square.f8));
		assertTrue(colorBoard.isEmpty(Square.e7));

		colorBoard.validar(piecePlacement);
		positionState.validar(piecePlacement);

		// undos
		moveExecutor.undoMove(piecePlacement);
		moveExecutor.undoMove(positionState);
		moveExecutor.undoMove(colorBoard);


		// asserts undos
		assertEquals(Piece.PAWN_WHITE, piecePlacement.getPiece(Square.e7));
		assertEquals(Piece.KNIGHT_BLACK, piecePlacement.getPiece(Square.f8));

		assertNull(positionState.getEnPassantSquare());
		assertEquals(Color.WHITE, positionState.getCurrentTurn());
		assertEquals(3, positionState.getHalfMoveClock());
		assertEquals(5, positionState.getFullMoveClock());

		assertEquals(Color.WHITE, colorBoard.getColor(Square.e7));
		assertEquals(Color.BLACK, colorBoard.getColor(Square.f8));

		colorBoard.validar(piecePlacement);
		positionState.validar(piecePlacement);
	}
}
