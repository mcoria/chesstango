package net.chesstango.search.smartminmax;

import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MoveFactory;
import net.chesstango.board.moves.factories.MoveFactoryBlack;
import net.chesstango.board.moves.factories.MoveFactoryWhite;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

/**
 * @author Mauricio Coria
 */
public class MoveSorterTest {

    private MoveSorter moveSorterTest;

    private MoveFactory moveFactoryWhite = new MoveFactoryWhite();

    private MoveFactory moveFactoryBlack = new MoveFactoryBlack();

    @Before
    public void setUp() {
        moveSorterTest = new MoveSorter();
    }

    @Test
    public void sortMoveToEmptySquareWhite() {
        Move move = null;

        List<Move> moveList = new ArrayList<>();

        moveList.add(moveFactoryWhite.createSimplePawnMove(PiecePositioned.getPiecePositioned(Square.e2, Piece.PAWN_WHITE),
                PiecePositioned.getPosition(Square.e3)));

        moveList.add(moveFactoryWhite.createSimpleMove(PiecePositioned.getPiecePositioned(Square.e2, Piece.QUEEN_WHITE),
                PiecePositioned.getPosition(Square.e3)));

        moveList.add(moveFactoryWhite.createSimpleMove(PiecePositioned.getPiecePositioned(Square.e2, Piece.KING_WHITE),
                PiecePositioned.getPosition(Square.e3)));

        moveList.add(moveFactoryWhite.createSimpleMove(PiecePositioned.getPiecePositioned(Square.e2, Piece.KNIGHT_WHITE),
                PiecePositioned.getPosition(Square.e3)));

        moveList.add(moveFactoryWhite.createSimpleMove(PiecePositioned.getPiecePositioned(Square.e2, Piece.ROOK_WHITE),
                PiecePositioned.getPosition(Square.e3)));

        moveList.add(moveFactoryWhite.createSimpleMove(PiecePositioned.getPiecePositioned(Square.e2, Piece.BISHOP_WHITE),
                PiecePositioned.getPosition(Square.e3)));


        Queue<Move> movesSorted = moveSorterTest.sortMoves(moveList);


        move = movesSorted.poll();
        Assert.assertEquals(Piece.QUEEN_WHITE, move.getFrom().getPiece());

        move = movesSorted.poll();
        Assert.assertEquals(Piece.KNIGHT_WHITE, move.getFrom().getPiece());

        move = movesSorted.poll();
        Assert.assertEquals(Piece.BISHOP_WHITE, move.getFrom().getPiece());

        move = movesSorted.poll();
        Assert.assertEquals(Piece.ROOK_WHITE, move.getFrom().getPiece());

        move = movesSorted.poll();
        Assert.assertEquals(Piece.PAWN_WHITE, move.getFrom().getPiece());

        move = movesSorted.poll();
        Assert.assertEquals(Piece.KING_WHITE, move.getFrom().getPiece());

        Assert.assertTrue(movesSorted.isEmpty());
    }


    @Test
    public void sortMoveCaptureWhite() {
        Move move = null;

        List<Move> moveList = new ArrayList<>();

        moveList.add(moveFactoryWhite.createCapturePawnMove(PiecePositioned.getPiecePositioned(Square.e2, Piece.PAWN_WHITE),
                PiecePositioned.getPiecePositioned(Square.f3, Piece.QUEEN_BLACK), Cardinal.NorteEste));

        moveList.add(moveFactoryWhite.createSimpleMove(PiecePositioned.getPiecePositioned(Square.e2, Piece.QUEEN_WHITE),
                PiecePositioned.getPosition(Square.e3)));

        moveList.add(moveFactoryWhite.createSimpleMove(PiecePositioned.getPiecePositioned(Square.e2, Piece.KING_WHITE),
                PiecePositioned.getPosition(Square.e3)));

        moveList.add(moveFactoryWhite.createSimpleMove(PiecePositioned.getPiecePositioned(Square.e2, Piece.KNIGHT_WHITE),
                PiecePositioned.getPosition(Square.e3)));

        moveList.add(moveFactoryWhite.createSimpleMove(PiecePositioned.getPiecePositioned(Square.e2, Piece.ROOK_WHITE),
                PiecePositioned.getPosition(Square.e3)));

        moveList.add(moveFactoryWhite.createCaptureMove(PiecePositioned.getPiecePositioned(Square.e2, Piece.BISHOP_WHITE),
                PiecePositioned.getPiecePositioned(Square.e3, Piece.PAWN_BLACK)));

        Queue<Move> movesSorted = moveSorterTest.sortMoves(moveList);

        move = movesSorted.poll();
        Assert.assertEquals(Piece.PAWN_WHITE, move.getFrom().getPiece());

        move = movesSorted.poll();
        Assert.assertEquals(Piece.BISHOP_WHITE, move.getFrom().getPiece());

        move = movesSorted.poll();
        Assert.assertEquals(Piece.QUEEN_WHITE, move.getFrom().getPiece());

        move = movesSorted.poll();
        Assert.assertEquals(Piece.KNIGHT_WHITE, move.getFrom().getPiece());

        move = movesSorted.poll();
        Assert.assertEquals(Piece.ROOK_WHITE, move.getFrom().getPiece());

        move = movesSorted.poll();
        Assert.assertEquals(Piece.KING_WHITE, move.getFrom().getPiece());

        Assert.assertTrue(movesSorted.isEmpty());
    }

