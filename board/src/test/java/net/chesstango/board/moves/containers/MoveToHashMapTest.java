package net.chesstango.board.moves.containers;

import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.factories.MoveFactory;
import net.chesstango.board.internal.moves.factories.MoveFactoryWhite;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Mauricio Coria
 */
public class MoveToHashMapTest {
    private MoveToHashMap moveToHashMap;

    private MoveFactory factory;

    @BeforeEach
    public void setUp() throws Exception {
        moveToHashMap = new MoveToHashMap();
        factory = new MoveFactoryWhite();
    }

    @Test
    public void test01() {
        Move move = factory.createSimpleKnightMove(PiecePositioned.of(Square.a2, Piece.PAWN_WHITE), PiecePositioned.of(Square.a3, null));

        moveToHashMap.write(move, 1000L);

        assertEquals(1000L, moveToHashMap.read(move));

        moveToHashMap.clear();

        assertEquals(0L, moveToHashMap.read(move));
    }

    @Test
    public void test02() {
        Move move1 = factory.createSimplePromotionPawnMove(PiecePositioned.of(Square.e7, Piece.PAWN_WHITE), PiecePositioned.of(Square.e8, null), Piece.QUEEN_WHITE);
        Move move2 = factory.createSimplePromotionPawnMove(PiecePositioned.of(Square.e7, Piece.PAWN_WHITE), PiecePositioned.of(Square.e8, null), Piece.KNIGHT_WHITE);

        moveToHashMap.write(move1, 1000L);
        moveToHashMap.write(move2, 2000L);

        assertEquals(1000L, moveToHashMap.read(move1));
        assertEquals(2000L, moveToHashMap.read(move2));

        moveToHashMap.clear();

        assertEquals(0L, moveToHashMap.read(move1));
        assertEquals(0L, moveToHashMap.read(move2));
    }
}
