package net.chesstango.board.moves.imp;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.debug.chess.ColorBoardDebug;
import net.chesstango.board.moves.Move;
import net.chesstango.board.movesgenerators.legal.MoveFilter;
import net.chesstango.board.position.ChessPosition;
import net.chesstango.board.position.PiecePlacement;
import net.chesstango.board.position.PositionStateReader;
import net.chesstango.board.position.imp.ArrayPiecePlacement;
import net.chesstango.board.position.imp.ColorBoard;
import net.chesstango.board.position.imp.PositionState;
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
public class SimplePawnMoveTest {

    private PiecePlacement piecePlacement;

    private PositionState positionState;

    private Move moveExecutor;

    private ColorBoard colorBoard;

    private ZobristHash zobristHash;

    @Mock
    private ChessPosition chessPosition;

    @Mock
    private MoveFilter filter;

    @Before
    public void setUp() throws Exception {
        positionState = new PositionState();
        positionState.setCurrentTurn(Color.WHITE);
        positionState.setHalfMoveClock(2);
        positionState.setFullMoveClock(5);

        piecePlacement = new ArrayPiecePlacement();
        piecePlacement.setPieza(Square.e2, Piece.PAWN_WHITE);

        colorBoard = new ColorBoardDebug();
        colorBoard.init(piecePlacement);

        zobristHash = new ZobristHash();
        zobristHash.init(piecePlacement, positionState);

        PiecePositioned origen = piecePlacement.getPosicion(Square.e2);
        PiecePositioned destino = piecePlacement.getPosicion(Square.e3);
        moveExecutor = new SimplePawnMove(origen, destino);
    }

    @Test
    public void testZobristHash() {
        PositionStateReader oldPositionState = positionState.getCurrentState();
        moveExecutor.executeMove(positionState);
        moveExecutor.executeMove(zobristHash, oldPositionState, positionState, null);

        Assert.assertEquals(PolyglotEncoder.getKey("8/8/8/8/8/4P3/8/8 b - - 0 1").longValue(), zobristHash.getZobristHash());
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
        moveExecutor.executeMove(piecePlacement);

        // asserts execute
        assertEquals(Piece.PAWN_WHITE, piecePlacement.getPiece(Square.e3));
        assertTrue(piecePlacement.isEmpty(Square.e2));

        // undos
        moveExecutor.undoMove(piecePlacement);

        // asserts undos
        assertEquals(Piece.PAWN_WHITE, piecePlacement.getPiece(Square.e2));
        assertTrue(piecePlacement.isEmpty(Square.e3));
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
        assertEquals(2, positionState.getHalfMoveClock());
        assertEquals(5, positionState.getFullMoveClock());
    }

    @Test
    public void testColorBoard() {
        // execute
        moveExecutor.executeMove(colorBoard);

        // asserts execute
        assertEquals(Color.WHITE, colorBoard.getColor(Square.e3));
        assertTrue(colorBoard.isEmpty(Square.e2));

        // undos
        moveExecutor.undoMove(colorBoard);

        // asserts undos
        assertEquals(Color.WHITE, colorBoard.getColor(Square.e2));
        assertTrue(colorBoard.isEmpty(Square.e3));
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
}