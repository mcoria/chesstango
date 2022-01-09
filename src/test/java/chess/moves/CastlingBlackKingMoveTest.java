package chess.moves;

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

import chess.ChessPosition;
import chess.Color;
import chess.Piece;
import chess.Square;
import chess.layers.ChessPositionState;
import chess.layers.ColorBoard;
import chess.layers.KingCacheBoard;
import chess.layers.PiecePlacement;
import chess.layers.imp.ArrayPiecePlacement;
import chess.pseudomovesfilters.MoveFilter;


/**
 * @author Mauricio Coria
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class CastlingBlackKingMoveTest {
	
	private PiecePlacement piezaBoard;
	
	private ChessPositionState chessPositionState;	
	
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
		
		chessPositionState = new ChessPositionState();		
		chessPositionState.setTurnoActual(Color.BLACK);
		chessPositionState.setCastlingBlackQueenPermitido(true);
		chessPositionState.setCastlingBlackKingPermitido(true);
		
		piezaBoard = new ArrayPiecePlacement();
		piezaBoard.setPieza(Square.e8, Piece.KING_BLACK);
		piezaBoard.setPieza(Square.h8, Piece.ROOK_BLACK);		
		
		kingCacheBoard = new KingCacheBoard();
		colorBoard = new ColorBoard(piezaBoard);
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
		moveExecutor.executeMove(chessPositionState);		

		assertNull(chessPositionState.getPawnPasanteSquare());
		assertEquals(Color.WHITE, chessPositionState.getTurnoActual());		
		assertFalse(chessPositionState.isCastlingBlackQueenPermitido());
		assertFalse(chessPositionState.isCastlingBlackKingPermitido());
		
		moveExecutor.undoMove(chessPositionState);
		
		assertNull(chessPositionState.getPawnPasanteSquare());
		assertEquals(Color.BLACK, chessPositionState.getTurnoActual());		
		assertTrue(chessPositionState.isCastlingBlackQueenPermitido());
		assertTrue(chessPositionState.isCastlingBlackKingPermitido());		
		
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
		verify(chessPosition).executeKingMove(moveExecutor);

		// undos
		moveExecutor.undoMove(chessPosition);

		
		// asserts undos
		verify(chessPosition).undoKingMove(moveExecutor);
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
