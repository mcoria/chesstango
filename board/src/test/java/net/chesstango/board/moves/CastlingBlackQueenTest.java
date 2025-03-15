package net.chesstango.board.moves;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.Square;
import net.chesstango.board.debug.chess.BitBoardDebug;
import net.chesstango.board.debug.chess.KingSquareDebug;
import net.chesstango.board.debug.chess.MoveCacheBoardDebug;
import net.chesstango.board.debug.chess.PositionStateDebug;
import net.chesstango.board.moves.factories.MoveFactory;
import net.chesstango.board.moves.factories.imp.MoveFactoryBlack;
import net.chesstango.board.moves.generators.legal.LegalMoveFilter;
import net.chesstango.board.moves.generators.pseudo.MoveGeneratorResult;
import net.chesstango.board.moves.imp.MoveCastlingImp;
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
public class CastlingBlackQueenTest {
    private MoveFactory moveFactory;

    private SquareBoard squareBoard;

    private PositionStateDebug positionState;

    private MoveCastlingImp moveExecutor;

    private KingSquareDebug kingCacheBoard;

    private BitBoardDebug colorBoard;

    private MoveCacheBoardDebug moveCacheBoard;

    private ZobristHash zobristHash;

    @Mock
    private ChessPosition chessPosition;

    @Mock
    private LegalMoveFilter filter;

    @BeforeEach
    public void setUp() throws Exception {
        moveFactory = new MoveFactoryBlack();

        moveExecutor = moveFactory.createCastlingQueenMove();

        positionState = new PositionStateDebug();
        positionState.setCurrentTurn(Color.BLACK);
        positionState.setCastlingBlackQueenAllowed(true);
        positionState.setCastlingBlackKingAllowed(false);
        positionState.setHalfMoveClock(3);
        positionState.setFullMoveClock(10);

        squareBoard = new SquareBoardImp();
        squareBoard.setPiece(Square.a8, Piece.ROOK_BLACK);
        squareBoard.setPiece(Square.e8, Piece.KING_BLACK);

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
        assertEquals(moveFactory.createCastlingQueenMove(), moveExecutor);
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

        assertEquals(PolyglotEncoder.getKey("2kr4/8/8/8/8/8/8/8 w - - 0 1").longValue(), zobristHash.getZobristHash());
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

        assertEquals(Piece.KING_BLACK, squareBoard.getPiece(Square.c8));
        assertEquals(Piece.ROOK_BLACK, squareBoard.getPiece(Square.d8));

        assertTrue(squareBoard.isEmpty(Square.a8));
        assertTrue(squareBoard.isEmpty(Square.e8));

        moveExecutor.undoMove(squareBoard);

        assertEquals(Piece.KING_BLACK, squareBoard.getPiece(Square.e8));
        assertEquals(Piece.ROOK_BLACK, squareBoard.getPiece(Square.a8));

        assertTrue(squareBoard.isEmpty(Square.c8));
        assertTrue(squareBoard.isEmpty(Square.d8));
    }

    @Test
    public void testBoardState() {
        moveExecutor.doMove(positionState);

        assertNull(positionState.getEnPassantSquare());
        assertEquals(Color.WHITE, positionState.getCurrentTurn());
        assertFalse(positionState.isCastlingBlackQueenAllowed());
        assertFalse(positionState.isCastlingBlackKingAllowed());
        assertEquals(4, positionState.getHalfMoveClock());
        assertEquals(11, positionState.getFullMoveClock());

        moveExecutor.undoMove(positionState);

        assertNull(positionState.getEnPassantSquare());
        assertEquals(Color.BLACK, positionState.getCurrentTurn());
        assertTrue(positionState.isCastlingBlackQueenAllowed());
        assertFalse(positionState.isCastlingBlackKingAllowed());
        assertEquals(3, positionState.getHalfMoveClock());
        assertEquals(10, positionState.getFullMoveClock());

    }

    @Test
    public void testColorBoard() {
        // execute
        moveExecutor.doMove(colorBoard);

        // asserts execute
        assertEquals(Color.BLACK, colorBoard.getColor(Square.c8));
        assertEquals(Color.BLACK, colorBoard.getColor(Square.d8));

        assertTrue(colorBoard.isEmpty(Square.a8));
        assertTrue(colorBoard.isEmpty(Square.e8));

        // undos
        moveExecutor.undoMove(colorBoard);

        // asserts undos
        assertEquals(Color.BLACK, colorBoard.getColor(Square.e8));
        assertEquals(Color.BLACK, colorBoard.getColor(Square.a8));

        assertTrue(colorBoard.isEmpty(Square.c8));
        assertTrue(colorBoard.isEmpty(Square.d8));
    }

    @Test
    public void testKingCacheBoard() {
        moveExecutor.doMove(kingCacheBoard);

        assertEquals(Square.c8, kingCacheBoard.getKingSquareBlack());

        moveExecutor.undoMove(kingCacheBoard);

        assertEquals(Square.e8, kingCacheBoard.getKingSquareBlack());
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
