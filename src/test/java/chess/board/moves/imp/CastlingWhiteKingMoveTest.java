package chess.board.moves.imp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
import chess.board.Square;
import chess.board.debug.chess.ColorBoardDebug;
import chess.board.debug.chess.KingCacheBoardDebug;
import chess.board.debug.chess.PositionStateDebug;
import chess.board.legalmovesgenerators.MoveFilter;
import chess.board.moves.imp.CastlingWhiteKingMove;
import chess.board.position.ChessPosition;
import chess.board.position.PiecePlacement;
import chess.board.position.imp.ArrayPiecePlacement;


/**
 * @author Mauricio Coria
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class CastlingWhiteKingMoveTest {
	
	private PiecePlacement piezaBoard;
	
	private PositionStateDebug boardState;	
	
	private CastlingWhiteKingMove moveExecutor;
	
	private KingCacheBoardDebug kingCacheBoard;
	
	private ColorBoardDebug colorBoard;
	
	@Mock
	private ChessPosition chessPosition;
	
	@Mock
	private MoveFilter filter;	

	@Before
	public void setUp() throws Exception {
		moveExecutor = new CastlingWhiteKingMove();
		
		boardState = new PositionStateDebug();		
		boardState.setTurnoActual(Color.WHITE);
		boardState.setCastlingWhiteQueenAllowed(false);
		boardState.setCastlingWhiteKingAllowed(true);
		
		piezaBoard = new ArrayPiecePlacement();
		piezaBoard.setPieza(Square.e1, Piece.KING_WHITE);
		piezaBoard.setPieza(Square.h1, Piece.ROOK_WHITE);		
		
		kingCacheBoard = new KingCacheBoardDebug();
		kingCacheBoard.init(piezaBoard);
		
		colorBoard = new ColorBoardDebug();
		colorBoard.init(piezaBoard);
	}
	
	@Test
	public void testPosicionPiezaBoard() {
		moveExecutor.executeMove(piezaBoard);
		
		assertEquals(Piece.KING_WHITE, piezaBoard.getPieza(Square.g1));		
		assertEquals(Piece.ROOK_WHITE, piezaBoard.getPieza(Square.f1));
		
		assertTrue(piezaBoard.isEmtpy(Square.e1));
		assertTrue(piezaBoard.isEmtpy(Square.h1));
		
		moveExecutor.undoMove(piezaBoard);
		
		assertEquals(Piece.KING_WHITE, piezaBoard.getPieza(Square.e1));
		assertEquals(Piece.ROOK_WHITE, piezaBoard.getPieza(Square.h1));
		
		assertTrue(piezaBoard.isEmtpy(Square.g1));
		assertTrue(piezaBoard.isEmtpy(Square.f1));		
	}

	@Test
	public void testBoardState() {
		// execute		
		moveExecutor.executeMove(boardState);		

		// asserts execute
		assertNull(boardState.getEnPassantSquare());
		assertEquals(Color.BLACK, boardState.getTurnoActual());		
		assertFalse(boardState.isCastlingWhiteQueenAllowed());
		assertFalse(boardState.isCastlingWhiteKingAllowed());
		boardState.validar();
		
		// undos
		moveExecutor.undoMove(boardState);
		
		// asserts undos		
		assertNull(boardState.getEnPassantSquare());
		assertEquals(Color.WHITE, boardState.getTurnoActual());		
		assertFalse(boardState.isCastlingWhiteQueenAllowed());
		assertTrue(boardState.isCastlingWhiteKingAllowed());
		boardState.validar();
		
	}
	
	@Test
	public void testColorBoard() {
		// execute
		moveExecutor.executeMove(colorBoard);

		// asserts execute
		assertEquals(Color.WHITE, colorBoard.getColor(Square.g1));
		assertEquals(Color.WHITE, colorBoard.getColor(Square.f1));
		
		assertTrue(colorBoard.isEmpty(Square.e1));
		assertTrue(colorBoard.isEmpty(Square.h8));


		// undos
		moveExecutor.undoMove(colorBoard);

		// asserts undos
		assertEquals(Color.WHITE, colorBoard.getColor(Square.e1));
		assertEquals(Color.WHITE, colorBoard.getColor(Square.h1));
		
		assertTrue(colorBoard.isEmpty(Square.g1));
		assertTrue(colorBoard.isEmpty(Square.f1));		
	}	
	
	@Test
	public void testKingCacheBoard() {
		moveExecutor.executeMove(kingCacheBoard);

		assertEquals(Square.g1, kingCacheBoard.getSquareKingWhiteCache());

		moveExecutor.undoMove(kingCacheBoard);

		assertEquals(Square.e1, kingCacheBoard.getSquareKingWhiteCache());
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
	//TODO: Add test body
	public void testFilter() {
		/*
		// execute
		moveExecutor.filter(filter);

		// asserts execute
		verify(filter).filterKingMove(moveExecutor);
		*/
	}
	
	@Test
	public void testIntegrated() {
		// execute
		moveExecutor.executeMove(piezaBoard);
		moveExecutor.executeMove(boardState);
		moveExecutor.executeMove(colorBoard);
		moveExecutor.executeMove(kingCacheBoard);

		// asserts execute
		colorBoard.validar(piezaBoard);
		boardState.validar(piezaBoard);
		kingCacheBoard.validar(piezaBoard);
		
		// undos
		moveExecutor.undoMove(piezaBoard);
		moveExecutor.undoMove(boardState);
		moveExecutor.undoMove(colorBoard);
		moveExecutor.undoMove(kingCacheBoard);

		
		// asserts undos
		colorBoard.validar(piezaBoard);	
		boardState.validar(piezaBoard);
		kingCacheBoard.validar(piezaBoard);
	}	
}
