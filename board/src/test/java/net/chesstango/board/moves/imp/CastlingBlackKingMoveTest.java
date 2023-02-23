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
public class CastlingBlackKingMoveTest {
	
	private PiecePlacement piecePlacement;
	
	private PositionState positionState;	
	
	private CastlingBlackKingMove moveExecutor;
	
	private KingCacheBoard kingCacheBoard;
	
	private ColorBoard colorBoard;
	
	@Mock
	private ChessPosition chessPosition;
	
	@Mock
	private MoveFilter filter;	

	@Before
	public void setUp() throws Exception {
		moveExecutor = new CastlingBlackKingMove();
		
		positionState = new PositionState();		
		positionState.setCurrentTurn(Color.BLACK);
		positionState.setCastlingBlackQueenAllowed(true);
		positionState.setCastlingBlackKingAllowed(true);
		positionState.setHalfMoveClock(3);
		positionState.setFullMoveClock(10);
		
		piecePlacement = new ArrayPiecePlacement();
		piecePlacement.setPieza(Square.e8, Piece.KING_BLACK);
		piecePlacement.setPieza(Square.h8, Piece.ROOK_BLACK);
		
		kingCacheBoard = new KingCacheBoard();
		colorBoard = new ColorBoardDebug();
		colorBoard.init(piecePlacement);
	}
	
	@Test
	public void testPosicionPiezaBoard() {
		moveExecutor.executeMove(piecePlacement);
		
		assertEquals(Piece.KING_BLACK, piecePlacement.getPiece(Square.g8));
		assertEquals(Piece.ROOK_BLACK, piecePlacement.getPiece(Square.f8));
		
		assertTrue(piecePlacement.isEmpty(Square.e8));
		assertTrue(piecePlacement.isEmpty(Square.h8));
		
		moveExecutor.undoMove(piecePlacement);
		
		assertEquals(Piece.KING_BLACK, piecePlacement.getPiece(Square.e8));
		assertEquals(Piece.ROOK_BLACK, piecePlacement.getPiece(Square.h8));
		
		assertTrue(piecePlacement.isEmpty(Square.g8));
		assertTrue(piecePlacement.isEmpty(Square.f8));
	}

	@Test
	public void testBoardState() {
		moveExecutor.executeMove(positionState);		

		assertNull(positionState.getEnPassantSquare());
		assertEquals(Color.WHITE, positionState.getCurrentTurn());
		assertFalse(positionState.isCastlingBlackQueenAllowed());
		assertFalse(positionState.isCastlingBlackKingAllowed());
		assertEquals(4, positionState.getHalfMoveClock());
		assertEquals(11, positionState.getFullMoveClock());
		
		moveExecutor.undoMove(positionState);
		
		assertNull(positionState.getEnPassantSquare());
		assertEquals(Color.BLACK, positionState.getCurrentTurn());
		assertTrue(positionState.isCastlingBlackQueenAllowed());
		assertTrue(positionState.isCastlingBlackKingAllowed());
		assertEquals(3, positionState.getHalfMoveClock());
		assertEquals(10, positionState.getFullMoveClock());
		
	}
	
	@Test
	public void testColorBoard() {
		// execute
		moveExecutor.executeMove(colorBoard);

		// asserts execute
		assertEquals(Color.BLACK, colorBoard.getColor(Square.g8));
		assertEquals(Color.BLACK, colorBoard.getColor(Square.f8));
		
		assertTrue(colorBoard.isEmpty(Square.e8));
		assertTrue(colorBoard.isEmpty(Square.h8));


		// undos
		moveExecutor.undoMove(colorBoard);

		// asserts undos
		assertEquals(Color.BLACK, colorBoard.getColor(Square.e8));
		assertEquals(Color.BLACK, colorBoard.getColor(Square.h8));
		
		assertTrue(colorBoard.isEmpty(Square.g8));
		assertTrue(colorBoard.isEmpty(Square.f8));		
	}	
	
	@Test
	public void testKingCacheBoard() {
		moveExecutor.executeMove(kingCacheBoard);

		assertEquals(Square.g8, kingCacheBoard.getSquareKingBlackCache());

		moveExecutor.undoMove(kingCacheBoard);

		assertEquals(Square.e8, kingCacheBoard.getSquareKingBlackCache());
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
