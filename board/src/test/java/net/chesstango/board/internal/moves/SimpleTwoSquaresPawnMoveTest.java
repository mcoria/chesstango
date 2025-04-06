package net.chesstango.board.internal.moves;

import net.chesstango.board.*;
import net.chesstango.board.internal.GameImp;
import net.chesstango.board.internal.position.BitBoardDebug;
import net.chesstango.board.internal.position.MoveCacheBoardDebug;
import net.chesstango.board.internal.position.StateDebug;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.internal.moves.factories.MoveFactoryWhite;
import net.chesstango.board.moves.generators.legal.LegalMoveFilter;
import net.chesstango.board.moves.generators.pseudo.MoveGeneratorByPieceResult;
import net.chesstango.board.position.SquareBoard;
import net.chesstango.board.position.ZobristHash;
import net.chesstango.board.internal.position.PositionImp;
import net.chesstango.board.internal.position.SquareBoardImp;
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
public class SimpleTwoSquaresPawnMoveTest {

    private MoveFactoryWhite moveFactory;

    private MoveImp moveExecutor;
    private SquareBoard squareBoard;

    private StateDebug positionState;
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
        positionState = new StateDebug();
        positionState.setCurrentTurn(Color.WHITE);
        positionState.setHalfMoveClock(2);
        positionState.setFullMoveClock(5);

        squareBoard = new SquareBoardImp();
        squareBoard.setPiece(Square.e2, Piece.PAWN_WHITE);
        squareBoard.setPiece(Square.f4, Piece.PAWN_BLACK);

        bitBoard = new BitBoardDebug();
        bitBoard.init(squareBoard);

        PiecePositioned origen = squareBoard.getPosition(Square.e2);
        PiecePositioned destino = squareBoard.getPosition(Square.e4);
        PiecePositioned peonNegro = squareBoard.getPosition(Square.f4);

        moveCacheBoard = new MoveCacheBoardDebug();
        moveCacheBoard.setPseudoMoves(Square.e2, new MoveGeneratorByPieceResult(origen));
        MoveGeneratorByPieceResult mgr = new MoveGeneratorByPieceResult(peonNegro);
        mgr.addAffectedByPositions(Square.e3);
        moveCacheBoard.setPseudoMoves(Square.f4, mgr);

        zobristHash = new ZobristHashImp();
        zobristHash.init(squareBoard, positionState);

        chessPosition = new PositionImp();
        chessPosition.setSquareBoard(squareBoard);
        chessPosition.setState(positionState);
        chessPosition.setBitBoard(bitBoard);
        chessPosition.setMoveCache(moveCacheBoard);
        chessPosition.setZobristHash(zobristHash);

        moveFactory = new MoveFactoryWhite(gameImp);

        moveExecutor = moveFactory.createSimpleTwoSquaresPawnMove(origen, destino, Square.e3);
    }

    @Test
    public void testEquals() {
        assertEquals(moveFactory.createSimpleTwoSquaresPawnMove(squareBoard.getPosition(Square.e2), squareBoard.getPosition(Square.e4), Square.e3), moveExecutor);
    }

    @Test
    public void testGetDirection() {
        assertEquals(Cardinal.calculateSquaresDirection(moveExecutor.getFrom().getSquare(), moveExecutor.getTo().getSquare()), moveExecutor.getMoveDirection());
    }

    @Test
    public void testZobristHash() {
        when(gameImp.getPosition()).thenReturn(chessPosition);

        moveExecutor.doMove(squareBoard);
        moveExecutor.doMove(positionState);
        moveExecutor.doMove(zobristHash);

        assertEquals(PolyglotEncoder.getKey("8/8/8/8/4Pp2/8/8/8 b - e3 0 1").longValue(), zobristHash.getZobristHash());
    }

    @Test
    public void testZobristHashUndo() {
        when(gameImp.getPosition()).thenReturn(chessPosition);

        long initialHash = zobristHash.getZobristHash();

        moveExecutor.doMove(squareBoard);
        moveExecutor.doMove(positionState);
        moveExecutor.doMove(zobristHash);

        moveExecutor.undoMove(squareBoard);
        moveExecutor.undoMove(positionState);
        moveExecutor.undoMove(zobristHash);

        assertEquals(initialHash, zobristHash.getZobristHash());
    }

    @Test
    public void testPosicionPiezaBoard() {
        // execute
        moveExecutor.doMove(squareBoard);

        // asserts execute
        assertEquals(Piece.PAWN_WHITE, squareBoard.getPiece(Square.e4));
        assertTrue(squareBoard.isEmpty(Square.e2));

        // undos
        moveExecutor.undoMove(squareBoard);

        // asserts undos
        assertEquals(Piece.PAWN_WHITE, squareBoard.getPiece(Square.e2));
        assertTrue(squareBoard.isEmpty(Square.e4));
    }

    @Test
    public void testMoveState() {
        // execute
        moveExecutor.doMove(positionState);

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
        moveExecutor.doMove(bitBoard);

        // asserts execute
        assertTrue(bitBoard.isEmpty(Square.e2));
        assertEquals(Color.WHITE, bitBoard.getColor(Square.e4));

        // undos
        moveExecutor.undoMove(bitBoard);

        // asserts undos
        assertEquals(Color.WHITE, bitBoard.getColor(Square.e2));
        assertTrue(bitBoard.isEmpty(Square.e4));
    }

    @Test
    public void testMoveCacheBoard() {
        moveExecutor.doMove(moveCacheBoard);

        assertNull(moveCacheBoard.getPseudoMovesResult(Square.e2));
        assertNull(moveCacheBoard.getPseudoMovesResult(Square.f4));

        moveExecutor.undoMove(moveCacheBoard);

        assertNotNull(moveCacheBoard.getPseudoMovesResult(Square.e2));
        assertNotNull(moveCacheBoard.getPseudoMovesResult(Square.f4));
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
        moveExecutor.doMove(moveCacheBoard);

        // asserts execute
        bitBoard.validar(squareBoard);
        positionState.validar(squareBoard);
        moveCacheBoard.validar(squareBoard);

        // undos
        moveExecutor.undoMove(squareBoard);
        moveExecutor.undoMove(positionState);
        moveExecutor.undoMove(bitBoard);
        moveExecutor.undoMove(moveCacheBoard);


        // asserts undos
        bitBoard.validar(squareBoard);
        positionState.validar(squareBoard);
        moveCacheBoard.validar(squareBoard);
    }
}
