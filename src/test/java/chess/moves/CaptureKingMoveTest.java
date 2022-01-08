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
import chess.Pieza;
import chess.PosicionPieza;
import chess.Square;
import chess.layers.ColorBoard;
import chess.layers.KingCacheBoard;
import chess.layers.PosicionPiezaBoard;
import chess.layers.imp.ArrayPosicionPiezaBoard;
import chess.moves.CaptureKingMove;
import chess.pseudomovesfilters.MoveFilter;


/**
 * @author Mauricio Coria
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class CaptureKingMoveTest {

	private CaptureKingMove moveExecutor;
	
	private PosicionPiezaBoard piezaBoard;
	
	private BoardState boardState;

	private KingCacheBoard kingCacheBoard;
	
	private ColorBoard colorBoard;
	
	@Mock
	private Board board;
	
	@Mock
	private MoveFilter filter;

	@Before
	public void setUp() throws Exception {
		boardState = new BoardState();
		kingCacheBoard = new KingCacheBoard();
		moveExecutor = null;
		piezaBoard = null;
	}
	
	
	@Test
	public void testPosicionPiezaBoard() {
		piezaBoard = new ArrayPosicionPiezaBoard();
		piezaBoard.setPieza(Square.e1, Pieza.KING_WHITE);

		PosicionPieza origen = new PosicionPieza(Square.e1, Pieza.KING_WHITE);
		PosicionPieza destino = new PosicionPieza(Square.e2, Pieza.KNIGHT_BLACK);

		moveExecutor = new CaptureKingMove(origen, destino);

		// execute
		moveExecutor.executeMove(piezaBoard);

		// asserts execute
		assertEquals(Pieza.KING_WHITE, piezaBoard.getPieza(Square.e2));
		assertNull(piezaBoard.getPieza(Square.e1));

		// undos
		moveExecutor.undoMove(piezaBoard);
		
		// asserts undos
		assertEquals(Pieza.KING_WHITE, piezaBoard.getPieza(Square.e1));
		assertEquals(Pieza.KNIGHT_BLACK, piezaBoard.getPieza(Square.e2));
	}	
	
	@Test
	public void testBoardState() {
		boardState.setTurnoActual(Color.WHITE);

		PosicionPieza origen = new PosicionPieza(Square.e1, Pieza.KING_WHITE);
		PosicionPieza destino = new PosicionPieza(Square.e2, Pieza.KNIGHT_BLACK);

		moveExecutor = new CaptureKingMove(origen, destino);

		moveExecutor.executeMove(boardState);

		assertEquals(Color.BLACK, boardState.getTurnoActual());

		moveExecutor.undoMove(boardState);

		assertEquals(Color.WHITE, boardState.getTurnoActual());
	}		

	@Test
	public void testKingCacheBoard() {
		kingCacheBoard.setKingSquare(Color.WHITE, Square.d2);

		PosicionPieza origen = new PosicionPieza(Square.e1, Pieza.KING_WHITE);
		PosicionPieza destino = new PosicionPieza(Square.e2, Pieza.KNIGHT_BLACK);

		moveExecutor = new CaptureKingMove(origen, destino);

		moveExecutor.executeMove(kingCacheBoard);

		assertEquals(Square.e2, kingCacheBoard.getSquareKingWhiteCache());

		moveExecutor.undoMove(kingCacheBoard);

		assertEquals(Square.e1, kingCacheBoard.getSquareKingWhiteCache());
	}
	
	@Test
	public void testColorBoard() {
		piezaBoard = new ArrayPosicionPiezaBoard();
		piezaBoard.setPieza(Square.e1, Pieza.KING_WHITE);
		
		colorBoard = new ColorBoard(piezaBoard);

		PosicionPieza origen = new PosicionPieza(Square.e1, Pieza.KING_WHITE);
		PosicionPieza destino = new PosicionPieza(Square.e2, Pieza.KNIGHT_BLACK);

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
		PosicionPieza origen = new PosicionPieza(Square.e1, Pieza.KING_WHITE);
		PosicionPieza destino = new PosicionPieza(Square.e2, Pieza.KNIGHT_BLACK);

		moveExecutor = new CaptureKingMove(origen, destino);

		// execute
		moveExecutor.executeMove(board);

		// asserts execute
		verify(board).executeKingMove(moveExecutor);

		// undos
		moveExecutor.undoMove(board);

		
		// asserts undos
		verify(board).undoKingMove(moveExecutor);
	}
	
	
	@Test
	public void testFilter() {
		PosicionPieza origen = new PosicionPieza(Square.e1, Pieza.KING_WHITE);
		PosicionPieza destino = new PosicionPieza(Square.e2, Pieza.KNIGHT_BLACK);

		moveExecutor = new CaptureKingMove(origen, destino);

		// execute
		moveExecutor.filter(filter);

		// asserts execute
		verify(filter).filterKingMove(moveExecutor);
	}	
	
	@Test
	public void testExecuteUndo() {
		piezaBoard = new ArrayPosicionPiezaBoard();
		piezaBoard.setPieza(Square.e1, Pieza.KING_WHITE);
		
		colorBoard = new ColorBoard(piezaBoard);
		
		boardState.setTurnoActual(Color.WHITE);

		PosicionPieza origen = new PosicionPieza(Square.e1, Pieza.KING_WHITE);
		PosicionPieza destino = new PosicionPieza(Square.e2, Pieza.KNIGHT_BLACK);

		moveExecutor = new CaptureKingMove(origen, destino);

		// execute
		moveExecutor.executeMove(piezaBoard);
		moveExecutor.executeMove(kingCacheBoard);
		moveExecutor.executeMove(boardState);
		moveExecutor.executeMove(colorBoard);

		// asserts execute
		assertEquals(Pieza.KING_WHITE, piezaBoard.getPieza(Square.e2));
		assertNull(piezaBoard.getPieza(Square.e1));
		
		assertEquals(Square.e2, kingCacheBoard.getSquareKingWhiteCache());
		
		assertEquals(Color.BLACK, boardState.getTurnoActual());
		
		assertEquals(Color.WHITE, colorBoard.getColor(Square.e2));
		assertTrue(colorBoard.isEmpty(Square.e1));

		// undos
		moveExecutor.undoMove(piezaBoard);
		moveExecutor.undoMove(kingCacheBoard);
		moveExecutor.undoMove(boardState);
		moveExecutor.undoMove(colorBoard);

		
		// asserts undos
		assertEquals(Pieza.KING_WHITE, piezaBoard.getPieza(Square.e1));
		assertEquals(Pieza.KNIGHT_BLACK, piezaBoard.getPieza(Square.e2));
		
		assertEquals(Square.e1, kingCacheBoard.getSquareKingWhiteCache());
		
		assertEquals(Color.WHITE, boardState.getTurnoActual());
		
		assertEquals(Color.WHITE, colorBoard.getColor(Square.e1));
		assertEquals(Color.BLACK, colorBoard.getColor(Square.e2));
	}
}
