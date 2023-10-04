package net.chesstango.search.smart.sorters;

import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.factory.SingletonMoveFactories;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MoveFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Mauricio Coria
 */
public class MoveComparatorTest {
    private final MoveFactory moveFactoryWhite = SingletonMoveFactories.getDefaultMoveFactoryWhite();

    private final MoveFactory moveFactoryBlack = SingletonMoveFactories.getDefaultMoveFactoryBlack();

    private MoveComparator moveComparator;

    @BeforeEach
    public void setUp() {
        moveComparator = new MoveComparator();
    }

    @Test
    public void testMoveByPiece() {
        Move moveQueen = moveFactoryWhite.createSimpleMove(PiecePositioned.getPiecePositioned(Square.e2, Piece.QUEEN_WHITE),
                PiecePositioned.getPosition(Square.e3));

        Move moveKnight = moveFactoryWhite.createSimpleMove(PiecePositioned.getPiecePositioned(Square.e2, Piece.KNIGHT_WHITE),
                PiecePositioned.getPosition(Square.e3));

        Move moveBishop = moveFactoryWhite.createSimpleMove(PiecePositioned.getPiecePositioned(Square.e2, Piece.BISHOP_WHITE),
                PiecePositioned.getPosition(Square.e3));

        Move moveRook = moveFactoryWhite.createSimpleMove(PiecePositioned.getPiecePositioned(Square.e2, Piece.ROOK_WHITE),
                PiecePositioned.getPosition(Square.e3));

        Move movePawn = moveFactoryWhite.createSimpleOneSquarePawnMove(PiecePositioned.getPiecePositioned(Square.e2, Piece.PAWN_WHITE),
                PiecePositioned.getPosition(Square.e3));

        Move moveKing = moveFactoryWhite.createSimpleMove(PiecePositioned.getPiecePositioned(Square.e2, Piece.KING_WHITE),
                PiecePositioned.getPosition(Square.e3));

        assertTrue(moveComparator.compare(moveQueen, moveKnight) > 0);
        assertTrue(moveComparator.compare(moveQueen, moveBishop) > 0);
        assertTrue(moveComparator.compare(moveQueen, moveRook) > 0);
        assertTrue(moveComparator.compare(moveQueen, movePawn) > 0);
        assertTrue(moveComparator.compare(moveQueen, moveKing) > 0);

        assertTrue(moveComparator.compare(moveKnight, moveBishop) > 0);
        assertTrue(moveComparator.compare(moveKnight, moveRook) > 0);
        assertTrue(moveComparator.compare(moveKnight, movePawn) > 0);
        assertTrue(moveComparator.compare(moveKnight, moveKing) > 0);

        assertTrue(moveComparator.compare(moveBishop, moveRook) > 0);
        assertTrue(moveComparator.compare(moveBishop, movePawn) > 0);
        assertTrue(moveComparator.compare(moveBishop, moveKing) > 0);

        assertTrue(moveComparator.compare(moveRook, movePawn) > 0);
        assertTrue(moveComparator.compare(moveRook, moveKing) > 0);

        assertTrue(moveComparator.compare(movePawn, moveKing) > 0);
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

    @Test
    public void testKnightMove() {
        Move move1 = moveFactoryWhite.createSimpleMove(PiecePositioned.getPiecePositioned(Square.g1, Piece.KNIGHT_WHITE), PiecePositioned.getPosition(Square.f3));

        Move move2 = moveFactoryWhite.createCaptureMove(PiecePositioned.getPiecePositioned(Square.g1, Piece.KNIGHT_WHITE), PiecePositioned.getPosition(Square.h3));


        assertTrue(moveComparator.compare(move1, move2) < 0);

        assertTrue(moveComparator.compare(move2, move1) > 0);
    }

    @Test
    public void testCapture01() {
        Move move1 = moveFactoryWhite.createCapturePawnMove(PiecePositioned.getPiecePositioned(Square.e4, Piece.PAWN_WHITE), PiecePositioned.getPiecePositioned(Square.f5, Piece.QUEEN_BLACK), Cardinal.NorteEste);

        Move move2 = moveFactoryWhite.createCaptureMove(PiecePositioned.getPiecePositioned(Square.h4, Piece.KNIGHT_WHITE), PiecePositioned.getPiecePositioned(Square.f5, Piece.QUEEN_BLACK));

        assertTrue(moveComparator.compare(move1, move2) > 0);

        assertTrue(moveComparator.compare(move2, move1) < 0);
    }


    @Test
    public void testCapture01_Black() {
        Move move1 = moveFactoryBlack.createCapturePawnMove(PiecePositioned.getPiecePositioned(Square.e5, Piece.PAWN_BLACK), PiecePositioned.getPiecePositioned(Square.f4, Piece.QUEEN_WHITE), Cardinal.SurEste);

        Move move2 = moveFactoryBlack.createCaptureMove(PiecePositioned.getPiecePositioned(Square.h5, Piece.KNIGHT_BLACK), PiecePositioned.getPiecePositioned(Square.f4, Piece.QUEEN_WHITE));

        assertTrue(moveComparator.compare(move1, move2) > 0);

        assertTrue(moveComparator.compare(move2, move1) < 0);
    }


}
