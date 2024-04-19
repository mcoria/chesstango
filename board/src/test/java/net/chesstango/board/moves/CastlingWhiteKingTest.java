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
public class CastlingWhiteKingTest {

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
        moveExecutor = SingletonMoveFactories.getDefaultMoveFactoryWhite().createCastlingKingMove();

        positionState = new PositionStateDebug();
        positionState.setCurrentTurn(Color.WHITE);
        positionState.setCastlingWhiteQueenAllowed(false);
        positionState.setCastlingWhiteKingAllowed(true);
        positionState.setHalfMoveClock(3);
        positionState.setFullMoveClock(10);

        squareBoard = new SquareBoardImp();
        squareBoard.setPiece(Square.e1, Piece.KING_WHITE);
        squareBoard.setPiece(Square.h1, Piece.ROOK_WHITE);

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
        assertEquals(SingletonMoveFactories.getDefaultMoveFactoryWhite().createCastlingKingMove(), moveExecutor);
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

        moveExecutor.doMove(positionState);
        moveExecutor.doMove(zobristHash, chessPositionImp);

        assertEquals(PolyglotEncoder.getKey("8/8/8/8/8/8/8/5RK1 b - - 0 1").longValue(), zobristHash.getZobristHash());
    }

    @Test
    public void testZobristHashUndo() {
        ChessPositionImp chessPositionImp = new ChessPositionImp();
        chessPositionImp.setZobristHash(zobristHash);
        chessPositionImp.setPositionState(positionState);

        long initialHash = zobristHash.getZobristHash();

        moveExecutor.doMove(positionState);
        moveExecutor.doMove(zobristHash, chessPositionImp);

        moveExecutor.undoMove(positionState);
        moveExecutor.undoMove(zobristHash);

        assertEquals(initialHash, zobristHash.getZobristHash());
    }


    @Test
    public void testPosicionPiezaBoard() {
        moveExecutor.doMove(squareBoard);

        assertEquals(Piece.KING_WHITE, squareBoard.getPiece(Square.g1));
        assertEquals(Piece.ROOK_WHITE, squareBoard.getPiece(Square.f1));

        assertTrue(squareBoard.isEmpty(Square.e1));
        assertTrue(squareBoard.isEmpty(Square.h1));

        moveExecutor.undoMove(squareBoard);

        assertEquals(Piece.KING_WHITE, squareBoard.getPiece(Square.e1));
        assertEquals(Piece.ROOK_WHITE, squareBoard.getPiece(Square.h1));

        assertTrue(squareBoard.isEmpty(Square.g1));
        assertTrue(squareBoard.isEmpty(Square.f1));
    }

    @Test
    public void testBoardState() {
        // execute
        moveExecutor.doMove(positionState);

        // asserts execute
        assertNull(positionState.getEnPassantSquare());
        assertEquals(Color.BLACK, positionState.getCurrentTurn());
        assertFalse(positionState.isCastlingWhiteQueenAllowed());
        assertFalse(positionState.isCastlingWhiteKingAllowed());
        assertEquals(4, positionState.getHalfMoveClock());
        assertEquals(10, positionState.getFullMoveClock());
        positionState.validar();

        // undos
        moveExecutor.undoMove(positionState);

        // asserts undos
        assertNull(positionState.getEnPassantSquare());
        assertEquals(Color.WHITE, positionState.getCurrentTurn());
        assertFalse(positionState.isCastlingWhiteQueenAllowed());
        assertTrue(positionState.isCastlingWhiteKingAllowed());
        assertEquals(3, positionState.getHalfMoveClock());
        assertEquals(10, positionState.getFullMoveClock());
        positionState.validar();

    }

    @Test
    public void testColorBoard() {
        // execute
        moveExecutor.doMove(colorBoard);

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
        moveExecutor.doMove(kingCacheBoard);

        assertEquals(Square.g1, kingCacheBoard.getKingSquareWhite());

        moveExecutor.undoMove(kingCacheBoard);

        assertEquals(Square.e1, kingCacheBoard.getKingSquareWhite());
    }

    @Test
    public void testCacheBoard() {
        // execute
        moveExecutor.doMove(moveCacheBoard);

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
        moveExecutor.doMove(squareBoard);
        moveExecutor.doMove(positionState);
        moveExecutor.doMove(colorBoard);
        moveExecutor.doMove(kingCacheBoard);
        moveExecutor.doMove(moveCacheBoard);

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
