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
import net.chesstango.board.position.imp.ChessPositionImp;
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
public class CastlingBlackKingTest {

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
        moveExecutor = SingletonMoveFactories.getDefaultMoveFactoryBlack().createCastlingKingMove();

        positionState = new PositionStateDebug();
        positionState.setCurrentTurn(Color.BLACK);
        positionState.setCastlingBlackQueenAllowed(false);
        positionState.setCastlingBlackKingAllowed(true);
        positionState.setHalfMoveClock(3);
        positionState.setFullMoveClock(10);

        squareBoard = new SquareBoardImp();
        squareBoard.setPiece(Square.e8, Piece.KING_BLACK);
        squareBoard.setPiece(Square.h8, Piece.ROOK_BLACK);

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
        assertEquals(SingletonMoveFactories.getDefaultMoveFactoryBlack().createCastlingKingMove(), moveExecutor);
    }

    @Test
    public void testGetDirection() {
        assertEquals(null, moveExecutor.getMoveDirection());
    }

    @Test
    public void testZobristHash() {
        ChessPositionImp chessPositionImp = new ChessPositionImp();
        chessPositionImp.setZobristHash(zobristHash);
        chessPositionImp.setPositionState(positionState);

        moveExecutor.executeMove(positionState);

        moveExecutor.executeMove(zobristHash, chessPositionImp);

        assertEquals(PolyglotEncoder.getKey("5rk1/8/8/8/8/8/8/8 w - - 0 1").longValue(), zobristHash.getZobristHash());
    }

    @Test
    public void testZobristHashUndo() {
        ChessPositionImp chessPositionImp = new ChessPositionImp();
        chessPositionImp.setZobristHash(zobristHash);
        chessPositionImp.setPositionState(positionState);

        long initialHash = zobristHash.getZobristHash();

        moveExecutor.executeMove(positionState);
        moveExecutor.executeMove(zobristHash, chessPositionImp);

        moveExecutor.undoMove(positionState);
        moveExecutor.undoMove(zobristHash);

        assertEquals(initialHash, zobristHash.getZobristHash());
    }

    @Test
    public void testPosicionPiezaBoard() {
        moveExecutor.executeMove(squareBoard);

        assertEquals(Piece.KING_BLACK, squareBoard.getPiece(Square.g8));
        assertEquals(Piece.ROOK_BLACK, squareBoard.getPiece(Square.f8));

        assertTrue(squareBoard.isEmpty(Square.e8));
        assertTrue(squareBoard.isEmpty(Square.h8));

        moveExecutor.undoMove(squareBoard);

        assertEquals(Piece.KING_BLACK, squareBoard.getPiece(Square.e8));
        assertEquals(Piece.ROOK_BLACK, squareBoard.getPiece(Square.h8));

        assertTrue(squareBoard.isEmpty(Square.g8));
        assertTrue(squareBoard.isEmpty(Square.f8));
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

        assertEquals(Square.g8, kingCacheBoard.getKingSquareBlack());

        moveExecutor.undoMove(kingCacheBoard);

        assertEquals(Square.e8, kingCacheBoard.getKingSquareBlack());
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
