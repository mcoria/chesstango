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

import chess.Color;
import chess.Piece;
import chess.Square;
import chess.debug.chess.BoardStateDebug;
import chess.debug.chess.ColorBoardDebug;
import chess.debug.chess.KingCacheBoardDebug;
import chess.moves.CastlingWhiteKingMove;
import chess.position.ChessPosition;
import chess.position.PiecePlacement;
import chess.position.imp.ArrayPiecePlacement;
import chess.pseudomovesfilters.MoveFilter;


/**
 * @author Mauricio Coria
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class CastlingWhiteKingMoveTest {
	
	private PiecePlacement piezaBoard;
	
	private BoardStateDebug boardState;	
	
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
		
		boardState = new BoardStateDebug();		
		boardState.setTurnoActual(Color.WHITE);
		boardState.setCastlingWhiteQueenPermitido(false);
		boardState.setCastlingWhiteKingPermitido(true);
		
		piezaBoard = new ArrayPiecePlacement();
		piezaBoard.setPieza(Square.e1, Piece.KING_WHITE);
		piezaBoard.setPieza(Square.h1, Piece.ROOK_WHITE);		
		
		kingCacheBoard = new KingCacheBoardDebug(piezaBoard);
		colorBoard = new ColorBoardDebug(piezaBoard);
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
		assertNull(boardState.getPawnPasanteSquare());
		assertEquals(Color.BLACK, boardState.getTurnoActual());		
		assertFalse(boardState.isCastlingWhiteQueenPermitido());
		assertFalse(boardState.isCastlingWhiteKingPermitido());
		boardState.validar();
		
		// undos
		moveExecutor.undoMove(boardState);
		
		// asserts undos		
		assertNull(boardState.getPawnPasanteSquare());
		assertEquals(Color.WHITE, boardState.getTurnoActual());		
		assertFalse(boardState.isCastlingWhiteQueenPermitido());
		assertTrue(boardState.isCastlingWhiteKingPermitido());
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