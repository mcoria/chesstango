package net.chesstango.board.moves;

import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.factory.SingletonMoveFactories;
import net.chesstango.board.moves.factories.MoveFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * @author Mauricio Coria
 */
public class AbstractMoveTest {

    private MoveFactory moveFactory;

    @BeforeEach
    public void setup() {
        moveFactory = SingletonMoveFactories.getDefaultMoveFactoryWhite();
    }

    @Test
    public void testEquals01() {
        PiecePositioned origen = PiecePositioned.getPiecePositioned(Square.e5, Piece.ROOK_WHITE);
        PiecePositioned destino = PiecePositioned.getPiecePositioned(Square.e7, null);

        assertEquals(moveFactory.createSimpleKnightMove(origen, destino), moveFactory.createSimpleKnightMove(origen, destino));
    }


    @Test
    public void testCompare01() {
        PiecePositioned a2 = PiecePositioned.getPiecePositioned(Square.a2, Piece.PAWN_WHITE);
        PiecePositioned a3 = PiecePositioned.getPiecePositioned(Square.a3, null);
        PiecePositioned a4 = PiecePositioned.getPiecePositioned(Square.a4, Piece.ROOK_WHITE);
        PiecePositioned b1 = PiecePositioned.getPiecePositioned(Square.b1, Piece.QUEEN_WHITE);
        PiecePositioned b2 = PiecePositioned.getPiecePositioned(Square.b2, Piece.PAWN_WHITE);
        PiecePositioned b3 = PiecePositioned.getPiecePositioned(Square.b3, null);


        Move move1 = moveFactory.createSimpleKnightMove(a2, a3);
        Move move2 = moveFactory.createSimpleKnightMove(a2, a4);
        Move move3 = moveFactory.createSimpleKnightMove(b2, b3);
        Move move4 = moveFactory.createSimpleKnightMove(b1, a3);

        assertTrue(move1.compareTo(move2) > 0);
        assertTrue(move1.compareTo(move3) > 0);
        assertTrue(move1.compareTo(move4) > 0);

        assertTrue(move2.compareTo(move3) > 0);

        assertTrue(move3.compareTo(move4) > 0);
    }

}
