package net.chesstango.board.internal.moves;

import net.chesstango.board.*;
import net.chesstango.board.internal.GameImp;
import net.chesstango.board.internal.position.*;
import net.chesstango.board.internal.position.PositionStateDebug;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.internal.moves.factories.MoveFactoryWhite;
import net.chesstango.board.moves.generators.legal.LegalMoveFilter;
import net.chesstango.board.moves.generators.pseudo.MoveGeneratorByPieceResult;
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
import static org.mockito.Mockito.when;


/**
 * @author Mauricio Coria
 */
@ExtendWith(MockitoExtension.class)
public class CapturePawnPromotionTest {
    private MoveFactoryWhite moveFactory;
    private MoveImp moveExecutor;
    private SquareBoard squareBoard;

    private PositionStateDebug positionState;
    private BitBoardDebug bitBoard;
    private MoveCacheBoardDebug moveCacheBoard;
    private ZobristHash zobristHash;

    @Mock
    private GameImp gameImp;

    @Mock
    private LegalMoveFilter filter;

    private PositionImp chessPosition;

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

        bitBoard = new BitBoardDebug();
        bitBoard.init(squareBoard);

        PiecePositioned origen = squareBoard.getPosition(Square.e7);
        PiecePositioned destino = squareBoard.getPosition(Square.f8);

        moveCacheBoard = new MoveCacheBoardDebug();
        moveCacheBoard.setPseudoMoves(Square.e7, new MoveGeneratorByPieceResult(origen));
        moveCacheBoard.setPseudoMoves(Square.f8, new MoveGeneratorByPieceResult(destino));

        zobristHash = new ZobristHashImp();
        zobristHash.init(squareBoard, positionState);

        chessPosition = new PositionImp();
        chessPosition.setSquareBoard(squareBoard);
        chessPosition.setPositionState(positionState);
        chessPosition.setBitBoard(bitBoard);
        chessPosition.setMoveCache(moveCacheBoard);
        chessPosition.setZobristHash(zobristHash);

        moveFactory = new MoveFactoryWhite(gameImp);

        moveExecutor = moveFactory.createCapturePromotionPawnMove(origen, destino, Piece.QUEEN_WHITE, Cardinal.NorteEste);
    }

    @Test
    public void testEquals() {
        assertEquals(moveFactory.createCapturePromotionPawnMove(squareBoard.getPosition(Square.e7), squareBoard.getPosition(Square.f8), Piece.QUEEN_WHITE, Cardinal.NorteEste), moveExecutor);
        assertNotEquals(moveFactory.createCapturePromotionPawnMove(squareBoard.getPosition(Square.e7), squareBoard.getPosition(Square.f8), Piece.ROOK_WHITE, Cardinal.NorteEste), moveExecutor);
    }

    @Test
    public void testGetDirection() {
        assertEquals(Cardinal.calculateSquaresDirection(moveExecutor.getFrom().getSquare(), moveExecutor.getTo().getSquare()), moveExecutor.getMoveDirection());
    }

    @Test
    public void testZobristHash() {
        when(gameImp.getPosition()).thenReturn(chessPosition);

        moveExecutor.doMove(positionState);
        moveExecutor.doMove(zobristHash);

        assertEquals(getPolyglotKey("5Q2/8/8/8/8/8/8/8 b - - 0 1"), zobristHash.getZobristHash());
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
        // execute
        moveExecutor.doMove(squareBoard);

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
        moveExecutor.doMove(positionState);

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
        moveExecutor.doMove(bitBoard);

        // asserts execute
        assertEquals(Color.WHITE, bitBoard.getColor(Square.f8));
        assertTrue(bitBoard.isEmpty(Square.e7));

        // undos
        moveExecutor.undoMove(bitBoard);

        // asserts undos
        assertEquals(Color.WHITE, bitBoard.getColor(Square.e7));
        assertEquals(Color.BLACK, bitBoard.getColor(Square.f8));
    }

    @Test
    public void testMoveCacheBoard() {
        moveExecutor.doMove(moveCacheBoard);

        assertNull(moveCacheBoard.getPseudoMovesResult(Square.e7));
        assertNull(moveCacheBoard.getPseudoMovesResult(Square.f8));

        moveExecutor.undoMove(moveCacheBoard);

        assertNotNull(moveCacheBoard.getPseudoMovesResult(Square.e7));
        assertNotNull(moveCacheBoard.getPseudoMovesResult(Square.f8));
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
        moveExecutor.doMove(bitBoard);

        // asserts execute
        assertEquals(Piece.QUEEN_WHITE, squareBoard.getPiece(Square.f8));
        assertTrue(squareBoard.isEmpty(Square.e7));

        assertNull(positionState.getEnPassantSquare());
        assertEquals(Color.BLACK, positionState.getCurrentTurn());
        assertEquals(0, positionState.getHalfMoveClock());
        assertEquals(5, positionState.getFullMoveClock());

        assertEquals(Color.WHITE, bitBoard.getColor(Square.f8));
        assertTrue(bitBoard.isEmpty(Square.e7));

        bitBoard.validar(squareBoard);
        positionState.validar(squareBoard);

        // undos
        moveExecutor.undoMove(squareBoard);
        moveExecutor.undoMove(positionState);
        moveExecutor.undoMove(bitBoard);


        // asserts undos
        assertEquals(Piece.PAWN_WHITE, squareBoard.getPiece(Square.e7));
        assertEquals(Piece.KNIGHT_BLACK, squareBoard.getPiece(Square.f8));

        assertNull(positionState.getEnPassantSquare());
        assertEquals(Color.WHITE, positionState.getCurrentTurn());
        assertEquals(3, positionState.getHalfMoveClock());
        assertEquals(5, positionState.getFullMoveClock());

        assertEquals(Color.WHITE, bitBoard.getColor(Square.e7));
        assertEquals(Color.BLACK, bitBoard.getColor(Square.f8));

        bitBoard.validar(squareBoard);
        positionState.validar(squareBoard);
    }

    private long getPolyglotKey(String fen){
        PolyglotKeyBuilder polyglotKeyBuilder = new PolyglotKeyBuilder();
        FEN.of(fen).export(polyglotKeyBuilder);
        return polyglotKeyBuilder.getPositionRepresentation();
    }
}
