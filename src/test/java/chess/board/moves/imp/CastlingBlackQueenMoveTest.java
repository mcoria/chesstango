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
import chess.board.legalmovesgenerators.MoveFilter;
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
public class CastlingBlackQueenMoveTest {

	private PiecePlacement piezaBoard;
	
	private PositionState positionState;
	
	private KingCacheBoard kingCacheBoard;
	
	private ColorBoard colorBoard;	
	
	private CastlingBlackQueenMove moveExecutor;

	@Mock
	private ChessPosition chessPosition;
	
	@Mock
	private MoveFilter filter;	
	
	@Before
	public void setUp() throws Exception {
		moveExecutor = new CastlingBlackQueenMove();
		
		positionState = new PositionState();		
		positionState.setCurrentTurn(Color.WHITE);
		positionState.setCastlingBlackQueenAllowed(true);
		positionState.setCastlingBlackKingAllowed(true);
		
		piezaBoard = new ArrayPiecePlacement();
		piezaBoard.setPieza(Square.a8, Piece.ROOK_BLACK);	
		piezaBoard.setPieza(Square.e8, Piece.KING_BLACK);	
		
		kingCacheBoard = new KingCacheBoard();
		colorBoard = new ColorBoardDebug();
		colorBoard.init(piezaBoard);
	}
	
	@Test
	public void testPosicionPiezaBoard() {
		moveExecutor.executeMove(piezaBoard);
		
		assertEquals(Piece.KING_BLACK, piezaBoard.getPieza(Square.c8));		
		assertEquals(Piece.ROOK_BLACK, piezaBoard.getPieza(Square.d8));
		
		assertTrue(piezaBoard.isEmtpy(Square.a8));
		assertTrue(piezaBoard.isEmtpy(Square.e8));
		
		moveExecutor.undoMove(piezaBoard);
		
		assertEquals(Piece.KING_BLACK, piezaBoard.getPieza(Square.e8));
		assertEquals(Piece.ROOK_BLACK, piezaBoard.getPieza(Square.a8));
		
		assertTrue(piezaBoard.isEmtpy(Square.c8));
		assertTrue(piezaBoard.isEmtpy(Square.d8));		
	}

	@Test
	public void testBoardState() {
		moveExecutor.executeMove(positionState);		

		assertNull(positionState.getEnPassantSquare());
		assertEquals(Color.BLACK, positionState.getCurrentTurn());
		assertFalse(positionState.isCastlingBlackQueenAllowed());
		assertFalse(positionState.isCastlingBlackKingAllowed());
		
		moveExecutor.undoMove(positionState);
		
		assertNull(positionState.getEnPassantSquare());
		assertEquals(Color.WHITE, positionState.getCurrentTurn());
		assertTrue(positionState.isCastlingBlackQueenAllowed());
		assertTrue(positionState.isCastlingBlackKingAllowed());		
		
	}	
	
	@Test
	public void testColorBoard() {
		// execute
		moveExecutor.executeMove(colorBoard);

		// asserts execute
		assertEquals(Color.BLACK, colorBoard.getColor(Square.c8));
		assertEquals(Color.BLACK, colorBoard.getColor(Square.d8));
		
		assertTrue(colorBoard.isEmpty(Square.a8));
		assertTrue(colorBoard.isEmpty(Square.e8));

		// undos
		moveExecutor.undoMove(colorBoard);

		// asserts undos
		assertEquals(Color.BLACK, colorBoard.getColor(Square.e8));
		assertEquals(Color.BLACK, colorBoard.getColor(Square.a8));
		
		assertTrue(colorBoard.isEmpty(Square.c8));
		assertTrue(colorBoard.isEmpty(Square.d8));		
	}	
	
	@Test
	public void testKingCacheBoard() {
		moveExecutor.executeMove(kingCacheBoard);

		assertEquals(Square.c8, kingCacheBoard.getSquareKingBlackCache());

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
