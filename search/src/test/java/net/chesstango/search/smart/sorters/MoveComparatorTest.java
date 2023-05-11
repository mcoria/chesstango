package net.chesstango.search.smart.sorters;

import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.factory.SingletonMoveFactories;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MoveFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Mauricio Coria
 */
public class MoveComparatorTest {
    private MoveFactory moveFactoryWhite = SingletonMoveFactories.getDefaultMoveFactoryWhite();

    private MoveFactory moveFactoryBlack = SingletonMoveFactories.getDefaultMoveFactoryBlack();

    private MoveComparator moveComparator;

    @BeforeEach
    public void setUp() {
        moveComparator = new MoveComparator();
    }

    @Test
    public void testPawnMove() {
        Move move1 = moveFactoryWhite.createSimpleOneSquarePawnMove(PiecePositioned.getPiecePositioned(Square.a2, Piece.PAWN_WHITE), PiecePositioned.getPosition(Square.a3));
        Move move2 = moveFactoryWhite.createSimpleTwoSquaresPawnMove(PiecePositioned.getPiecePositioned(Square.a2, Piece.PAWN_WHITE), PiecePositioned.getPosition(Square.a4), Square.a3);

        assertTrue(moveComparator.compare(move1, move2) < 0);

        assertTrue(moveComparator.compare(move2, move1) > 0);
    }

    @Test
    public void testPawnAndKnightMove() {
        Move move1 = moveFactoryWhite.createSimpleMove(PiecePositioned.getPiecePositioned(Square.b1, Piece.KNIGHT_WHITE), PiecePositioned.getPosition(Square.a3));

        Move move2 = moveFactoryWhite.createSimpleTwoSquaresPawnMove(PiecePositioned.getPiecePositioned(Square.a2, Piece.PAWN_WHITE), PiecePositioned.getPosition(Square.a4), Square.a3);

        assertTrue(moveComparator.compare(move1, move2) > 0);

        assertTrue(moveComparator.compare(move2, move1) < 0);
    }

}
