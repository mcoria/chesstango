package net.chesstango.board.moves;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.debug.chess.ColorBoardDebug;
import net.chesstango.board.debug.chess.KingCacheBoardDebug;
import net.chesstango.board.debug.chess.PositionStateDebug;
import net.chesstango.board.factory.SingletonMoveFactories;
import net.chesstango.board.movesgenerators.legal.MoveFilter;
import net.chesstango.board.position.ChessPosition;
import net.chesstango.board.position.PiecePlacement;
import net.chesstango.board.position.imp.ArrayPiecePlacement;
import net.chesstango.board.position.imp.ColorBoard;
import net.chesstango.board.position.imp.KingCacheBoard;
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
public class CaptureKingMoveTest {

	private MoveKing moveExecutor;
	
	private PiecePlacement piecePlacement;
	
	private PositionStateDebug positionState;

	private KingCacheBoardDebug kingCacheBoard;
	
	private ColorBoardDebug colorBoard;

	
	@Mock
	private ChessPosition chessPosition;
	
	@Mock
	private MoveFilter filter;

	@Before
	public void setUp() throws Exception {
		positionState = new PositionStateDebug();
		positionState.setCurrentTurn(Color.WHITE);
		positionState.setHalfMoveClock(2);
		positionState.setFullMoveClock(5);

		piecePlacement = new ArrayPiecePlacement();
		piecePlacement.setPieza(Square.e1, Piece.KING_WHITE);
		piecePlacement.setPieza(Square.e2, Piece.KNIGHT_BLACK);

		colorBoard = new ColorBoardDebug();
		colorBoard.init(piecePlacement);

		kingCacheBoard = new KingCacheBoardDebug();
		colorBoard.init(piecePlacement);

		PiecePositioned origen = piecePlacement.getPosicion(Square.e1);
		PiecePositioned destino = piecePlacement.getPosicion(Square.e2);

		moveExecutor = SingletonMoveFactories.getDefaultMoveFactoryWhite().createCaptureKingMove(origen, destino);
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
		assertEquals(Piece.KING_WHITE, piecePlacement.getPiece(Square.e2));
		assertNull(piecePlacement.getPiece(Square.e1));

		// undos
		moveExecutor.undoMove(piecePlacement);
		
		// asserts undos
		assertEquals(Piece.KING_WHITE, piecePlacement.getPiece(Square.e1));
		assertEquals(Piece.KNIGHT_BLACK, piecePlacement.getPiece(Square.e2));
	}	
	
	@Test
	public void testBoardState() {
		moveExecutor.executeMove(positionState);
		assertEquals(0, positionState.getHalfMoveClock());
		assertEquals(5, positionState.getFullMoveClock());

		assertEquals(Color.BLACK, positionState.getCurrentTurn());

		moveExecutor.undoMove(positionState);

		assertEquals(Color.WHITE, positionState.getCurrentTurn());
		assertEquals(2, positionState.getHalfMoveClock());
		assertEquals(5, positionState.getFullMoveClock());
	}		

	@Test
	public void testKingCacheBoard() {
		moveExecutor.executeMove(kingCacheBoard);

		assertEquals(Square.e2, kingCacheBoard.getSquareKingWhiteCache());

		moveExecutor.undoMove(kingCacheBoard);

		assertEquals(Square.e1, kingCacheBoard.getSquareKingWhiteCache());
	}
	
	@Test
	public void testColorBoard() {
		// execute
		moveExecutor.executeMove(colorBoard);

		// asserts execute
		assertEquals(Color.WHITE, colorBoard.getColor(Square.e2));
		assertTrue(colorBoard.isEmpty(Square.e1));

		// undos
		moveExecutor.undoMove(colorBoard);

		// asserts undos
		assertEquals(Color.WHITE, colorBoard.getColor(Square.e1));
		assertEquals(Color.BLACK, colorBoard.getColor(Square.e2));
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
	public void testExecuteUndo() {
		// execute
		moveExecutor.executeMove(piecePlacement);
		moveExecutor.executeMove(kingCacheBoard);
		moveExecutor.executeMove(positionState);
		moveExecutor.executeMove(colorBoard);

		// asserts execute
		assertEquals(Piece.KING_WHITE, piecePlacement.getPiece(Square.e2));
		assertNull(piecePlacement.getPiece(Square.e1));
		
		assertEquals(Square.e2, kingCacheBoard.getSquareKingWhiteCache());
		
		assertEquals(Color.BLACK, positionState.getCurrentTurn());
		
		assertEquals(Color.WHITE, colorBoard.getColor(Square.e2));
		assertTrue(colorBoard.isEmpty(Square.e1));

		// undos
		moveExecutor.undoMove(piecePlacement);
		moveExecutor.undoMove(kingCacheBoard);
		moveExecutor.undoMove(positionState);
		moveExecutor.undoMove(colorBoard);

		
		// asserts undos
		assertEquals(Piece.KING_WHITE, piecePlacement.getPiece(Square.e1));
		assertEquals(Piece.KNIGHT_BLACK, piecePlacement.getPiece(Square.e2));
		
		assertEquals(Square.e1, kingCacheBoard.getSquareKingWhiteCache());
		
		assertEquals(Color.WHITE, positionState.getCurrentTurn());
		
		assertEquals(Color.WHITE, colorBoard.getColor(Square.e1));
		assertEquals(Color.BLACK, colorBoard.getColor(Square.e2));
	}


	@Test
	public void testIntegrated() {
		// execute
		moveExecutor.executeMove(piecePlacement);
		moveExecutor.executeMove(positionState);
		moveExecutor.executeMove(colorBoard);
		moveExecutor.executeMove(kingCacheBoard);

		// asserts execute
		colorBoard.validar(piecePlacement);
		positionState.validar(piecePlacement);
		kingCacheBoard.validar(piecePlacement);

		// undos
		moveExecutor.undoMove(piecePlacement);
		moveExecutor.undoMove(positionState);
		moveExecutor.undoMove(colorBoard);
		moveExecutor.undoMove(kingCacheBoard);


		// asserts undos
		colorBoard.validar(piecePlacement);
		positionState.validar(piecePlacement);
		kingCacheBoard.validar(piecePlacement);
	}
}
