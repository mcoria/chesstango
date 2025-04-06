package net.chesstango.board.internal.moves;

import net.chesstango.board.*;
import net.chesstango.board.internal.GameImp;
import net.chesstango.board.internal.position.BitBoardDebug;
import net.chesstango.board.internal.position.KingSquareDebug;
import net.chesstango.board.internal.position.MoveCacheBoardDebug;
import net.chesstango.board.internal.position.PositionStateDebug;
import net.chesstango.board.internal.moves.factories.MoveFactoryBlack;
import net.chesstango.board.moves.generators.legal.LegalMoveFilter;
import net.chesstango.board.moves.generators.pseudo.MoveGeneratorByPieceResult;
import net.chesstango.board.position.SquareBoard;
import net.chesstango.board.internal.position.ChessPositionImp;
import net.chesstango.board.internal.position.SquareBoardImp;
import net.chesstango.board.position.ZobristHash;
import net.chesstango.board.internal.position.ZobristHashImp;
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
public class CastlingBlackQueenTest {
    private MoveFactoryBlack moveFactory;

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

        bitBoard = new BitBoardDebug();
        bitBoard.init(squareBoard);

        moveCacheBoard = new MoveCacheBoardDebug();
        moveCacheBoard.setPseudoMoves(PiecePositioned.KING_BLACK.getSquare(), new MoveGeneratorByPieceResult(PiecePositioned.KING_BLACK));
        moveCacheBoard.setPseudoMoves(PiecePositioned.ROOK_BLACK_QUEEN.getSquare(), new MoveGeneratorByPieceResult(PiecePositioned.ROOK_BLACK_QUEEN));

        zobristHash = new ZobristHashImp();
        zobristHash.init(squareBoard, positionState);

        chessPosition = new ChessPositionImp();
        chessPosition.setSquareBoard(squareBoard);
        chessPosition.setPositionState(positionState);
        chessPosition.setBitBoard(bitBoard);
        chessPosition.setMoveCache(moveCacheBoard);
        chessPosition.setZobristHash(zobristHash);

        moveFactory = new MoveFactoryBlack(gameImp);

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
        when(gameImp.getPosition()).thenReturn(chessPosition);

        moveExecutor.doMove(positionState);
        moveExecutor.doMove(zobristHash);

        assertEquals(PolyglotEncoder.getKey("2kr4/8/8/8/8/8/8/8 w - - 0 1").longValue(), zobristHash.getZobristHash());
    }

    @Test
    public void testZobristHashUndo() {
        when(gameImp.getPosition()).thenReturn(chessPosition);

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
        moveExecutor.doMove(bitBoard);

        // asserts execute
        assertEquals(Color.BLACK, bitBoard.getColor(Square.c8));
        assertEquals(Color.BLACK, bitBoard.getColor(Square.d8));

        assertTrue(bitBoard.isEmpty(Square.a8));
        assertTrue(bitBoard.isEmpty(Square.e8));

        // undos
        moveExecutor.undoMove(bitBoard);

        // asserts undos
        assertEquals(Color.BLACK, bitBoard.getColor(Square.e8));
        assertEquals(Color.BLACK, bitBoard.getColor(Square.a8));

        assertTrue(bitBoard.isEmpty(Square.c8));
        assertTrue(bitBoard.isEmpty(Square.d8));
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
