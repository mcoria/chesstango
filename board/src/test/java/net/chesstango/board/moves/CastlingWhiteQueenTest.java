package net.chesstango.board.moves;

import net.chesstango.board.*;
import net.chesstango.board.debug.chess.BitBoardDebug;
import net.chesstango.board.debug.chess.KingSquareDebug;
import net.chesstango.board.debug.chess.MoveCacheBoardDebug;
import net.chesstango.board.debug.chess.PositionStateDebug;
import net.chesstango.board.moves.factories.MoveFactory;
import net.chesstango.board.moves.factories.imp.MoveFactoryWhite;
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
import static org.mockito.Mockito.when;


/**
 * @author Mauricio Coria
 */
@ExtendWith(MockitoExtension.class)
public class CastlingWhiteQueenTest {
    private MoveFactory moveFactory;

    private SquareBoard squareBoard;

    private PositionStateDebug positionState;

    private MoveCastlingImp moveExecutor;

    private KingSquareDebug kingCacheBoard;

    private BitBoardDebug bitBoard;

    private MoveCacheBoardDebug moveCacheBoard;

    private ZobristHash zobristHash;

    @Mock
    private GameImp gameImp;

    @Mock
    private LegalMoveFilter filter;

    private ChessPositionImp chessPosition;

    @BeforeEach
    public void setUp() throws Exception {
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

        bitBoard = new BitBoardDebug();
        bitBoard.init(squareBoard);

        moveCacheBoard = new MoveCacheBoardDebug();
        moveCacheBoard.setPseudoMoves(PiecePositioned.KING_WHITE.getSquare(), new MoveGeneratorResult(PiecePositioned.KING_WHITE));
        moveCacheBoard.setPseudoMoves(PiecePositioned.ROOK_WHITE_QUEEN.getSquare(), new MoveGeneratorResult(PiecePositioned.ROOK_WHITE_QUEEN));

        zobristHash = new ZobristHashImp();
        zobristHash.init(squareBoard, positionState);

        chessPosition = new ChessPositionImp();
        chessPosition.setSquareBoard(squareBoard);
        chessPosition.setPositionState(positionState);
        chessPosition.setBitBoard(bitBoard);
        chessPosition.setMoveCache(moveCacheBoard);
        chessPosition.setZobristHash(zobristHash);

        moveFactory = new MoveFactoryWhite(gameImp);

        moveExecutor = moveFactory.createCastlingQueenMove();
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
        when(gameImp.getChessPosition()).thenReturn(chessPosition);

        moveExecutor.doMove(positionState);
        moveExecutor.doMove(zobristHash);

        assertEquals(PolyglotEncoder.getKey("8/8/8/8/8/8/8/2KR4 b - - 0 1").longValue(), zobristHash.getZobristHash());
    }

    @Test
    public void testZobristHashUndo() {
        when(gameImp.getChessPosition()).thenReturn(chessPosition);

        long initialHash = zobristHash.getZobristHash();

        moveExecutor.doMove(positionState);
        moveExecutor.doMove(zobristHash);

        moveExecutor.undoMove(positionState);
        moveExecutor.undoMove(zobristHash);

        assertEquals(initialHash, zobristHash.getZobristHash());
    }

    @Test
    public void testPosicionPiezaBoard() {
        moveExecutor.doMove(squareBoard);

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
        moveExecutor.doMove(positionState);

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
        moveExecutor.doMove(bitBoard);

        // asserts execute
        assertEquals(Color.WHITE, bitBoard.getColor(Square.c1));
        assertEquals(Color.WHITE, bitBoard.getColor(Square.d1));

        assertTrue(bitBoard.isEmpty(Square.a1));
        assertTrue(bitBoard.isEmpty(Square.e1));

        // undos
        moveExecutor.undoMove(bitBoard);

        // asserts undos
        assertEquals(Color.WHITE, bitBoard.getColor(Square.e1));
        assertEquals(Color.WHITE, bitBoard.getColor(Square.a1));

        assertTrue(bitBoard.isEmpty(Square.c1));
        assertTrue(bitBoard.isEmpty(Square.d1));
    }

    @Test
    public void testKingCacheBoard() {
        moveExecutor.doMove(kingCacheBoard);

        assertEquals(Square.c1, kingCacheBoard.getKingSquareWhite());

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
        moveExecutor.doMove(bitBoard);
        moveExecutor.doMove(kingCacheBoard);
        moveExecutor.doMove(moveCacheBoard);

        // asserts execute
        bitBoard.validar(squareBoard);
        positionState.validar(squareBoard);
        kingCacheBoard.validar(squareBoard);
        moveCacheBoard.validar(squareBoard);

        // undos
        moveExecutor.undoMove(squareBoard);
        moveExecutor.undoMove(positionState);
        moveExecutor.undoMove(bitBoard);
        moveExecutor.undoMove(kingCacheBoard);
        moveExecutor.undoMove(moveCacheBoard);


        // asserts undos
        bitBoard.validar(squareBoard);
        positionState.validar(squareBoard);
        kingCacheBoard.validar(squareBoard);
        moveCacheBoard.validar(squareBoard);
    }
}
