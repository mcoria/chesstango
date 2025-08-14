package net.chesstango.search.smart.sorters.comparators;

import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.Move;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * @author Mauricio Coria
 */
public class DefaultMoveComparatorReversedTest {

    private DefaultMoveComparator defaultMoveComparator;

    @BeforeEach
    public void setUp() {
        defaultMoveComparator = new DefaultMoveComparator();
    }

    @Test
    public void sortMoveToEmptySquareWhite() {
        Move move = null;

        List<Move> moveList = new ArrayList<>();

        moveList.add(createSimpleOneSquarePawnMove(PiecePositioned.of(Square.e2, Piece.PAWN_WHITE),
                PiecePositioned.getPosition(Square.e3)));

        moveList.add(createSimpleKnightMove(PiecePositioned.of(Square.e2, Piece.QUEEN_WHITE),
                PiecePositioned.getPosition(Square.e3)));

        moveList.add(createSimpleKnightMove(PiecePositioned.of(Square.e2, Piece.KING_WHITE),
                PiecePositioned.getPosition(Square.e3)));

        moveList.add(createSimpleKnightMove(PiecePositioned.of(Square.e2, Piece.KNIGHT_WHITE),
                PiecePositioned.getPosition(Square.e3)));

        moveList.add(createSimpleKnightMove(PiecePositioned.of(Square.e2, Piece.ROOK_WHITE),
                PiecePositioned.getPosition(Square.e3)));

        moveList.add(createSimpleKnightMove(PiecePositioned.of(Square.e2, Piece.BISHOP_WHITE),
                PiecePositioned.getPosition(Square.e3)));

        moveList.sort(defaultMoveComparator.reversed());
        Iterator<Move> movesSortedIt = moveList.iterator();

        move = movesSortedIt.next();
        assertEquals(Piece.QUEEN_WHITE, move.getFrom().piece());

        move = movesSortedIt.next();
        assertEquals(Piece.KNIGHT_WHITE, move.getFrom().piece());

        move = movesSortedIt.next();
        assertEquals(Piece.BISHOP_WHITE, move.getFrom().piece());

        move = movesSortedIt.next();
        assertEquals(Piece.ROOK_WHITE, move.getFrom().piece());

        move = movesSortedIt.next();
        assertEquals(Piece.PAWN_WHITE, move.getFrom().piece());

        move = movesSortedIt.next();
        assertEquals(Piece.KING_WHITE, move.getFrom().piece());

        assertFalse(movesSortedIt.hasNext());
    }


    @Test
    public void sortMoveCaptureWhite() {
        Move move = null;

        List<Move> moveList = new ArrayList<>();

        moveList.add(createCapturePawnMove(PiecePositioned.of(Square.e2, Piece.PAWN_WHITE),
                PiecePositioned.of(Square.f3, Piece.QUEEN_BLACK), Cardinal.NorteEste));

        moveList.add(createSimpleKnightMove(PiecePositioned.of(Square.e2, Piece.QUEEN_WHITE),
                PiecePositioned.getPosition(Square.e3)));

        moveList.add(createSimpleKnightMove(PiecePositioned.of(Square.e2, Piece.KING_WHITE),
                PiecePositioned.getPosition(Square.e3)));

        moveList.add(createSimpleKnightMove(PiecePositioned.of(Square.e2, Piece.KNIGHT_WHITE),
                PiecePositioned.getPosition(Square.e3)));

        moveList.add(createSimpleKnightMove(PiecePositioned.of(Square.e2, Piece.ROOK_WHITE),
                PiecePositioned.getPosition(Square.e3)));

        moveList.add(createCaptureKnightMove(PiecePositioned.of(Square.e2, Piece.BISHOP_WHITE),
                PiecePositioned.of(Square.e3, Piece.PAWN_BLACK)));

        moveList.sort(defaultMoveComparator.reversed());
        Iterator<Move> movesSortedIt = moveList.iterator();

        move = movesSortedIt.next();
        assertEquals(Piece.PAWN_WHITE, move.getFrom().piece());

        move = movesSortedIt.next();
        assertEquals(Piece.BISHOP_WHITE, move.getFrom().piece());

        move = movesSortedIt.next();
        assertEquals(Piece.QUEEN_WHITE, move.getFrom().piece());

        move = movesSortedIt.next();
        assertEquals(Piece.KNIGHT_WHITE, move.getFrom().piece());

        move = movesSortedIt.next();
        assertEquals(Piece.ROOK_WHITE, move.getFrom().piece());

        move = movesSortedIt.next();
        assertEquals(Piece.KING_WHITE, move.getFrom().piece());

        assertFalse(movesSortedIt.hasNext());
    }

