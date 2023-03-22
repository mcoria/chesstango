package net.chesstango.board;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Mauricio Coria
 *
 */
public class PiecePositionedTest {

    @Test
    public void toStringTest(){
        PiecePositioned e5_ROOK_WHITE = PiecePositioned.getPiecePositioned(Square.e5, Piece.ROOK_WHITE);
        assertEquals("e5=ROOK_WHITE", e5_ROOK_WHITE.toString());
    }

    @Test
    public void toStringTest01(){
        PiecePositioned e5_Empty = PiecePositioned.getPosition(Square.e5);
        assertEquals("e5=null", e5_Empty.toString());
    }
}
