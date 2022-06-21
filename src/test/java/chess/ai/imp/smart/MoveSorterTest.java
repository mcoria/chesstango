package chess.ai.imp.smart;

import chess.board.Piece;
import chess.board.PiecePositioned;
import chess.board.Square;
import chess.board.iterators.Cardinal;
import chess.board.moves.Move;
import chess.board.moves.MoveFactory;
import chess.board.moves.imp.MoveFactoryBlack;
import chess.board.moves.imp.MoveFactoryWhite;
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
    public void sortMoveToEmptySquareWhite(){
        Move move = null;

        Move move1 = moveFactoryWhite.createSimplePawnMove(PiecePositioned.getPiecePositioned(Square.e2, Piece.PAWN_WHITE),
                PiecePositioned.getPiecePositioned(Square.e3));

        Move move2 = moveFactoryWhite.createSimpleMove(PiecePositioned.getPiecePositioned(Square.e2, Piece.QUEEN_WHITE),
                PiecePositioned.getPiecePositioned(Square.e3));

        Move move3 = moveFactoryWhite.createSimpleMove(PiecePositioned.getPiecePositioned(Square.e2, Piece.KING_WHITE),
                PiecePositioned.getPiecePositioned(Square.e3));

        Move move4 = moveFactoryWhite.createSimpleMove(PiecePositioned.getPiecePositioned(Square.e2, Piece.KNIGHT_WHITE),
                PiecePositioned.getPiecePositioned(Square.e3));

        Move move5 = moveFactoryWhite.createSimpleMove(PiecePositioned.getPiecePositioned(Square.e2, Piece.ROOK_WHITE),
                PiecePositioned.getPiecePositioned(Square.e3));


        List<Move> moveList = Arrays.asList(move1, move2, move3, move4, move5);

        Queue<Move> movesSorted = moveSorterTest.sortMoves(moveList);

        move = movesSorted.poll();
        Assert.assertEquals(Piece.KING_WHITE, move.getFrom().getValue());

        move = movesSorted.poll();
        Assert.assertEquals(Piece.QUEEN_WHITE, move.getFrom().getValue());

        move = movesSorted.poll();
        Assert.assertEquals(Piece.ROOK_WHITE, move.getFrom().getValue());

        move = movesSorted.poll();
        Assert.assertEquals(Piece.KNIGHT_WHITE, move.getFrom().getValue());

        move = movesSorted.poll();
        Assert.assertEquals(Piece.PAWN_WHITE, move.getFrom().getValue());

        Assert.assertTrue(movesSorted.isEmpty());
    }


    @Test
    public void sortMoveCaptureWhite(){
        Move move = null;

        Move move1 = moveFactoryWhite.createCapturePawnMove(PiecePositioned.getPiecePositioned(Square.e2, Piece.PAWN_WHITE),
                PiecePositioned.getPiecePositioned(Square.f3, Piece.QUEEN_BLACK), Cardinal.NorteEste);

        Move move2 = moveFactoryWhite.createSimpleMove(PiecePositioned.getPiecePositioned(Square.e2, Piece.QUEEN_WHITE),
                PiecePositioned.getPiecePositioned(Square.e3));

        Move move3 = moveFactoryWhite.createSimpleMove(PiecePositioned.getPiecePositioned(Square.e2, Piece.KING_WHITE),
                PiecePositioned.getPiecePositioned(Square.e3));

        Move move4 = moveFactoryWhite.createSimpleMove(PiecePositioned.getPiecePositioned(Square.e2, Piece.KNIGHT_WHITE),
                PiecePositioned.getPiecePositioned(Square.e3));

        Move move5 = moveFactoryWhite.createSimpleMove(PiecePositioned.getPiecePositioned(Square.e2, Piece.ROOK_WHITE),
                PiecePositioned.getPiecePositioned(Square.e3));

        Move move6 = moveFactoryWhite.createCaptureMove(PiecePositioned.getPiecePositioned(Square.e2, Piece.BISHOP_WHITE),
                PiecePositioned.getPiecePositioned(Square.e3, Piece.PAWN_BLACK));


        List<Move> moveList = Arrays.asList(move1, move2, move3, move4, move5, move6);

        Queue<Move> movesSorted = moveSorterTest.sortMoves(moveList);

        move = movesSorted.poll();
        Assert.assertEquals(Piece.PAWN_WHITE, move.getFrom().getValue());

        move = movesSorted.poll();
        Assert.assertEquals(Piece.BISHOP_WHITE, move.getFrom().getValue());

        move = movesSorted.poll();
        Assert.assertEquals(Piece.KING_WHITE, move.getFrom().getValue());

        move = movesSorted.poll();
        Assert.assertEquals(Piece.QUEEN_WHITE, move.getFrom().getValue());

        move = movesSorted.poll();
        Assert.assertEquals(Piece.ROOK_WHITE, move.getFrom().getValue());

        move = movesSorted.poll();
        Assert.assertEquals(Piece.KNIGHT_WHITE, move.getFrom().getValue());

        Assert.assertTrue(movesSorted.isEmpty());
    }

    @Test
    public void sortMoveToEmptySquareBlack(){
        Move move = null;

        Move move1 = moveFactoryBlack.createSimplePawnMove(PiecePositioned.getPiecePositioned(Square.e7, Piece.PAWN_BLACK),
                PiecePositioned.getPiecePositioned(Square.e6));

        Move move2 = moveFactoryBlack.createSimpleMove(PiecePositioned.getPiecePositioned(Square.e7, Piece.QUEEN_BLACK),
                PiecePositioned.getPiecePositioned(Square.e6));

        Move move3 = moveFactoryBlack.createSimpleMove(PiecePositioned.getPiecePositioned(Square.e7, Piece.KING_BLACK),
                PiecePositioned.getPiecePositioned(Square.e6));

        Move move4 = moveFactoryBlack.createSimpleMove(PiecePositioned.getPiecePositioned(Square.e7, Piece.KNIGHT_BLACK),
                PiecePositioned.getPiecePositioned(Square.e6));

        Move move5 = moveFactoryBlack.createSimpleMove(PiecePositioned.getPiecePositioned(Square.e7, Piece.ROOK_BLACK),
                PiecePositioned.getPiecePositioned(Square.e6));


        List<Move> moveList = Arrays.asList(move1, move2, move3, move4, move5);

        Queue<Move> movesSorted = moveSorterTest.sortMoves(moveList);

        move = movesSorted.poll();
        Assert.assertEquals(Piece.KING_BLACK, move.getFrom().getValue());

        move = movesSorted.poll();
        Assert.assertEquals(Piece.QUEEN_BLACK, move.getFrom().getValue());

        move = movesSorted.poll();
        Assert.assertEquals(Piece.ROOK_BLACK, move.getFrom().getValue());

        move = movesSorted.poll();
        Assert.assertEquals(Piece.KNIGHT_BLACK, move.getFrom().getValue());

        move = movesSorted.poll();
        Assert.assertEquals(Piece.PAWN_BLACK, move.getFrom().getValue());

        Assert.assertTrue(movesSorted.isEmpty());
    }

    @Test
    public void sortMoveCaptureBlack(){
        Move move = null;

        Move move1 = moveFactoryBlack.createCapturePawnMove(PiecePositioned.getPiecePositioned(Square.e7, Piece.PAWN_BLACK),
                PiecePositioned.getPiecePositioned(Square.f3, Piece.QUEEN_WHITE), Cardinal.NorteEste);

        Move move2 = moveFactoryBlack.createSimpleMove(PiecePositioned.getPiecePositioned(Square.e7, Piece.QUEEN_BLACK),
                PiecePositioned.getPiecePositioned(Square.e3));

        Move move3 = moveFactoryBlack.createSimpleMove(PiecePositioned.getPiecePositioned(Square.e7, Piece.KING_BLACK),
                PiecePositioned.getPiecePositioned(Square.e3));

        Move move4 = moveFactoryBlack.createSimpleMove(PiecePositioned.getPiecePositioned(Square.e7, Piece.KNIGHT_BLACK),
                PiecePositioned.getPiecePositioned(Square.e3));

        Move move5 = moveFactoryBlack.createSimpleMove(PiecePositioned.getPiecePositioned(Square.e7, Piece.ROOK_BLACK),
                PiecePositioned.getPiecePositioned(Square.e3));

        Move move6 = moveFactoryBlack.createCaptureMove(PiecePositioned.getPiecePositioned(Square.e7, Piece.BISHOP_BLACK),
                PiecePositioned.getPiecePositioned(Square.e3, Piece.PAWN_WHITE));


        List<Move> moveList = Arrays.asList(move1, move2, move3, move4, move5, move6);

        Queue<Move> movesSorted = moveSorterTest.sortMoves(moveList);

        move = movesSorted.poll();
        Assert.assertEquals(Piece.PAWN_BLACK, move.getFrom().getValue());

        move = movesSorted.poll();
        Assert.assertEquals(Piece.BISHOP_BLACK, move.getFrom().getValue());

        move = movesSorted.poll();
        Assert.assertEquals(Piece.KING_BLACK, move.getFrom().getValue());

        move = movesSorted.poll();
        Assert.assertEquals(Piece.QUEEN_BLACK, move.getFrom().getValue());

        move = movesSorted.poll();
        Assert.assertEquals(Piece.ROOK_BLACK, move.getFrom().getValue());

        move = movesSorted.poll();
        Assert.assertEquals(Piece.KNIGHT_BLACK, move.getFrom().getValue());

        Assert.assertTrue(movesSorted.isEmpty());
    }

    @Test
    public void testQueue(){
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
