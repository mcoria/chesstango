package net.chesstango.board.moves.imp;

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

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.Square;
import net.chesstango.board.debug.chess.ColorBoardDebug;
import net.chesstango.board.debug.chess.KingCacheBoardDebug;
import net.chesstango.board.debug.chess.PositionStateDebug;
import net.chesstango.board.movesgenerators.legal.MoveFilter;
import net.chesstango.board.position.ChessPosition;
import net.chesstango.board.position.PiecePlacement;
import net.chesstango.board.position.imp.ArrayPiecePlacement;


/**
 * @author Mauricio Coria
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class CastlingWhiteKingMoveTest {
	
	private PiecePlacement piecePlacement;
	
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
		boardState.setCurrentTurn(Color.WHITE);
		boardState.setCastlingWhiteQueenAllowed(false);
		boardState.setCastlingWhiteKingAllowed(true);
		boardState.setHalfMoveClock(3);
		boardState.setFullMoveClock(10);
		
		piecePlacement = new ArrayPiecePlacement();
		piecePlacement.setPieza(Square.e1, Piece.KING_WHITE);
		piecePlacement.setPieza(Square.h1, Piece.ROOK_WHITE);
		
		kingCacheBoard = new KingCacheBoardDebug();
		kingCacheBoard.init(piecePlacement);
		
		colorBoard = new ColorBoardDebug();
		colorBoard.init(piecePlacement);
	}
	
	@Test
	public void testPosicionPiezaBoard() {
		moveExecutor.executeMove(piecePlacement);
		
		assertEquals(Piece.KING_WHITE, piecePlacement.getPiece(Square.g1));
		assertEquals(Piece.ROOK_WHITE, piecePlacement.getPiece(Square.f1));
		
		assertTrue(piecePlacement.isEmpty(Square.e1));
		assertTrue(piecePlacement.isEmpty(Square.h1));
		
		moveExecutor.undoMove(piecePlacement);
		
		assertEquals(Piece.KING_WHITE, piecePlacement.getPiece(Square.e1));
		assertEquals(Piece.ROOK_WHITE, piecePlacement.getPiece(Square.h1));
		
		assertTrue(piecePlacement.isEmpty(Square.g1));
		assertTrue(piecePlacement.isEmpty(Square.f1));
	}

	@Test
	public void testBoardState() {
		// execute		
		moveExecutor.executeMove(boardState);		

		// asserts execute
		assertNull(boardState.getEnPassantSquare());
		assertEquals(Color.BLACK, boardState.getCurrentTurn());
		assertFalse(boardState.isCastlingWhiteQueenAllowed());
		assertFalse(boardState.isCastlingWhiteKingAllowed());
		assertEquals(4, boardState.getHalfMoveClock());
		assertEquals(10, boardState.getFullMoveClock());
		boardState.validar();
		
		// undos
		moveExecutor.undoMove(boardState);
		
		// asserts undos		
		assertNull(boardState.getEnPassantSquare());
		assertEquals(Color.WHITE, boardState.getCurrentTurn());
		assertFalse(boardState.isCastlingWhiteQueenAllowed());
		assertTrue(boardState.isCastlingWhiteKingAllowed());
		assertEquals(3, boardState.getHalfMoveClock());
		assertEquals(10, boardState.getFullMoveClock());
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
		moveExecutor.executeMove(piecePlacement);
		moveExecutor.executeMove(boardState);
		moveExecutor.executeMove(colorBoard);
		moveExecutor.executeMove(kingCacheBoard);

		// asserts execute
		colorBoard.validar(piecePlacement);
		boardState.validar(piecePlacement);
		kingCacheBoard.validar(piecePlacement);
		
		// undos
		moveExecutor.undoMove(piecePlacement);
		moveExecutor.undoMove(boardState);
		moveExecutor.undoMove(colorBoard);
		moveExecutor.undoMove(kingCacheBoard);

		
		// asserts undos
		colorBoard.validar(piecePlacement);
		boardState.validar(piecePlacement);
		kingCacheBoard.validar(piecePlacement);
	}	
}
