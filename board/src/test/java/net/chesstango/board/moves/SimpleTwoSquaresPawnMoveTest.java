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
import net.chesstango.board.position.imp.ArrayPiecePlacement;
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
public class SimpleTwoSquaresPawnMoveTest {

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
        positionState.setHalfMoveClock(2);
        positionState.setFullMoveClock(5);

        board = new ArrayPiecePlacement();
        board.setPieza(Square.e2, Piece.PAWN_WHITE);
        board.setPieza(Square.f4, Piece.PAWN_BLACK);

        colorBoard = new ColorBoardDebug();
        colorBoard.init(board);

        PiecePositioned origen = board.getPosition(Square.e2);
        PiecePositioned destino = board.getPosition(Square.e4);
        PiecePositioned peonNegro = board.getPosition(Square.f4);

        moveCacheBoard = new MoveCacheBoardDebug();
        moveCacheBoard.setPseudoMoves(Square.e2, new MoveGeneratorResult(origen));
        moveCacheBoard.setPseudoMoves(Square.f4, new MoveGeneratorResult(peonNegro).addAffectedByPositions(Square.e3));

        zobristHash = new ZobristHash();
        zobristHash.init(board, positionState);

        moveExecutor = SingletonMoveFactories.getDefaultMoveFactoryWhite().createSimpleTwoSquaresPawnMove(origen, destino, Square.e3);
    }

    @Test
    public void testEquals() {
        assertEquals(SingletonMoveFactories.getDefaultMoveFactoryWhite().createSimpleTwoSquaresPawnMove(board.getPosition(Square.e2), board.getPosition(Square.e4), Square.e3), moveExecutor);
    }

    @Test
    public void testGetDirection() {
        assertEquals(Cardinal.calculateSquaresDirection(moveExecutor.getFrom().getSquare(), moveExecutor.getTo().getSquare()), moveExecutor.getMoveDirection());
    }

    @Test
    public void testZobristHash() {
        moveExecutor.executeMove(board);
        PositionStateReader oldPositionState = positionState.getCurrentState();
        moveExecutor.executeMove(positionState);
        moveExecutor.executeMove(zobristHash, oldPositionState, positionState, board);

        Assert.assertEquals(PolyglotEncoder.getKey("8/8/8/8/4Pp2/8/8/8 b - e3 0 1").longValue(), zobristHash.getZobristHash());
    }

    @Test
    public void testZobristHashUndo() {
        long initialHash = zobristHash.getZobristHash();

        moveExecutor.executeMove(board);
        PositionStateReader oldPositionState = positionState.getCurrentState();
        moveExecutor.executeMove(positionState);
        moveExecutor.executeMove(zobristHash, oldPositionState, positionState, board);

        moveExecutor.undoMove(board);
        oldPositionState = positionState.getCurrentState();
        moveExecutor.undoMove(positionState);
        moveExecutor.undoMove(zobristHash, oldPositionState, positionState, board);

        Assert.assertEquals(initialHash, zobristHash.getZobristHash());
    }

    @Test
    public void testPosicionPiezaBoard() {
        // execute
        moveExecutor.executeMove(board);

        // asserts execute
        assertEquals(Piece.PAWN_WHITE, board.getPiece(Square.e4));
        assertTrue(board.isEmpty(Square.e2));

        // undos
        moveExecutor.undoMove(board);

        // asserts undos
        assertEquals(Piece.PAWN_WHITE, board.getPiece(Square.e2));
        assertTrue(board.isEmpty(Square.e4));
    }

    @Test
    public void testMoveState() {
        // execute
        moveExecutor.executeMove(positionState);

        // asserts execute
        assertEquals(Square.e3, positionState.getEnPassantSquare());
        assertEquals(Color.BLACK, positionState.getCurrentTurn());
        assertEquals(0, positionState.getHalfMoveClock());
        assertEquals(5, positionState.getFullMoveClock());

        // undos
        moveExecutor.undoMove(positionState);

        // asserts undos
        assertNull(positionState.getEnPassantSquare());
        assertEquals(Color.WHITE, positionState.getCurrentTurn());
        assertEquals(2, positionState.getHalfMoveClock());
        assertEquals(5, positionState.getFullMoveClock());
    }

    @Test
    public void testColorBoard() {
        // execute
        moveExecutor.executeMove(colorBoard);

        // asserts execute
        assertTrue(colorBoard.isEmpty(Square.e2));
        assertEquals(Color.WHITE, colorBoard.getColor(Square.e4));

        // undos
        moveExecutor.undoMove(colorBoard);

        // asserts undos
        assertEquals(Color.WHITE, colorBoard.getColor(Square.e2));
        assertTrue(colorBoard.isEmpty(Square.e4));
    }

    @Test
    public void testMoveCacheBoard() {
        moveExecutor.executeMove(moveCacheBoard);

        assertNull(moveCacheBoard.getPseudoMovesResult(Square.e2));
        assertNull(moveCacheBoard.getPseudoMovesResult(Square.f4));

        moveExecutor.undoMove(moveCacheBoard);

        assertNotNull(moveCacheBoard.getPseudoMovesResult(Square.e2));
        assertNotNull(moveCacheBoard.getPseudoMovesResult(Square.f4));
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
        moveExecutor.executeMove(moveCacheBoard);

        // asserts execute
        colorBoard.validar(board);
        positionState.validar(board);
        moveCacheBoard.validar(board);

        // undos
        moveExecutor.undoMove(board);
        moveExecutor.undoMove(positionState);
        moveExecutor.undoMove(colorBoard);
        moveExecutor.undoMove(moveCacheBoard);


        // asserts undos
        colorBoard.validar(board);
        positionState.validar(board);
        moveCacheBoard.validar(board);
    }
}
