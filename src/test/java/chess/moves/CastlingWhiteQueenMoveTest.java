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
import chess.position.ChessPosition;
import chess.position.ColorBoard;
import chess.position.KingCacheBoard;
import chess.position.PiecePlacement;
import chess.position.PositionState;
import chess.position.imp.ArrayPiecePlacement;
import chess.pseudomovesfilters.MoveFilter;


/**
 * @author Mauricio Coria
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class CastlingWhiteQueenMoveTest {	
	
	private PiecePlacement piezaBoard;
	
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
		positionState.setTurnoActual(Color.WHITE);
		positionState.setCastlingWhiteQueenPermitido(true);
		positionState.setCastlingWhiteKingPermitido(true);
		
		piezaBoard = new ArrayPiecePlacement();
		piezaBoard.setPieza(Square.a1, Piece.ROOK_WHITE);	
		piezaBoard.setPieza(Square.e1, Piece.KING_WHITE);	
		
		kingCacheBoard = new KingCacheBoard();
		colorBoard = new ColorBoard(piezaBoard);
	}
	
	@Test
	public void testPosicionPiezaBoard() {
		moveExecutor.executeMove(piezaBoard);
		
		assertEquals(Piece.KING_WHITE, piezaBoard.getPieza(Square.c1));		
		assertEquals(Piece.ROOK_WHITE, piezaBoard.getPieza(Square.d1));
		
		assertTrue(piezaBoard.isEmtpy(Square.a1));
		assertTrue(piezaBoard.isEmtpy(Square.e1));
		
		moveExecutor.undoMove(piezaBoard);
		
		assertEquals(Piece.KING_WHITE, piezaBoard.getPieza(Square.e1));
		assertEquals(Piece.ROOK_WHITE, piezaBoard.getPieza(Square.a1));
		
		assertTrue(piezaBoard.isEmtpy(Square.c1));
		assertTrue(piezaBoard.isEmtpy(Square.d1));		
	}

	@Test
	public void testBoardState() {
		moveExecutor.executeMove(positionState);		

		assertNull(positionState.getPawnPasanteSquare());
		assertEquals(Color.BLACK, positionState.getTurnoActual());		
		assertFalse(positionState.isCastlingWhiteQueenPermitido());
		assertFalse(positionState.isCastlingWhiteKingPermitido());
		
		moveExecutor.undoMove(positionState);
		
		assertNull(positionState.getPawnPasanteSquare());
		assertEquals(Color.WHITE, positionState.getTurnoActual());		
		assertTrue(positionState.isCastlingWhiteQueenPermitido());
		assertTrue(positionState.isCastlingWhiteKingPermitido());		
		
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
