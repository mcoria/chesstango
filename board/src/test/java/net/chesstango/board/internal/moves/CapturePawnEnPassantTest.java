package net.chesstango.board.internal.moves;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.internal.position.*;
import net.chesstango.board.internal.position.PositionStateDebug;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.internal.moves.factories.MoveFactoryWhite;
import net.chesstango.board.moves.generators.legal.LegalMoveFilter;
import net.chesstango.board.moves.generators.pseudo.MoveGeneratorByPieceResult;
import net.chesstango.board.position.Position;
import net.chesstango.board.position.SquareBoard;
import net.chesstango.board.position.ZobristHash;
import net.chesstango.gardel.fen.FEN;
import net.chesstango.gardel.polyglot.PolyglotKeyBuilder;
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

    private MoveFactoryWhite moveFactory;

    private SquareBoard squareBoard;

    private PositionStateDebug positionState;

    private MoveImp moveExecutor;

    private BitBoardDebug colorBoard;

    private MoveCacheBoardDebug moveCacheBoard;

    private ZobristHash zobristHash;

    @Mock
    private Position position;

    @Mock
    private LegalMoveFilter filter;

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
        moveCacheBoard.setPseudoMoves(Square.b5, new MoveGeneratorByPieceResult(pawnWhite));
        moveCacheBoard.setPseudoMoves(Square.a5, new MoveGeneratorByPieceResult(pawnBlack));

        zobristHash = new ZobristHashImp();
        zobristHash.init(squareBoard, positionState);

        moveFactory = new MoveFactoryWhite();

        moveExecutor = moveFactory.createCaptureEnPassantPawnMove(pawnWhite, pawnPasanteSquare, pawnBlack, Cardinal.NorteOeste);
    }

    @Test
    public void testEquals() {
        assertEquals(moveFactory.createCaptureEnPassantPawnMove(squareBoard.getPosition(Square.b5), squareBoard.getPosition(Square.a6), squareBoard.getPosition(Square.a5), Cardinal.NorteOeste), moveExecutor);
    }

    @Test
    public void testGetDirection() {
        assertEquals(Cardinal.calculateSquaresDirection(moveExecutor.getFrom().getSquare(), moveExecutor.getTo().getSquare()), moveExecutor.getMoveDirection());
    }

    @Test
    public void testZobristHash() {
        PositionImp chessPositionImp = new PositionImp();
        chessPositionImp.setZobristHash(zobristHash);
        chessPositionImp.setPositionState(positionState);

        moveExecutor.doMove(positionState);
        moveExecutor.doMove(zobristHash);

        assertEquals(getPolyglotKey("8/8/P7/8/8/8/8/8 b - - 0 1"), zobristHash.getZobristHash());
    }

    @Test
    public void testZobristHashUndo() {
        PositionImp chessPositionImp = new PositionImp();
        chessPositionImp.setZobristHash(zobristHash);
        chessPositionImp.setPositionState(positionState);

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
        moveExecutor.doMove(positionState);

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
        moveExecutor.doMove(colorBoard);

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
        moveExecutor.doMove(moveCacheBoard);

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
    public void testFilter() {
        // execute
        moveExecutor.isLegalMove(filter);

        // asserts execute
        verify(filter).isLegalMovePawn(moveExecutor, moveExecutor);
    }

    @Test
    public void testIntegrated() {
        // execute
        moveExecutor.doMove(squareBoard);
        moveExecutor.doMove(positionState);
        moveExecutor.doMove(colorBoard);
        moveExecutor.doMove(moveCacheBoard);

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

    private long getPolyglotKey(String fen){
        PolyglotKeyBuilder polyglotKeyBuilder = new PolyglotKeyBuilder();
        FEN.of(fen).export(polyglotKeyBuilder);
        return polyglotKeyBuilder.getPositionRepresentation();
    }
}
