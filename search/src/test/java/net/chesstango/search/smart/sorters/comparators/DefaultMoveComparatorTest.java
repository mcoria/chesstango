package net.chesstango.search.smart.sorters.comparators;

import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.Move;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Mauricio Coria
 */
public class DefaultMoveComparatorTest {

    private DefaultMoveComparator defaultMoveComparator;

    @BeforeEach
    public void setUp() {
        defaultMoveComparator = new DefaultMoveComparator();
    }

    @Test
    public void testMoveByPiece() {
        Move moveQueen = createSimpleKnightMove(PiecePositioned.of(Square.e2, Piece.QUEEN_WHITE),
                PiecePositioned.getPosition(Square.e3));

        Move moveKnight = createSimpleKnightMove(PiecePositioned.of(Square.e2, Piece.KNIGHT_WHITE),
                PiecePositioned.getPosition(Square.e3));

        Move moveBishop = createSimpleKnightMove(PiecePositioned.of(Square.e2, Piece.BISHOP_WHITE),
                PiecePositioned.getPosition(Square.e3));

        Move moveRook = createSimpleKnightMove(PiecePositioned.of(Square.e2, Piece.ROOK_WHITE),
                PiecePositioned.getPosition(Square.e3));

        Move movePawn = createSimpleOneSquarePawnMove(PiecePositioned.of(Square.e2, Piece.PAWN_WHITE),
                PiecePositioned.getPosition(Square.e3));

        Move moveKing = createSimpleKnightMove(PiecePositioned.of(Square.e2, Piece.KING_WHITE),
                PiecePositioned.getPosition(Square.e3));

        assertTrue(defaultMoveComparator.compare(moveQueen, moveKnight) > 0);
        assertTrue(defaultMoveComparator.compare(moveQueen, moveBishop) > 0);
        assertTrue(defaultMoveComparator.compare(moveQueen, moveRook) > 0);
        assertTrue(defaultMoveComparator.compare(moveQueen, movePawn) > 0);
        assertTrue(defaultMoveComparator.compare(moveQueen, moveKing) > 0);

        assertTrue(defaultMoveComparator.compare(moveKnight, moveBishop) > 0);
        assertTrue(defaultMoveComparator.compare(moveKnight, moveRook) > 0);
        assertTrue(defaultMoveComparator.compare(moveKnight, movePawn) > 0);
        assertTrue(defaultMoveComparator.compare(moveKnight, moveKing) > 0);

        assertTrue(defaultMoveComparator.compare(moveBishop, moveRook) > 0);
        assertTrue(defaultMoveComparator.compare(moveBishop, movePawn) > 0);
        assertTrue(defaultMoveComparator.compare(moveBishop, moveKing) > 0);

        assertTrue(defaultMoveComparator.compare(moveRook, movePawn) > 0);
        assertTrue(defaultMoveComparator.compare(moveRook, moveKing) > 0);

        assertTrue(defaultMoveComparator.compare(movePawn, moveKing) > 0);
    }



    @Test
    public void testPawnMove() {
        Move move1 = createSimpleOneSquarePawnMove(PiecePositioned.of(Square.a2, Piece.PAWN_WHITE), PiecePositioned.getPosition(Square.a3));
        Move move2 = createSimpleTwoSquaresPawnMove(PiecePositioned.of(Square.a2, Piece.PAWN_WHITE), PiecePositioned.getPosition(Square.a4), Square.a3);

        assertTrue(defaultMoveComparator.compare(move1, move2) < 0);

        assertTrue(defaultMoveComparator.compare(move2, move1) > 0);
    }


    @Test
    public void testPawnAndKnightMove() {
        Move move1 = createSimpleKnightMove(PiecePositioned.of(Square.b1, Piece.KNIGHT_WHITE), PiecePositioned.getPosition(Square.a3));

        Move move2 = createSimpleTwoSquaresPawnMove(PiecePositioned.of(Square.a2, Piece.PAWN_WHITE), PiecePositioned.getPosition(Square.a4), Square.a3);

        assertTrue(defaultMoveComparator.compare(move1, move2) > 0);

        assertTrue(defaultMoveComparator.compare(move2, move1) < 0);
    }

    @Test
    public void testKnightMove() {
        Move move1 = createSimpleKnightMove(PiecePositioned.of(Square.g1, Piece.KNIGHT_WHITE), PiecePositioned.getPosition(Square.f3));

        Move move2 = createCaptureKnightMove(PiecePositioned.of(Square.g1, Piece.KNIGHT_WHITE), PiecePositioned.getPosition(Square.h3));


        assertTrue(defaultMoveComparator.compare(move1, move2) < 0);

        assertTrue(defaultMoveComparator.compare(move2, move1) > 0);
    }

    @Test
    public void testCapture01() {
        Move move1 = createCapturePawnMove(PiecePositioned.of(Square.e4, Piece.PAWN_WHITE), PiecePositioned.of(Square.f5, Piece.QUEEN_BLACK), Cardinal.NorteEste);

        Move move2 = createCaptureKnightMove(PiecePositioned.of(Square.h4, Piece.KNIGHT_WHITE), PiecePositioned.of(Square.f5, Piece.QUEEN_BLACK));

        assertTrue(defaultMoveComparator.compare(move1, move2) > 0);

        assertTrue(defaultMoveComparator.compare(move2, move1) < 0);
    }


    @Test
    public void testCapture01_Black() {
        Move move1 = createCapturePawnMove(PiecePositioned.of(Square.e5, Piece.PAWN_BLACK), PiecePositioned.of(Square.f4, Piece.QUEEN_WHITE), Cardinal.SurEste);

        Move move2 = createCaptureKnightMove(PiecePositioned.of(Square.h5, Piece.KNIGHT_BLACK), PiecePositioned.of(Square.f4, Piece.QUEEN_WHITE));

        assertTrue(defaultMoveComparator.compare(move1, move2) > 0);

        assertTrue(defaultMoveComparator.compare(move2, move1) < 0);
    }

    private Move createSimpleKnightMove(PiecePositioned from, PiecePositioned to) {
        return createMove(from, to);
    }

    private Move createSimpleOneSquarePawnMove(PiecePositioned from, PiecePositioned to) {
        return createMove(from, to);
    }

    private Move createSimpleTwoSquaresPawnMove(PiecePositioned from, PiecePositioned to, Square square) {
        return createMove(from, to);
    }

    private Move createCaptureKnightMove(PiecePositioned from, PiecePositioned to) {
        return createMove(from, to);
    }

    private Move createCapturePawnMove(PiecePositioned from, PiecePositioned to, Cardinal cardinal) {
        return createMove(from, to);
    }

    private Move createMove(PiecePositioned from, PiecePositioned to) {
        return new Move() {
            @Override
            public PiecePositioned getFrom() {
                return from;
            }

            @Override
            public PiecePositioned getTo() {
                return to;
            }

            @Override
            public void executeMove() {
                throw new RuntimeException("Not meant for execution");
            }

            @Override
            public void undoMove() {
                throw new RuntimeException("Not meant for execution");
            }

            @Override
            public Cardinal getMoveDirection() {
                throw new RuntimeException("Not meant for execution");
            }

            @Override
            public boolean isQuiet() {
                throw new RuntimeException("Not meant for execution");
            }

            @Override
            public long getZobristHash() {
                throw new RuntimeException("Not meant for execution");
            }
        };
    }

}
