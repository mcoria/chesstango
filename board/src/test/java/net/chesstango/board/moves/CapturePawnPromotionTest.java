package net.chesstango.board.moves;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.debug.chess.ColorBoardDebug;
import net.chesstango.board.debug.chess.MoveCacheBoardDebug;
import net.chesstango.board.debug.chess.PositionStateDebug;
import net.chesstango.board.factory.SingletonMoveFactories;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.movesgenerators.legal.MoveFilter;
import net.chesstango.board.movesgenerators.pseudo.MoveGeneratorResult;
import net.chesstango.board.position.ChessPosition;
import net.chesstango.board.position.Board;
import net.chesstango.board.position.PositionStateReader;
import net.chesstango.board.position.imp.ArrayBoard;
import net.chesstango.board.position.imp.ZobristHash;
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
 */
@RunWith(MockitoJUnitRunner.class)
public class CapturePawnPromotionTest {

    private Move moveExecutor;
    private Board board;

    private PositionStateDebug positionState;
    private ColorBoardDebug colorBoard;
    private MoveCacheBoardDebug moveCacheBoard;
    private ZobristHash zobristHash;

    @Mock
    private ChessPosition chessPosition;

    @Mock
    private MoveFilter filter;

    @Before
    public void setUp() throws Exception {
        positionState = new PositionStateDebug();
        positionState.setCurrentTurn(Color.WHITE);
        positionState.setEnPassantSquare(null);
        positionState.setHalfMoveClock(3);
        positionState.setFullMoveClock(5);

        board = new ArrayBoard();
        board.setPieza(Square.e7, Piece.PAWN_WHITE);
        board.setPieza(Square.f8, Piece.KNIGHT_BLACK);

        colorBoard = new ColorBoardDebug();
        colorBoard.init(board);

        PiecePositioned origen = board.getPosition(Square.e7);
        PiecePositioned destino = board.getPosition(Square.f8);

        moveCacheBoard = new MoveCacheBoardDebug();
        moveCacheBoard.setPseudoMoves(Square.e7, new MoveGeneratorResult(origen));
        moveCacheBoard.setPseudoMoves(Square.f8, new MoveGeneratorResult(destino));

        zobristHash = new ZobristHash();
        zobristHash.init(board, positionState);

        moveExecutor = SingletonMoveFactories.getDefaultMoveFactoryWhite().createCapturePromotionPawnMove(origen, destino, Piece.QUEEN_WHITE, Cardinal.NorteEste);
    }

    @Test
    public void testEquals() {
        assertEquals(SingletonMoveFactories.getDefaultMoveFactoryWhite().createCapturePromotionPawnMove(board.getPosition(Square.e7), board.getPosition(Square.f8), Piece.QUEEN_WHITE, Cardinal.NorteEste), moveExecutor);
        assertNotEquals(SingletonMoveFactories.getDefaultMoveFactoryWhite().createCapturePromotionPawnMove(board.getPosition(Square.e7), board.getPosition(Square.f8), Piece.ROOK_WHITE, Cardinal.NorteEste), moveExecutor);
    }

    @Test
    public void testGetDirection() {
        assertEquals(Cardinal.calculateSquaresDirection(moveExecutor.getFrom().getSquare(), moveExecutor.getTo().getSquare()), moveExecutor.getMoveDirection());
    }

    @Test
    public void testZobristHash() {
        PositionStateReader oldPositionState = positionState.getCurrentState();
        moveExecutor.executeMove(positionState);
        moveExecutor.executeMove(zobristHash, oldPositionState, positionState, null);

        Assert.assertEquals(PolyglotEncoder.getKey("5Q2/8/8/8/8/8/8/8 b - - 0 1").longValue(), zobristHash.getZobristHash());
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

        Assert.assertEquals(initialHash, zobristHash.getZobristHash());
    }

    @Test
    public void testPosicionPiezaBoard() {
        // execute
        moveExecutor.executeMove(board);

        // asserts execute
        assertEquals(Piece.QUEEN_WHITE, board.getPiece(Square.f8));
        assertTrue(board.isEmpty(Square.e7));

        // undos
        moveExecutor.undoMove(board);

        // asserts undos
        assertEquals(Piece.PAWN_WHITE, board.getPiece(Square.e7));
        assertEquals(Piece.KNIGHT_BLACK, board.getPiece(Square.f8));
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
        moveExecutor.executeMove(board);
        moveExecutor.executeMove(positionState);
        moveExecutor.executeMove(colorBoard);

        // asserts execute
        assertEquals(Piece.QUEEN_WHITE, board.getPiece(Square.f8));
        assertTrue(board.isEmpty(Square.e7));

        assertNull(positionState.getEnPassantSquare());
        assertEquals(Color.BLACK, positionState.getCurrentTurn());
        assertEquals(0, positionState.getHalfMoveClock());
        assertEquals(5, positionState.getFullMoveClock());

        assertEquals(Color.WHITE, colorBoard.getColor(Square.f8));
        assertTrue(colorBoard.isEmpty(Square.e7));

        colorBoard.validar(board);
        positionState.validar(board);

        // undos
        moveExecutor.undoMove(board);
        moveExecutor.undoMove(positionState);
        moveExecutor.undoMove(colorBoard);


        // asserts undos
        assertEquals(Piece.PAWN_WHITE, board.getPiece(Square.e7));
        assertEquals(Piece.KNIGHT_BLACK, board.getPiece(Square.f8));

        assertNull(positionState.getEnPassantSquare());
        assertEquals(Color.WHITE, positionState.getCurrentTurn());
        assertEquals(3, positionState.getHalfMoveClock());
        assertEquals(5, positionState.getFullMoveClock());

        assertEquals(Color.WHITE, colorBoard.getColor(Square.e7));
        assertEquals(Color.BLACK, colorBoard.getColor(Square.f8));

        colorBoard.validar(board);
        positionState.validar(board);
    }
}
