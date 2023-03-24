package net.chesstango.board.moves;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.Square;
import net.chesstango.board.debug.chess.ColorBoardDebug;
import net.chesstango.board.debug.chess.KingCacheBoardDebug;
import net.chesstango.board.debug.chess.PositionStateDebug;
import net.chesstango.board.factory.SingletonMoveFactories;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.movesgenerators.legal.MoveFilter;
import net.chesstango.board.position.ChessPosition;
import net.chesstango.board.position.PiecePlacement;
import net.chesstango.board.position.PositionStateReader;
import net.chesstango.board.position.imp.*;
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
public class CastlingBlackKingMoveTest {
	
	private PiecePlacement piecePlacement;
	
	private PositionStateDebug positionState;
	
	private MoveKing moveExecutor;
	
	private KingCacheBoardDebug kingCacheBoard;
	
	private ColorBoardDebug colorBoard;

	private ZobristHash zobristHash;
	
	@Mock
	private ChessPosition chessPosition;
	
	@Mock
	private MoveFilter filter;	

	@Before
	public void setUp() throws Exception {
		moveExecutor = SingletonMoveFactories.getDefaultMoveFactoryBlack().createCastlingKingMove();
		
		positionState = new PositionStateDebug();
		positionState.setCurrentTurn(Color.BLACK);
		positionState.setCastlingBlackQueenAllowed(false);
		positionState.setCastlingBlackKingAllowed(true);
		positionState.setHalfMoveClock(3);
		positionState.setFullMoveClock(10);
		
		piecePlacement = new ArrayPiecePlacement();
		piecePlacement.setPieza(Square.e8, Piece.KING_BLACK);
		piecePlacement.setPieza(Square.h8, Piece.ROOK_BLACK);
		
		kingCacheBoard = new KingCacheBoardDebug();
		kingCacheBoard.init(piecePlacement);

		colorBoard = new ColorBoardDebug();
		colorBoard.init(piecePlacement);

		zobristHash = new ZobristHash();
		zobristHash.init(piecePlacement, positionState);
	}

	@Test
	public void testEquals() {
		assertEquals(SingletonMoveFactories.getDefaultMoveFactoryBlack().createCastlingKingMove(), moveExecutor);
	}

	@Test
	public void testGetDirection() {
		assertEquals(null, moveExecutor.getMoveDirection());
	}

	@Test
	public void testZobristHash(){
		PositionStateReader oldPositionState = positionState.getCurrentState();
		moveExecutor.executeMove(positionState);

		moveExecutor.executeMove(zobristHash, oldPositionState, positionState);

		Assert.assertEquals(PolyglotEncoder.getKey("5rk1/8/8/8/8/8/8/8 w - - 0 1").longValue(), zobristHash.getZobristHash());
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
		assertFalse(positionState.isCastlingBlackQueenAllowed());
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

	@Test
	public void testIntegrated() {
		// execute
		moveExecutor.executeMove(piecePlacement);
		moveExecutor.executeMove(positionState);
		moveExecutor.executeMove(colorBoard);
		moveExecutor.executeMove(kingCacheBoard);

		// asserts execute
		colorBoard.validar(piecePlacement);
		positionState.validar(piecePlacement);
		kingCacheBoard.validar(piecePlacement);

		// undos
		moveExecutor.undoMove(piecePlacement);
		moveExecutor.undoMove(positionState);
		moveExecutor.undoMove(colorBoard);
		moveExecutor.undoMove(kingCacheBoard);


		// asserts undos
		colorBoard.validar(piecePlacement);
		positionState.validar(piecePlacement);
		kingCacheBoard.validar(piecePlacement);
	}
}
