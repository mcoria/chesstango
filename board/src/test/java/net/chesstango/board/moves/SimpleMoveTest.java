package net.chesstango.board.moves;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.debug.chess.BitBoardDebug;
import net.chesstango.board.debug.chess.MoveCacheBoardDebug;
import net.chesstango.board.debug.chess.PositionStateDebug;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.factories.MoveFactory;
import net.chesstango.board.moves.factories.imp.MoveFactoryWhite;
import net.chesstango.board.moves.generators.legal.LegalMoveFilter;
import net.chesstango.board.moves.generators.pseudo.MoveGeneratorResult;
import net.chesstango.board.moves.imp.MoveImp;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;


/**
 * @author Mauricio Coria
 */
@ExtendWith(MockitoExtension.class)
public class SimpleMoveTest {

    private MoveFactory moveFactory;

    private MoveImp moveExecutor;
    private SquareBoard squareBoard;

    private PositionStateDebug positionState;
    private BitBoardDebug colorBoard;
    private MoveCacheBoardDebug moveCacheBoard;
    private ZobristHash zobristHash;

    @Mock
    private ChessPosition chessPosition;

    @Mock
    private LegalMoveFilter filter;

    @BeforeEach
    public void setUp() throws Exception {
        positionState = new PositionStateDebug();
        positionState.setCurrentTurn(Color.WHITE);
        positionState.setHalfMoveClock(2);
        positionState.setFullMoveClock(5);

        squareBoard = new SquareBoardImp();
        squareBoard.setPiece(Square.e5, Piece.ROOK_WHITE);

        colorBoard = new BitBoardDebug();
        colorBoard.init(squareBoard);

        PiecePositioned origen = squareBoard.getPosition(Square.e5);
        PiecePositioned destino = squareBoard.getPosition(Square.e7);

        moveCacheBoard = new MoveCacheBoardDebug();
        moveCacheBoard.setPseudoMoves(Square.e5, new MoveGeneratorResult(origen));

        zobristHash = new ZobristHashImp();
        zobristHash.init(squareBoard, positionState);

        moveFactory = new MoveFactoryWhite();

        moveExecutor = moveFactory.createSimpleKnightMove(origen, destino);
    }

    @Test
    public void testBinaryEncoding() {
        short expected = (short)  (Square.e5.getRank() << 9  | Square.e5.getFile()  << 6| Square.e7.getRank() << 3  | Square.e7.getFile());
        assertEquals(expected, moveExecutor.binaryEncoding());
    }

    @Test
    public void testEquals() {
        assertEquals(moveFactory.createSimpleKnightMove(squareBoard.getPosition(Square.e5), squareBoard.getPosition(Square.e7)), moveExecutor);
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

        moveExecutor.doMove(squareBoard);
        moveExecutor.doMove(positionState);
        moveExecutor.doMove(zobristHash, chessPositionImp);

        assertEquals(PolyglotEncoder.getKey("8/4R3/8/8/8/8/8/8 b - - 0 1").longValue(), zobristHash.getZobristHash());
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
        // execute
        moveExecutor.doMove(squareBoard);

        // asserts execute
        assertEquals(Piece.ROOK_WHITE, squareBoard.getPiece(Square.e7));
        assertTrue(squareBoard.isEmpty(Square.e5));

        // undos
        moveExecutor.undoMove(squareBoard);

        // asserts undos
        assertEquals(Piece.ROOK_WHITE, squareBoard.getPiece(Square.e5));
        assertTrue(squareBoard.isEmpty(Square.e7));
    }

    @Test
    public void testMoveState() {
        // execute
        moveExecutor.doMove(positionState);

        // asserts execute
        assertNull(positionState.getEnPassantSquare());
        assertEquals(Color.BLACK, positionState.getCurrentTurn());
        assertEquals(3, positionState.getHalfMoveClock());
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
        moveExecutor.doMove(colorBoard);

        // asserts execute
        assertEquals(Color.WHITE, colorBoard.getColor(Square.e7));
        assertTrue(colorBoard.isEmpty(Square.e5));

        // undos
        moveExecutor.undoMove(colorBoard);

        // asserts undos
        assertEquals(Color.WHITE, colorBoard.getColor(Square.e5));
        assertTrue(colorBoard.isEmpty(Square.e7));
    }

    @Test
    public void testMoveCacheBoard() {
        moveExecutor.doMove(moveCacheBoard);

        assertNull(moveCacheBoard.getPseudoMovesResult(Square.e5));

        moveExecutor.undoMove(moveCacheBoard);

        assertNotNull(moveCacheBoard.getPseudoMovesResult(Square.e5));
    }

    @Test
    public void testFilter() {
        // execute
        moveExecutor.isLegalMove(filter);

        // asserts execute
        verify(filter).isLegalMove(moveExecutor);
    }

    @Test
    public void testIntegrated() {
        // execute
        moveExecutor.doMove(squareBoard);
        moveExecutor.doMove(positionState);
        moveExecutor.doMove(colorBoard);
        moveExecutor.doMove(moveCacheBoard);

        // asserts execute
        colorBoard.validar(squareBoard);
        positionState.validar(squareBoard);
        moveCacheBoard.validar(squareBoard);

        // undos
        moveExecutor.undoMove(squareBoard);
        moveExecutor.undoMove(positionState);
        moveExecutor.undoMove(colorBoard);
        moveExecutor.undoMove(moveCacheBoard);


        // asserts undos
        colorBoard.validar(squareBoard);
        positionState.validar(squareBoard);
        moveCacheBoard.validar(squareBoard);
    }
}
