package net.chesstango.board.moves;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.Square;
import net.chesstango.board.debug.chess.BitBoardDebug;
import net.chesstango.board.debug.chess.KingSquareDebug;
import net.chesstango.board.debug.chess.MoveCacheBoardDebug;
import net.chesstango.board.debug.chess.PositionStateDebug;
import net.chesstango.board.factory.SingletonMoveFactories;
import net.chesstango.board.movesgenerators.legal.MoveFilter;
import net.chesstango.board.movesgenerators.pseudo.MoveGeneratorResult;
import net.chesstango.board.position.SquareBoard;
import net.chesstango.board.position.ChessPosition;
import net.chesstango.board.position.PositionStateReader;
import net.chesstango.board.position.imp.SquareBoardImp;
import net.chesstango.board.position.ZobristHash;
import net.chesstango.board.position.imp.ZobristHashImp;
import net.chesstango.board.representations.polyglot.PolyglotEncoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;


/**
 * @author Mauricio Coria
 */
@ExtendWith(MockitoExtension.class)
public class CastlingWhiteQueenTest {

    private SquareBoard squareBoard;

    private PositionStateDebug positionState;

    private MoveCastling moveExecutor;

    private KingSquareDebug kingCacheBoard;

    private BitBoardDebug colorBoard;

    private MoveCacheBoardDebug moveCacheBoard;

    private ZobristHash zobristHash;

    @Mock
    private ChessPosition chessPosition;

    @Mock
    private MoveFilter filter;

    @BeforeEach
    public void setUp() throws Exception {
        moveExecutor = SingletonMoveFactories.getDefaultMoveFactoryWhite().createCastlingQueenMove();

        positionState = new PositionStateDebug();
        positionState.setCurrentTurn(Color.WHITE);
        positionState.setCastlingWhiteQueenAllowed(true);
        positionState.setCastlingWhiteKingAllowed(false);
        positionState.setHalfMoveClock(3);
        positionState.setFullMoveClock(10);

        squareBoard = new SquareBoardImp();
        squareBoard.setPiece(Square.a1, Piece.ROOK_WHITE);
        squareBoard.setPiece(Square.e1, Piece.KING_WHITE);

        kingCacheBoard = new KingSquareDebug();
        kingCacheBoard.init(squareBoard);

        colorBoard = new BitBoardDebug();
        colorBoard.init(squareBoard);

        moveCacheBoard = new MoveCacheBoardDebug();
        moveCacheBoard.setPseudoMoves(moveExecutor.getFrom().getSquare(), new MoveGeneratorResult(moveExecutor.getFrom()));
        moveCacheBoard.setPseudoMoves(moveExecutor.getRookFrom().getSquare(), new MoveGeneratorResult(moveExecutor.getRookFrom()));

        zobristHash = new ZobristHashImp();
        zobristHash.init(squareBoard, positionState);
    }

    @Test
    public void testEquals() {
        assertEquals(SingletonMoveFactories.getDefaultMoveFactoryWhite().createCastlingQueenMove(), moveExecutor);
    }

    @Test
    public void testGetDirection() {
        assertEquals(null, moveExecutor.getMoveDirection());
    }

    @Test
    public void testZobristHash() {
        PositionStateReader oldPositionState = positionState.getCurrentState();
        moveExecutor.executeMove(positionState);
        moveExecutor.executeMove(zobristHash, oldPositionState, positionState, null);

        assertEquals(PolyglotEncoder.getKey("8/8/8/8/8/8/8/2KR4 b - - 0 1").longValue(), zobristHash.getZobristHash());
    }

    @Test
    public void testZobristHashUndo() {
        long initialHash = zobristHash.getZobristHash();

        PositionStateReader oldPositionState = positionState.getCurrentState();
        moveExecutor.executeMove(positionState);
        moveExecutor.executeMove(zobristHash, oldPositionState, positionState, null);

        oldPositionState = positionState.getCurrentState();
        moveExecutor.undoMove(positionState);
        moveExecutor.undoMove(zobristHash, oldPositionState, positionState, null);

        assertEquals(initialHash, zobristHash.getZobristHash());
    }

    @Test
    public void testPosicionPiezaBoard() {
        moveExecutor.executeMove(squareBoard);

        assertEquals(Piece.KING_WHITE, squareBoard.getPiece(Square.c1));
        assertEquals(Piece.ROOK_WHITE, squareBoard.getPiece(Square.d1));

        assertTrue(squareBoard.isEmpty(Square.a1));
        assertTrue(squareBoard.isEmpty(Square.e1));

        moveExecutor.undoMove(squareBoard);

        assertEquals(Piece.KING_WHITE, squareBoard.getPiece(Square.e1));
        assertEquals(Piece.ROOK_WHITE, squareBoard.getPiece(Square.a1));

        assertTrue(squareBoard.isEmpty(Square.c1));
        assertTrue(squareBoard.isEmpty(Square.d1));
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
        assertFalse(positionState.isCastlingWhiteKingAllowed());
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

        assertEquals(Square.c1, kingCacheBoard.getKingSquareWhite());

        moveExecutor.undoMove(kingCacheBoard);

        assertEquals(Square.e1, kingCacheBoard.getKingSquareWhite());
    }

    @Test
    public void testBoard() {
        // execute
        moveExecutor.executeMove(chessPosition);

        // asserts execute
        verify(chessPosition).executeMoveKing(moveExecutor);

        // undos
        moveExecutor.undoMove(chessPosition);


        // asserts undos
        verify(chessPosition).undoMoveKing(moveExecutor);
    }

    @Test
    public void testCacheBoard() {
        // execute
        moveExecutor.executeMove(moveCacheBoard);

        // asserts execute
        assertNull(moveCacheBoard.getPseudoMovesResult(moveExecutor.getFrom().getSquare()));
        assertNull(moveCacheBoard.getPseudoMovesResult(moveExecutor.getTo().getSquare()));
        assertNull(moveCacheBoard.getPseudoMovesResult(moveExecutor.getRookFrom().getSquare()));
        assertNull(moveCacheBoard.getPseudoMovesResult(moveExecutor.getRookTo().getSquare()));

        // undos
        moveExecutor.undoMove(moveCacheBoard);

        // asserts undos
        assertNotNull(moveCacheBoard.getPseudoMovesResult(moveExecutor.getFrom().getSquare()));
        assertNull(moveCacheBoard.getPseudoMovesResult(moveExecutor.getTo().getSquare()));
        assertNotNull(moveCacheBoard.getPseudoMovesResult(moveExecutor.getRookFrom().getSquare()));
        assertNull(moveCacheBoard.getPseudoMovesResult(moveExecutor.getRookTo().getSquare()));
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
        moveExecutor.executeMove(squareBoard);
        moveExecutor.executeMove(positionState);
        moveExecutor.executeMove(colorBoard);
        moveExecutor.executeMove(kingCacheBoard);
        moveExecutor.executeMove(moveCacheBoard);

        // asserts execute
        colorBoard.validar(squareBoard);
        positionState.validar(squareBoard);
        kingCacheBoard.validar(squareBoard);
        moveCacheBoard.validar(squareBoard);

        // undos
        moveExecutor.undoMove(squareBoard);
        moveExecutor.undoMove(positionState);
        moveExecutor.undoMove(colorBoard);
        moveExecutor.undoMove(kingCacheBoard);
        moveExecutor.undoMove(moveCacheBoard);


        // asserts undos
        colorBoard.validar(squareBoard);
        positionState.validar(squareBoard);
        kingCacheBoard.validar(squareBoard);
        moveCacheBoard.validar(squareBoard);
    }
}
