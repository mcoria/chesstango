package net.chesstango.board.moves;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.factory.SingletonMoveFactories;
import net.chesstango.board.position.PiecePlacement;
import net.chesstango.board.position.imp.ArrayPiecePlacement;
import net.chesstango.board.position.imp.PositionState;
import net.chesstango.board.position.imp.ZobristHash;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

/**
 * @author Mauricio Coria
 */
public class LoseCastlingBlackAfterBlackMovesTest {

    private MoveFactory moveFactoryImp;

    private Move moveExecutor;

    private PositionState positionState;

    private PiecePlacement piecePlacement;

    private ZobristHash zobristHash;

    @Before
    public void setUp() throws Exception {
        moveFactoryImp = SingletonMoveFactories.getDefaultMoveFactoryBlack();
        positionState = new PositionState();
        piecePlacement = new ArrayPiecePlacement();
        zobristHash = new ZobristHash();
        moveExecutor = null;
    }


    @Test
    public void testSimpleKingMove() {
        positionState.setCurrentTurn(Color.BLACK);
        positionState.setCastlingWhiteKingAllowed(true);
        positionState.setCastlingWhiteQueenAllowed(true);
        positionState.setCastlingBlackKingAllowed(true);
        positionState.setCastlingBlackQueenAllowed(true);
        positionState.setHalfMoveClock(2);
        positionState.setFullMoveClock(5);

        PiecePositioned origen = PiecePositioned.getPiecePositioned(Square.e8, Piece.KING_BLACK);
        PiecePositioned destino = PiecePositioned.getPiecePositioned(Square.e7, null);

        moveExecutor = moveFactoryImp.createSimpleKingMove(origen, destino);

        moveExecutor.executeMove(positionState);

        assertEquals(Color.WHITE, positionState.getCurrentTurn());
        assertTrue(positionState.isCastlingWhiteKingAllowed());
        assertTrue(positionState.isCastlingWhiteQueenAllowed());
        assertFalse(positionState.isCastlingBlackKingAllowed());
        assertFalse(positionState.isCastlingBlackQueenAllowed());
        assertEquals(3, positionState.getHalfMoveClock());
        assertEquals(6, positionState.getFullMoveClock());
    }

    @Test
    public void testCapturaKingMove() {
        positionState.setCurrentTurn(Color.BLACK);
        positionState.setCastlingWhiteKingAllowed(true);
        positionState.setCastlingWhiteQueenAllowed(true);
        positionState.setCastlingBlackKingAllowed(true);
        positionState.setCastlingBlackQueenAllowed(true);
        positionState.setHalfMoveClock(2);
        positionState.setFullMoveClock(5);

        PiecePositioned origen = PiecePositioned.getPiecePositioned(Square.e8, Piece.KING_BLACK);
        PiecePositioned destino = PiecePositioned.getPiecePositioned(Square.e7, Piece.KNIGHT_WHITE);

        moveExecutor = moveFactoryImp.createCaptureKingMove(origen, destino);

        moveExecutor.executeMove(positionState);

        assertEquals(Color.WHITE, positionState.getCurrentTurn());
        assertTrue(positionState.isCastlingWhiteKingAllowed());
        assertTrue(positionState.isCastlingWhiteQueenAllowed());
        assertFalse(positionState.isCastlingBlackKingAllowed());
        assertFalse(positionState.isCastlingBlackQueenAllowed());
        assertEquals(0, positionState.getHalfMoveClock());
        assertEquals(6, positionState.getFullMoveClock());
    }
}
