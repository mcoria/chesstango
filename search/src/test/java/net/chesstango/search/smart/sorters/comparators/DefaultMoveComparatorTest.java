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
        Move moveQueen = createSimpleKnightMove(PiecePositioned.getPiecePositioned(Square.e2, Piece.QUEEN_WHITE),
                PiecePositioned.getPosition(Square.e3));

        Move moveKnight = createSimpleKnightMove(PiecePositioned.getPiecePositioned(Square.e2, Piece.KNIGHT_WHITE),
                PiecePositioned.getPosition(Square.e3));

        Move moveBishop = createSimpleKnightMove(PiecePositioned.getPiecePositioned(Square.e2, Piece.BISHOP_WHITE),
                PiecePositioned.getPosition(Square.e3));

        Move moveRook = createSimpleKnightMove(PiecePositioned.getPiecePositioned(Square.e2, Piece.ROOK_WHITE),
                PiecePositioned.getPosition(Square.e3));

        Move movePawn = createSimpleOneSquarePawnMove(PiecePositioned.getPiecePositioned(Square.e2, Piece.PAWN_WHITE),
                PiecePositioned.getPosition(Square.e3));

        Move moveKing = createSimpleKnightMove(PiecePositioned.getPiecePositioned(Square.e2, Piece.KING_WHITE),
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
        Move move1 = createSimpleOneSquarePawnMove(PiecePositioned.getPiecePositioned(Square.a2, Piece.PAWN_WHITE), PiecePositioned.getPosition(Square.a3));
        Move move2 = createSimpleTwoSquaresPawnMove(PiecePositioned.getPiecePositioned(Square.a2, Piece.PAWN_WHITE), PiecePositioned.getPosition(Square.a4), Square.a3);

        assertTrue(defaultMoveComparator.compare(move1, move2) < 0);

        assertTrue(defaultMoveComparator.compare(move2, move1) > 0);
    }


    @Test
    public void testPawnAndKnightMove() {
        Move move1 = createSimpleKnightMove(PiecePositioned.getPiecePositioned(Square.b1, Piece.KNIGHT_WHITE), PiecePositioned.getPosition(Square.a3));

        Move move2 = createSimpleTwoSquaresPawnMove(PiecePositioned.getPiecePositioned(Square.a2, Piece.PAWN_WHITE), PiecePositioned.getPosition(Square.a4), Square.a3);

        assertTrue(defaultMoveComparator.compare(move1, move2) > 0);

        assertTrue(defaultMoveComparator.compare(move2, move1) < 0);
    }

    @Test
    public void testKnightMove() {
        Move move1 = createSimpleKnightMove(PiecePositioned.getPiecePositioned(Square.g1, Piece.KNIGHT_WHITE), PiecePositioned.getPosition(Square.f3));

        Move move2 = createCaptureKnightMove(PiecePositioned.getPiecePositioned(Square.g1, Piece.KNIGHT_WHITE), PiecePositioned.getPosition(Square.h3));


        assertTrue(defaultMoveComparator.compare(move1, move2) < 0);

        assertTrue(defaultMoveComparator.compare(move2, move1) > 0);
    }

    @Test
    public void testCapture01() {
        Move move1 = createCapturePawnMove(PiecePositioned.getPiecePositioned(Square.e4, Piece.PAWN_WHITE), PiecePositioned.getPiecePositioned(Square.f5, Piece.QUEEN_BLACK), Cardinal.NorteEste);

        Move move2 = createCaptureKnightMove(PiecePositioned.getPiecePositioned(Square.h4, Piece.KNIGHT_WHITE), PiecePositioned.getPiecePositioned(Square.f5, Piece.QUEEN_BLACK));

        assertTrue(defaultMoveComparator.compare(move1, move2) > 0);

        assertTrue(defaultMoveComparator.compare(move2, move1) < 0);
    }


    @Test
    public void testCapture01_Black() {
        Move move1 = createCapturePawnMove(PiecePositioned.getPiecePositioned(Square.e5, Piece.PAWN_BLACK), PiecePositioned.getPiecePositioned(Square.f4, Piece.QUEEN_WHITE), Cardinal.SurEste);

        Move move2 = createCaptureKnightMove(PiecePositioned.getPiecePositioned(Square.h5, Piece.KNIGHT_BLACK), PiecePositioned.getPiecePositioned(Square.f4, Piece.QUEEN_WHITE));

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
            }

            @Override
            public void undoMove() {
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
