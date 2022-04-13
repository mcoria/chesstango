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
import chess.board.debug.chess.ColorBoardDebug;
import chess.board.debug.chess.KingCacheBoardDebug;
import chess.board.legalmovesgenerators.MoveFilter;
import chess.board.moves.imp.SimpleKingMove;
import chess.board.position.ChessPosition;
import chess.board.position.PiecePlacement;
import chess.board.position.imp.ArrayPiecePlacement;
import chess.board.position.imp.PositionState;


/**
 * @author Mauricio Coria
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class SimpleKingMoveTest {

	private SimpleKingMove moveExecutor;
	
	private PiecePlacement piezaBoard;
	
	private PositionState positionState;

	private KingCacheBoardDebug kingCacheBoard;
	
	private ColorBoardDebug colorBoard;
	
	@Mock
	private ChessPosition chessPosition;
	
	@Mock
	private MoveFilter filter;

	@Before
	public void setUp() throws Exception {
		piezaBoard = new ArrayPiecePlacement();
		piezaBoard.setPieza(Square.e1, Piece.KING_WHITE);
		
		colorBoard = new ColorBoardDebug();
		colorBoard.init(piezaBoard);

		kingCacheBoard = new KingCacheBoardDebug();
		kingCacheBoard.init(piezaBoard);

		PiecePositioned origen = PiecePositioned.getPiecePositioned(Square.e1, Piece.KING_WHITE);
		PiecePositioned destino = PiecePositioned.getPiecePositioned(Square.e2, null);

		moveExecutor = new SimpleKingMove(origen, destino);
		
		positionState = new PositionState();
		positionState.setTurnoActual(Color.WHITE);
		positionState.setCastlingWhiteKingAllowed(true);
		positionState.setCastlingWhiteQueenAllowed(true);
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
		moveExecutor.executeMove(positionState);

		assertEquals(Color.BLACK, positionState.getTurnoActual());

		moveExecutor.undoMove(positionState);

		assertEquals(Color.WHITE, positionState.getTurnoActual());
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

		colorBoard.validar(piezaBoard);
		kingCacheBoard.validar(piezaBoard);
		
		// undos
		moveExecutor.undoMove(piezaBoard);
		moveExecutor.undoMove(kingCacheBoard);
		moveExecutor.undoMove(positionState);
		moveExecutor.undoMove(colorBoard);

		
		// asserts undos
		assertEquals(Piece.KING_WHITE, piezaBoard.getPieza(Square.e1));
		assertTrue(piezaBoard.isEmtpy(Square.e2));
		
		assertEquals(Square.e1, kingCacheBoard.getSquareKingWhiteCache());
		
		assertEquals(Color.WHITE, positionState.getTurnoActual());
		
		assertEquals(Color.WHITE, colorBoard.getColor(Square.e1));
		assertTrue(colorBoard.isEmpty(Square.e2));
		
		colorBoard.validar(piezaBoard);
		kingCacheBoard.validar(piezaBoard);		
	}
}