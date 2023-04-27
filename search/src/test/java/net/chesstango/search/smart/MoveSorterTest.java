package net.chesstango.search.smart;

import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.factory.SingletonMoveFactories;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MoveFactory;




import java.util.*;

/**
 * @author Mauricio Coria
 */
public class MoveSorterTest {

    private MoveSorter moveSorterTest;

    private MoveFactory moveFactoryWhite = SingletonMoveFactories.getDefaultMoveFactoryWhite();

    private MoveFactory moveFactoryBlack = SingletonMoveFactories.getDefaultMoveFactoryBlack();

    @BeforeEach
    public void setUp() {
        moveSorterTest = new MoveSorter();
    }

    @Test
    public void sortMoveToEmptySquareWhite() {
        Move move = null;

        List<Move> moveList = new ArrayList<>();

        moveList.add(moveFactoryWhite.createSimpleOneSquarePawnMove(PiecePositioned.getPiecePositioned(Square.e2, Piece.PAWN_WHITE),
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
        assertEquals(Piece.QUEEN_WHITE, move.getFrom().getPiece());

        move = movesSorted.poll();
        assertEquals(Piece.KNIGHT_WHITE, move.getFrom().getPiece());

        move = movesSorted.poll();
        assertEquals(Piece.BISHOP_WHITE, move.getFrom().getPiece());

        move = movesSorted.poll();
        assertEquals(Piece.ROOK_WHITE, move.getFrom().getPiece());

        move = movesSorted.poll();
        assertEquals(Piece.PAWN_WHITE, move.getFrom().getPiece());

        move = movesSorted.poll();
        assertEquals(Piece.KING_WHITE, move.getFrom().getPiece());

        assertTrue(movesSorted.isEmpty());
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
        assertEquals(Piece.PAWN_WHITE, move.getFrom().getPiece());

        move = movesSorted.poll();
        assertEquals(Piece.BISHOP_WHITE, move.getFrom().getPiece());

        move = movesSorted.poll();
        assertEquals(Piece.QUEEN_WHITE, move.getFrom().getPiece());

        move = movesSorted.poll();
        assertEquals(Piece.KNIGHT_WHITE, move.getFrom().getPiece());

        move = movesSorted.poll();
        assertEquals(Piece.ROOK_WHITE, move.getFrom().getPiece());

        move = movesSorted.poll();
        assertEquals(Piece.KING_WHITE, move.getFrom().getPiece());

        assertTrue(movesSorted.isEmpty());
    }

    @Test
    public void sortMoveToEmptySquareBlack() {
        Move move = null;

        List<Move> moveList = new ArrayList<>();

        moveList.add(moveFactoryBlack.createSimpleOneSquarePawnMove(PiecePositioned.getPiecePositioned(Square.e7, Piece.PAWN_BLACK),
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
        assertEquals(Piece.QUEEN_BLACK, move.getFrom().getPiece());

        move = movesSorted.poll();
        assertEquals(Piece.KNIGHT_BLACK, move.getFrom().getPiece());

        move = movesSorted.poll();
        assertEquals(Piece.BISHOP_BLACK, move.getFrom().getPiece());

        move = movesSorted.poll();
        assertEquals(Piece.ROOK_BLACK, move.getFrom().getPiece());

        move = movesSorted.poll();
        assertEquals(Piece.PAWN_BLACK, move.getFrom().getPiece());

        move = movesSorted.poll();
        assertEquals(Piece.KING_BLACK, move.getFrom().getPiece());

        assertTrue(movesSorted.isEmpty());
    }

    @Test
    public void sortMoveCaptureBlack() {
        Move move = null;

        List<Move> moveList = new ArrayList<>();

        moveList.add(moveFactoryBlack.createCapturePawnMove(PiecePositioned.getPiecePositioned(Square.e7, Piece.PAWN_BLACK),
                PiecePositioned.getPiecePositioned(Square.f6, Piece.QUEEN_WHITE), Cardinal.SurEste));

        moveList.add(moveFactoryBlack.createSimpleMove(PiecePositioned.getPiecePositioned(Square.e7, Piece.QUEEN_BLACK),
                PiecePositioned.getPosition(Square.e6)));

        moveList.add(moveFactoryBlack.createSimpleMove(PiecePositioned.getPiecePositioned(Square.e7, Piece.KING_BLACK),
                PiecePositioned.getPosition(Square.e5)));

        moveList.add(moveFactoryBlack.createSimpleMove(PiecePositioned.getPiecePositioned(Square.e7, Piece.KNIGHT_BLACK),
                PiecePositioned.getPosition(Square.e6)));

        moveList.add(moveFactoryBlack.createSimpleMove(PiecePositioned.getPiecePositioned(Square.e7, Piece.ROOK_BLACK),
                PiecePositioned.getPosition(Square.e6)));

        moveList.add(moveFactoryBlack.createCaptureMove(PiecePositioned.getPiecePositioned(Square.e7, Piece.BISHOP_BLACK),
                PiecePositioned.getPiecePositioned(Square.e6, Piece.PAWN_WHITE)));


        Queue<Move> movesSorted = moveSorterTest.sortMoves(moveList);

        move = movesSorted.poll();
        assertEquals(Piece.PAWN_BLACK, move.getFrom().getPiece());

        move = movesSorted.poll();
        assertEquals(Piece.BISHOP_BLACK, move.getFrom().getPiece());

        move = movesSorted.poll();
        assertEquals(Piece.QUEEN_BLACK, move.getFrom().getPiece());

        move = movesSorted.poll();
        assertEquals(Piece.KNIGHT_BLACK, move.getFrom().getPiece());

        move = movesSorted.poll();
        assertEquals(Piece.ROOK_BLACK, move.getFrom().getPiece());

        move = movesSorted.poll();
        assertEquals(Piece.KING_BLACK, move.getFrom().getPiece());

        assertTrue(movesSorted.isEmpty());
    }

    @Test
    public void testQueue() {
        Queue<Integer> queue = new PriorityQueue<Integer>(new MyComparator());

        queue.add(3);
        queue.add(1);
        queue.add(7);
        queue.add(5);

        assertEquals(7, queue.poll().intValue());
        assertEquals(5, queue.poll().intValue());
        assertEquals(3, queue.poll().intValue());
        assertEquals(1, queue.poll().intValue());
    }

    public static class MyComparator implements Comparator<Integer> {

        @Override
        public int compare(Integer o1, Integer o2) {
            int result = o1 - o2;
            return -result;
        }
    }

}
