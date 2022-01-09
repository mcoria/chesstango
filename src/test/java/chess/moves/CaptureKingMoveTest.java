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
import chess.moves.CaptureKingMove;
import chess.pseudomovesfilters.MoveFilter;


/**
 * @author Mauricio Coria
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class CaptureKingMoveTest {

	private CaptureKingMove moveExecutor;
	
	private PiecePlacement piezaBoard;
	
	private ChessPositionState chessPositionState;

	private KingCacheBoard kingCacheBoard;
	
	private ColorBoard colorBoard;
	
	@Mock
	private ChessPosition chessPosition;
	
	@Mock
	private MoveFilter filter;

	@Before
	public void setUp() throws Exception {
		chessPositionState = new ChessPositionState();
		kingCacheBoard = new KingCacheBoard();
		moveExecutor = null;
		piezaBoard = null;
	}
	
	
	@Test
	public void testPosicionPiezaBoard() {
		piezaBoard = new ArrayPiecePlacement();
		piezaBoard.setPieza(Square.e1, Piece.KING_WHITE);

		PiecePositioned origen = new PiecePositioned(Square.e1, Piece.KING_WHITE);
		PiecePositioned destino = new PiecePositioned(Square.e2, Piece.KNIGHT_BLACK);

		moveExecutor = new CaptureKingMove(origen, destino);

		// execute
		moveExecutor.executeMove(piezaBoard);

		// asserts execute
		assertEquals(Piece.KING_WHITE, piezaBoard.getPieza(Square.e2));
		assertNull(piezaBoard.getPieza(Square.e1));

		// undos
		moveExecutor.undoMove(piezaBoard);
		
		// asserts undos
		assertEquals(Piece.KING_WHITE, piezaBoard.getPieza(Square.e1));
		assertEquals(Piece.KNIGHT_BLACK, piezaBoard.getPieza(Square.e2));
	}	
	
	@Test
	public void testBoardState() {
		chessPositionState.setTurnoActual(Color.WHITE);

		PiecePositioned origen = new PiecePositioned(Square.e1, Piece.KING_WHITE);
		PiecePositioned destino = new PiecePositioned(Square.e2, Piece.KNIGHT_BLACK);

		moveExecutor = new CaptureKingMove(origen, destino);

		moveExecutor.executeMove(chessPositionState);

		assertEquals(Color.BLACK, chessPositionState.getTurnoActual());

		moveExecutor.undoMove(chessPositionState);

		assertEquals(Color.WHITE, chessPositionState.getTurnoActual());
	}		

	@Test
	public void testKingCacheBoard() {
		kingCacheBoard.setKingSquare(Color.WHITE, Square.d2);

		PiecePositioned origen = new PiecePositioned(Square.e1, Piece.KING_WHITE);
		PiecePositioned destino = new PiecePositioned(Square.e2, Piece.KNIGHT_BLACK);

		moveExecutor = new CaptureKingMove(origen, destino);

		moveExecutor.executeMove(kingCacheBoard);

		assertEquals(Square.e2, kingCacheBoard.getSquareKingWhiteCache());

		moveExecutor.undoMove(kingCacheBoard);

		assertEquals(Square.e1, kingCacheBoard.getSquareKingWhiteCache());
	}
	
	@Test
	public void testColorBoard() {
		piezaBoard = new ArrayPiecePlacement();
		piezaBoard.setPieza(Square.e1, Piece.KING_WHITE);
		
		colorBoard = new ColorBoard(piezaBoard);

		PiecePositioned origen = new PiecePositioned(Square.e1, Piece.KING_WHITE);
		PiecePositioned destino = new PiecePositioned(Square.e2, Piece.KNIGHT_BLACK);

		moveExecutor = new CaptureKingMove(origen, destino);

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
		PiecePositioned origen = new PiecePositioned(Square.e1, Piece.KING_WHITE);
		PiecePositioned destino = new PiecePositioned(Square.e2, Piece.KNIGHT_BLACK);

		moveExecutor = new CaptureKingMove(origen, destino);

		// execute
		moveExecutor.executeMove(chessPosition);

		// asserts execute
		verify(chessPosition).executeKingMove(moveExecutor);

		// undos
		moveExecutor.undoMove(chessPosition);

		
		// asserts undos
		verify(chessPosition).undoKingMove(moveExecutor);
	}
	
	
	@Test
	public void testFilter() {
		PiecePositioned origen = new PiecePositioned(Square.e1, Piece.KING_WHITE);
		PiecePositioned destino = new PiecePositioned(Square.e2, Piece.KNIGHT_BLACK);

		moveExecutor = new CaptureKingMove(origen, destino);

		// execute
		moveExecutor.filter(filter);

		// asserts execute
		verify(filter).filterKingMove(moveExecutor);
	}	
	
	@Test
	public void testExecuteUndo() {
		piezaBoard = new ArrayPiecePlacement();
		piezaBoard.setPieza(Square.e1, Piece.KING_WHITE);
		
		colorBoard = new ColorBoard(piezaBoard);
		
		chessPositionState.setTurnoActual(Color.WHITE);

		PiecePositioned origen = new PiecePositioned(Square.e1, Piece.KING_WHITE);
		PiecePositioned destino = new PiecePositioned(Square.e2, Piece.KNIGHT_BLACK);

		moveExecutor = new CaptureKingMove(origen, destino);

		// execute
		moveExecutor.executeMove(piezaBoard);
		moveExecutor.executeMove(kingCacheBoard);
		moveExecutor.executeMove(chessPositionState);
		moveExecutor.executeMove(colorBoard);

		// asserts execute
		assertEquals(Piece.KING_WHITE, piezaBoard.getPieza(Square.e2));
		assertNull(piezaBoard.getPieza(Square.e1));
		
		assertEquals(Square.e2, kingCacheBoard.getSquareKingWhiteCache());
		
		assertEquals(Color.BLACK, chessPositionState.getTurnoActual());
		
		assertEquals(Color.WHITE, colorBoard.getColor(Square.e2));
		assertTrue(colorBoard.isEmpty(Square.e1));

		// undos
		moveExecutor.undoMove(piezaBoard);
		moveExecutor.undoMove(kingCacheBoard);
		moveExecutor.undoMove(chessPositionState);
		moveExecutor.undoMove(colorBoard);

		
		// asserts undos
		assertEquals(Piece.KING_WHITE, piezaBoard.getPieza(Square.e1));
		assertEquals(Piece.KNIGHT_BLACK, piezaBoard.getPieza(Square.e2));
		
		assertEquals(Square.e1, kingCacheBoard.getSquareKingWhiteCache());
		
		assertEquals(Color.WHITE, chessPositionState.getTurnoActual());
		
		assertEquals(Color.WHITE, colorBoard.getColor(Square.e1));
		assertEquals(Color.BLACK, colorBoard.getColor(Square.e2));
	}
}
