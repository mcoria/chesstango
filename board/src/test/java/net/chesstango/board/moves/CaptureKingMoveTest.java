package net.chesstango.board.moves;

import net.chesstango.board.*;
import net.chesstango.board.debug.chess.BitBoardDebug;
import net.chesstango.board.debug.chess.KingSquareDebug;
import net.chesstango.board.debug.chess.MoveCacheBoardDebug;
import net.chesstango.board.debug.chess.PositionStateDebug;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.factories.MoveFactory;
import net.chesstango.board.moves.factories.imp.MoveFactoryWhite;
import net.chesstango.board.moves.generators.legal.LegalMoveFilter;
import net.chesstango.board.moves.generators.pseudo.MoveGeneratorResult;
import net.chesstango.board.moves.imp.MoveKingImp;
import net.chesstango.board.position.SquareBoard;
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
public class CaptureKingMoveTest {
    private MoveFactoryWhite moveFactory;
    private MoveKingImp moveExecutor;
    private SquareBoard squareBoard;
    private PositionStateDebug positionState;
    private BitBoardDebug bitBoard;
    private KingSquareDebug kingCacheBoard;
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
        positionState.setHalfMoveClock(2);
        positionState.setFullMoveClock(5);

        squareBoard = new SquareBoardImp();
        squareBoard.setPiece(Square.e1, Piece.KING_WHITE);
        squareBoard.setPiece(Square.e2, Piece.KNIGHT_BLACK);

        bitBoard = new BitBoardDebug();
        bitBoard.init(squareBoard);

        kingCacheBoard = new KingSquareDebug();
        kingCacheBoard.init(squareBoard);

        PiecePositioned origen = squareBoard.getPosition(Square.e1);
        PiecePositioned destino = squareBoard.getPosition(Square.e2);

        moveCacheBoard = new MoveCacheBoardDebug();
        moveCacheBoard.setPseudoMoves(Square.e1, new MoveGeneratorResult(origen));
        moveCacheBoard.setPseudoMoves(Square.e2, new MoveGeneratorResult(destino));

        zobristHash = new ZobristHashImp();
        zobristHash.init(squareBoard, positionState);

        chessPosition = new ChessPositionImp();
        chessPosition.setSquareBoard(squareBoard);
        chessPosition.setPositionState(positionState);
        chessPosition.setBitBoard(bitBoard);
        chessPosition.setMoveCache(moveCacheBoard);
        chessPosition.setZobristHash(zobristHash);

        moveFactory = new MoveFactoryWhite(gameImp);

