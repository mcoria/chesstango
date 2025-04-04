package net.chesstango.board.internal.moves;

import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.moves.factories.MoveFactory;
import net.chesstango.board.internal.moves.factories.MoveFactoryWhite;
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
        moveFactory = new MoveFactoryWhite();
    }

    @Test
    public void testEquals01() {
        PiecePositioned origen = PiecePositioned.of(Square.e5, Piece.ROOK_WHITE);
        PiecePositioned destino = PiecePositioned.of(Square.e7, null);

        assertEquals(moveFactory.createSimpleKnightMove(origen, destino), moveFactory.createSimpleKnightMove(origen, destino));
    }

}
