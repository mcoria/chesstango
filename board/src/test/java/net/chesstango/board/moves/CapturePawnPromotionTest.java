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
public class CapturePawnPromotionTest {

    private Move moveExecutor;
    private SquareBoard squareBoard;

    private PositionStateDebug positionState;
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
        positionState.setEnPassantSquare(null);
        positionState.setHalfMoveClock(3);
        positionState.setFullMoveClock(5);

        squareBoard = new SquareBoardImp();
        squareBoard.setPiece(Square.e7, Piece.PAWN_WHITE);
        squareBoard.setPiece(Square.f8, Piece.KNIGHT_BLACK);

        colorBoard = new BitBoardDebug();
        colorBoard.init(squareBoard);

        PiecePositioned origen = squareBoard.getPosition(Square.e7);
        PiecePositioned destino = squareBoard.getPosition(Square.f8);

        moveCacheBoard = new MoveCacheBoardDebug();
        moveCacheBoard.setPseudoMoves(Square.e7, new MoveGeneratorResult(origen));
        moveCacheBoard.setPseudoMoves(Square.f8, new MoveGeneratorResult(destino));

        zobristHash = new ZobristHashImp();
        zobristHash.init(squareBoard, positionState);

        moveExecutor = SingletonMoveFactories.getDefaultMoveFactoryWhite().createCapturePromotionPawnMove(origen, destino, Piece.QUEEN_WHITE, Cardinal.NorteEste);
    }

    @Test
    public void testEquals() {
        assertEquals(SingletonMoveFactories.getDefaultMoveFactoryWhite().createCapturePromotionPawnMove(squareBoard.getPosition(Square.e7), squareBoard.getPosition(Square.f8), Piece.QUEEN_WHITE, Cardinal.NorteEste), moveExecutor);
        assertNotEquals(SingletonMoveFactories.getDefaultMoveFactoryWhite().createCapturePromotionPawnMove(squareBoard.getPosition(Square.e7), squareBoard.getPosition(Square.f8), Piece.ROOK_WHITE, Cardinal.NorteEste), moveExecutor);
    }

    @Test
    public void testGetDirection() {
        assertEquals(Cardinal.calculateSquaresDirection(moveExecutor.getFrom().getSquare(), moveExecutor.getTo().getSquare()), moveExecutor.getMoveDirection());
    }

    @Test
    public void testZobristHash() {
        ChessPositionImp chessPositionImp = new ChessPositionImp();
        chessPositionImp.setZobristHash(zobristHash);
        chessPositionImp.setBoardState(positionState);

        moveExecutor.executeMove(positionState);
        moveExecutor.executeMove(zobristHash, chessPositionImp);

        assertEquals(PolyglotEncoder.getKey("5Q2/8/8/8/8/8/8/8 b - - 0 1").longValue(), zobristHash.getZobristHash());
    }

    @Test
    public void testZobristHashUndo() {
        ChessPositionImp chessPositionImp = new ChessPositionImp();
        chessPositionImp.setZobristHash(zobristHash);
        chessPositionImp.setBoardState(positionState);

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
        assertEquals(Piece.QUEEN_WHITE, squareBoard.getPiece(Square.f8));
        assertTrue(squareBoard.isEmpty(Square.e7));

        // undos
        moveExecutor.undoMove(squareBoard);

        // asserts undos
        assertEquals(Piece.PAWN_WHITE, squareBoard.getPiece(Square.e7));
        assertEquals(Piece.KNIGHT_BLACK, squareBoard.getPiece(Square.f8));
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
        assertEquals(3, positionState.getHalfMoveClock());
        assertEquals(5, positionState.getFullMoveClock());
    }

    @Test
    public void testColorBoard() {
        // execute
        moveExecutor.executeMove(colorBoard);

        // asserts execute
        assertEquals(Color.WHITE, colorBoard.getColor(Square.f8));
        assertTrue(colorBoard.isEmpty(Square.e7));

        // undos
        moveExecutor.undoMove(colorBoard);

        // asserts undos
        assertEquals(Color.WHITE, colorBoard.getColor(Square.e7));
        assertEquals(Color.BLACK, colorBoard.getColor(Square.f8));
    }

    @Test
    public void testMoveCacheBoard() {
        moveExecutor.executeMove(moveCacheBoard);

        assertNull(moveCacheBoard.getPseudoMovesResult(Square.e7));
        assertNull(moveCacheBoard.getPseudoMovesResult(Square.f8));

        moveExecutor.undoMove(moveCacheBoard);

        assertNotNull(moveCacheBoard.getPseudoMovesResult(Square.e7));
        assertNotNull(moveCacheBoard.getPseudoMovesResult(Square.f8));
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

        // asserts execute
        assertEquals(Piece.QUEEN_WHITE, squareBoard.getPiece(Square.f8));
        assertTrue(squareBoard.isEmpty(Square.e7));

        assertNull(positionState.getEnPassantSquare());
        assertEquals(Color.BLACK, positionState.getCurrentTurn());
        assertEquals(0, positionState.getHalfMoveClock());
        assertEquals(5, positionState.getFullMoveClock());

        assertEquals(Color.WHITE, colorBoard.getColor(Square.f8));
        assertTrue(colorBoard.isEmpty(Square.e7));

        colorBoard.validar(squareBoard);
        positionState.validar(squareBoard);

        // undos
        moveExecutor.undoMove(squareBoard);
        moveExecutor.undoMove(positionState);
        moveExecutor.undoMove(colorBoard);


        // asserts undos
        assertEquals(Piece.PAWN_WHITE, squareBoard.getPiece(Square.e7));
        assertEquals(Piece.KNIGHT_BLACK, squareBoard.getPiece(Square.f8));

        assertNull(positionState.getEnPassantSquare());
        assertEquals(Color.WHITE, positionState.getCurrentTurn());
        assertEquals(3, positionState.getHalfMoveClock());
        assertEquals(5, positionState.getFullMoveClock());

        assertEquals(Color.WHITE, colorBoard.getColor(Square.e7));
        assertEquals(Color.BLACK, colorBoard.getColor(Square.f8));

        colorBoard.validar(squareBoard);
        positionState.validar(squareBoard);
    }
}
