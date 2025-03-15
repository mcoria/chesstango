package net.chesstango.search.smart.sorters.comparators;

import net.chesstango.board.Game;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.Move;
import net.chesstango.board.position.*;
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

        moveList.add(createSimpleOneSquarePawnMove(PiecePositioned.getPiecePositioned(Square.e2, Piece.PAWN_WHITE),
                PiecePositioned.getPosition(Square.e3)));

        moveList.add(createSimpleKnightMove(PiecePositioned.getPiecePositioned(Square.e2, Piece.QUEEN_WHITE),
                PiecePositioned.getPosition(Square.e3)));

        moveList.add(createSimpleKnightMove(PiecePositioned.getPiecePositioned(Square.e2, Piece.KING_WHITE),
                PiecePositioned.getPosition(Square.e3)));

        moveList.add(createSimpleKnightMove(PiecePositioned.getPiecePositioned(Square.e2, Piece.KNIGHT_WHITE),
                PiecePositioned.getPosition(Square.e3)));

        moveList.add(createSimpleKnightMove(PiecePositioned.getPiecePositioned(Square.e2, Piece.ROOK_WHITE),
                PiecePositioned.getPosition(Square.e3)));

        moveList.add(createSimpleKnightMove(PiecePositioned.getPiecePositioned(Square.e2, Piece.BISHOP_WHITE),
                PiecePositioned.getPosition(Square.e3)));

        moveList.sort(defaultMoveComparator.reversed());
        Iterator<Move> movesSortedIt = moveList.iterator();

        move = movesSortedIt.next();
        assertEquals(Piece.QUEEN_WHITE, move.getFrom().getPiece());

        move = movesSortedIt.next();
        assertEquals(Piece.KNIGHT_WHITE, move.getFrom().getPiece());

        move = movesSortedIt.next();
        assertEquals(Piece.BISHOP_WHITE, move.getFrom().getPiece());

        move = movesSortedIt.next();
        assertEquals(Piece.ROOK_WHITE, move.getFrom().getPiece());

        move = movesSortedIt.next();
        assertEquals(Piece.PAWN_WHITE, move.getFrom().getPiece());

        move = movesSortedIt.next();
        assertEquals(Piece.KING_WHITE, move.getFrom().getPiece());

        assertFalse(movesSortedIt.hasNext());
    }


    @Test
    public void sortMoveCaptureWhite() {
        Move move = null;

        List<Move> moveList = new ArrayList<>();

        moveList.add(createCapturePawnMove(PiecePositioned.getPiecePositioned(Square.e2, Piece.PAWN_WHITE),
                PiecePositioned.getPiecePositioned(Square.f3, Piece.QUEEN_BLACK), Cardinal.NorteEste));

        moveList.add(createSimpleKnightMove(PiecePositioned.getPiecePositioned(Square.e2, Piece.QUEEN_WHITE),
                PiecePositioned.getPosition(Square.e3)));

        moveList.add(createSimpleKnightMove(PiecePositioned.getPiecePositioned(Square.e2, Piece.KING_WHITE),
                PiecePositioned.getPosition(Square.e3)));

        moveList.add(createSimpleKnightMove(PiecePositioned.getPiecePositioned(Square.e2, Piece.KNIGHT_WHITE),
                PiecePositioned.getPosition(Square.e3)));

        moveList.add(createSimpleKnightMove(PiecePositioned.getPiecePositioned(Square.e2, Piece.ROOK_WHITE),
                PiecePositioned.getPosition(Square.e3)));

        moveList.add(createCaptureKnightMove(PiecePositioned.getPiecePositioned(Square.e2, Piece.BISHOP_WHITE),
                PiecePositioned.getPiecePositioned(Square.e3, Piece.PAWN_BLACK)));

        moveList.sort(defaultMoveComparator.reversed());
        Iterator<Move> movesSortedIt = moveList.iterator();

        move = movesSortedIt.next();
        assertEquals(Piece.PAWN_WHITE, move.getFrom().getPiece());

        move = movesSortedIt.next();
        assertEquals(Piece.BISHOP_WHITE, move.getFrom().getPiece());

        move = movesSortedIt.next();
        assertEquals(Piece.QUEEN_WHITE, move.getFrom().getPiece());

        move = movesSortedIt.next();
        assertEquals(Piece.KNIGHT_WHITE, move.getFrom().getPiece());

        move = movesSortedIt.next();
        assertEquals(Piece.ROOK_WHITE, move.getFrom().getPiece());

        move = movesSortedIt.next();
        assertEquals(Piece.KING_WHITE, move.getFrom().getPiece());

        assertFalse(movesSortedIt.hasNext());
    }

    @Test
    public void sortMoveToEmptySquareBlack() {
        Move move = null;

        List<Move> moveList = new ArrayList<>();

        moveList.add(createSimpleOneSquarePawnMove(PiecePositioned.getPiecePositioned(Square.e7, Piece.PAWN_BLACK),
                PiecePositioned.getPosition(Square.e6)));

        moveList.add(createSimpleKnightMove(PiecePositioned.getPiecePositioned(Square.e7, Piece.QUEEN_BLACK),
                PiecePositioned.getPosition(Square.e6)));

        moveList.add(createSimpleKnightMove(PiecePositioned.getPiecePositioned(Square.e7, Piece.KING_BLACK),
                PiecePositioned.getPosition(Square.e6)));

        moveList.add(createSimpleKnightMove(PiecePositioned.getPiecePositioned(Square.e7, Piece.KNIGHT_BLACK),
                PiecePositioned.getPosition(Square.e6)));

        moveList.add(createSimpleKnightMove(PiecePositioned.getPiecePositioned(Square.e7, Piece.ROOK_BLACK),
                PiecePositioned.getPosition(Square.e6)));

        moveList.add(createSimpleKnightMove(PiecePositioned.getPiecePositioned(Square.e7, Piece.BISHOP_BLACK),
                PiecePositioned.getPosition(Square.e6)));


        moveList.sort(defaultMoveComparator.reversed());
        Iterator<Move> movesSortedIt = moveList.iterator();

        move = movesSortedIt.next();
        assertEquals(Piece.QUEEN_BLACK, move.getFrom().getPiece());

        move = movesSortedIt.next();
        assertEquals(Piece.KNIGHT_BLACK, move.getFrom().getPiece());

        move = movesSortedIt.next();
        assertEquals(Piece.BISHOP_BLACK, move.getFrom().getPiece());

        move = movesSortedIt.next();
        assertEquals(Piece.ROOK_BLACK, move.getFrom().getPiece());

        move = movesSortedIt.next();
        assertEquals(Piece.PAWN_BLACK, move.getFrom().getPiece());

        move = movesSortedIt.next();
        assertEquals(Piece.KING_BLACK, move.getFrom().getPiece());

        assertFalse(movesSortedIt.hasNext());
    }

    @Test
    public void sortMoveCaptureBlack() {
        Move move = null;

        List<Move> moveList = new ArrayList<>();

        moveList.add(createCapturePawnMove(PiecePositioned.getPiecePositioned(Square.e7, Piece.PAWN_BLACK),
                PiecePositioned.getPiecePositioned(Square.f6, Piece.QUEEN_WHITE), Cardinal.SurEste));

        moveList.add(createSimpleKnightMove(PiecePositioned.getPiecePositioned(Square.e7, Piece.QUEEN_BLACK),
                PiecePositioned.getPosition(Square.e6)));

        moveList.add(createSimpleKnightMove(PiecePositioned.getPiecePositioned(Square.e7, Piece.KING_BLACK),
                PiecePositioned.getPosition(Square.e5)));

        moveList.add(createSimpleKnightMove(PiecePositioned.getPiecePositioned(Square.e7, Piece.KNIGHT_BLACK),
                PiecePositioned.getPosition(Square.e6)));

        moveList.add(createSimpleKnightMove(PiecePositioned.getPiecePositioned(Square.e7, Piece.ROOK_BLACK),
                PiecePositioned.getPosition(Square.e6)));

        moveList.add(createCaptureKnightMove(PiecePositioned.getPiecePositioned(Square.e7, Piece.BISHOP_BLACK),
                PiecePositioned.getPiecePositioned(Square.e6, Piece.PAWN_WHITE)));


        moveList.sort(defaultMoveComparator.reversed());
        Iterator<Move> movesSortedIt = moveList.iterator();

        move = movesSortedIt.next();
        assertEquals(Piece.PAWN_BLACK, move.getFrom().getPiece());

        move = movesSortedIt.next();
        assertEquals(Piece.BISHOP_BLACK, move.getFrom().getPiece());

        move = movesSortedIt.next();
        assertEquals(Piece.QUEEN_BLACK, move.getFrom().getPiece());

        move = movesSortedIt.next();
        assertEquals(Piece.KNIGHT_BLACK, move.getFrom().getPiece());

        move = movesSortedIt.next();
        assertEquals(Piece.ROOK_BLACK, move.getFrom().getPiece());

        move = movesSortedIt.next();
        assertEquals(Piece.KING_BLACK, move.getFrom().getPiece());

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
            public Cardinal getMoveDirection() {
                return null;
            }

            @Override
            public boolean isQuiet() {
                return false;
            }

            @Override
            public long getZobristHash(ChessPosition chessPosition) {
                return 0;
            }

            @Override
            public void doMove(BitBoardWriter bitBoard) {

            }

            @Override
            public void undoMove(BitBoardWriter bitBoard) {

            }

            @Override
            public void doMove(ChessPosition chessPosition) {

            }

            @Override
            public void undoMove(ChessPosition chessPosition) {

            }

            @Override
            public void doMove(MoveCacheBoardWriter moveCache) {

            }

            @Override
            public void undoMove(MoveCacheBoardWriter moveCache) {

            }

            @Override
            public void doMove(PositionStateWriter positionState) {

            }

            @Override
            public void undoMove(PositionStateWriter positionStateWriter) {

            }

            @Override
            public void doMove(SquareBoardWriter squareBoard) {

            }

            @Override
            public void undoMove(SquareBoardWriter squareBoard) {

            }

            @Override
            public void doMove(ZobristHashWriter hash, ChessPositionReader chessPositionReader) {

            }

            @Override
            public void undoMove(ZobristHashWriter hash) {

            }
        };
    }

}