        moveExecutor = moveFactory.createCaptureKingMove(origen, destino);
    }

    @Test
    public void testEquals() {
        assertEquals(moveFactory.createCaptureKingMove(squareBoard.getPosition(Square.e1), squareBoard.getPosition(Square.e2)), moveExecutor);
    }

    @Test
    public void testGetDirection() {
        assertEquals(Cardinal.calculateSquaresDirection(moveExecutor.getFrom().getSquare(), moveExecutor.getTo().getSquare()), moveExecutor.getMoveDirection());
    }

    @Test
    public void testZobristHash() {
        when(gameImp.getChessPosition()).thenReturn(chessPosition);

        moveExecutor.doMove(positionState);
        moveExecutor.doMove(zobristHash);

        assertEquals(PolyglotEncoder.getKey("8/8/8/8/8/8/4K3/8 b - - 0 1").longValue(), zobristHash.getZobristHash());
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
        // execute
        moveExecutor.doMove(squareBoard);

        // asserts execute
        assertEquals(Piece.KING_WHITE, squareBoard.getPiece(Square.e2));
        assertNull(squareBoard.getPiece(Square.e1));

        // undos
        moveExecutor.undoMove(squareBoard);

        // asserts undos
        assertEquals(Piece.KING_WHITE, squareBoard.getPiece(Square.e1));
        assertEquals(Piece.KNIGHT_BLACK, squareBoard.getPiece(Square.e2));
    }

    @Test
    public void testBoardState() {
        moveExecutor.doMove(positionState);
        assertEquals(0, positionState.getHalfMoveClock());
        assertEquals(5, positionState.getFullMoveClock());

        assertEquals(Color.BLACK, positionState.getCurrentTurn());

        moveExecutor.undoMove(positionState);

        assertEquals(Color.WHITE, positionState.getCurrentTurn());
        assertEquals(2, positionState.getHalfMoveClock());
        assertEquals(5, positionState.getFullMoveClock());
    }

    @Test
    public void testKingCacheBoard() {
        moveExecutor.doMove(kingCacheBoard);

        assertEquals(Square.e2, kingCacheBoard.getKingSquareWhite());

        moveExecutor.undoMove(kingCacheBoard);

        assertEquals(Square.e1, kingCacheBoard.getKingSquareWhite());
    }

    @Test
    public void testColorBoard() {
        // execute
        moveExecutor.doMove(bitBoard);

        // asserts execute
        assertEquals(Color.WHITE, bitBoard.getColor(Square.e2));
        assertTrue(bitBoard.isEmpty(Square.e1));

        // undos
        moveExecutor.undoMove(bitBoard);

        // asserts undos
        assertEquals(Color.WHITE, bitBoard.getColor(Square.e1));
        assertEquals(Color.BLACK, bitBoard.getColor(Square.e2));
    }

    @Test
    public void testMoveCacheBoard() {
        moveExecutor.doMove(moveCacheBoard);

        assertNull(moveCacheBoard.getPseudoMovesResult(Square.e1));
        assertNull(moveCacheBoard.getPseudoMovesResult(Square.e2));

        moveExecutor.undoMove(moveCacheBoard);

        assertNotNull(moveCacheBoard.getPseudoMovesResult(Square.e1));
        assertNotNull(moveCacheBoard.getPseudoMovesResult(Square.e2));
    }


    @Test
    public void testFilter() {
        // execute
        moveExecutor.isLegalMove(filter);

        // asserts execute
        verify(filter).isLegalMoveKing(moveExecutor);
    }

    @Test
    public void testIntegrated() {
        // execute
        moveExecutor.doMove(squareBoard);
        moveExecutor.doMove(kingCacheBoard);
        moveExecutor.doMove(positionState);
        moveExecutor.doMove(bitBoard);
        moveExecutor.doMove(moveCacheBoard);

        // asserts execute
        assertEquals(Piece.KING_WHITE, squareBoard.getPiece(Square.e2));
        assertNull(squareBoard.getPiece(Square.e1));

        assertEquals(Square.e2, kingCacheBoard.getKingSquareWhite());

        assertEquals(Color.BLACK, positionState.getCurrentTurn());

        assertEquals(Color.WHITE, bitBoard.getColor(Square.e2));
        assertTrue(bitBoard.isEmpty(Square.e1));


        bitBoard.validar(squareBoard);
        kingCacheBoard.validar(squareBoard);
        moveCacheBoard.validar(squareBoard);
        moveCacheBoard.validar(squareBoard);

        // undos
        moveExecutor.undoMove(squareBoard);
        moveExecutor.undoMove(kingCacheBoard);
        moveExecutor.undoMove(positionState);
        moveExecutor.undoMove(bitBoard);
        moveExecutor.undoMove(moveCacheBoard);


        // asserts undos
        assertEquals(Piece.KING_WHITE, squareBoard.getPiece(Square.e1));
        assertEquals(Piece.KNIGHT_BLACK, squareBoard.getPiece(Square.e2));

        assertEquals(Square.e1, kingCacheBoard.getKingSquareWhite());

        assertEquals(Color.WHITE, positionState.getCurrentTurn());

        assertEquals(Color.WHITE, bitBoard.getColor(Square.e1));
        assertEquals(Color.BLACK, bitBoard.getColor(Square.e2));

        bitBoard.validar(squareBoard);
        kingCacheBoard.validar(squareBoard);
        moveCacheBoard.validar(squareBoard);
        moveCacheBoard.validar(squareBoard);
    }
}
