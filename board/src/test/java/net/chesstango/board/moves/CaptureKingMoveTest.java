package net.chesstango.board.moves;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.factory.MoveFactories;
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
	
	private PositionState positionState;

	private KingCacheBoard kingCacheBoard;
	
	private ColorBoard colorBoard;

	private MoveFactory moveFactoryWhite;

	
	@Mock
	private ChessPosition chessPosition;
	
	@Mock
	private MoveFilter filter;

	@Before
	public void setUp() throws Exception {
		positionState = new PositionState();
		kingCacheBoard = new KingCacheBoard();
		moveFactoryWhite = MoveFactories.getDefaultMoveFactoryWhite();
		moveExecutor = null;
		piecePlacement = null;
	}
	
	
	@Test
	public void testPosicionPiezaBoard() {
		piecePlacement = new ArrayPiecePlacement();
		piecePlacement.setPieza(Square.e1, Piece.KING_WHITE);

		PiecePositioned origen = PiecePositioned.getPiecePositioned(Square.e1, Piece.KING_WHITE);
		PiecePositioned destino = PiecePositioned.getPiecePositioned(Square.e2, Piece.KNIGHT_BLACK);

		moveExecutor = moveFactoryWhite.createCaptureKingMove(origen, destino);

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
		positionState.setCurrentTurn(Color.WHITE);
		positionState.setHalfMoveClock(2);
		positionState.setFullMoveClock(5);

		PiecePositioned origen = PiecePositioned.getPiecePositioned(Square.e1, Piece.KING_WHITE);
		PiecePositioned destino = PiecePositioned.getPiecePositioned(Square.e2, Piece.KNIGHT_BLACK);

		moveExecutor = moveFactoryWhite.createCaptureKingMove(origen, destino);

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
		kingCacheBoard.setKingSquare(Color.WHITE, Square.d2);

		PiecePositioned origen = PiecePositioned.getPiecePositioned(Square.e1, Piece.KING_WHITE);
		PiecePositioned destino = PiecePositioned.getPiecePositioned(Square.e2, Piece.KNIGHT_BLACK);

		moveExecutor = moveFactoryWhite.createCaptureKingMove(origen, destino);

		moveExecutor.executeMove(kingCacheBoard);

		assertEquals(Square.e2, kingCacheBoard.getSquareKingWhiteCache());

		moveExecutor.undoMove(kingCacheBoard);

		assertEquals(Square.e1, kingCacheBoard.getSquareKingWhiteCache());
	}
	
	@Test
	public void testColorBoard() {
		piecePlacement = new ArrayPiecePlacement();
		piecePlacement.setPieza(Square.e1, Piece.KING_WHITE);
		
		colorBoard = new ColorBoard();
		colorBoard.init(piecePlacement);

		PiecePositioned origen = PiecePositioned.getPiecePositioned(Square.e1, Piece.KING_WHITE);
		PiecePositioned destino = PiecePositioned.getPiecePositioned(Square.e2, Piece.KNIGHT_BLACK);

		moveExecutor = moveFactoryWhite.createCaptureKingMove(origen, destino);

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
		PiecePositioned origen = PiecePositioned.getPiecePositioned(Square.e1, Piece.KING_WHITE);
		PiecePositioned destino = PiecePositioned.getPiecePositioned(Square.e2, Piece.KNIGHT_BLACK);

		moveExecutor = moveFactoryWhite.createCaptureKingMove(origen, destino);

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
		PiecePositioned origen = PiecePositioned.getPiecePositioned(Square.e1, Piece.KING_WHITE);
		PiecePositioned destino = PiecePositioned.getPiecePositioned(Square.e2, Piece.KNIGHT_BLACK);

		moveExecutor = moveFactoryWhite.createCaptureKingMove(origen, destino);

		// execute
		moveExecutor.filter(filter);

		// asserts execute
		verify(filter).filterMove(moveExecutor);
	}	
	
	@Test
	public void testExecuteUndo() {
		piecePlacement = new ArrayPiecePlacement();
		piecePlacement.setPieza(Square.e1, Piece.KING_WHITE);
		
		colorBoard = new ColorBoard();
		colorBoard.init(piecePlacement);
		
		positionState.setCurrentTurn(Color.WHITE);

		PiecePositioned origen = PiecePositioned.getPiecePositioned(Square.e1, Piece.KING_WHITE);
		PiecePositioned destino = PiecePositioned.getPiecePositioned(Square.e2, Piece.KNIGHT_BLACK);

		moveExecutor = moveFactoryWhite.createCaptureKingMove(origen, destino);

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
}