    @Test
    public void sortMoveToEmptySquareBlack() {
        Move move = null;

        List<Move> moveList = new ArrayList<>();

        moveList.add(moveFactoryBlack.createSimplePawnMove(PiecePositioned.getPiecePositioned(Square.e7, Piece.PAWN_BLACK),
                PiecePositioned.getPosition(Square.e6)));

        moveList.add(moveFactoryBlack.createSimpleMove(PiecePositioned.getPiecePositioned(Square.e7, Piece.QUEEN_BLACK),
                PiecePositioned.getPosition(Square.e6)));

        moveList.add(moveFactoryBlack.createSimpleMove(PiecePositioned.getPiecePositioned(Square.e7, Piece.KING_BLACK),
                PiecePositioned.getPosition(Square.e6)));

        moveList.add(moveFactoryBlack.createSimpleMove(PiecePositioned.getPiecePositioned(Square.e7, Piece.KNIGHT_BLACK),
                PiecePositioned.getPosition(Square.e6)));

        moveList.add(moveFactoryBlack.createSimpleMove(PiecePositioned.getPiecePositioned(Square.e7, Piece.ROOK_BLACK),
                PiecePositioned.getPosition(Square.e6)));

        moveList.add(moveFactoryWhite.createSimpleMove(PiecePositioned.getPiecePositioned(Square.e7, Piece.BISHOP_BLACK),
                PiecePositioned.getPosition(Square.e6)));


        Queue<Move> movesSorted = moveSorterTest.sortMoves(moveList);

        move = movesSorted.poll();
        Assert.assertEquals(Piece.QUEEN_BLACK, move.getFrom().getPiece());

        move = movesSorted.poll();
        Assert.assertEquals(Piece.KNIGHT_BLACK, move.getFrom().getPiece());

        move = movesSorted.poll();
        Assert.assertEquals(Piece.BISHOP_BLACK, move.getFrom().getPiece());

        move = movesSorted.poll();
        Assert.assertEquals(Piece.ROOK_BLACK, move.getFrom().getPiece());

        move = movesSorted.poll();
        Assert.assertEquals(Piece.PAWN_BLACK, move.getFrom().getPiece());

        move = movesSorted.poll();
        Assert.assertEquals(Piece.KING_BLACK, move.getFrom().getPiece());

        Assert.assertTrue(movesSorted.isEmpty());
    }

    @Test
    public void sortMoveCaptureBlack() {
        Move move = null;

        List<Move> moveList = new ArrayList<>();

        moveList.add(moveFactoryBlack.createCapturePawnMove(PiecePositioned.getPiecePositioned(Square.e7, Piece.PAWN_BLACK),
                PiecePositioned.getPiecePositioned(Square.f3, Piece.QUEEN_WHITE), Cardinal.NorteEste));

        moveList.add(moveFactoryBlack.createSimpleMove(PiecePositioned.getPiecePositioned(Square.e7, Piece.QUEEN_BLACK),
                PiecePositioned.getPosition(Square.e3)));

        moveList.add(moveFactoryBlack.createSimpleMove(PiecePositioned.getPiecePositioned(Square.e7, Piece.KING_BLACK),
                PiecePositioned.getPosition(Square.e3)));

        moveList.add(moveFactoryBlack.createSimpleMove(PiecePositioned.getPiecePositioned(Square.e7, Piece.KNIGHT_BLACK),
                PiecePositioned.getPosition(Square.e3)));

        moveList.add(moveFactoryBlack.createSimpleMove(PiecePositioned.getPiecePositioned(Square.e7, Piece.ROOK_BLACK),
                PiecePositioned.getPosition(Square.e3)));

        moveList.add(moveFactoryBlack.createCaptureMove(PiecePositioned.getPiecePositioned(Square.e7, Piece.BISHOP_BLACK),
                PiecePositioned.getPiecePositioned(Square.e3, Piece.PAWN_WHITE)));


        Queue<Move> movesSorted = moveSorterTest.sortMoves(moveList);

        move = movesSorted.poll();
        Assert.assertEquals(Piece.PAWN_BLACK, move.getFrom().getPiece());

        move = movesSorted.poll();
        Assert.assertEquals(Piece.BISHOP_BLACK, move.getFrom().getPiece());

        move = movesSorted.poll();
        Assert.assertEquals(Piece.QUEEN_BLACK, move.getFrom().getPiece());

        move = movesSorted.poll();
        Assert.assertEquals(Piece.KNIGHT_BLACK, move.getFrom().getPiece());

        move = movesSorted.poll();
        Assert.assertEquals(Piece.ROOK_BLACK, move.getFrom().getPiece());

        move = movesSorted.poll();
        Assert.assertEquals(Piece.KING_BLACK, move.getFrom().getPiece());

        Assert.assertTrue(movesSorted.isEmpty());
    }

    @Test
    public void testQueue() {
        Queue<Integer> queue = new PriorityQueue<Integer>(new MyComparator());

        queue.add(3);
        queue.add(1);
        queue.add(7);
        queue.add(5);

        Assert.assertEquals(7, queue.poll().intValue());
        Assert.assertEquals(5, queue.poll().intValue());
        Assert.assertEquals(3, queue.poll().intValue());
        Assert.assertEquals(1, queue.poll().intValue());
    }

    public static class MyComparator implements Comparator<Integer> {

        @Override
        public int compare(Integer o1, Integer o2) {
            int result = o1 - o2;
            return -result;
        }
    }

}
