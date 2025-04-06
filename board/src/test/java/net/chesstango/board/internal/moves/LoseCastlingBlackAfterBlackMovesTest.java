package net.chesstango.board.internal.moves;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.factories.MoveFactory;
import net.chesstango.board.internal.moves.factories.MoveFactoryBlack;
import net.chesstango.board.position.State;
import net.chesstango.board.internal.position.StateImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Mauricio Coria
 */
public class LoseCastlingBlackAfterBlackMovesTest {

    private MoveFactory moveFactory;

    private MoveFactoryBlack moveFactoryImp;

    private MoveImp moveExecutor;

    private State state;

    @BeforeEach
    public void setUp() throws Exception {
        moveFactoryImp = new MoveFactoryBlack();
        state = new StateImp();
        moveExecutor = null;
    }

    @Test
    public void testSimpleKingMove() {
        state.setCurrentTurn(Color.BLACK);
        state.setCastlingWhiteKingAllowed(true);
        state.setCastlingWhiteQueenAllowed(true);
        state.setCastlingBlackKingAllowed(true);
        state.setCastlingBlackQueenAllowed(true);
        state.setHalfMoveClock(2);
        state.setFullMoveClock(5);

        PiecePositioned origen = PiecePositioned.of(Square.e8, Piece.KING_BLACK);
        PiecePositioned destino = PiecePositioned.of(Square.e7, null);

        moveExecutor = moveFactoryImp.createSimpleKingMove(origen, destino);

        moveExecutor.doMove(state);

        assertEquals(Color.WHITE, state.getCurrentTurn());
        assertTrue(state.isCastlingWhiteKingAllowed());
        assertTrue(state.isCastlingWhiteQueenAllowed());
        assertFalse(state.isCastlingBlackKingAllowed());
        assertFalse(state.isCastlingBlackQueenAllowed());
        assertEquals(3, state.getHalfMoveClock());
        assertEquals(6, state.getFullMoveClock());
    }

    @Test
    public void testCapturaKingMove() {
        state.setCurrentTurn(Color.BLACK);
        state.setCastlingWhiteKingAllowed(true);
        state.setCastlingWhiteQueenAllowed(true);
        state.setCastlingBlackKingAllowed(true);
        state.setCastlingBlackQueenAllowed(true);
        state.setHalfMoveClock(2);
        state.setFullMoveClock(5);

        PiecePositioned origen = PiecePositioned.of(Square.e8, Piece.KING_BLACK);
        PiecePositioned destino = PiecePositioned.of(Square.e7, Piece.KNIGHT_WHITE);

        moveExecutor = moveFactoryImp.createCaptureKingMove(origen, destino);

        moveExecutor.doMove(state);

        assertEquals(Color.WHITE, state.getCurrentTurn());
        assertTrue(state.isCastlingWhiteKingAllowed());
        assertTrue(state.isCastlingWhiteQueenAllowed());
        assertFalse(state.isCastlingBlackKingAllowed());
        assertFalse(state.isCastlingBlackQueenAllowed());
        assertEquals(0, state.getHalfMoveClock());
        assertEquals(6, state.getFullMoveClock());
    }

    @Test
    public void testSimpleRookMovePierdeEnroque() {
        state.setCurrentTurn(Color.BLACK);
        state.setCastlingWhiteKingAllowed(true);
        state.setCastlingWhiteQueenAllowed(true);
        state.setCastlingBlackKingAllowed(true);
        state.setCastlingBlackQueenAllowed(true);
        state.setHalfMoveClock(2);
        state.setFullMoveClock(5);


        PiecePositioned origen = PiecePositioned.of(Square.a8, Piece.ROOK_BLACK);
        PiecePositioned destino = PiecePositioned.of(Square.a7, null);

        moveExecutor = moveFactoryImp.createSimpleRookMove(origen, destino, Cardinal.Sur);

        moveExecutor.doMove(state);

        assertEquals(Color.WHITE, state.getCurrentTurn());
        assertTrue(state.isCastlingWhiteKingAllowed());
        assertTrue(state.isCastlingWhiteQueenAllowed());
        assertTrue(state.isCastlingBlackKingAllowed());
        assertFalse(state.isCastlingBlackQueenAllowed());
        assertEquals(3, state.getHalfMoveClock());
        assertEquals(6, state.getFullMoveClock());
    }

    @Test
    public void testCaptureRookMovePierdeEnroque() {
        state.setCurrentTurn(Color.BLACK);
        state.setCastlingWhiteKingAllowed(true);
        state.setCastlingWhiteQueenAllowed(true);
        state.setCastlingBlackKingAllowed(true);
        state.setCastlingBlackQueenAllowed(true);
        state.setHalfMoveClock(2);
        state.setFullMoveClock(5);


        PiecePositioned origen = PiecePositioned.of(Square.a8, Piece.ROOK_BLACK);
        PiecePositioned destino = PiecePositioned.of(Square.a7, Piece.PAWN_WHITE);

        moveExecutor = moveFactoryImp.createCaptureRookMove(origen, destino, Cardinal.Sur);

        moveExecutor.doMove(state);

        assertEquals(Color.WHITE, state.getCurrentTurn());
        assertTrue(state.isCastlingWhiteKingAllowed());
        assertTrue(state.isCastlingWhiteQueenAllowed());
        assertTrue(state.isCastlingBlackKingAllowed());
        assertFalse(state.isCastlingBlackQueenAllowed());
        assertEquals(0, state.getHalfMoveClock());
        assertEquals(6, state.getFullMoveClock());
    }

}
