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
import net.chesstango.board.position.PositionStateReader;
import net.chesstango.board.position.imp.ArrayPiecePlacement;
import net.chesstango.board.position.imp.ZobristHash;
import net.chesstango.board.representations.polyglot.PolyglotEncoder;
import org.junit.Assert;
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
public class SimplePawnMoveTest {

	private Move moveExecutor;
	private PiecePlacement piecePlacement;
	
	private PositionStateDebug positionState;
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
		positionState.setHalfMoveClock(2);
		positionState.setFullMoveClock(5);
		
		piecePlacement = new ArrayPiecePlacement();
		piecePlacement.setPieza(Square.e2, Piece.PAWN_WHITE);
		
		colorBoard = new ColorBoardDebug();
		colorBoard.init(piecePlacement);

		PiecePositioned origen = piecePlacement.getPosicion(Square.e2);
		PiecePositioned destino = piecePlacement.getPosicion(Square.e3);

		moveCacheBoard = new MoveCacheBoardDebug();
		moveCacheBoard.setPseudoMoves(Square.e2, new MoveGeneratorResult(origen));

		zobristHash = new ZobristHash();
		zobristHash.init(piecePlacement, positionState);

		moveExecutor =  SingletonMoveFactories.getDefaultMoveFactoryWhite().createSimplePawnMove(origen, destino);
	}

	@Test
	public void testEquals() {
		assertEquals(SingletonMoveFactories.getDefaultMoveFactoryWhite().createSimplePawnMove(piecePlacement.getPosicion(Square.e2), piecePlacement.getPosicion(Square.e3)), moveExecutor);
	}

	@Test
	public void testGetDirection() {
		assertEquals(Cardinal.calculateSquaresDirection(moveExecutor.getFrom().getSquare(), moveExecutor.getTo().getSquare()), moveExecutor.getMoveDirection());
	}

	@Test
	public void testZobristHash() {
		PositionStateReader oldPositionState = positionState.getCurrentState();
		moveExecutor.executeMove(positionState);
		moveExecutor.executeMove(zobristHash, oldPositionState, positionState);

		Assert.assertEquals(PolyglotEncoder.getKey("8/8/8/8/8/4P3/8/8 b - - 0 1").longValue(), zobristHash.getZobristHash());
	}

	@Test
	public void testZobristHashUndo() {
		long initialHash = zobristHash.getZobristHash();

		PositionStateReader oldPositionState = positionState.getCurrentState();
		moveExecutor.executeMove(positionState);
		moveExecutor.executeMove(zobristHash, oldPositionState, positionState);

		oldPositionState = positionState.getCurrentState();
		moveExecutor.undoMove(positionState);
		moveExecutor.undoMove(zobristHash, oldPositionState, positionState);

		Assert.assertEquals(initialHash, zobristHash.getZobristHash());
	}
	
	@Test
	public void testPosicionPiezaBoard() {
		// execute
		moveExecutor.executeMove(piecePlacement);
		
		// asserts execute		
		assertEquals(Piece.PAWN_WHITE, piecePlacement.getPiece(Square.e3));
		assertTrue(piecePlacement.isEmpty(Square.e2));
		
		// undos		
		moveExecutor.undoMove(piecePlacement);
		
		// asserts undos		
		assertEquals(Piece.PAWN_WHITE, piecePlacement.getPiece(Square.e2));
		assertTrue(piecePlacement.isEmpty(Square.e3));
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
		assertEquals(Color.WHITE, positionState.getCurrentTurn());
		assertEquals(2, positionState.getHalfMoveClock());
		assertEquals(5, positionState.getFullMoveClock());
	}
	
	@Test
	public void testColorBoard() {
		// execute
		moveExecutor.executeMove(colorBoard);

		// asserts execute
		assertEquals(Color.WHITE, colorBoard.getColor(Square.e3));
		assertTrue(colorBoard.isEmpty(Square.e2));

		// undos
		moveExecutor.undoMove(colorBoard);
		
		// asserts undos
		assertEquals(Color.WHITE, colorBoard.getColor(Square.e2));
		assertTrue(colorBoard.isEmpty(Square.e3));
	}

	@Test
	public void testMoveCacheBoard(){
		moveExecutor.executeMove(moveCacheBoard);

		assertNull(moveCacheBoard.getPseudoMovesResult(Square.e2));

		moveExecutor.undoMove(moveCacheBoard);

		assertNotNull(moveCacheBoard.getPseudoMovesResult(Square.e2));
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
		colorBoard.validar(piecePlacement);
		positionState.validar(piecePlacement);
		moveCacheBoard.validar(piecePlacement);

		// undos
		moveExecutor.undoMove(piecePlacement);
		moveExecutor.undoMove(positionState);
		moveExecutor.undoMove(colorBoard);
		moveExecutor.undoMove(moveCacheBoard);


		// asserts undos
		colorBoard.validar(piecePlacement);
		positionState.validar(piecePlacement);
		moveCacheBoard.validar(piecePlacement);
	}
}
