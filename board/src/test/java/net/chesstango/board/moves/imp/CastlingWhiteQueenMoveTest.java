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
import net.chesstango.board.movesgenerators.legal.MoveFilter;
import net.chesstango.board.position.ChessPosition;
import net.chesstango.board.position.PiecePlacement;
import net.chesstango.board.position.imp.ArrayPiecePlacement;
import net.chesstango.board.position.imp.ColorBoard;
import net.chesstango.board.position.imp.KingCacheBoard;
import net.chesstango.board.position.imp.PositionState;


/**
 * @author Mauricio Coria
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class CastlingWhiteQueenMoveTest {	
	
	private PiecePlacement piecePlacement;
	
	private PositionState positionState;
	
	private KingCacheBoard kingCacheBoard;
	
	private ColorBoard colorBoard;	
	
	private CastlingWhiteQueenMove moveExecutor;

	@Mock
	private ChessPosition chessPosition;
	
	@Mock
	private MoveFilter filter;	
	
	@Before
	public void setUp() throws Exception {
		moveExecutor = new CastlingWhiteQueenMove();
		
		positionState = new PositionState();		
		positionState.setCurrentTurn(Color.WHITE);
		positionState.setCastlingWhiteQueenAllowed(true);
		positionState.setCastlingWhiteKingAllowed(true);
		positionState.setHalfMoveClock(3);
		positionState.setFullMoveClock(10);
		
		piecePlacement = new ArrayPiecePlacement();
		piecePlacement.setPieza(Square.a1, Piece.ROOK_WHITE);
		piecePlacement.setPieza(Square.e1, Piece.KING_WHITE);
		
		kingCacheBoard = new KingCacheBoard();
		colorBoard = new ColorBoardDebug();
		colorBoard.init(piecePlacement);
	}
	
	@Test
	public void testPosicionPiezaBoard() {
		moveExecutor.executeMove(piecePlacement);
		
		assertEquals(Piece.KING_WHITE, piecePlacement.getPiece(Square.c1));
		assertEquals(Piece.ROOK_WHITE, piecePlacement.getPiece(Square.d1));
		
		assertTrue(piecePlacement.isEmpty(Square.a1));
		assertTrue(piecePlacement.isEmpty(Square.e1));
		
		moveExecutor.undoMove(piecePlacement);
		
		assertEquals(Piece.KING_WHITE, piecePlacement.getPiece(Square.e1));
		assertEquals(Piece.ROOK_WHITE, piecePlacement.getPiece(Square.a1));
		
		assertTrue(piecePlacement.isEmpty(Square.c1));
		assertTrue(piecePlacement.isEmpty(Square.d1));
	}

	@Test
	public void testBoardState() {
		moveExecutor.executeMove(positionState);		

		assertNull(positionState.getEnPassantSquare());
		assertEquals(Color.BLACK, positionState.getCurrentTurn());
		assertFalse(positionState.isCastlingWhiteQueenAllowed());
		assertFalse(positionState.isCastlingWhiteKingAllowed());
		assertEquals(4, positionState.getHalfMoveClock());
		assertEquals(10, positionState.getFullMoveClock());
		
		moveExecutor.undoMove(positionState);
		
		assertNull(positionState.getEnPassantSquare());
		assertEquals(Color.WHITE, positionState.getCurrentTurn());
		assertTrue(positionState.isCastlingWhiteQueenAllowed());
		assertTrue(positionState.isCastlingWhiteKingAllowed());
		assertEquals(3, positionState.getHalfMoveClock());
		assertEquals(10, positionState.getFullMoveClock());
		
	}	
	
	@Test
	public void testColorBoard() {
		// execute
		moveExecutor.executeMove(colorBoard);

		// asserts execute
		assertEquals(Color.WHITE, colorBoard.getColor(Square.c1));
		assertEquals(Color.WHITE, colorBoard.getColor(Square.d1));
		
		assertTrue(colorBoard.isEmpty(Square.a1));
		assertTrue(colorBoard.isEmpty(Square.e1));

		// undos
		moveExecutor.undoMove(colorBoard);

		// asserts undos
		assertEquals(Color.WHITE, colorBoard.getColor(Square.e1));
		assertEquals(Color.WHITE, colorBoard.getColor(Square.a1));
		
		assertTrue(colorBoard.isEmpty(Square.c1));
		assertTrue(colorBoard.isEmpty(Square.d1));		
	}	
	
	@Test
	public void testKingCacheBoard() {
		moveExecutor.executeMove(kingCacheBoard);

		assertEquals(Square.c1, kingCacheBoard.getSquareKingWhiteCache());

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

}
