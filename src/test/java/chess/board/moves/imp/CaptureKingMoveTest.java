package chess.board.moves.imp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import chess.board.Color;
import chess.board.Piece;
import chess.board.PiecePositioned;
import chess.board.Square;
import chess.board.legalmovesgenerators.MoveFilter;
import chess.board.moves.imp.CaptureKingMove;
import chess.board.position.ChessPosition;
import chess.board.position.PiecePlacement;
import chess.board.position.imp.ArrayPiecePlacement;
import chess.board.position.imp.ColorBoard;
import chess.board.position.imp.KingCacheBoard;
import chess.board.position.imp.PositionState;


/**
 * @author Mauricio Coria
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class CaptureKingMoveTest {

	private CaptureKingMove moveExecutor;
	
	private PiecePlacement piezaBoard;
	
	private PositionState positionState;

	private KingCacheBoard kingCacheBoard;
	
	private ColorBoard colorBoard;
	
	@Mock
	private ChessPosition chessPosition;
	
	@Mock
	private MoveFilter filter;

	@Before
	public void setUp() throws Exception {
		positionState = new PositionState();
		kingCacheBoard = new KingCacheBoard();
		moveExecutor = null;
		piezaBoard = null;
	}
	
	
	@Test
	public void testPosicionPiezaBoard() {
		piezaBoard = new ArrayPiecePlacement();
		piezaBoard.setPieza(Square.e1, Piece.KING_WHITE);

		PiecePositioned origen = PiecePositioned.getPiecePositioned(Square.e1, Piece.KING_WHITE);
		PiecePositioned destino = PiecePositioned.getPiecePositioned(Square.e2, Piece.KNIGHT_BLACK);

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
		positionState.setTurnoActual(Color.WHITE);

		PiecePositioned origen = PiecePositioned.getPiecePositioned(Square.e1, Piece.KING_WHITE);
		PiecePositioned destino = PiecePositioned.getPiecePositioned(Square.e2, Piece.KNIGHT_BLACK);

		moveExecutor = new CaptureKingMove(origen, destino);

		moveExecutor.executeMove(positionState);

		assertEquals(Color.BLACK, positionState.getTurnoActual());

		moveExecutor.undoMove(positionState);

		assertEquals(Color.WHITE, positionState.getTurnoActual());
	}		

	@Test
	public void testKingCacheBoard() {
		kingCacheBoard.setKingSquare(Color.WHITE, Square.d2);

		PiecePositioned origen = PiecePositioned.getPiecePositioned(Square.e1, Piece.KING_WHITE);
		PiecePositioned destino = PiecePositioned.getPiecePositioned(Square.e2, Piece.KNIGHT_BLACK);

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
		
		colorBoard = new ColorBoard();
		colorBoard.init(piezaBoard);

		PiecePositioned origen = PiecePositioned.getPiecePositioned(Square.e1, Piece.KING_WHITE);
		PiecePositioned destino = PiecePositioned.getPiecePositioned(Square.e2, Piece.KNIGHT_BLACK);

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
		PiecePositioned origen = PiecePositioned.getPiecePositioned(Square.e1, Piece.KING_WHITE);
		PiecePositioned destino = PiecePositioned.getPiecePositioned(Square.e2, Piece.KNIGHT_BLACK);

		moveExecutor = new CaptureKingMove(origen, destino);

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

		moveExecutor = new CaptureKingMove(origen, destino);

		// execute
		moveExecutor.filter(filter);

		// asserts execute
		verify(filter).filterMove(moveExecutor);
	}	
	
	@Test
	public void testExecuteUndo() {
		piezaBoard = new ArrayPiecePlacement();
		piezaBoard.setPieza(Square.e1, Piece.KING_WHITE);
		
		colorBoard = new ColorBoard();
		colorBoard.init(piezaBoard);
		
		positionState.setTurnoActual(Color.WHITE);

		PiecePositioned origen = PiecePositioned.getPiecePositioned(Square.e1, Piece.KING_WHITE);
		PiecePositioned destino = PiecePositioned.getPiecePositioned(Square.e2, Piece.KNIGHT_BLACK);

		moveExecutor = new CaptureKingMove(origen, destino);

		// execute
		moveExecutor.executeMove(piezaBoard);
		moveExecutor.executeMove(kingCacheBoard);
		moveExecutor.executeMove(positionState);
		moveExecutor.executeMove(colorBoard);

		// asserts execute
		assertEquals(Piece.KING_WHITE, piezaBoard.getPieza(Square.e2));
		assertNull(piezaBoard.getPieza(Square.e1));
		
		assertEquals(Square.e2, kingCacheBoard.getSquareKingWhiteCache());
		
		assertEquals(Color.BLACK, positionState.getTurnoActual());
		
		assertEquals(Color.WHITE, colorBoard.getColor(Square.e2));
		assertTrue(colorBoard.isEmpty(Square.e1));

		// undos
		moveExecutor.undoMove(piezaBoard);
		moveExecutor.undoMove(kingCacheBoard);
		moveExecutor.undoMove(positionState);
		moveExecutor.undoMove(colorBoard);

		
		// asserts undos
		assertEquals(Piece.KING_WHITE, piezaBoard.getPieza(Square.e1));
		assertEquals(Piece.KNIGHT_BLACK, piezaBoard.getPieza(Square.e2));
		
		assertEquals(Square.e1, kingCacheBoard.getSquareKingWhiteCache());
		
		assertEquals(Color.WHITE, positionState.getTurnoActual());
		
		assertEquals(Color.WHITE, colorBoard.getColor(Square.e1));
		assertEquals(Color.BLACK, colorBoard.getColor(Square.e2));
	}
}
