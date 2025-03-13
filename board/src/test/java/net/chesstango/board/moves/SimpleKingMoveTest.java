package net.chesstango.board.moves;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.debug.chess.BitBoardDebug;
import net.chesstango.board.debug.chess.KingSquareDebug;
import net.chesstango.board.debug.chess.MoveCacheBoardDebug;
import net.chesstango.board.debug.chess.PositionStateDebug;
import net.chesstango.board.factory.SingletonMoveFactories;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.generators.legal.LegalMoveFilter;
import net.chesstango.board.moves.generators.pseudo.MoveGeneratorResult;
import net.chesstango.board.moves.imp.MoveKingImp;
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
public class SimpleKingMoveTest {

    private MoveKingImp moveExecutor;
    private SquareBoard squareBoard;

    private PositionStateDebug positionState;
    private BitBoardDebug colorBoard;
    private KingSquareDebug kingCacheBoard;
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
        positionState.setCastlingWhiteKingAllowed(true);
        positionState.setCastlingWhiteQueenAllowed(true);
        positionState.setHalfMoveClock(2);
        positionState.setFullMoveClock(5);

        squareBoard = new SquareBoardImp();
        squareBoard.setPiece(Square.e1, Piece.KING_WHITE);

        colorBoard = new BitBoardDebug();
        colorBoard.init(squareBoard);

        kingCacheBoard = new KingSquareDebug();
        kingCacheBoard.init(squareBoard);

        PiecePositioned origen = squareBoard.getPosition(Square.e1);
        PiecePositioned destino = squareBoard.getPosition(Square.e2);

        moveCacheBoard = new MoveCacheBoardDebug();
        moveCacheBoard.setPseudoMoves(Square.e1, new MoveGeneratorResult(origen));

        zobristHash = new ZobristHashImp();
        zobristHash.init(squareBoard, positionState);

        moveExecutor = SingletonMoveFactories.getDefaultMoveFactoryWhite().createSimpleKingMove(origen, destino);
    }

    @Test
    public void testEquals() {
        assertEquals(SingletonMoveFactories.getDefaultMoveFactoryWhite().createSimpleKingMove(squareBoard.getPosition(Square.e1), squareBoard.getPosition(Square.e2)), moveExecutor);
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

        assertEquals(PolyglotEncoder.getKey("8/8/8/8/8/8/4K3/8 b - - 0 1").longValue(), zobristHash.getZobristHash());
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
        assertEquals(Piece.KING_WHITE, squareBoard.getPiece(Square.e2));
        assertNull(squareBoard.getPiece(Square.e1));

        // undos
        moveExecutor.undoMove(squareBoard);

        // asserts undos
        assertEquals(Piece.KING_WHITE, squareBoard.getPiece(Square.e1));
        assertTrue(squareBoard.isEmpty(Square.e2));
    }

    @Test
    public void testPositionState() {
        moveExecutor.doMove(positionState);

        assertEquals(Color.BLACK, positionState.getCurrentTurn());
        assertEquals(3, positionState.getHalfMoveClock());
        assertEquals(5, positionState.getFullMoveClock());
        assertEquals(false, positionState.isCastlingWhiteKingAllowed());
        assertEquals(false, positionState.isCastlingWhiteQueenAllowed());

        moveExecutor.undoMove(positionState);

        assertEquals(Color.WHITE, positionState.getCurrentTurn());
        assertEquals(2, positionState.getHalfMoveClock());
        assertEquals(5, positionState.getFullMoveClock());
        assertEquals(true, positionState.isCastlingWhiteKingAllowed());
        assertEquals(true, positionState.isCastlingWhiteQueenAllowed());
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
        moveExecutor.doMove(colorBoard);

        // asserts execute
        assertEquals(Color.WHITE, colorBoard.getColor(Square.e2));
        assertTrue(colorBoard.isEmpty(Square.e1));

        // undos
        moveExecutor.undoMove(colorBoard);


        // asserts undos
        assertEquals(Color.WHITE, colorBoard.getColor(Square.e1));
        assertTrue(colorBoard.isEmpty(Square.e2));
    }

    @Test
    public void testMoveCacheBoard() {
        moveExecutor.doMove(moveCacheBoard);

        assertNull(moveCacheBoard.getPseudoMovesResult(Square.e1));

        moveExecutor.undoMove(moveCacheBoard);

        assertNotNull(moveCacheBoard.getPseudoMovesResult(Square.e1));
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
        moveExecutor.doMove(colorBoard);
        moveExecutor.doMove(moveCacheBoard);

        // asserts execute
        assertEquals(Piece.KING_WHITE, squareBoard.getPiece(Square.e2));
        assertNull(squareBoard.getPiece(Square.e1));

        assertEquals(Square.e2, kingCacheBoard.getKingSquareWhite());

        assertEquals(Color.BLACK, positionState.getCurrentTurn());

        assertEquals(Color.WHITE, colorBoard.getColor(Square.e2));
        assertTrue(colorBoard.isEmpty(Square.e1));

        colorBoard.validar(squareBoard);
        kingCacheBoard.validar(squareBoard);
        moveCacheBoard.validar(squareBoard);

        // undos
        moveExecutor.undoMove(squareBoard);
        moveExecutor.undoMove(kingCacheBoard);
        moveExecutor.undoMove(positionState);
        moveExecutor.undoMove(colorBoard);
        moveExecutor.undoMove(moveCacheBoard);


        // asserts undos
        assertEquals(Piece.KING_WHITE, squareBoard.getPiece(Square.e1));
        assertTrue(squareBoard.isEmpty(Square.e2));

        assertEquals(Square.e1, kingCacheBoard.getKingSquareWhite());

        assertEquals(Color.WHITE, positionState.getCurrentTurn());

        assertEquals(Color.WHITE, colorBoard.getColor(Square.e1));
        assertTrue(colorBoard.isEmpty(Square.e2));

        colorBoard.validar(squareBoard);
        kingCacheBoard.validar(squareBoard);
        moveCacheBoard.validar(squareBoard);
    }
}
