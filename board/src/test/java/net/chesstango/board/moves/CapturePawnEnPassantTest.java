package net.chesstango.board.moves;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.debug.chess.ColorBoardDebug;
import net.chesstango.board.debug.chess.MoveCacheBoardDebug;
import net.chesstango.board.debug.chess.PositionStateDebug;
import net.chesstango.board.factory.SingletonMoveFactories;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.movesgenerators.legal.MoveFilter;
import net.chesstango.board.movesgenerators.pseudo.MoveGeneratorResult;
import net.chesstango.board.position.ChessPosition;
import net.chesstango.board.position.PiecePlacement;
import net.chesstango.board.position.imp.ArrayPiecePlacement;
import net.chesstango.board.position.imp.ZobristHash;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;


/**
 * @author Mauricio Coria
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class CapturePawnEnPassantTest {

	private PiecePlacement piecePlacement;
	
	private PositionStateDebug positionState;
	
	private Move moveExecutor;
	
	private ColorBoardDebug colorBoard;

	private MoveCacheBoardDebug moveCacheBoard;

	private ZobristHash zobristHash;
	
	@Mock
	private ChessPosition chessPosition;
	
	@Mock
	private MoveFilter filter;	

	@Before
	public void setUp() throws Exception {
		positionState = new PositionStateDebug();
		positionState.setCurrentTurn(Color.WHITE);
		positionState.setEnPassantSquare(Square.a6);
		positionState.setHalfMoveClock(2);
		positionState.setFullMoveClock(5);
		
		piecePlacement = new ArrayPiecePlacement();
		piecePlacement.setPieza(Square.b5, Piece.PAWN_WHITE);
		piecePlacement.setPieza(Square.a5, Piece.PAWN_BLACK);
		
		colorBoard = new ColorBoardDebug();
		colorBoard.init(piecePlacement);
		
		PiecePositioned pawnWhite = piecePlacement.getPosicion(Square.b5);
		PiecePositioned pawnBlack = piecePlacement.getPosicion(Square.a5);
		PiecePositioned pawnPasanteSquare = piecePlacement.getPosicion(Square.a6);

		moveCacheBoard = new MoveCacheBoardDebug();
		moveCacheBoard.setPseudoMoves(Square.b5, new MoveGeneratorResult(pawnWhite));
		moveCacheBoard.setPseudoMoves(Square.a5, new MoveGeneratorResult(pawnBlack));

		zobristHash = new ZobristHash();
		zobristHash.init(piecePlacement, positionState);
		
		moveExecutor = SingletonMoveFactories.getDefaultMoveFactoryWhite().createCaptureEnPassant (pawnWhite, pawnPasanteSquare, Cardinal.NorteOeste, pawnBlack);
	}

	@Test
	public void testEquals() {
		assertEquals(SingletonMoveFactories.getDefaultMoveFactoryWhite().createCaptureEnPassant(piecePlacement.getPosicion(Square.b5), piecePlacement.getPosicion(Square.a6), Cardinal.NorteOeste, piecePlacement.getPosicion(Square.a5)), moveExecutor);
	}

	@Test
	public void testGetDirection() {
		assertEquals(Cardinal.calculateSquaresDirection(moveExecutor.getFrom().getSquare(), moveExecutor.getTo().getSquare()), moveExecutor.getMoveDirection());
	}
	@Test
	public void testPosicionPiezaBoard() {		
		// execute
		moveExecutor.executeMove(piecePlacement);
		
		// asserts execute
		assertTrue(piecePlacement.isEmpty(Square.a5));
		assertTrue(piecePlacement.isEmpty(Square.b5));
		assertEquals(Piece.PAWN_WHITE, piecePlacement.getPiece(Square.a6));
		
		// undos
		moveExecutor.undoMove(piecePlacement);
		
		// asserts undos
		assertTrue(piecePlacement.isEmpty(Square.a6));
		assertEquals(Piece.PAWN_WHITE, piecePlacement.getPiece(Square.b5));
		assertEquals(Piece.PAWN_BLACK, piecePlacement.getPiece(Square.a5));
		
	}
	
	@Test
	public void testMoveState() {
		// execute
		moveExecutor.executeMove(positionState);		
		
		// asserts execute
		assertNull(positionState.getEnPassantSquare());
		assertEquals(Color.BLACK, positionState.getCurrentTurn());
		assertEquals(0, positionState.getHalfMoveClock());
		assertEquals(5, positionState.getFullMoveClock());
		
		// undos
		moveExecutor.undoMove(positionState);	
		
		// asserts undos
		assertEquals(Square.a6, positionState.getEnPassantSquare());
		assertEquals(Color.WHITE, positionState.getCurrentTurn());
		assertEquals(2, positionState.getHalfMoveClock());
		assertEquals(5, positionState.getFullMoveClock());
		
	}
	
	@Test
	public void testColorBoard() {
		// execute
		moveExecutor.executeMove(colorBoard);
		
		// asserts execute
		assertEquals(Color.WHITE, colorBoard.getColor(Square.a6));
		assertTrue(colorBoard.isEmpty(Square.a5));
		assertTrue(colorBoard.isEmpty(Square.b5));

		// undos
		moveExecutor.undoMove(colorBoard);
		
		// asserts undos
		assertTrue(colorBoard.isEmpty(Square.a6));
		assertEquals(Color.BLACK, colorBoard.getColor(Square.a5));
		assertEquals(Color.WHITE, colorBoard.getColor(Square.b5));
		
	}


	@Test
	public void testCacheBoard() {
		// execute
		moveExecutor.executeMove(moveCacheBoard);

		// asserts execute
		assertNull(moveCacheBoard.getPseudoMovesResult(Square.a5));
		assertNull(moveCacheBoard.getPseudoMovesResult(Square.b5));

		// undos
		moveExecutor.undoMove(moveCacheBoard);

		// asserts undos
		assertNotNull(moveCacheBoard.getPseudoMovesResult(Square.a5));
		assertNotNull(moveCacheBoard.getPseudoMovesResult(Square.b5));
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
		moveExecutor.executeMove(piecePlacement);
		moveExecutor.executeMove(positionState);
		moveExecutor.executeMove(colorBoard);
		moveExecutor.executeMove(moveCacheBoard);

		// asserts execute
		assertTrue(piecePlacement.isEmpty(Square.a5));
		assertTrue(piecePlacement.isEmpty(Square.b5));
		assertEquals(Piece.PAWN_WHITE, piecePlacement.getPiece(Square.a6));
		
		assertNull(positionState.getEnPassantSquare());
		assertEquals(Color.BLACK, positionState.getCurrentTurn());
		
		assertEquals(Color.WHITE, colorBoard.getColor(Square.a6));
		assertTrue(colorBoard.isEmpty(Square.a5));
		assertTrue(colorBoard.isEmpty(Square.b5));

		assertNull(moveCacheBoard.getPseudoMovesResult(Square.a5));

		colorBoard.validar(piecePlacement);
		positionState.validar(piecePlacement);
		moveCacheBoard.validar(piecePlacement);
		
		// undos
		moveExecutor.undoMove(piecePlacement);
		moveExecutor.undoMove(positionState);
		moveExecutor.undoMove(colorBoard);
		moveExecutor.undoMove(moveCacheBoard);

		
		// asserts undos
		assertTrue(piecePlacement.isEmpty(Square.a6));
		assertEquals(Piece.PAWN_WHITE, piecePlacement.getPiece(Square.b5));
		assertEquals(Piece.PAWN_BLACK, piecePlacement.getPiece(Square.a5));
		
		assertEquals(Square.a6, positionState.getEnPassantSquare());
		assertEquals(Color.WHITE, positionState.getCurrentTurn());
		
		assertTrue(colorBoard.isEmpty(Square.a6));
		assertEquals(Color.BLACK, colorBoard.getColor(Square.a5));
		assertEquals(Color.WHITE, colorBoard.getColor(Square.b5));

		assertNotNull(moveCacheBoard.getPseudoMovesResult(Square.a5));
		assertNotNull(moveCacheBoard.getPseudoMovesResult(Square.b5));

		colorBoard.validar(piecePlacement);
		positionState.validar(piecePlacement);
		moveCacheBoard.validar(piecePlacement);
	}	
}
