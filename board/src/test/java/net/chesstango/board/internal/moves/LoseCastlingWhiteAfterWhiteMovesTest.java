package net.chesstango.board.internal.moves;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.internal.moves.factories.MoveFactoryWhite;
import net.chesstango.board.position.State;
import net.chesstango.board.internal.position.StateImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Mauricio Coria
 */
public class LoseCastlingWhiteAfterWhiteMovesTest {

    private MoveFactoryWhite moveFactoryImp;

    private MoveImp moveExecutor;

    private State state;

    @BeforeEach
    public void setUp() throws Exception {
        moveFactoryImp = new MoveFactoryWhite();
        state = new StateImp();
        moveExecutor = null;
    }

    @Test
    public void testSimpleKingMovePierdeEnroque() {
        state.setCurrentTurn(Color.WHITE);
        state.setCastlingWhiteKingAllowed(true);
        state.setCastlingWhiteQueenAllowed(true);
        state.setCastlingBlackKingAllowed(true);
        state.setCastlingBlackQueenAllowed(true);
        state.setHalfMoveClock(2);
        state.setFullMoveClock(5);

        PiecePositioned origen = PiecePositioned.of(Square.e1, Piece.KING_WHITE);
        PiecePositioned destino = PiecePositioned.of(Square.e2, null);

        moveExecutor = moveFactoryImp.createSimpleKingMove(origen, destino);

        moveExecutor.doMove(state);

        assertEquals(Color.BLACK, state.getCurrentTurn());
        assertFalse(state.isCastlingWhiteKingAllowed());
        assertFalse(state.isCastlingWhiteQueenAllowed());
        assertTrue(state.isCastlingBlackKingAllowed());
        assertTrue(state.isCastlingBlackQueenAllowed());
        assertEquals(3, state.getHalfMoveClock());
        assertEquals(5, state.getFullMoveClock());
    }

    @Test
    public void testCapturaKingMovePierdeEnroque() {
        state.setCurrentTurn(Color.WHITE);
        state.setCastlingWhiteKingAllowed(true);
        state.setCastlingWhiteQueenAllowed(true);
        state.setCastlingBlackKingAllowed(true);
        state.setCastlingBlackQueenAllowed(true);
        state.setHalfMoveClock(2);
        state.setFullMoveClock(5);

        PiecePositioned origen = PiecePositioned.of(Square.e1, Piece.KING_WHITE);
        PiecePositioned destino = PiecePositioned.of(Square.e2, Piece.KNIGHT_BLACK);

        moveExecutor = moveFactoryImp.createCaptureKingMove(origen, destino);

        moveExecutor.doMove(state);

        assertEquals(Color.BLACK, state.getCurrentTurn());
        assertFalse(state.isCastlingWhiteKingAllowed());
        assertFalse(state.isCastlingWhiteQueenAllowed());
        assertTrue(state.isCastlingBlackKingAllowed());
        assertTrue(state.isCastlingBlackQueenAllowed());
        assertEquals(0, state.getHalfMoveClock());
        assertEquals(5, state.getFullMoveClock());
    }

    @Test
    public void testSimpleRookMovePierdeEnroque() {
        state.setCurrentTurn(Color.WHITE);
        state.setCastlingWhiteKingAllowed(true);
        state.setCastlingWhiteQueenAllowed(true);
        state.setCastlingBlackKingAllowed(true);
        state.setCastlingBlackQueenAllowed(true);
        state.setHalfMoveClock(2);
        state.setFullMoveClock(5);


        PiecePositioned origen = PiecePositioned.of(Square.a1, Piece.ROOK_WHITE);
        PiecePositioned destino = PiecePositioned.of(Square.a2, null);

        moveExecutor = moveFactoryImp.createSimpleRookMove(origen, destino, Cardinal.Norte);

        moveExecutor.doMove(state);

        assertEquals(Color.BLACK, state.getCurrentTurn());
        assertTrue(state.isCastlingWhiteKingAllowed());
        assertFalse(state.isCastlingWhiteQueenAllowed());
        assertTrue(state.isCastlingBlackKingAllowed());
        assertTrue(state.isCastlingBlackQueenAllowed());
        assertEquals(3, state.getHalfMoveClock());
        assertEquals(5, state.getFullMoveClock());
    }

    @Test
    public void testCaptureRookMovePierdeEnroque() {
        state.setCurrentTurn(Color.WHITE);
        state.setCastlingWhiteKingAllowed(true);
        state.setCastlingWhiteQueenAllowed(true);
        state.setCastlingBlackKingAllowed(true);
        state.setCastlingBlackQueenAllowed(true);
        state.setHalfMoveClock(2);
        state.setFullMoveClock(5);


        PiecePositioned origen = PiecePositioned.of(Square.a1, Piece.ROOK_WHITE);
        PiecePositioned destino = PiecePositioned.of(Square.a2, Piece.PAWN_BLACK);

        moveExecutor = moveFactoryImp.createCaptureRookMove(origen, destino, Cardinal.Norte);

        moveExecutor.doMove(state);

        assertEquals(Color.BLACK, state.getCurrentTurn());
        assertTrue(state.isCastlingWhiteKingAllowed());
        assertFalse(state.isCastlingWhiteQueenAllowed());
        assertTrue(state.isCastlingBlackKingAllowed());
        assertTrue(state.isCastlingBlackQueenAllowed());
        assertEquals(0, state.getHalfMoveClock());
        assertEquals(5, state.getFullMoveClock());
    }

}
