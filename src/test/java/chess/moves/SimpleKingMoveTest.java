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
import chess.Piece;
import chess.PiecePositioned;
import chess.Square;
import chess.debug.chess.ColorBoardDebug;
import chess.debug.chess.KingCacheBoardDebug;
import chess.layers.PosicionPiezaBoard;
import chess.layers.imp.ArrayPosicionPiezaBoard;
import chess.moves.SimpleKingMove;
import chess.pseudomovesfilters.MoveFilter;


/**
 * @author Mauricio Coria
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class SimpleKingMoveTest {

	private SimpleKingMove moveExecutor;
	
	private PosicionPiezaBoard piezaBoard;
	
	private BoardState boardState;

	private KingCacheBoardDebug kingCacheBoard;
	
	private ColorBoardDebug colorBoard;
	
	@Mock
	private Board board;
	
	@Mock
	private MoveFilter filter;

	@Before
	public void setUp() throws Exception {
		piezaBoard = new ArrayPosicionPiezaBoard();
		piezaBoard.setPieza(Square.e1, Piece.KING_WHITE);
		
		colorBoard = new ColorBoardDebug(piezaBoard);
		kingCacheBoard = new KingCacheBoardDebug(piezaBoard);

		PiecePositioned origen = new PiecePositioned(Square.e1, Piece.KING_WHITE);
		PiecePositioned destino = new PiecePositioned(Square.e2, null);

		moveExecutor = new SimpleKingMove(origen, destino);
		
		boardState = new BoardState();
		boardState.setTurnoActual(Color.WHITE);
		boardState.setCastlingWhiteKingPermitido(true);
		boardState.setCastlingWhiteQueenPermitido(true);
	}
	
	
	@Test
	public void testPosicionPiezaBoard() {
		// execute
		moveExecutor.executeMove(piezaBoard);

		// asserts execute
		assertEquals(Piece.KING_WHITE, piezaBoard.getPieza(Square.e2));
		assertNull(piezaBoard.getPieza(Square.e1));

		// undos
		moveExecutor.undoMove(piezaBoard);
		
		// asserts undos
		assertEquals(Piece.KING_WHITE, piezaBoard.getPieza(Square.e1));
		assertTrue(piezaBoard.isEmtpy(Square.e2));
	}	
	
	@Test
	public void testBoardState() {
		moveExecutor.executeMove(boardState);

		assertEquals(Color.BLACK, boardState.getTurnoActual());

		moveExecutor.undoMove(boardState);

		assertEquals(Color.WHITE, boardState.getTurnoActual());
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
		assertTrue(colorBoard.isEmpty(Square.e2));
	}
	
	@Test
	public void testBoard() {
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
		// execute
		moveExecutor.filter(filter);

		// asserts execute
		verify(filter).filterKingMove(moveExecutor);
	}	
	
	@Test
	public void testIntegrated() {
		// execute
		moveExecutor.executeMove(piezaBoard);
		moveExecutor.executeMove(kingCacheBoard);
		moveExecutor.executeMove(boardState);
		moveExecutor.executeMove(colorBoard);

		// asserts execute
		assertEquals(Piece.KING_WHITE, piezaBoard.getPieza(Square.e2));
		assertNull(piezaBoard.getPieza(Square.e1));
		
		assertEquals(Square.e2, kingCacheBoard.getSquareKingWhiteCache());
		
		assertEquals(Color.BLACK, boardState.getTurnoActual());
		
		assertEquals(Color.WHITE, colorBoard.getColor(Square.e2));
		assertTrue(colorBoard.isEmpty(Square.e1));

		colorBoard.validar(piezaBoard);
		kingCacheBoard.validar(piezaBoard);
		
		// undos
		moveExecutor.undoMove(piezaBoard);
		moveExecutor.undoMove(kingCacheBoard);
		moveExecutor.undoMove(boardState);
		moveExecutor.undoMove(colorBoard);

		
		// asserts undos
		assertEquals(Piece.KING_WHITE, piezaBoard.getPieza(Square.e1));
		assertTrue(piezaBoard.isEmtpy(Square.e2));
		
		assertEquals(Square.e1, kingCacheBoard.getSquareKingWhiteCache());
		
		assertEquals(Color.WHITE, boardState.getTurnoActual());
		
		assertEquals(Color.WHITE, colorBoard.getColor(Square.e1));
		assertTrue(colorBoard.isEmpty(Square.e2));
		
		colorBoard.validar(piezaBoard);
		kingCacheBoard.validar(piezaBoard);		
	}
}
