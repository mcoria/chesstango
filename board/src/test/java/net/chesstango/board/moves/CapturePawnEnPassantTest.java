package net.chesstango.board.moves;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.debug.chess.BitBoardDebug;
import net.chesstango.board.debug.chess.MoveCacheBoardDebug;
import net.chesstango.board.debug.chess.PositionStateDebug;
import net.chesstango.board.factory.SingletonMoveFactories;
import net.chesstango.board.iterators.Cardinal;
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
public class CapturePawnEnPassantTest {

    private SquareBoard squareBoard;

    private PositionStateDebug positionState;

    private Move moveExecutor;

    private BitBoardDebug colorBoard;

    private MoveCacheBoardDebug moveCacheBoard;

    private ZobristHash zobristHash;

    @Mock
    private ChessPosition chessPosition;

    @Mock
    private MoveFilter filter;

    @BeforeEach
    public void setUp() throws Exception {
        positionState = new PositionStateDebug();
        positionState.setCurrentTurn(Color.WHITE);
        positionState.setEnPassantSquare(Square.a6);
        positionState.setHalfMoveClock(2);
        positionState.setFullMoveClock(5);

        squareBoard = new SquareBoardImp();
        squareBoard.setPiece(Square.b5, Piece.PAWN_WHITE);
        squareBoard.setPiece(Square.a5, Piece.PAWN_BLACK);

        colorBoard = new BitBoardDebug();
        colorBoard.init(squareBoard);

        PiecePositioned pawnWhite = squareBoard.getPosition(Square.b5);
        PiecePositioned pawnBlack = squareBoard.getPosition(Square.a5);
        PiecePositioned pawnPasanteSquare = squareBoard.getPosition(Square.a6);

        moveCacheBoard = new MoveCacheBoardDebug();
        moveCacheBoard.setPseudoMoves(Square.b5, new MoveGeneratorResult(pawnWhite));
        moveCacheBoard.setPseudoMoves(Square.a5, new MoveGeneratorResult(pawnBlack));

        zobristHash = new ZobristHashImp();
        zobristHash.init(squareBoard, positionState);

        moveExecutor = SingletonMoveFactories.getDefaultMoveFactoryWhite().createCaptureEnPassantPawnMove(pawnWhite, pawnPasanteSquare, pawnBlack, Cardinal.NorteOeste);
    }

    @Test
    public void testEquals() {
        assertEquals(SingletonMoveFactories.getDefaultMoveFactoryWhite().createCaptureEnPassantPawnMove(squareBoard.getPosition(Square.b5), squareBoard.getPosition(Square.a6), squareBoard.getPosition(Square.a5), Cardinal.NorteOeste), moveExecutor);
    }

    @Test
    public void testGetDirection() {
        assertEquals(Cardinal.calculateSquaresDirection(moveExecutor.getFrom().getSquare(), moveExecutor.getTo().getSquare()), moveExecutor.getMoveDirection());
    }

    @Test
    public void testZobristHash() {
        ChessPositionImp chessPositionImp = new ChessPositionImp();
        chessPositionImp.setZobristHash(zobristHash);
        chessPositionImp.setPositionState(positionState);

        moveExecutor.executeMove(positionState);
        moveExecutor.executeMove(zobristHash, chessPositionImp);

        assertEquals(PolyglotEncoder.getKey("8/8/P7/8/8/8/8/8 b - - 0 1").longValue(), zobristHash.getZobristHash());
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
        // execute
        moveExecutor.executeMove(squareBoard);

        // asserts execute
        assertTrue(squareBoard.isEmpty(Square.a5));
        assertTrue(squareBoard.isEmpty(Square.b5));
        assertEquals(Piece.PAWN_WHITE, squareBoard.getPiece(Square.a6));

        // undos
        moveExecutor.undoMove(squareBoard);

        // asserts undos
        assertTrue(squareBoard.isEmpty(Square.a6));
        assertEquals(Piece.PAWN_WHITE, squareBoard.getPiece(Square.b5));
        assertEquals(Piece.PAWN_BLACK, squareBoard.getPiece(Square.a5));

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
        moveExecutor.executeMove(squareBoard);
        moveExecutor.executeMove(positionState);
        moveExecutor.executeMove(colorBoard);
        moveExecutor.executeMove(moveCacheBoard);

        // asserts execute
        assertTrue(squareBoard.isEmpty(Square.a5));
        assertTrue(squareBoard.isEmpty(Square.b5));
        assertEquals(Piece.PAWN_WHITE, squareBoard.getPiece(Square.a6));

        assertNull(positionState.getEnPassantSquare());
        assertEquals(Color.BLACK, positionState.getCurrentTurn());

        assertEquals(Color.WHITE, colorBoard.getColor(Square.a6));
        assertTrue(colorBoard.isEmpty(Square.a5));
        assertTrue(colorBoard.isEmpty(Square.b5));

        assertNull(moveCacheBoard.getPseudoMovesResult(Square.a5));

        colorBoard.validar(squareBoard);
        positionState.validar(squareBoard);
        moveCacheBoard.validar(squareBoard);

        // undos
        moveExecutor.undoMove(squareBoard);
        moveExecutor.undoMove(positionState);
        moveExecutor.undoMove(colorBoard);
        moveExecutor.undoMove(moveCacheBoard);


        // asserts undos
        assertTrue(squareBoard.isEmpty(Square.a6));
        assertEquals(Piece.PAWN_WHITE, squareBoard.getPiece(Square.b5));
        assertEquals(Piece.PAWN_BLACK, squareBoard.getPiece(Square.a5));

        assertEquals(Square.a6, positionState.getEnPassantSquare());
        assertEquals(Color.WHITE, positionState.getCurrentTurn());

        assertTrue(colorBoard.isEmpty(Square.a6));
        assertEquals(Color.BLACK, colorBoard.getColor(Square.a5));
        assertEquals(Color.WHITE, colorBoard.getColor(Square.b5));

        assertNotNull(moveCacheBoard.getPseudoMovesResult(Square.a5));
        assertNotNull(moveCacheBoard.getPseudoMovesResult(Square.b5));

        colorBoard.validar(squareBoard);
        positionState.validar(squareBoard);
        moveCacheBoard.validar(squareBoard);
    }
}
