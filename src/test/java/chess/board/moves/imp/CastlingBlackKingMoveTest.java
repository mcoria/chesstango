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
import chess.board.moves.imp.CastlingBlackKingMove;
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
public class CastlingBlackKingMoveTest {
	
	private PiecePlacement piezaBoard;
	
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
		positionState.setTurnoActual(Color.BLACK);
		positionState.setCastlingBlackQueenAllowed(true);
		positionState.setCastlingBlackKingAllowed(true);
		
		piezaBoard = new ArrayPiecePlacement();
		piezaBoard.setPieza(Square.e8, Piece.KING_BLACK);
		piezaBoard.setPieza(Square.h8, Piece.ROOK_BLACK);		
		
		kingCacheBoard = new KingCacheBoard();
		colorBoard = new ColorBoardDebug();
		colorBoard.init(piezaBoard);
	}
	
	@Test
	public void testPosicionPiezaBoard() {
		moveExecutor.executeMove(piezaBoard);
		
		assertEquals(Piece.KING_BLACK, piezaBoard.getPieza(Square.g8));		
		assertEquals(Piece.ROOK_BLACK, piezaBoard.getPieza(Square.f8));
		
		assertTrue(piezaBoard.isEmtpy(Square.e8));
		assertTrue(piezaBoard.isEmtpy(Square.h8));
		
		moveExecutor.undoMove(piezaBoard);
		
		assertEquals(Piece.KING_BLACK, piezaBoard.getPieza(Square.e8));
		assertEquals(Piece.ROOK_BLACK, piezaBoard.getPieza(Square.h8));
		
		assertTrue(piezaBoard.isEmtpy(Square.g8));
		assertTrue(piezaBoard.isEmtpy(Square.f8));		
	}

	@Test
	public void testBoardState() {
		moveExecutor.executeMove(positionState);		

		assertNull(positionState.getEnPassantSquare());
		assertEquals(Color.WHITE, positionState.getTurnoActual());		
		assertFalse(positionState.isCastlingBlackQueenAllowed());
		assertFalse(positionState.isCastlingBlackKingAllowed());
		
		moveExecutor.undoMove(positionState);
		
		assertNull(positionState.getEnPassantSquare());
		assertEquals(Color.BLACK, positionState.getTurnoActual());		
		assertTrue(positionState.isCastlingBlackQueenAllowed());
		assertTrue(positionState.isCastlingBlackKingAllowed());		
		
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