    @Test
    public void sortMoveToEmptySquareBlack() {
        Move move = null;

        List<Move> moveList = new ArrayList<>();

        moveList.add(createSimpleOneSquarePawnMove(PiecePositioned.of(Square.e7, Piece.PAWN_BLACK),
                PiecePositioned.getPosition(Square.e6)));

        moveList.add(createSimpleKnightMove(PiecePositioned.of(Square.e7, Piece.QUEEN_BLACK),
                PiecePositioned.getPosition(Square.e6)));

        moveList.add(createSimpleKnightMove(PiecePositioned.of(Square.e7, Piece.KING_BLACK),
                PiecePositioned.getPosition(Square.e6)));

        moveList.add(createSimpleKnightMove(PiecePositioned.of(Square.e7, Piece.KNIGHT_BLACK),
                PiecePositioned.getPosition(Square.e6)));

        moveList.add(createSimpleKnightMove(PiecePositioned.of(Square.e7, Piece.ROOK_BLACK),
                PiecePositioned.getPosition(Square.e6)));

        moveList.add(createSimpleKnightMove(PiecePositioned.of(Square.e7, Piece.BISHOP_BLACK),
                PiecePositioned.getPosition(Square.e6)));


        moveList.sort(defaultMoveComparator.reversed());
        Iterator<Move> movesSortedIt = moveList.iterator();

        move = movesSortedIt.next();
        assertEquals(Piece.QUEEN_BLACK, move.getFrom().piece());

        move = movesSortedIt.next();
        assertEquals(Piece.KNIGHT_BLACK, move.getFrom().piece());

        move = movesSortedIt.next();
        assertEquals(Piece.BISHOP_BLACK, move.getFrom().piece());

        move = movesSortedIt.next();
        assertEquals(Piece.ROOK_BLACK, move.getFrom().piece());

        move = movesSortedIt.next();
        assertEquals(Piece.PAWN_BLACK, move.getFrom().piece());

        move = movesSortedIt.next();
        assertEquals(Piece.KING_BLACK, move.getFrom().piece());

        assertFalse(movesSortedIt.hasNext());
    }

    @Test
    public void sortMoveCaptureBlack() {
        Move move = null;

        List<Move> moveList = new ArrayList<>();

        moveList.add(createCapturePawnMove(PiecePositioned.of(Square.e7, Piece.PAWN_BLACK),
                PiecePositioned.of(Square.f6, Piece.QUEEN_WHITE), Cardinal.SurEste));

        moveList.add(createSimpleKnightMove(PiecePositioned.of(Square.e7, Piece.QUEEN_BLACK),
                PiecePositioned.getPosition(Square.e6)));

        moveList.add(createSimpleKnightMove(PiecePositioned.of(Square.e7, Piece.KING_BLACK),
                PiecePositioned.getPosition(Square.e5)));

        moveList.add(createSimpleKnightMove(PiecePositioned.of(Square.e7, Piece.KNIGHT_BLACK),
                PiecePositioned.getPosition(Square.e6)));

        moveList.add(createSimpleKnightMove(PiecePositioned.of(Square.e7, Piece.ROOK_BLACK),
                PiecePositioned.getPosition(Square.e6)));

        moveList.add(createCaptureKnightMove(PiecePositioned.of(Square.e7, Piece.BISHOP_BLACK),
                PiecePositioned.of(Square.e6, Piece.PAWN_WHITE)));


        moveList.sort(defaultMoveComparator.reversed());
        Iterator<Move> movesSortedIt = moveList.iterator();

        move = movesSortedIt.next();
        assertEquals(Piece.PAWN_BLACK, move.getFrom().piece());

        move = movesSortedIt.next();
        assertEquals(Piece.BISHOP_BLACK, move.getFrom().piece());

        move = movesSortedIt.next();
        assertEquals(Piece.QUEEN_BLACK, move.getFrom().piece());

        move = movesSortedIt.next();
        assertEquals(Piece.KNIGHT_BLACK, move.getFrom().piece());

        move = movesSortedIt.next();
        assertEquals(Piece.ROOK_BLACK, move.getFrom().piece());

        move = movesSortedIt.next();
        assertEquals(Piece.KING_BLACK, move.getFrom().piece());

        assertFalse(movesSortedIt.hasNext());
    }

    private Move createCaptureKnightMove(PiecePositioned from, PiecePositioned to) {
        return createMove(from, to);
    }

    private Move createSimpleKnightMove(PiecePositioned from, PiecePositioned to) {
        return createMove(from, to);
    }

    private Move createCapturePawnMove(PiecePositioned from, PiecePositioned to, Cardinal cardinal) {
        return createMove(from, to);
    }

    private Move createSimpleOneSquarePawnMove(PiecePositioned from, PiecePositioned to) {
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
